package acs.boundaries;

import java.io.Serializable;

public class ActionID implements Comparable<ActionID> , Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3450663907976046802L;
	private String domain;
	private String id;
	
	public ActionID(String domain, String id) {
		super();
		this.domain = domain;
		this.id = id;
	}
	
	public ActionID() {
		super();
	}
 
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public int compareTo(ActionID actionId) {
		if (this.domain.equals(actionId.getDomain()) && this.id.equals(actionId.getID()))
			return 0;
		else return 1;
	}
	
	@Override
	public String toString() {
		return "ActionId [domain=" + domain + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionID other = (ActionID) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}


