package com.smkw.commerce.inventory.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400ConnectionPool;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.ConnectionPoolException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.IllegalObjectTypeException;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.QSYSObjectPathName;
import com.smkw.commerce.inventory.api.data.DateStamp;
import com.smkw.commerce.inventory.api.data.InventoryAdjusmentHistory;
import com.smkw.commerce.inventory.api.data.InventoryTransactionProfile;
import com.smkw.commerce.inventory.api.data.InventoryTransactionProfileRepository;
import com.smkw.commerce.inventory.api.data.ItemInventoryLocator;
import com.smkw.commerce.inventory.api.data.ItemInventoryLocatorRepository;
import com.smkw.commerce.inventory.api.data.ItemMonthlySaleLedger;
import com.smkw.commerce.inventory.api.data.ItemMonthlySaleLedgerId;
import com.smkw.commerce.inventory.api.data.ItemMonthlySaleLedgerRepository;
import com.smkw.commerce.inventory.api.data.MonetaryAmount;
import com.smkw.commerce.inventory.api.data.SaleTransaction;
import com.smkw.commerce.inventory.api.data.SaleTransactionRepository;
import com.smkw.commerce.util.IdGenerator;

@Service
@Slf4j
public class InventoryService {
	// TODO Change this library to the production one
	private static final String EVRG_PRICE_SQL = "SELECT IVACST FROM ECOMMERCE.LWINVNT WHERE IVDELE!='D' AND IVITEM= :sku ";
	private static final String FIND_PREF_LOC = "SELECT IQLOCN FROM ECOMMERCE.LWINQTY WHERE IQITEM = :sku AND IQWHSE= :wh";
	@Autowired
	@PersistenceUnit(unitName = "inventory")
	private EntityManagerFactory emf;
	private EntityManager em;
	@Autowired
	private SaleTransactionRepository saleTransactionRepository;
	@Autowired
	private ItemMonthlySaleLedgerRepository itemMonthlySaleLedgerRepository;
	@Autowired
	private ItemInventoryLocatorRepository itemLocatorRepository;
	@Autowired
	private InventoryTransactionProfileRepository inventoryTransactionProfileRepository;
	@Autowired
	private IdGenerator generator;
	@Autowired
	private AS400ConnectionPool legacySystem;
	@Autowired
	private Environment env;

	public Integer createSaleTransaction(@NotNull InventoryTransactionProfile aProfile, @NotNull String aSku, int qty,
			@NotNull MonetaryAmount aPrice) {

		MonetaryAmount aTransactionAmount = aPrice.multiply(qty);
		Integer id = this.getTransactionSequence();
		SaleTransaction aTran = new SaleTransaction(id, aProfile.getCreditAccount(), generator.getRandomString(15));
		aTran.setDepartmentNumber(aProfile.getDepartmentNmbr());
		aTran.setCompanyNumber(aProfile.getCompanyNmbr());
		aTran.setDescription(aProfile.getDescription());
		aTran.setSourceIdentifier(aProfile.getSourceId());
		aTran.setCredit(aTransactionAmount);
		this.getEm().persist(aTran);
		aTran = new SaleTransaction(id, aProfile.getDebitAccount(), generator.getRandomString(15));
		aTran.setDepartmentNumber(aProfile.getDepartmentNmbr());
		aTran.setCompanyNumber(aProfile.getCompanyNmbr());
		aTran.setDescription(aProfile.getDescription());
		aTran.setSourceIdentifier(aProfile.getSourceId());
		aTran.setDebit(aTransactionAmount);
		this.getEm().persist(aTran);
		return id;
	}

	private EntityManager getEm() {
		if (em == null) {
			em = emf.createEntityManager();
		}
		return em;
	}

	@Transactional(readOnly = true, value = "transactionManager")
	public MonetaryAmount findEveragePriceBySku(@NotNull String aSku) {
		BigDecimal aPrice = (BigDecimal) this.getEm().createNativeQuery(EVRG_PRICE_SQL).setParameter("sku", aSku)
				.getSingleResult();
		return new MonetaryAmount(MonetaryAmount.USD, aPrice);
	}

