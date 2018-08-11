package com.smkw.commerce.inventory.api.data;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Embeddable
public class ItemPriceRangeId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7308360513650289130L;
	@NonNull
	private String itemNumber;
	@NonNull
	private int rangeQuantity;

}
