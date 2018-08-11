package com.smkw.commerce.inventory.api.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Table(name = "INVENT_TRN", schema = "ECOMMERCE")
@IdClass(value = InventoryTransactionProfileId.class)

public class InventoryTransactionProfile {

	//COMPANY,STORE,WH_CODE,CRED_ACCT,DEBIT_ACCT,SOURCE_ID,TRAN_DESC,DEPARTMENT
	@Column(name = "COMPANY")
	@NonNull
	@Id
	private Integer companyNmbr;
	@Column(name = "STORE")
	@NonNull
	@Id
	private String storeId;
	@Column(name = "WH_CODE")
	@NotNull
	@Id
	private String warehouseCode;
	@Column(name = "CRED_ACCT")
	@NotNull
	private Integer creditAccount;
	@Column(name = "DEBIT_ACCT")
	@NotNull
	private Integer debitAccount;
	@Column(name = "SOURCE_ID")
	@NotNull
	private String sourceId;
	@Column(name = "TRAN_DESC")
	@NotNull
	private String description;
	@Column(name = "DEPARTMENT")
	@NotNull
	private Integer departmentNmbr;
}
