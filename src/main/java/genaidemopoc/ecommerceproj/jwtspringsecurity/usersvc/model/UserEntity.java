package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "users")
public class UserEntity {
    
    @Id
    private String id;
    
	@NotBlank(message = "Name cannot be empty")
    private String name;
    
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	@Field("email")
    private String email;
    
	@NotBlank(message = "Password cannot be empty")
    private String password;
    
	private List<Address> addresses;
	@Field("roles")
	private List<String> roles;
	private List<String> orderIds;

	public UserEntity() {

	}

	public UserEntity(String name, String email, String password, List<Address> addresses, List<String> orderIds,
			List<String> roles) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.addresses = addresses;
		this.orderIds = orderIds;
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public List<String> getOrderIds() {
		return orderIds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public void setOrderIds(List<String> orderIds) {
		this.orderIds = orderIds;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
