package com.smkw.commerce.inventory.integration.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.smkw.commerce.inventory.api.data.Flag;
import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAudit;
import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAuditRepository;
import com.smkw.commerce.inventory.integration.mail.FailedInventoryAdjustmentNotification;
import com.smkw.commerce.inventory.xml.IMMessage;

@Service
public class ResendFailedJob {
	@PersistenceUnit(unitName = "audit")
	private EntityManagerFactory emf;
	@Autowired
	private InventoryAdjustmentRequestAuditRepository repository;
	@Autowired
	private MessageChannel javaInventoryChannel;
	@Autowired
	private MessageChannel notifications;

	@Scheduled(cron = "0 0/20 * * * ?")
	void resendFailedInventoryAdjustmentRequests() {

		List<InventoryAdjustmentRequestAudit> all = repository.findByCompletedOrderByReceivedDesc(Flag.N);

		if (all != null && !all.isEmpty()) {
			this.notifyOfFailure(all);
			EntityManager em = emf.createEntityManager();
			for (InventoryAdjustmentRequestAudit each : all) {
				this.reSendMessage(em, each);
			}
		}
	}

	private void notifyOfFailure(List<InventoryAdjustmentRequestAudit> all) {
		FailedInventoryAdjustmentNotification aMessage = new FailedInventoryAdjustmentNotification(all);
		try {
			notifications.send(MessageBuilder.withPayload(aMessage).build());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}

	private void reSendMessage(EntityManager em, InventoryAdjustmentRequestAudit each) {
		em.getTransaction().begin();
		IMMessage aMessage = new IMMessage();
		aMessage.getItemMovement().add(each.getRequest());
		Message<IMMessage> message = MessageBuilder.withPayload(aMessage).build();
		javaInventoryChannel.send(message);
		each.setCompleted(Flag.Y);
		each.setReason("Re-Sent");
		em.merge(each);
		em.getTransaction().commit();
	}

}
