package ch.zhaw.lakerouting.navigation;


public class Track {

	private double trackLong;
	private double trackLat;

	public Track(double[] crd1, double[] crd2) {
		setTrackLong(crd1, crd2);
		setTrackLat(crd1, crd2);
	}

	public double getTrackLong() {
		return trackLong;
	}

	public void setTrackLong(double[] crd1, double[] crd2) {
		this.trackLong = -Math.cos(crd2[1])
				* Math.sin(crd1[0] - crd2[0]);
	}

	public double getTrackLat() {
		return trackLat;
	}

	public void setTrackLat(double[] crd1, double[] crd2) {
		this.trackLat = (Math.cos(crd1[1])* Math.sin(crd2[1]))
				- (Math.cos(crd1[0] - crd2[0]) * Math.cos(crd2[1]) * Math.sin(crd1[1]));
	}
}
