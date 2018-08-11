package com.smkw.commerce.inventory.api.data;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Entity
@Table(name = "LWNETITM", schema = "ECOMMERCE")
@Access(AccessType.FIELD)
public class WebItem {
	@Column(name = "PNITEM", length = 15)
	@NonNull
	@Id
	private String itemNumber;
	@Column(name = "PNTPIC", length = 10)
	private String smallPicture;
	@Column(name = "PNLPIC", length = 10)
	private String largePicture;
	@Column(name = "PNTTYP", length = 20)
	private String categoryType;
	@Column(name = "PNCAT ", length = 20)
	private String category;
	@Column(name = "PNSCAT", length = 20)
	private String subCategory;
	@Column(name = "PNRDY ", length = 1)
	@Enumerated(EnumType.STRING)
	private Flag ready;
	@Column(name = "PNWPIC", length = 10)
	private String picture;
	@Column(name = "PNNAM1", length = 100)
	@NotNull
	private String title;
	@Column(name = "PNHAND", length = 25)
	private String handle;
	@Column(name = "PNPATT", length = 25)
	private String pattern;
	@Column(name = "PNKEYW", length = 250)
	private String keywords;
	@Column(name = "PNSITE", length = 15)
	@NotNull
	private String site;
	@Column(name = "PNUPDT", length = 8)
	private String published;
	@Column(name = "PNOFST", length = 1)
	@Enumerated(EnumType.STRING)
	@NotNull
	private Flag available;
	@Column(name = "PNDWDT", length = 8)
	private String removed;
	@OneToMany
	@JoinColumn(name = "IBITEM", referencedColumnName = "PNITEM")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ItemPriceRange> prices;
	@ManyToOne
	@JoinColumn(name = "PNVEND")
	private ItemBrand brand;
	@ManyToMany
	@JoinTable(name = "LWCATITM", schema = "ECOMMERCE", joinColumns = { @JoinColumn(name = "PCITEM") }, inverseJoinColumns = { @JoinColumn(name = "PNID1") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ItemCategory> categories;
	@ElementCollection
	@CollectionTable(name = "LWCANTSL", schema = "ECOMMERCE", joinColumns = @JoinColumn(name = "CITEM"))
	@Column(name = "CSTAT")
	private List<String> restrictingStates;
}
