package acs.data.entityProperties;

public class ActionAttributesEntity {
	private String key1;
	private String key2;
	private Boolean booleanValue;
	private String lastKey;
	
	public ActionAttributesEntity(String key1, String key2, Boolean booleanValue, String lastKey) {
		super();
		this.key1 = key1;
		this.key2 = key2;
		this.booleanValue = booleanValue;
		this.lastKey = lastKey;
	}
	
	public ActionAttributesEntity() {
		
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getLastKey() {
		return lastKey;
	}

	public void setLastKey(String lastKey) {
		this.lastKey = lastKey;
	}
	
	
}


