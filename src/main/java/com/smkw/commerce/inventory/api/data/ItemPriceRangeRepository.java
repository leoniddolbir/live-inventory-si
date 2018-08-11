package com.smkw.commerce.inventory.api.data;

import org.springframework.data.repository.CrudRepository;
import java.lang.String;
import com.smkw.commerce.inventory.api.data.ItemPriceRange;
import java.util.List;

public interface ItemPriceRangeRepository extends CrudRepository<ItemPriceRange, ItemPriceRangeId> {
	List<ItemPriceRange> findByItemNumber(String anItemNumber);
}
