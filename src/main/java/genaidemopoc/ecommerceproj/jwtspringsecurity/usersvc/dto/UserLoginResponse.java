package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto;

public class UserLoginResponse {

	private String token;
	private String message;

	public UserLoginResponse(String token, String message) {
		this.token = token;
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public String getMessage() {
		return message;
	}
}
