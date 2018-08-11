package com.smkw.commerce.inventory.audit.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.smkw.commerce.inventory.api.data.Flag;

public interface InventoryAdjustmentRequestAuditRepository extends
		CrudRepository<InventoryAdjustmentRequestAudit, Long> {
	List<InventoryAdjustmentRequestAudit> findByCompletedOrderByReceivedDesc(
			Flag aFlag);

}
