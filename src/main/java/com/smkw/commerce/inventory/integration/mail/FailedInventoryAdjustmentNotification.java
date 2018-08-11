package com.smkw.commerce.inventory.integration.mail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.smkw.commerce.inventory.audit.data.InventoryAdjustmentRequestAudit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FailedInventoryAdjustmentNotification {
	private Timestamp lastTimestamp;
	private int size;
	private List<String> reasons;

	public FailedInventoryAdjustmentNotification(@NotNull List<InventoryAdjustmentRequestAudit> records) {
		this.initialize(records);
	}

	public FailedInventoryAdjustmentNotification(@NotNull Timestamp last, @NotNull String message) {
		this.lastTimestamp = last;
		this.reasons = new ArrayList<String>();
		this.reasons.add(message);
		this.size = 0;
	}

	private void initialize(List<InventoryAdjustmentRequestAudit> records) {
		Map<Integer, String> allReasons = new HashMap<Integer, String>();
		for (InventoryAdjustmentRequestAudit each : records) {
			String aReason = each.getReason();
			if (aReason != null && !aReason.isEmpty()) {
				if (allReasons.get(aReason.hashCode()) == null) {
					allReasons.put(aReason.hashCode(), aReason);
				}
			}
		}
		if (allReasons.values() != null && !allReasons.isEmpty()) {
			this.reasons = new ArrayList<String>(allReasons.values());
		} else {
			this.reasons = new ArrayList<String>();
		}
		this.size = (records != null && !records.isEmpty()) ? records.size() : 0;
	}
}
