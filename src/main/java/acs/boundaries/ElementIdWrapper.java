package acs.boundaries;


public class ElementIdWrapper {
	
	public ElementIdWrapper() {
		super();
	}
	
	
	public ElementIdWrapper(ElementID elementId) {
		super();
		this.elementId = elementId;
	}

	private ElementID elementId;

	public ElementID getElementId() {
		return elementId;
	}

	public void setElementId(ElementID elementId) {
		this.elementId = elementId;
	}


}
