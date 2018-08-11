package com.smkw.commerce.inventory.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smkw.commerce.inventory.MailerConfig;
import com.smkw.commerce.inventory.api.data.Flag;
import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAudit;
import com.smkw.commerce.inventory.integration.mail.FailedInventoryAdjustmentNotification;

@ContextConfiguration(classes = { MailerConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class MailTest {

	@Autowired
	private MessageChannel notifications;

	@Test
	public void sendEmail() {
		List<InventoryAdjustmentRequestAudit> all = new ArrayList<InventoryAdjustmentRequestAudit>(4);
		all.add(new InventoryAdjustmentRequestAudit("001", "ABCD", 1, 7.99, Flag.N, "Leo Test"));
		all.add(new InventoryAdjustmentRequestAudit("001", "ABCD", 1, 7.99, Flag.N, "Leo Test"));
		all.add(new InventoryAdjustmentRequestAudit("001", "ABCD", 1, 7.99, Flag.N, "Leo Test"));
		all.add(new InventoryAdjustmentRequestAudit("001", "ABCD", 1, 7.99, Flag.N, "Leo Test"));
		FailedInventoryAdjustmentNotification aMessage = new FailedInventoryAdjustmentNotification(all);

		notifications.send(MessageBuilder.withPayload(aMessage).build());
	}
}
