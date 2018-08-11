package com.smkw.commerce.inventory.api.data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "LWGLTRN", schema = "ECOMMERCE")
public class SaleTransaction {
	public SaleTransaction(Integer id, Integer account, String uuid) {
		this();
		this.setTransactionId(id);
		this.setAccountNumber(account);
		this.setSecondaryReference(uuid);
	}

	protected SaleTransaction() {
		super();
		this.setDefaults();
	}

	@Column(name = "GRJV#")
	@NotNull
	private Integer transactionId;
	@Column(name = "GRTGL#")
	@NotNull
	private Integer accountNumber;
	@Column(name = "GRTSRC", length = 2)
	@NotNull
	private String sourceIdentifier;
	@Column(name = "GRDESC", length = 30)
	@NotNull
	private String description;
	@Column(name = "GRTCO#")
	@NotNull
	private Integer companyNumber;
	@Column(name = "GRTDPT")
	@NotNull
	private Integer departmentNumber;
	@Embedded
	@AttributeOverride(name = "amount", column = @Column(name = "GRTDB", precision = 11, scale = 2))
	@NotNull
	private MonetaryAmount debit;
	@Embedded
	@AttributeOverride(name = "amount", column = @Column(name = "GRTCR", precision = 11, scale = 2))
	@NotNull
	private MonetaryAmount credit;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "century", column = @Column(name = "GRPRCC")),
			@AttributeOverride(name = "year", column = @Column(name = "GRPRYY")),
			@AttributeOverride(name = "month", column = @Column(name = "GRPRMM")),
			@AttributeOverride(name = "day", column = @Column(name = "GRPRDD")) })
	@Valid
	@NotNull
	private DateStamp processed;
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "century", column = @Column(name = "GREFCC")),
			@AttributeOverride(name = "year", column = @Column(name = "GREFYY")),
			@AttributeOverride(name = "month", column = @Column(name = "GREFMM")),
			@AttributeOverride(name = "day", column = @Column(name = "GREFDD")) })
	@Valid
	@NotNull
	private DateStamp effective;
	@Column(name = "GRPREF")
	private Integer referenceAccount;
	@Column(name = "GRSREF", length = 15)
	@Id
	private String secondaryReference;

	@Column(name = "GRPSTD", length = 1)
	@NotNull
	@Enumerated(EnumType.STRING)
	private Flag posted;
	@Column(name = "GRTRCD", length = 3)
	@NotNull
	private String transaction;

	private SaleTransaction setDefaults() {
		this.setCompanyNumber(1);
		this.setSourceIdentifier("MO");
		this.setDescription("Showroom Sales");
		this.setDepartmentNumber(15);
		this.setPosted(Flag.N);
		this.setTransaction("");
		this.setDebit(new MonetaryAmount());
		this.setCredit(new MonetaryAmount());
		this.setProcessed(new DateStamp());
		this.setEffective(new DateStamp());
		this.setReferenceAccount(0);
		return this;
	}
}
