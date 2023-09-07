package org.jsp.api.dto;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
private String name;
private long phone;
private String email;
private String password;
private String token;
private String status;
@OneToMany
@JoinTable(name="user_Cart",joinColumns=@JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="product_id"))
private List<Product> cart;

@OneToMany
@JoinTable(name="user_WishList",joinColumns=@JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="product_id"))
private List<Product> wishlist;

@OneToMany(mappedBy = "user")
private List<Address> addresses;

}
