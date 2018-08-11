package com.smkw.commerce.inventory.integration.service;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAuditRepository;
import com.smkw.commerce.inventory.integration.mail.FailedInventoryAdjustmentNotification;

@Service
public class ServiceStatusJob {
	@PersistenceUnit(unitName = "audit")
	private EntityManagerFactory emf;
	@Autowired
	private InventoryAdjustmentRequestAuditRepository repository;
	@Autowired
	private MessageChannel notifications;

	@Scheduled(cron = "0 0/60 10-19 * * ?")
	@Transactional(readOnly = true, value = "auditTransactionManager")
	public void checkStatus() {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			Timestamp aTimestamp = (Timestamp) em.createQuery(
					"select max(au.received) from InventoryAdjustmentRequestAudit au").getSingleResult();
			this.sendNotification(aTimestamp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private void sendNotification(Timestamp aTimestamp) {
		FailedInventoryAdjustmentNotification aMessage = new FailedInventoryAdjustmentNotification(aTimestamp,
				"NO Messages received for over an hour! Last Message was received on " + aTimestamp.toString());
		try {
			notifications.send(MessageBuilder.withPayload(aMessage).build());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
