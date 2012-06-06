public class WindVector {
    /** Just a limiter for preventing completely unreasonable values */
    private static final int MAXWIND = 180;
    /** u is the unit vector with value (1,0) pointing to the north */
    private double u;
    /** v is the unit vector with value (0,1) pointing to the east */
    private double v;
    private Logger logger = Logger.getLogger(this.getClass());
    
    private void validateInput (double input) {
        if (Math.abs(input) > MAXWIND){
        	logger.error("Input value too high: max " + MAXWIND + "kts.");
            throw new IllegalArgumentException("Input value too high: max " + MAXWIND + "kts.");
        }
    }

    public WindVector (double i, double j) {
        setU(i);
        setV(j);
    }

    public final double getU() {
        return u;
    }

    public final void setU(double input) {
        validateInput(input);
        this.u = input;
    }

    public final double getV() {
        return v;
    }

    public final void setV(double input) {
        validateInput(input);
        this.v = input;
    }

    public final double getWindspeed() {
        return Math.sqrt(Math.pow(getU(),2) + Math.pow(getV(),2));
    }

    public final double getAngle() {
        /**
         * Assume: (1,0) is the vector pointing to the North
         * Assume: (u,v) is our wind vector
         * dot-product is defined as Sum(i=0 -> n) {A_i * B_i}
         * We simplify: 1*u + 0*v = u
         */
        return Math.acos( ( getU() ) / getWindspeed() );
    }

    public final String toString() {
        String s = "";

        s += "u: " + getU() + ", ";
        s += "v: " + getV() + ", ";
        s += "angle: " + getAngle() + ", ";
        s += "velocity: " + getWindspeed() + " kts";
        
        return s;
    }

    public final String mathematicaVectorOutput() {
        String s = "";
        return s += "{" + getU() + "," + getV() + "}";
    }
}
