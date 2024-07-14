package com.compass.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.compass.model.enums.ItemType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_SHELTER")
public class Shelter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String address;
	private String responsible;

	@Column(name = "phone_number")
	private String phoneNumber;

	private String email;

	@OneToMany(mappedBy = "shelter")
	private List<Order> orders = new ArrayList<>();
	
	@OneToMany(mappedBy = "shelter")
	private List<Donation> donations = new ArrayList<>();
	
	public Shelter() {
		
	}

	public Shelter(Long id, String name, String address, String responsible, String phoneNumber, String email,
			List<Order> orders) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.responsible = responsible;
		this.phoneNumber = phoneNumber;
		this.email = email;
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

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Donation> getDonations() {
		return donations;
	}

	public void setDonations(List<Donation> donations) {
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
		Shelter other = (Shelter) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Shelter:\n");
	    sb.append("  ID:              ").append(id).append("\n");
	    sb.append("  Nome:            ").append(name).append("\n");
	    sb.append("  Endereço:        ").append(address).append("\n");
	    sb.append("  Responsável:     ").append(responsible).append("\n");
	    sb.append("  Telefone:        ").append(phoneNumber).append("\n");
	    sb.append("  Email:           ").append(email).append("\n");

	    // Opcional: Exibir informações sobre pedidos e doações
	    if (orders != null && !orders.isEmpty()) {
	        sb.append("  Pedidos (IDs):   ");
	        for (Order order : orders) {
	            sb.append(order.getId()).append(", ");
	        }
	        sb.setLength(sb.length() - 2); // Remover a última vírgula
	        sb.append("\n");
	    }

	    if (donations != null && !donations.isEmpty()) {
	        sb.append("  Doações (IDs):   ");
	        for (Donation donation : donations) {
	            sb.append(donation.getId()).append(", ");
	        }
	        sb.setLength(sb.length() - 2); // Remover a última vírgula
	        sb.append("\n");
	    }
	    
	    // Calcula as quantidades por tipo de item
	    Map<ItemType, Integer> itemQuantities = new EnumMap<>(ItemType.class);
	    for (Donation donation : donations) {
	        ItemType itemType = donation.getLot().getItemType();
	        int quantity = donation.getLot().getQuantity();
	        itemQuantities.put(itemType, itemQuantities.getOrDefault(itemType, 0) + quantity);
	    }

	    // Exibe as quantidades por tipo de item
	    if (!itemQuantities.isEmpty()) {
	        sb.append("  Quantidades por Tipo de Item:\n");
	        for (Map.Entry<ItemType, Integer> entry : itemQuantities.entrySet()) {
	            sb.append("    ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
	        }
	    }

	    return sb.toString();
	}
	
	

}
