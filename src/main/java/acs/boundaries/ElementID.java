package acs.boundaries;

import java.io.Serializable;


public class ElementID implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1762135935443154664L;
	private String domain;
	private String id;
	
	public ElementID(String domain, String id) {
		super();
		this.domain = domain;
		this.id = id;
	}
	
	public ElementID() {
		super();
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		ElementID other = (ElementID) obj;
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

	@Override
	public String toString() {
		return "ElementID [domain=" + domain + ", id=" + id + "]";
	}

}
