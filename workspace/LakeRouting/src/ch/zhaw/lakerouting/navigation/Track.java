package ch.zhaw.lakerouting.navigation;

import ch.zhaw.lakerouting.interpolation.algorithms.Coordinate;

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
		this.trackLong = -Math.cos(crd2.getLatitude())
				* Math.sin(crd1.getLongitude() - crd2.getLongitude());
	}

	public double getTrackLat() {
		return trackLat;
	}

	public void setTrackLat(Coordinate crd1, Coordinate crd2) {
		this.trackLat = (Math.cos(crd1.getLatitude())* Math.sin(crd2.getLatitude()))
				- (Math.cos(crd1.getLongitude() - crd2.getLongitude()) * Math.cos(crd2.getLatitude()) * Math.sin(crd1.getLatitude()));
	}
}
