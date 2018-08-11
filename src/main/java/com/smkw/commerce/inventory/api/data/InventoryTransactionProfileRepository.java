package com.smkw.commerce.inventory.api.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface InventoryTransactionProfileRepository extends
		CrudRepository<InventoryTransactionProfile, InventoryTransactionProfileId> {
	public List<InventoryTransactionProfile> findByStoreId(String anId);

}
