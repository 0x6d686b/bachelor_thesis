package ch.zhaw.lakerouting.datatypes;


public class Track {

	private double trackLong;
	private double trackLat;

	public Track(Coordinate crd1, Coordinate crd2) {
		setTrackLong(crd1, crd2);
		setTrackLat(crd1, crd2);
	}

	public double getTrackLong() {
		return trackLong;
	}

	public void setTrackLong(Coordinate crd1, Coordinate crd2) {
		this.trackLong = -Math.cos(crd2.getLatitudeInRadian())
				* Math.sin(crd1.getLongitudeInRadian() - crd2.getLongitudeInRadian());
	}

	public double getTrackLat() {
		return trackLat;
	}

	public void setTrackLat(Coordinate crd1, Coordinate crd2) {
		this.trackLat = (Math.cos(crd1.getLatitudeInRadian())* Math.sin(crd2.getLatitudeInRadian()))
				- (Math.cos(crd1.getLongitudeInRadian() - crd2.getLongitudeInRadian()) * Math.cos(crd2.getLatitudeInRadian()) * Math.sin(crd1.getLatitudeInRadian()));
	}
	
	public double getTrackLength(){
		return Math.sqrt(Math.pow(getTrackLong(),2) + Math.pow(getTrackLat(),2));
	}
}
