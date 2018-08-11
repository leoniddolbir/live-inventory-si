package com.smkw.commerce.inventory.api.data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "LWINHST", schema = "ECOMMERCE")
@IdClass(value = InventoryAdjustmentHistoryId.class)
public class InventoryAdjusmentHistory {
	public InventoryAdjusmentHistory(@NotNull Long id, @NotNull String anItemNbr, @NotNull String aWarehouseCd,
			@NotNull String aLocation, @NotNull Integer qty, @NotNull MonetaryAmount total) {
		super();
		this.sequenceNbr = id;
		this.itemNumber = anItemNbr;
		this.itemWarehouse = aWarehouseCd;
		this.itemLocation = aLocation;
		this.adjustedQty = qty;
		this.transactionAmt = total;
		this.transactionCode = "102";
		this.userId = "WEBSITE";
		this.transactionDate = new DateStamp();
		this.companyNbr = 1;

	}

	@Column(name = "IHSEQ#", precision = 11, scale = 0)
	@NotNull
	@Id
	private Long sequenceNbr;
	@Column(name = "IHITEM", length = 8)
	@NotNull
	@Id
	private String itemNumber;
	@Column(name = "IHWHSE", length = 8)
	@NotNull
	private String itemWarehouse;
	@Column(name = "IHLOCN", length = 8)
	@NotNull
	private String itemLocation;
	@Column(name = "IHTRCD", length = 3)
	@NotNull
	private String transactionCode;
	@Column(name = "IHTQTY")
	@NotNull
	private Integer adjustedQty;
	@Embedded
	@AttributeOverride(name = "amount", column = @Column(name = "IHTCST", precision = 11, scale = 4))
	@NotNull
	private MonetaryAmount transactionAmt;
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "century", column = @Column(name = "IHTRCC")),
			@AttributeOverride(name = "year", column = @Column(name = "IHTRYY")),
			@AttributeOverride(name = "month", column = @Column(name = "IHTRMM")),
			@AttributeOverride(name = "day", column = @Column(name = "IHTRDD")) })
	@Valid
	@NotNull
	private DateStamp transactionDate;
	@Column(name = "IHCOM3", length = 10)
	private String userId;
	@Column(name = "IHCMP#")
	private Integer companyNbr;
}
