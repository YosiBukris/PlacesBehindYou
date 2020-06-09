package acs.boundaries;

import java.util.Date;
import java.util.HashMap;

public class ActionBoundary {
	private ActionID actionId;
	private String type;
	private ElementIdWrapper element;
	private Date createdTimestamp;
	private UserIdWrapper invokedBy; 
	private HashMap<String,Object> actionAttributes;
	
	public ActionBoundary() {
		super();
	}

	public ActionBoundary(ActionID actionId, String type, ElementIdWrapper element, Date createdTimestamp,
			UserIdWrapper invokedBy, HashMap<String,Object> actionAttributes) {
		super();
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
	}

	public ActionID getActionId() {
		return actionId;
	}

	public void setActionId(ActionID actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ElementIdWrapper getElement() {
		return element;
	}

	public void setElement(ElementIdWrapper element) {
		this.element = element;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public UserIdWrapper getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(UserIdWrapper invokedBy) {
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
		return "ActionBoundary [actionId=" + actionId + ", type=" + type + ", element=" + element
				+ ", createdTimestamp=" + createdTimestamp + ", invokedBy=" + invokedBy + ", actionAttributes="
				+ actionAttributes + "]";
	}
	
}
