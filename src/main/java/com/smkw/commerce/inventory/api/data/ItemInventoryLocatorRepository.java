package com.smkw.commerce.inventory.api.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ItemInventoryLocatorRepository extends
		CrudRepository<ItemInventoryLocator, ItemInventoryLocatorId> {
	List<ItemInventoryLocator> findByItemNumberAndItemWarehouseOrderByAvailableQtyDesc(String anItemNumber, String aWarehouse);

}
