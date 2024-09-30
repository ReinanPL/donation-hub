package com.compass.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="TB_DISTRIBUTION_CENTER")
public class DistributionCenter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String address;

	@OneToMany(mappedBy = "distributionCenter")
	@JsonIgnore
	private List<Donation> donations = new ArrayList<>();
	
	@OneToMany(mappedBy = "distributionCenter")
	@JsonIgnore
	private List<Order> orders = new ArrayList<>();
	

	public DistributionCenter() {
		
	}
	
	public DistributionCenter(Long id, String name, String address, List<Donation> donations, List<Order> orders) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.donations = donations;
		this.orders = orders;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Donation> getDonations() {
		return donations;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
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
		DistributionCenter other = (DistributionCenter) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
			StringBuilder sb = new StringBuilder();

			sb.append("Centro de distribuição:\n");
			sb.append("  ID:         ").append(getId()).append("\n");
			sb.append("  Nome:       ").append(getName()).append("\n");
			sb.append("  Endereço:   ").append(getAddress()).append("\n");


			return sb.toString();
		}
	}
	
	
	
	
	


