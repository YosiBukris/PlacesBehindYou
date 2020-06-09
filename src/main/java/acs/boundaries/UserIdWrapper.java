package acs.boundaries;

public class UserIdWrapper {
	private UserID userId;
	
	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		this.userId = userId;
	}

	public UserIdWrapper(UserID userId) {
		super();
		this.userId = userId;
	}
	
	public UserIdWrapper() {
		super();
	}
	
	
}
