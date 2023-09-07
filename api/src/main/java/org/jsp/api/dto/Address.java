package org.jsp.api.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int house_no;
	private String building_name;
	private String area;
	private String city;
	private String state;
	private String country;
	private String landmark;
	private int pincode;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}