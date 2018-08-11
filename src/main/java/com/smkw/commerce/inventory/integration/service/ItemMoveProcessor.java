package com.smkw.commerce.inventory.integration.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smkw.commerce.inventory.api.data.Flag;
import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAudit;
import com.smkw.commerce.inventory.service.InvalidSkuException;
import com.smkw.commerce.inventory.service.InventoryService;
import com.smkw.commerce.inventory.xml.IMMessage;
import com.smkw.commerce.inventory.xml.IMMessage.ItemMovement;
import com.smkw.commerce.util.ExceptionReporter;

@Slf4j
@Service
public class ItemMoveProcessor {
	@PersistenceUnit(unitName = "audit")
	private EntityManagerFactory emf;
	@Autowired
	private InventoryService service;

	private static class ItemMovePrinter {
		public StringBuffer print(ItemMovement anItem, StringBuffer aBuff) {
			return aBuff.append("itemNum=").append(anItem.getItemNum()).append("\r").append("StoreId=")
					.append(anItem.getStoreId()).append("\r").append("packNum=").append(anItem.getPackNum())
					.append("\r").append("QtySol=").append(anItem.getQtySold()).append("\r").append("NetSales=")
					.append(anItem.getNetSales()).append("\r").append("rtnToInv=").append(anItem.getRtnToInv())
					.append("\r").append("RtnDamage=").append(anItem.getRtnDamaged()).append("\r").append("discAmt=")
					.append(anItem.getDiscAmt()).append("\r").append("MarkdownAmt=").append(anItem.getMarkdownAmt())
					.append("\r");
		}
	}

	public void process(IMMessage aMessage) {
		for (ItemMovement each : aMessage.getItemMovement()) {
			String aPrint = new ItemMoveProcessor.ItemMovePrinter().print(each, new StringBuffer()).toString();
			if (log.isInfoEnabled()) {
				log.info("Processing Item " + aPrint);
			}
			try {

				try {
					service.initialize();
					service.adjustSoldInventory(each.getQtySold().intValue(), each.getItemNum().trim().toUpperCase(),
							each.getNetSales(), each.getStoreId());
					this.saveAuditTrail(each, Flag.Y, "Success");
				} catch (InvalidSkuException e) {
					this.saveAuditTrail(each, Flag.E, "Invalid Sku");
				}

			} catch (Exception e) {
				e.printStackTrace(System.err);
				StringBuffer aMsg = ExceptionReporter.report(e, new StringBuffer());
				log.warn(aPrint + " processing failed with following exception: " + aMsg);
				this.saveAuditTrail(each, Flag.N, aMsg.toString());
			}

		}
	}

	private void saveAuditTrail(ItemMovement aRequest, Flag aFlag, String aMsg) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		InventoryAdjustmentRequestAudit anAudit = new InventoryAdjustmentRequestAudit(aRequest);
		anAudit.setReason(aMsg);
		anAudit.setCompleted(aFlag);
		em.persist(anAudit);
		em.getTransaction().commit();
	}

}
