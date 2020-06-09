package acs.boundaries;

import java.util.Date;
import java.util.HashMap;

public class ElementBoundary {

	private ElementID elementId;
	private String type;
	private String name;
	private Boolean active;
	private Date createTimestamp;
	private UserIdWrapper createdBy;
	private Location location;
	private HashMap<String,Object> elementAttributes;
	
	public ElementBoundary() {
		super();
		
	}
	
	public ElementBoundary(ElementID elementId, String type, String name, Boolean active, Date createTimestamp,
			UserIdWrapper createdBy, Location location, HashMap<String,Object> elementAttributes) {
		super();
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createTimestamp = createTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.elementAttributes = elementAttributes;
	}


	public ElementID getElementId() {
		return elementId;
	}

	public void setElementId(ElementID elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date creacteTimestamp) {
		this.createTimestamp = creacteTimestamp;
	}

	public UserIdWrapper getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserIdWrapper createdBy) {
		this.createdBy = createdBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public HashMap<String,Object> getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(HashMap<String,Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}

	@Override
	public String toString() {
		return "ElementBoundary [elementId=" + elementId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", creacteTimestamp=" + createTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", elementAttributes=" + elementAttributes + "]";
	}
	
	
	
	
}