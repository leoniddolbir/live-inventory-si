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
public class InventoryAdjustmentHistoryId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7536738491199986431L;
	@NotNull
	private Long sequenceNbr;
	@NotNull
	private String itemNumber;

}
