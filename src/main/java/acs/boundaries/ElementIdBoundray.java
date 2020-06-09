package acs.boundaries;


public class ElementIdBoundray {
	
	private String domain;
	private String id;
	
	public ElementIdBoundray(String domain, String id) {
		super();
		this.domain = domain;
		this.id = id;
	}

	public ElementIdBoundray() {
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
	public String toString() {
		return "ElementIdBoundray [domain=" + domain + ", id=" + id + "]";
	}
	
}