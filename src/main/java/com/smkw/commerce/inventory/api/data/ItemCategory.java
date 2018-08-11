package com.smkw.commerce.inventory.api.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "LWNETCAT", schema = "ECOMMERCE")
public class ItemCategory {
	@Id
	@Column(name = "PNID", length = 3, precision = 0)
	private int id;
	@Column(name = "PNPID", length = 3, precision = 0)
	private int parentId;
	@Column(name = "PNTEXT", length = 40)
	private String title;
	@Column(name = "PNSITE", length = 15)
	private String site;

}
