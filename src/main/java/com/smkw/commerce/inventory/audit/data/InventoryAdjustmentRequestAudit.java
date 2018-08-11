package com.smkw.commerce.inventory.audit.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.smkw.commerce.inventory.api.data.BaseEntity;
import com.smkw.commerce.inventory.api.data.Flag;
import com.smkw.commerce.inventory.xml.IMMessage.ItemMovement;
import lombok.Setter;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@Entity
@Table(name = "INV_AUDIT", schema = "INVENTORY")
public class InventoryAdjustmentRequestAudit extends BaseEntity {
	protected InventoryAdjustmentRequestAudit() {
		super();
		this.setDefaults();
	}

	public InventoryAdjustmentRequestAudit(String aStoreId, String aSKU, int qty, double total, Flag comp){
		this();
		this.request.setStoreId(aStoreId);
		this.request.setItemNum(aSKU);
		this.request.setQtySold(new BigDecimal(qty));
		this.request.setNetSales(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP));
		this.completed = comp;
	}
	public InventoryAdjustmentRequestAudit(String aStoreId, String aSKU, int qty, double total, Flag comp, String aReason){
		this(aStoreId, aSKU, qty, total, comp);
		this.reason = aReason;
	}
	public InventoryAdjustmentRequestAudit(ItemMovement aRequest){
		this.received = new Timestamp(System.currentTimeMillis());
		this.completed= Flag.N;
		this.request=aRequest;
	}
	private void setDefaults() {
		this.received = new Timestamp(System.currentTimeMillis());
		this.completed= Flag.N;
		this.request = new ItemMovement();
		this.request.setCouponAmt(new BigDecimal(0));
		this.request.setDiscAmt(new BigDecimal(0));
		this.request.setMarkdownAmt(new BigDecimal(0));
		this.request.setNetSales(new BigDecimal(0));
		this.request.setPackNum(1);
		this.request.setQtySold(new BigDecimal(0));
		this.request.setRtnDamaged(new BigDecimal(0));
		this.request.setRtnToInv(new BigDecimal(0));
	}

	@Embedded
	private ItemMovement request;
	private Timestamp received;
	@Enumerated(EnumType.STRING)
	private Flag completed;
	private String reason;
}
