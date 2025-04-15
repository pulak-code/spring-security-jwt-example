package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto;

import java.util.List;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequest {
	@NotBlank(message = "Name is required")
	private String name;

	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password cannot be empty")
	private String password;

	private List<Address> addresses;

	private List<String> orderIds;
	
	private List<String> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<String> getOrderIds() {		
		return orderIds;
	}

	public List<String> getRoles() {
		return roles;
	}

}
