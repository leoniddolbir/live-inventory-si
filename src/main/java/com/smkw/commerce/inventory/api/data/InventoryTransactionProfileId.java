package com.smkw.commerce.inventory.api.data;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data

public class InventoryTransactionProfileId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3742430844458547400L;
	@NotNull
	private Integer companyNmbr;
	@NotNull
	private String storeId;
}
