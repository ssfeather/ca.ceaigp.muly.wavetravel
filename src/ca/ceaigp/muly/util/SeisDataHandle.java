package ca.ceaigp.muly.util;


public class SeisDataHandle
{
	/** Invalid data value. */
	public static final double INVALID_AMPLITUDE = -Double.MAX_VALUE;

	/** The sampling interval in seconds. */
	public double sampleInt;
	/** The lag time in seconds (currently unused). */
	public double lagTime = 0.0;
	/** The amplitude units. */
	//public String ampUnits = PhysicalUnits.UNKNOWN;
	/** The time units (must be "Seconds"). */
	//public String timeUnits = PhysicalUnits.UNKNOWN;

	// calculated fields
	/** The minimum amplitude in the time series. */
	public double ampMin;
	/** The maximum amplitude in the time series. */
	public double ampMax;
	/** The mean amplitude value of the time series. */
	// protected double sampleMean = 0.0;
	/** The previous mean amplitude value of the time series. */
	protected double lastSampleMean = INVALID_AMPLITUDE;

	public static final float RANGE_MIN = -Float.MAX_VALUE;
	public static final float RANGE_MAX = Float.MAX_VALUE;

	public float outOfRangeMin = RANGE_MIN;
	public float outOfRangeMax = RANGE_MAX;

	/** float array to contain the time series samples */
	public float[] sample = null;

	/**
	 * Removes the mean of the time series samples.
	 * 
	 */
	public final void removeMean()
	{

		removeMean(sample.length);

	}

	/**
	 * Removes the mean of the time series samples. updates ampMin, ampMax.
	 * 
	 * @param nPointsFromBegin
	 *            the number of points from beginning of time series to use to
	 *            calculate mean.
	 * 
	 * @see sampleMean
	 */
	public void removeMean(int nPointsFromBegin)
	{

		int npoints = nPointsFromBegin;
		if (npoints < 0) return;
		if (npoints > sample.length) npoints = sample.length;

		double sampleMean = 0.0;
		for (int nsamp = 0; nsamp < npoints; nsamp++)
			sampleMean += (double) sample[nsamp];
		sampleMean = sampleMean / (double) npoints;
		lastSampleMean = INVALID_AMPLITUDE;

		for (int nsamp = 0; nsamp < sample.length; nsamp++)
			sample[nsamp] -= (float) sampleMean;

		ampMin -= sampleMean;
		ampMax -= sampleMean;

		lastSampleMean = sampleMean;
		// sampleMean = 0.0;

	}
	
	/** Undo the last remove mean.
    *
    */
   public final void unDoRemoveMean() {
       
       if (lastSampleMean == INVALID_AMPLITUDE)
           return;
       
       for (int nsamp = 0; nsamp < sample.length; nsamp++)
           sample[nsamp] += (float) lastSampleMean;
       
       ampMin += lastSampleMean;
       ampMax += lastSampleMean;
       
       //sampleMean = lastSampleMean;
       lastSampleMean = INVALID_AMPLITUDE;
       
   }
}
