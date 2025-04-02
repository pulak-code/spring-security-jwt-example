package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

import java.util.List;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.MessageUserServiceConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRegisterRequest extends BaseUserRequest {

	public UserRegisterRequest(
			@NotBlank(message = MessageUserServiceConstants.EMAIL_IS_REQUIRED) @Email(message = MessageUserServiceConstants.INVALID_EMAIL_FORMAT) String email,
			@NotBlank(message = MessageUserServiceConstants.PASSWORD_IS_REQUIRED) String password) {
		super(email, password);
	}

	@NotBlank(message = MessageUserServiceConstants.NAME_IS_REQUIRED)
	private String name;

	private String address;

	private List<String> roles;

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

	public List<String> getRoles() {
		return roles;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
