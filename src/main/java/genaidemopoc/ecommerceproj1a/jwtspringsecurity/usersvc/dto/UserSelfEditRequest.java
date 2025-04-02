package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

import java.util.List;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserSelfEditRequest {

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password cannot be empty")
	private String password;

	// Allow users to update their addresses
	private List<Address> addresses;

	// Getters and Setters

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
}
