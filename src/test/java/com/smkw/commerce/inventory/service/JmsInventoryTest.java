package com.smkw.commerce.inventory.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smkw.commerce.inventory.test.config.SellerConfigProd;
import com.smkw.commerce.inventory.xml.IMMessage;

@ContextConfiguration(classes = { SellerConfigProd.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class JmsInventoryTest {

	@Autowired
	private MessageChannel javaInventoryChannel;

	@Test
	public void sendAndRecieve() {
		IMMessage aMessage = new IMMessage();
		IMMessage.ItemMovement anItem = new IMMessage.ItemMovement();
		anItem.setItemNum("LEO08292013");
		anItem.setStoreId("001");
		anItem.setQtySold(new BigDecimal(5));
		anItem.setNetSales(new BigDecimal(44.95).setScale(2, BigDecimal.ROUND_HALF_UP));
		aMessage.getItemMovement().add(anItem);
		Message<IMMessage> message = MessageBuilder.withPayload(aMessage).build();
		javaInventoryChannel.send(message);
	}
}
