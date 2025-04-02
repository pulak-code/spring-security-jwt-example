package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.MessageUserServiceConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class BaseUserRequest {
	
	@NotBlank(message = MessageUserServiceConstants.EMAIL_IS_REQUIRED)
	@Email(message = MessageUserServiceConstants.INVALID_EMAIL_FORMAT)
	private String email;

	@NotBlank(message = MessageUserServiceConstants.PASSWORD_IS_REQUIRED)
	private String password;

	protected BaseUserRequest() {
	}

	protected BaseUserRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
