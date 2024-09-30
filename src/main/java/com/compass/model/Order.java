package com.compass.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.compass.model.enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="TB_ORDER")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    @OneToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "refusal_motive")
	private String refusalMotive;
    
    private LocalDate date;
    
	@ManyToOne
	@JoinColumn(name = "distribution_center_id")
	private DistributionCenter distributionCenter;
    
    public Order() {
    	
    }

	public Order(Shelter shelter, Lot lot, OrderStatus status, String refusalMotive, LocalDate date, DistributionCenter distributionCenter) {
		super();
		this.shelter = shelter;
		this.lot = lot;
		this.status = status;
		this.refusalMotive = refusalMotive;
		this.date = date;
		this.distributionCenter = distributionCenter;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Shelter getShelter() {
		return shelter;
	}

	public void setShelter(Shelter shelter) {
		this.shelter = shelter;
	}

	public Lot getLot() {
		return lot;
	}

	public void setLot(Lot lot) {
		this.lot = lot;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getRefusalMotive() {
		return refusalMotive;
	}

	public void setRefusalMotive(String refusalMotive) {
		this.refusalMotive = refusalMotive;
	}
	
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public DistributionCenter getDistributionCenter() {
		return distributionCenter;
	}

	public void setDistributionCenter(DistributionCenter distributionCenter) {
		this.distributionCenter = distributionCenter;
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
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Order:\n");
	    sb.append("  ID:              ").append(getId()).append("\n");
	    sb.append("  Abrigo:          ").append(getShelter().getId()).append(" - ").append(getShelter().getName()).append("\n");
		sb.append("  Data do pedido:  ").append(getDate()).append("\n");
		sb.append("  Status:          ").append(getStatus()).append("\n");
	    sb.append(lot.getDonations()).append("\n");
	    if (refusalMotive != null) { 
	        sb.append("  Motivo da Recusa: ").append(getRefusalMotive()).append("\n");
	    }

		return sb.toString();
	}
    
    
	

}
