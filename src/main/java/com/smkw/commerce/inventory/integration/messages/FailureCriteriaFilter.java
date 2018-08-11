package com.smkw.commerce.inventory.integration.messages;

import java.sql.Timestamp;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;

import com.smkw.commerce.inventory.integration.mail.FailedInventoryAdjustmentNotification;

@Data
@MessageEndpoint
@Slf4j
public class FailureCriteriaFilter {
	private static final long MINUTE_MILLS = 60 * 1000;
	private int messageThreashold;
	// Time in minutes 
	private int timeThreashold;

	@Filter
	public boolean isFailure(FailedInventoryAdjustmentNotification aNote) {
		log.info("Number of failed adjustments: "+ aNote.getSize());
		log.info("Failed adjustments threashold: "+ this.messageThreashold);
		log.info("Last message delivery: "+aNote.getLastTimestamp());
		long minSinceLast = this.getMinutesSinceLast(aNote.getLastTimestamp());
		log.info("Minutes since last delivery: "+minSinceLast);
		log.info("Minutes since last threashold: "+timeThreashold);
		return aNote.getSize() > this.messageThreashold || minSinceLast > this.timeThreashold;
	}

	private long getMinutesSinceLast(Timestamp lastTimestamp) {
		long now = System.currentTimeMillis();
		long last = lastTimestamp.getTime();
		return (now-last)/MINUTE_MILLS;
	}

}
