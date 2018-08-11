package com.smkw.commerce.inventory.api.data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name = "LWINBPR", schema = "ECOMMERCE")
@IdClass(value = ItemPriceRangeId.class)
public class ItemPriceRange {
	@Column(name = "IBITEM", length = 15)
	@NonNull
	@Id
	private String itemNumber;
	@Column(name = "IBDSAL", length = 1)
	@Enumerated(EnumType.STRING)
	private Flag allowDiscount;
	@Column(name = "IBSHND", length = 11, precision = 2)
	private BigDecimal shippingHandling;
	@Column(name = "IBSHNC", length = 1)
	private String amountPercent;
	@Column(name = "IBQTYS", length = 9)
	@NonNull
	@Id
	private int rangeQuantity;
	@Column(name = "IBUPRC", length = 11, precision = 4)
	@NotNull
	private BigDecimal unitPrice;
	@Column(name = "IBDITM", length = 15)
	private String dealItemNumber;
	@Column(name = "IBWHSE", length = 3)
	private String warehouse;
	@Column(name = "IBDDAL", length = 1)
	@Enumerated(EnumType.STRING)
	private Flag dealAllowDiscount;
	@Column(name = "IBDSHN", length = 11, precision = 2)
	private BigDecimal dealShippingHandling;
	@Column(name = "IBDSHC", length = 1)
	private String dealAmountPercent;
	@Column(name = "IBDQTY", length = 9)
	@NotNull
	private int dealRangeQuantity;
	@Column(name = "IBDUPR", length = 11, precision = 4)
	@NotNull
	private BigDecimal dealUnitPrice;
	@Column(name = "IBOQTY", length = 9, precision = 3)
	@NotNull
	private BigDecimal minOrderQuantity;
	@Column(name = "IBUPSL", length = 45)
	private String upSellMessage;
	
	public ItemPriceRange(String anItemNumber) {
		this(anItemNumber, 1);
	}

	protected ItemPriceRange() {
		super();
		this.setDefaults();
	}

	public ItemPriceRange(String anItemNumber, int qty) {
		this();
		this.itemNumber = anItemNumber;
		this.rangeQuantity = qty;
	}

	private void setDefaults() {
		this.allowDiscount = Flag.N;
		this.shippingHandling = new BigDecimal(0);
		this.amountPercent = "";
		this.unitPrice = new BigDecimal(0);
		this.dealItemNumber = "";
		this.warehouse = "";
		this.dealAllowDiscount = Flag.N;
		this.dealShippingHandling = new BigDecimal(0);
		this.dealAmountPercent = "";
		this.dealRangeQuantity = 0;
		this.dealUnitPrice = new BigDecimal(0);
		this.minOrderQuantity = new BigDecimal(0);
		this.upSellMessage = "";
	}
}
