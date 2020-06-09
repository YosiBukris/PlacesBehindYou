package acs.data;

import java.util.Date;
import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import acs.data.entityProperties.*;

@Document(collection = "ACTIONS")
public class ActionEntity {

	@Id
	private ActionIDEntity actionId;
	private String type;
	private ElementIdWrapperEntity element;
	private Date createdTimestamp;
	private UserIdWrapperEntity invokedBy; 
	private HashMap<String,Object> actionAttributes;
	
	public ActionEntity() {
		super();
	}

	public ActionEntity(ActionIDEntity actionId, String type, ElementIdWrapperEntity element, Date createdTimestamp, 
			UserIdWrapperEntity invokedBy, HashMap<String,Object> actionAttributes) {
		super();
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}


	public ActionIDEntity getActionId() {
		return actionId;
	}

	public void setActionId(ActionIDEntity actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public ElementIdWrapperEntity getElement() {
		return element;
	}

	public void setElement(ElementIdWrapperEntity element) {
		this.element = element;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public UserIdWrapperEntity getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(UserIdWrapperEntity invokedBy) {
		this.invokedBy = invokedBy;
	}

	public HashMap<String,Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(HashMap<String,Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

	@Override
	public String toString() {
		return "ActionEntity [actionId=" + actionId + ", type=" + type + ", element=" + element + ", createdTimestamp="
				+ createdTimestamp + ", invokedBy=" + invokedBy + ", actionAttributes=" + actionAttributes + "]";
	}
	
}
