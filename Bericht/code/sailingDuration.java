public double getSailingDuration(Coordinate crd1, Coordinate crd2, WindVector v1, WindVector v2, double distance) {

	double speed = sp.speedBoat(crd1, crd2, v1) + sp.speedBoat(crd1, crd2, v2);
		
	try {
		return (2 * distance) / speed;
	} catch (ArithmeticException e) {
		logger.error("Tried to divide by zero. So it returned zero.");
		return 0.0;
	}
}