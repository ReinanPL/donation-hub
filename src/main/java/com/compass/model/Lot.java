package com.compass.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.compass.model.enums.GenreCloth;
import com.compass.model.enums.ItemType;
import com.compass.model.enums.SizeCloth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_LOT")
public class Lot implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "item_type")
	private ItemType itemType;

	private String description;

	private Integer quantity;

	@Column(name = "measuring_unit")
	private String unitOfMeasurement;

	@Enumerated(EnumType.STRING)
	private GenreCloth genre;
	
	@Enumerated(EnumType.STRING)
	private SizeCloth size;
	
	private LocalDate validity;

	
	@OneToOne(mappedBy = "lot")
	private Donation donations;

	
	public Lot() {
		
	}
	
	public Lot(Long id, String name, ItemType itemType, String description, Integer quantity, String unitOfMeasurement,
			GenreCloth genre, SizeCloth size, LocalDate validity) {
		super();
		this.id = id;
		this.name = name;
		this.itemType = itemType;
		this.description = description;
		this.quantity = quantity;
		this.unitOfMeasurement = unitOfMeasurement;
		this.genre = genre;
		this.size = size;
		this.validity = validity;
	}


	public Lot(Long id, String name, ItemType itemType, String description, Integer quantity, String unitOfMeasurement,
			GenreCloth genre, SizeCloth size, LocalDate validity, Donation donations, List<Order> orders) {
		super();
		this.id = id;
		this.name = name;
		this.itemType = itemType;
		this.description = description;
		this.quantity = quantity;
		this.unitOfMeasurement = unitOfMeasurement;
		this.genre = genre;
		this.size = size;
		this.validity = validity;
		this.donations = donations;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public Integer getQuantity() {
		return quantity;
	}


	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public GenreCloth getGenre() {
		return genre;
	}

	public void setGenre(GenreCloth genre) {
		this.genre = genre;
	}

	public SizeCloth getSize() {
		return size;
	}

	public void setSize(SizeCloth size) {
		this.size = size;
	}

	public LocalDate getValidity() {
		return validity;
	}

	public void setValidity(LocalDate validity) {
		this.validity = validity;
	}

	public Donation getDonations() {
		return donations;
	}

	public void setDonations(Donation donations) {
		this.donations = donations;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lot other = (Lot) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public String toString() {
		return "Lot [id=" + id + ", name=" + name + ", itemType=" + itemType + ", description=" + description
				+ ", quantity=" + quantity + ", unitOfMeasurement=" + unitOfMeasurement + ", genre=" + genre + ", size="
				+ size + ", validity=" + validity + ", donations=" + donations + "]";
	}

	
	
	

}
