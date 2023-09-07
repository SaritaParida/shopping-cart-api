package org.jsp.api.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.jsp.api.dao.UserDao;
import org.jsp.api.dto.EmailConfiguration;
import org.jsp.api.dto.ResponseStructure;
import org.jsp.api.dto.User;
import org.jsp.api.exception.IdNotFoundException;
import org.jsp.api.exception.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
	@Autowired
	private GenerateLinkService service;
	@Autowired
	private UserDao dao;
	@Autowired
	private EmailConfiguration configuration;
	@Autowired
	private ShoppingCartMailService mailService;

	public ResponseEntity<ResponseStructure<User>> saveUser(User user, HttpServletRequest request) {
		ResponseStructure<User> structure=new ResponseStructure<>();
		structure.setData(dao.saveUser(user));
		structure.setMessage("User Register Succesfuly");
		structure.setStatusCode(HttpStatus.CREATED.value());
		configuration.setSubject("Registration succesful");
		HashMap<String, String> map = new LinkedHashMap<>();
		map.put("email", user.getEmail());
		map.put("name", user.getName());
		configuration.setText("Hello Mis." + user.getName()
				+ "You have succesfully intiated the register for Our E-commerce application"+"please click on the link"+service.getVerificationLink(request, user));
		configuration.setUser(map);
		mailService.sendMail(configuration);
		return  new ResponseEntity<ResponseStructure<User>>(structure,HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<User>> updateUser(User user) {
		ResponseStructure<User> structure=new ResponseStructure<>();
		structure.setData(dao.updateUser(user));
		structure.setMessage("User Updated Succesfully");
		structure.setStatusCode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<ResponseStructure<User>>(structure,HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ResponseStructure<String>> verifyUser(String token) {
		ResponseStructure<String> structure=new ResponseStructure<>();
		User user = dao.verifyUser(token);
		if (user != null) {
			user.setToken(null);
			user.setStatus("Active");
			dao.updateUser(user);
			structure.setData("Your Account is Activated");
			structure.setMessage("User is Verified");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(structure,HttpStatus.OK);
		}
		throw new InvalidCredentialsException();
	}

	public ResponseEntity<ResponseStructure<String>> sendResetPasswordLink(String email, HttpServletRequest request) {
		ResponseStructure<String> structure=new ResponseStructure<>();
		User user = dao.findUserByEmail(email);
		if (user != null) {
			HashMap<String, String> map = new LinkedHashMap<>();
			map.put("email", user.getEmail());
			map.put("name", user.getName());
			configuration.setSubject("Reset password");
			configuration.setUser(map);
			configuration.setText(
					"Hello Mis." + user.getName() + "You have  requested for Password changeplease click on the link"
							+ "please click on the link" + service.getResetPasswordLink(request, user));
			mailService.sendMail(configuration);
			structure.setData("Reset Password Link sent to email");
			structure.setMessage("Mail send to User");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.ACCEPTED);
		}
		throw new InvalidCredentialsException();
	}
	public ResponseEntity<ResponseStructure<User>> verifyUserByEmailAndPassword(String email, String password) {
		Optional<User> recUser = dao.verifyUserByEmailAndPassword(email, password);
		ResponseStructure<User> structure = new ResponseStructure<>();
		if (recUser.isPresent()) {
			structure.setData(recUser.get());
			structure.setMessage("User Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<User>>(structure, HttpStatus.OK);
		}
		throw new InvalidCredentialsException();
	}

}