	/**
	 * Find all inventory records for a SKU and Warehouse code sorted by
	 * available quantity. Starting with the first record adjust by sold
	 * quantity and if first record does not contain sufficient available amount
	 * proceed to the next record. If after the all records have been adjusted
	 * and there still remaining amount apply it to the first record on the
	 * negative side.
	 * 
	 * @throws InvalidSkuException
	 * 
	 **/
	public void adjustSoldInventory(@NotNull Integer soldQty, @NotNull String anItemNumber,
			@NotNull BigDecimal netSale, @NotNull String storeId) throws InvalidSkuException {
		getEm().getTransaction().begin();
		InventoryTransactionProfile aProfile = this.findProfileByStore(storeId);
		List<ItemInventoryLocator> all = null;
		MonetaryAmount price = null;
		try {
			all = itemLocatorRepository.findByItemNumberAndItemWarehouseOrderByAvailableQtyDesc(anItemNumber,
					aProfile.getWarehouseCode());
			price = this.findEveragePriceBySku(anItemNumber);
		} catch (NoResultException e) {
			log.warn(e.getMessage() + "\r\tItem Number: " + anItemNumber);
			throw new InvalidSkuException();
		}
		if (all == null || all.isEmpty()) {
			throw new InvalidSkuException();
		}

		this.adjustSoldInventory(all, this.findPreferedLocation(anItemNumber, aProfile.getWarehouseCode()), price,
				soldQty);
		this.createSaleTransaction(aProfile, anItemNumber, soldQty, price);
		this.updateMonthlySaleLedger(new DateStamp(), anItemNumber, soldQty, new MonetaryAmount(netSale));
		getEm().getTransaction().commit();
	}

	@Transactional(readOnly = true, value = "transactionManager")
	private String findPreferedLocation(String anItemNumber, String aWHCode) {
		return (String) this.getEm().createNativeQuery(FIND_PREF_LOC).setParameter("sku", anItemNumber)
				.setParameter("wh", aWHCode).getSingleResult();
	}

	private void adjustSoldInventory(List<ItemInventoryLocator> all, String aPrefLoc, MonetaryAmount price,
			int remaining) {
		int recordCount = all.size();
		// If preferred location is found then adjust it first
		// If preferred location has enough inventory on hand then use this
		// location only
		// If there is more sold inventory then available in preferred location,
		// and there are more locations
		// then adjust all available inventory in preferred location and use
		// other locations for remaining qty
		// If preferred location is the only location and does not contain
		// sufficient on-hand inventory then
		// go negative
		ItemInventoryLocator pref = null;
		if (aPrefLoc != null && !aPrefLoc.isEmpty()) {
			for (ItemInventoryLocator each : all) {
				if (each.getItemLocation().trim().equalsIgnoreCase(aPrefLoc.trim())) {
					pref = each;
					break;
				}
			}
			// Preferred location is found
			if (pref != null) {
				if (pref.getAvailableQty().compareTo(remaining) >= 0) {
					this.reduceInventory(pref, remaining, price);
					remaining = 0;
				} else if (pref.getAvailableQty() > 0 && recordCount > 1) {
					remaining = remaining - pref.getAvailableQty();
					this.reduceInventory(pref, pref.getAvailableQty(), price);
					all.remove(pref);
					recordCount--;
				} else if (recordCount == 1) {
					this.reduceInventory(pref, remaining, price);
					remaining = 0;
				}
			}
		}
		// Return if the work is done
		if (remaining == 0)
			return;
		// Keep adjusting all other records if preferred location didn't have
		// enough on-hand
		for (ItemInventoryLocator each : all) {
			if (remaining != 0) {
				// If we have sufficient inventory to finish adjustment using
				// this record
				if (each.getAvailableQty().compareTo(remaining) >= 0) {
					this.reduceInventory(each, remaining, price);
					remaining = 0;
				}
				// If we have partial inventory and more records to use
				// adjust this one for the available quantity
				else if (each.getAvailableQty() > 0 && recordCount > 1) {
					remaining = remaining - each.getAvailableQty();
					this.reduceInventory(each, each.getAvailableQty(), price);
				}
				// If we have last record and still have remaining quantity then
				// go negative
				else if (recordCount == 1) {
					this.reduceInventory(each, remaining, price);
					remaining = 0;
				}
			}
			recordCount--;
		}
	}

