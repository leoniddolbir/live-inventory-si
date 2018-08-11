package com.smkw.commerce.inventory.api.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = { "popular" })
@NoArgsConstructor
@Table(name = "LWBRAND", schema = "ECOMMERCE")
public class ItemBrand {
	@Column(name = "PBID", length = 4, precision = 0)
	@Id
	private int id;
	@Column(name = "PBNAM", length = 50)
	private String descripiton;
	@Column(name = "PBPOP", length = 1)
	@Enumerated(EnumType.STRING)
	private Flag popular;

}
