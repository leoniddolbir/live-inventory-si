package com.smkw.commerce.inventory.api.data;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

import org.springframework.util.Assert;

@Data
@Entity
@Table(name = "LSHSUM02", schema = "OPCUS001")
@IdClass(value = ItemMonthlySaleLedgerId.class)
public class ItemMonthlySaleLedger {
	public ItemMonthlySaleLedger(String anItemNumber, Integer soldQty,
			Integer returnedQty, MonetaryAmount aTotal) {
		this();
		Assert.notNull(anItemNumber);
		this.setItemNumber(anItemNumber);
		if (soldQty != null) {
			this.sold = soldQty;
		}
		if (returnedQty != null) {
			this.returned = returnedQty;
		}
		Assert.notNull(aTotal);
		this.total = aTotal;
	}

	protected ItemMonthlySaleLedger() {
		super();
		this.setDefaults();
	}

	private void setDefaults() {
		DateStamp aStamp = new DateStamp();
		this.saleCentury = aStamp.getCentury();
		this.saleMonth = aStamp.getMonth();
		this.saleYear = aStamp.getYear();
		this.sold = Integer.valueOf(0);
		this.returned = Integer.valueOf(0);
		this.total = new MonetaryAmount();
	}

	@Column(name = "SHITEM", length = 15)
	@NotNull
	@Id
	private String itemNumber;
	@Column(name = "SHISCC", precision = 2, scale = 0)
	@NotNull
	@Id
	private Integer saleCentury;
	@Column(name = "SHISYY", precision = 2, scale = 0)
	@NotNull
	@Id
	private Integer saleYear;
	@Column(name = "SHISMM", precision = 2, scale = 0)
	@NotNull
	@Id
	private Integer saleMonth;
	@Column(name = "SHSORD", precision = 11, scale = 3)
	@NotNull
	private Integer sold;
	@Column(name = "SHSRET", precision = 11, scale = 3)
	@NotNull
	private Integer returned;
	@Embedded
	@AttributeOverride(name = "amount", column = @Column(name = "SHSNET", precision = 11, scale = 3))
	@NotNull
	private MonetaryAmount total;

	public void processSale(Integer soldQty, MonetaryAmount netSale) {
		if (soldQty > 0) {
			this.sold += soldQty;
		} else {
			this.returned += -1 * soldQty;
		}
		total = total.add(netSale);
		this.itemNumber = this.itemNumber.trim();
	}
}
