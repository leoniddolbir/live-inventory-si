package com.smkw.commerce.inventory.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.smkw.commerce.inventory.InfrastructureConfigDev;
import com.smkw.commerce.inventory.api.data.InventoryTransactionProfile;
import com.smkw.commerce.inventory.api.data.InventoryTransactionProfileRepository;

@ContextConfiguration(classes = InfrastructureConfigDev.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class InventoryTransactionProfileTest {
	@Autowired
	private InventoryTransactionProfileRepository inventoryTransactionProfileRepository;

	@Test
	public void adjustInventoryTransaction() {
		List<InventoryTransactionProfile> all = inventoryTransactionProfileRepository.findByStoreId("001");
		assertNotNull(all);
		assertTrue(all.size()==1);
	}
}
