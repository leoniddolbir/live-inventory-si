package com.smkw.commerce.inventory.test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.smkw.commerce.inventory.InventoryConfig;
import com.smkw.commerce.inventory.api.data.Flag;
import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAudit;

@ContextConfiguration(classes = { InventoryConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class AuditTest {

	@PersistenceContext(unitName = "audit")
	private EntityManager em;

	@Test
	public void saveAuditTrail() {
		InventoryAdjustmentRequestAudit anAudit = new InventoryAdjustmentRequestAudit(
				"001", "LEOTEST123", 1, 29.99d, Flag.Y);
		em.persist(anAudit);
	}

}
