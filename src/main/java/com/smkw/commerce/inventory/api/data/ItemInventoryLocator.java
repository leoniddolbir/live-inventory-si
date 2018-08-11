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

import lombok.Data;

@Entity
@Data
@Table(name = "LWINQOH", schema = "ECOMMERCE")
@IdClass(value = ItemInventoryLocatorId.class)
public class ItemInventoryLocator {
	@Column(name = "IOITEM", length = 15)
	@NotNull
	@Id
	private String itemNumber;
	@Column(name = "IOLOCN", length = 8)
	@NotNull
	@Id
	private String itemLocation;
	@Column(name = "IOWHSE", length = 3)
	@NotNull
	@Id
	private String itemWarehouse;
	@Column(name = "IOQTYO")
	private Integer availableQty;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "century", column = @Column(name = "IOLCCC")),
			@AttributeOverride(name = "year", column = @Column(name = "IOLCYY")),
			@AttributeOverride(name = "month", column = @Column(name = "IOLCMM")),
			@AttributeOverride(name = "day", column = @Column(name = "IOLCDD")) })
	@NotNull
	private DateStamp lastCount;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "century", column = @Column(name = "IOLTCC")),
			@AttributeOverride(name = "year", column = @Column(name = "IOLTYY")),
			@AttributeOverride(name = "month", column = @Column(name = "IOLTMM")),
			@AttributeOverride(name = "day", column = @Column(name = "IOLTDD")) })
	@Valid
	@NotNull
	private DateStamp lastModified;
	@Column(name = "IOMINL")
	private Integer minStockLevel;
	@Column(name = "IOMAXL")
	private Integer maxStockLevel;
	@Column(name = "IOTRCD", length = 3)
	private String transactionCode;
	@Column(name = "IOUSER", length = 10)
	private String userId;
	@Column(name = "IOWSID", length = 10)
	private String workstationId;
	@AttributeOverrides({
			@AttributeOverride(name = "century", column = @Column(name = "IOCRCC")),
			@AttributeOverride(name = "year", column = @Column(name = "IOCRYY")),
			@AttributeOverride(name = "month", column = @Column(name = "IOCRMM")),
			@AttributeOverride(name = "day", column = @Column(name = "IOCRDD")),
			@AttributeOverride(name = "hour", column = @Column(name = "IOCRHR")),
			@AttributeOverride(name = "minutes", column = @Column(name = "IOCRMN")),
			@AttributeOverride(name = "seconds", column = @Column(name = "IOCRSC")) })
	@Valid
	@NotNull
	private TimeStamp originated;

	public void reduceAvailableBy(Integer soldQty) {
		this.availableQty = this.availableQty - soldQty;

	}
}
