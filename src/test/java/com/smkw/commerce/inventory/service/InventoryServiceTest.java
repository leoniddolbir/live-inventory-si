package com.smkw.commerce.inventory.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.smkw.commerce.inventory.InfrastructureConfigDev;
import com.smkw.commerce.inventory.api.data.DateStamp;
import com.smkw.commerce.inventory.api.data.ItemMonthlySaleLedger;

@ContextConfiguration(classes = InfrastructureConfigDev.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class InventoryServiceTest {
	@Autowired
	private InventoryService service;

	@Test
	public void adjustInventoryTransaction() {
		try {
			service.adjustSoldInventory(3, "BJ042", new BigDecimal(29.99d), "001");
		} catch (InvalidSkuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ItemMonthlySaleLedger aLedger = service.findMonthlySaleLedgerFor(new DateStamp(), "BJ047");
		assertNotNull(aLedger);
		assertEquals(aLedger.getItemNumber(), "BJ047");
		assertNotNull(aLedger.getTotal());
	}
}
