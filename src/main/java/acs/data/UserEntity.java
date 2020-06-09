package acs.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import acs.data.entityProperties.*;

@Document(collection = "USERS")
public class UserEntity {
	
	@Id
	private UserIDEntity userId;
	private UserRole role;
	private String username;
	private String avatar;
	
	public UserEntity() {
		super();
	}

	
	public UserIDEntity getUserId() {
		return userId;
	}

	public void setUserId(UserIDEntity userId) {
		this.userId = userId;
	}

	public UserRole getRole() {
		return role;
	}

	
	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}

}
