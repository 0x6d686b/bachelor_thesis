public class Coordinate {

    /** Restricting range of longitude */
    private static final double MIN_LONGITUDE = -180.0;
    /** Restricting range of longitude */
    private static final double MAX_LONGITUDE = 180.0;
    /** Restricting range of latitude */
    private static final double MIN_LATITUDE = -90.0;
    /** Restricting range of latitude */
    private static final double MAX_LATITUDE = 90.0;

    private Logger logger = Logger.getLogger(this.getClass());  
    private BigDecimal longitude;
    private BigDecimal latitude;

    public final String toString() {
        return "Longitude: " + longitude + " degree, Latitude: " + latitude;
    }

    public final double getLongitudeInDegree() {
        return longitude.doubleValue();
    }

    public final void setLongitudeInDegree(double l) {
        if (l > MAX_LONGITUDE || l <= MIN_LONGITUDE){
        	logger.error("Longitude is out of range.");
            throw new IllegalArgumentException("Longitude is out of range.");
        }
        this.longitude = BigDecimal.valueOf(l);
    }

    public final double getLatitudeInDegree() {
        return latitude.doubleValue();
    }

    public final void setLatitudeInDegree(double l) {
        if (l > MAX_LATITUDE || l < MIN_LATITUDE){
        	logger.error("Latitude is out of range.");
        	throw new IllegalArgumentException("Latitude is out of range.");
        }
        this.latitude = BigDecimal.valueOf(l);
    }

    public final double getLongitudeInRadian() {
        return getLongitudeInDegree()/180*Math.PI;
    }

    public final void setLongitudeInRadian(double l) {
        setLongitudeInDegree(l/Math.PI*180);
    }

    public final double getLatitudeInRadian() {
        return getLatitudeInDegree()/180*Math.PI;
    }

    public final void setLatitudeInRadian(double l) {
        setLatitudeInDegree(l/Math.PI*180);
    }
}