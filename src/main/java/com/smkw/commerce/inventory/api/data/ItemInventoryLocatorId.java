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
public class ItemInventoryLocatorId implements Serializable {
	private static final long serialVersionUID = 8986165476382778734L;
	@NotNull
	private String itemNumber;
	@NotNull
	private String itemLocation;
	@NotNull
	private String itemWarehouse;

}
