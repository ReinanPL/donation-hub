package com.compass.model;

import java.io.Serializable;
import java.util.Objects;

import com.compass.model.enums.ItemType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_DONATIONS")
public class Donation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "distribution_center_id")
	private DistributionCenter distributionCenter;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "lot_id")
	private Lot lot;
	
	@ManyToOne
	@JoinColumn(name = "shelter_id")
	private Shelter shelter;


	public Donation() {
		
	}
	
	public Donation(Long id, DistributionCenter distributionCenter, Lot lot) {
		super();
		this.id = id;
		this.distributionCenter = distributionCenter;
		this.lot = lot;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DistributionCenter getDistributionCenter() {
		return distributionCenter;
	}

	public void setDistributionCenter(DistributionCenter distributionCenter) {
		this.distributionCenter = distributionCenter;
	}

	public Lot getLot() {
		return lot;
	}

	public void setLot(Lot lot) {
		this.lot = lot;
	}

	public Shelter getShelter() {
		return shelter;
	}

	public void setShelter(Shelter shelter) {
		this.shelter = shelter;
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
		Donation other = (Donation) obj;
		return Objects.equals(id, other.id);
	}
	

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Donation:\n");
	    sb.append("  ID:               ").append(getId()).append("\n");

	    if (distributionCenter != null) {
	        sb.append("  Centro Dist.:     ").append(getDistributionCenter().getId()).append(" - ").append(getDistributionCenter().getName()).append("\n");
	    }

	    if (shelter != null) {
	        sb.append("  Abrigo:           ").append(getShelter().getId()).append(" - ").append(shelter.getName()).append("\n");
	    }

		sb.append(lot);
	    return sb.toString();
	}


	
	
	
	

}