	public void updateMonthlySaleLedger(DateStamp date, String anItemNumber, Integer soldQty, MonetaryAmount netSale) {
		ItemMonthlySaleLedger aLedger = this.findMonthlySaleLedgerFor(date, anItemNumber);
		if (aLedger != null) {
			aLedger.processSale(soldQty, netSale);
			this.getEm().merge(aLedger);
		} else {
			this.createItemMonthlySaleLedger(anItemNumber, (soldQty > 0) ? soldQty : 0, (soldQty < 0) ? soldQty * -1
					: 0, netSale);
		}

	}

	public Long nextHistorySequence() {
		AS400 connection = null;
		Long aSequence = 0l;
		try {
			connection = legacySystem.getConnection(env.getProperty("inventory.system"),
					env.getProperty("inventory.jdbc.username"), env.getProperty("inventory.jdbc.password"));
			QSYSObjectPathName path = new QSYSObjectPathName("OPCUS001", "INHSEQ", "DTAARA");
			CharacterDataArea dataArea = new CharacterDataArea(connection, path.getPath());
			String currentNumber = dataArea.read(0, 11);
			long sequence = Long.parseLong(currentNumber);
			String nextNumber = String.valueOf(sequence + 1);
			// prepend with 0 to match data area lenght
			while (nextNumber.length() < currentNumber.length()) {
				nextNumber = "0" + nextNumber;
			}
			dataArea.write(nextNumber);
			aSequence = sequence;
		} catch (ConnectionPoolException e) {
			e.printStackTrace();
			throw new LegacySystemException("Failed to get next History sequence", e);
		} catch (AS400SecurityException e) {
			e.printStackTrace();
			throw new LegacySystemException("Failed to get next History sequence", e);
		} catch (ErrorCompletingRequestException e) {
			e.printStackTrace();
			throw new LegacySystemException("Failed to get next History sequence", e);
		} catch (IllegalObjectTypeException e) {
			e.printStackTrace();
			throw new LegacySystemException("Failed to get next History sequence", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new LegacySystemException("Failed to get next History sequence", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LegacySystemException("Failed to get next History sequence", e);
		} catch (ObjectDoesNotExistException e) {
			e.printStackTrace();
			throw new LegacySystemException("Failed to get next History sequence", e);
		} finally {
			if (connection != null) {
				this.legacySystem.returnConnectionToPool(connection);
			}
		}
		return aSequence;
	}

	@Transactional(readOnly = true, value = "transactionManager")
	public Integer getTransactionSequence() {
		String nextIdQuery = "select max(h.transactionId) from SaleTransaction h";
		return (Integer) this.getEm().createQuery(nextIdQuery).getSingleResult();
	}

	private void reduceInventory(ItemInventoryLocator locator, int qty, MonetaryAmount price) {
		locator.reduceAvailableBy(qty);
		this.getEm().merge(locator);
		InventoryAdjusmentHistory aHistory = new InventoryAdjusmentHistory(this.nextHistorySequence(),
				locator.getItemNumber(), locator.getItemWarehouse(), locator.getItemLocation(), qty,
				price.multiply(qty));
		this.getEm().persist(aHistory);

	}

	@Transactional(readOnly = true, value = "transactionManager")
	private InventoryTransactionProfile findProfileByStore(String storeId) {
		List<InventoryTransactionProfile> all = this.inventoryTransactionProfileRepository.findByStoreId(storeId);
		return (all.isEmpty()) ? null : all.get(0);
	}

	@Transactional(readOnly = true, value = "transactionManager")
	public ItemMonthlySaleLedger findMonthlySaleLedgerFor(DateStamp aDate, String anItemNumber) {
		ItemMonthlySaleLedgerId anId = new ItemMonthlySaleLedgerId(anItemNumber, aDate.getCentury(), aDate.getYear(),
				aDate.getMonth());
		return this.getEm().find(ItemMonthlySaleLedger.class, anId);
	}

	public ItemMonthlySaleLedger createItemMonthlySaleLedger(String anItemNumber, Integer soldQty, Integer returnedQty,
			MonetaryAmount netSale) {
		/*
		 * DateStamp aDate = new DateStamp(); if (!ledgerRepository.exists(new
		 * ItemMonthlySaleLedgerId(anItemNumber, aDate.getCentury(),
		 * aDate.getYear(), aDate.getMonth()))) {
		 */
		ItemMonthlySaleLedger aLedger = new ItemMonthlySaleLedger(anItemNumber, soldQty, returnedQty, netSale);
		return itemMonthlySaleLedgerRepository.save(aLedger);
		// }
	}

	public void initialize() {
		this.em = null;
	}
}
