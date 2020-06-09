package acs.data.entityProperties;

public class LocationEntity {

	private float lat;
	private float lng;

	public LocationEntity(float lat, float lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}
	
	public LocationEntity() {
		super();
	}
	 
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getlng() {
		return lng;
	}
	public void setlng(float lng) {
		this.lng = lng;
	}

}
