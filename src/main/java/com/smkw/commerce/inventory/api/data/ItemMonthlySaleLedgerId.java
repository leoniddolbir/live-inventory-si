package com.smkw.commerce.inventory.api.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable

public class ItemMonthlySaleLedgerId implements Serializable {
	/**
	 * 
	 */
	protected ItemMonthlySaleLedgerId() {
		super();
		this.setDefaults();
	}
	public ItemMonthlySaleLedgerId(String aProductNumber, Integer century, Integer year, Integer month) {
		this.itemNumber = aProductNumber;
		this.saleCentury = century;
		this.saleYear = year;
		this.saleMonth = month;
	}

	private void setDefaults() {
		DateStamp aStamp = new DateStamp();
		this.saleCentury = aStamp.getCentury();
		this.saleMonth = aStamp.getMonth();
		this.saleYear = aStamp.getYear();
	}

	private static final long serialVersionUID = -151759356163745119L;
	@Column(name = "SHITEM", length = 15)
	@NotNull
	private String itemNumber;
	@Column(name = "SHISCC", precision = 2, scale = 0)
	@NotNull
	private Integer saleCentury;
	@Column(name = "SHISYY", precision = 2, scale = 0)
	@NotNull
	private Integer saleYear;
	@Column(name = "SHISMM", precision = 2, scale = 0)
	@NotNull
	private Integer saleMonth;

}
