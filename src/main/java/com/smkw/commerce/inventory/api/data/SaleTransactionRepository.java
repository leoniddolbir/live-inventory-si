/**
 * 
 */
package com.smkw.commerce.inventory.api.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * @author ldolbir
 * 
 */
public interface SaleTransactionRepository extends
		CrudRepository<SaleTransaction, String> {
	public List<SaleTransaction> findByTransactionId(Integer transactionId);
}
