package acs.data.entityProperties;

import java.util.UUID;

public class Comment {
	private String userName;
	private String comment;
	private String id;

	public Comment(String userName, String comment) {
		super();
		this.userName = userName;
		this.comment = comment;
		this.id = UUID.randomUUID().toString();
	}

	public Comment() {

	}

	public String getUserName() {
		return userName;
	}

	public String getComment() {
		return comment;
	}

	public String getId() {
		return id;
	}

}
