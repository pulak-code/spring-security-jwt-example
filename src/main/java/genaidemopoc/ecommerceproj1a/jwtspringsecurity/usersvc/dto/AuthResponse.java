package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

public class AuthResponse {
	private String accessToken;
	private String refreshToken;
	private String msg;

	public AuthResponse(String accessToken, String refreshToken,String msg) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.msg=msg;
	}
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}