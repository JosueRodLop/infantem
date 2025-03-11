
package com.isppG8.infantem.infantem.auth.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private String username;
	private String roles;

	public JwtResponse(String accessToken, String username, String roles) {
		this.token = accessToken;
		this.username = username;
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", type=" + type + ", username=" + username
				+ ", roles=" + roles + "]";
	}

}
