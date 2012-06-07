/* Call the method progrDyn to calculate the distances in TOA */
	for (int i = 1; i < getMaxi(); i++) {
		progrDyn(i, windfieldNo);
	}