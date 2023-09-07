package org.jsp.api.service;
import java.util.List;
import java.util.Optional;

import org.jsp.api.dao.MerchantDao;
import org.jsp.api.dao.ProductDao;
import org.jsp.api.dao.UserDao;
import org.jsp.api.dto.Merchant;
import org.jsp.api.dto.Product;
import org.jsp.api.exception.IdNotFoundException;
import org.jsp.api.dto.ResponseStructure;
import org.jsp.api.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ProductService {
@Autowired
private ProductDao pdao;
@Autowired
private MerchantDao mdao;
@Autowired
private UserDao udao;



public ResponseEntity<ResponseStructure<Product>> saveProduct(@RequestBody Product product, int mid) {
	ResponseStructure<Product> structure=new ResponseStructure<>();
	Optional<Merchant> recMerchant=mdao.findById(mid);
	if(recMerchant.isPresent()) {
		recMerchant.get().getProduct().add(product);
		product.setMerchant(recMerchant.get());
		pdao.saveProduct(product);
		mdao.updateMerchant(recMerchant.get());
		structure.setData(product);
		structure.setMessage("Product added");
		structure.setStatusCode(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<Product>>(structure,HttpStatus.CREATED);
	}
	
	throw new IdNotFoundException();
}

public ResponseEntity<ResponseStructure<Product>> updateProduct(@RequestBody Product product,int mid) {
	ResponseStructure<Product> structure=new ResponseStructure<>();
	Optional<Merchant> recMerchant=mdao.findById(mid);
	if(recMerchant.isPresent()) {
		recMerchant.get().getProduct().add(product);
		product.setMerchant(recMerchant.get());
		mdao.updateMerchant(recMerchant.get());
		structure.setData(pdao.updateProduct(product));
		structure.setMessage("Product updated");
		structure.setStatusCode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<Product>>(structure,HttpStatus.ACCEPTED);
	}
	
	throw new IdNotFoundException();
}

public ResponseEntity<ResponseStructure<Product>> findById(int id) {
	ResponseStructure<Product> structure=new ResponseStructure<>();
	Optional<Product> recProduct=pdao.findById(id);
	if(recProduct.isPresent()) {
		structure.setData(recProduct.get());
		structure.setMessage("Product found");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<Product>>(structure,HttpStatus.OK);
	}
	
	throw new IdNotFoundException();
}

public ResponseEntity<ResponseStructure<String>> deleteProduct(int id) {
	ResponseStructure<String> structure=new ResponseStructure<>();
	Optional<Product> recProduct=pdao.findById(id);
	if(recProduct.isPresent()) {
		pdao.deleteProduct(id);
		structure.setMessage("Product deleted");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
	}
	
	throw new IdNotFoundException();
}

public ResponseEntity<ResponseStructure<List<Product>>> findProductsByMerchantId(int merchant_id) {
	ResponseStructure<List<Product>> structure=new ResponseStructure<>();
		structure.setData(pdao.findProductsByMerchantId(merchant_id));
		structure.setMessage("Products loaded");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<List<Product>>>(structure,HttpStatus.OK);
	}

public ResponseEntity<ResponseStructure<String>> addToCart(int product_id,int user_id) {
	Optional<User>recUser=udao.findById(user_id);
	Optional<Product>recProduct=pdao.findById(product_id);
	ResponseStructure<String> structure=new ResponseStructure<>();
	if(recUser.isPresent()&& recProduct.isPresent()) {
		recUser.get().getCart().add(recProduct.get());
		udao.updateUser(recUser.get());
		structure.setData("Product added to the cart");
		structure.setMessage("User and product found");
		structure.setStatusCode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.ACCEPTED);
	}
	structure.setData(" cannot added Product to the cart");
	structure.setMessage("User and product not found");
	structure.setStatusCode(HttpStatus.NOT_FOUND.value());
	return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.NOT_FOUND);

	}

	public ResponseEntity<ResponseStructure<String>> addToWishList(int product_id,int user_id) {
	Optional<User>recUser=udao.findById(user_id);
	Optional<Product>recProduct=pdao.findById(product_id);
	ResponseStructure<String> structure=new ResponseStructure<>();
	if(recUser.isPresent()&& recProduct.isPresent()) {
		recUser.get().getWishlist().add(recProduct.get());
		udao.updateUser(recUser.get());
		structure.setData("Product added to the wishlist");
		structure.setMessage("User and product found");
		structure.setStatusCode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.ACCEPTED);
	}
	throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<Product>> rateProduct(int product_id,int user_id,double rating){
		ResponseStructure<Product> structure=new ResponseStructure<>();
		Optional<User> recUser=udao.findById(user_id);
		Optional<Product> recProduct=pdao.findById(product_id);
		if(recUser.isPresent() && recProduct.isPresent()) {
			Product p=recProduct.get();
			int n=p.getNo_of_users();
			double r=p.getRating() * n++;
			rating=(r + rating)/n;
			p.setNo_of_users(n);
			p.setRating(rating);
			pdao.updateProduct(p);
			structure.setData(p);
			structure.setMessage("Product Rated");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure,HttpStatus.ACCEPTED);
		}
		throw new IdNotFoundException();
	}
}