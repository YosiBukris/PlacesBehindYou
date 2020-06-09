package acs.data.entityProperties;


public class UserIdWrapperEntity {
	private UserIDEntity userId;
	
	public UserIDEntity getUserId() {
		return userId;
	}

	public void setUserId(UserIDEntity userId) {
		this.userId = userId;
	}

	public UserIdWrapperEntity(UserIDEntity userId) {
		super();
		this.userId = userId;
	}
	
	public UserIdWrapperEntity() {
		super();
	}
	
	
}
