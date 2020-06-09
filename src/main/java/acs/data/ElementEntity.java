package acs.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import acs.data.entityProperties.*;

@Document(collection = "ELEMENTS")
public class ElementEntity {

	@Id
	private String elementId;
	private String type;
	private String name;
	private Boolean active;
	private Date creacteTimestamp;
	private UserIdWrapperEntity createdBy;
	private LocationEntity location;
	private HashMap<String,Object> elementAttributes;
	// add another entity collection related to this one using ONE-TO-MANY relationship
	@DBRef(lazy = true)
	private Set<ElementEntity> childrens;
	// add another entity related to this one using MANY-TO-ONE relationship
	@DBRef(lazy = true)
	private ElementEntity parent;
	
	public ElementEntity() {
		super();
	}

	public ElementEntity(String elementId, String type, String name, Boolean active, Date creacteTimestamp,
			UserIdWrapperEntity createdBy, LocationEntity location, HashMap<String,Object> elementAttributes) {
		super();
		this.elementId = elementId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.creacteTimestamp = creacteTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.elementAttributes = elementAttributes;
	}
	
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
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
	
	public Date getCreacteTimestamp() {
		return creacteTimestamp;
	}

	public void setCreacteTimestamp(Date creacteTimestamp) {
		this.creacteTimestamp = creacteTimestamp;
	}

	
	public UserIdWrapperEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserIdWrapperEntity createdBy) {
		this.createdBy = createdBy;
	}

	public LocationEntity getLocation() {
		return location;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
	}

	public HashMap<String,Object> getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(HashMap<String,Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
	}

	public Set<ElementEntity> getChildrens() {
		return childrens;
	}
	
	public void addChild(ElementEntity element) {
		this.childrens.add(element);
		element.setParent(this);
	}

	public void setChildrens(Set<ElementEntity> childrens) {
		this.childrens = childrens;
	}

	public ElementEntity getParent() {
		return parent;
	}

	public void setParent(ElementEntity parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "ElementEntity [elementId=" + elementId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", creacteTimestamp=" + creacteTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", elementAttributes=" + elementAttributes + "]";
	}


	
	
}
