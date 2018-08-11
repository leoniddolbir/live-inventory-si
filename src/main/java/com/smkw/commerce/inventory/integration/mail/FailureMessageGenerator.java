package com.smkw.commerce.inventory.integration.mail;

import org.springframework.stereotype.Component;

@Component
public class FailureMessageGenerator {

	public String generateEmail(FailedInventoryAdjustmentNotification aMessage) {

		StringBuffer anEmail = new StringBuffer();
		if (aMessage.getSize() > 0) {
			anEmail.append(
					"Number of failed Inventory Adjustment message requests has crossed defined threashold. There are ")
					.append(aMessage.getSize()).append(" failed requests. Following are given reasons of failure: ")
					.append("\r\t");
		}
		for (String each : aMessage.getReasons()) {
			anEmail.append(each).append("\r\t");
		}
		return anEmail.toString();

	}
}
