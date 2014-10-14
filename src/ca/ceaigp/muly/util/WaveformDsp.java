package ca.ceaigp.muly.util;

public class WaveformDsp
{
	/** Invalid data value. */
	public static final double INVALID_AMPLITUDE = -Double.MAX_VALUE;

	/** The sampling interval in seconds. */
	public double sampleInt;
	/** The lag time in seconds (currently unused). */
	public double lagTime = 0.0;

	// calculated fields
	/** The minimum amplitude in the time series. */
	public double ampMin;
	/** The maximum amplitude in the time series. */
	public double ampMax;
	// /** The mean amplitude value of the time series. */
	// protected double sampleMean = 0.0;
	/** The previous mean amplitude value of the time series. */
	protected double lastSampleMean = INVALID_AMPLITUDE;

	public static final float RANGE_MIN = -Float.MAX_VALUE;
	public static final float RANGE_MAX = Float.MAX_VALUE;

	public float outOfRangeMin = RANGE_MIN;
	public float outOfRangeMax = RANGE_MAX;

	/** float array to contain the time series samples */
	public float[] sample = null;

	
	public WaveformDsp(float[] sampleFloat)
	{
		sample = sampleFloat;
	}
	
	public float[] getSample()
    {
    	return sample;
    }

	/**
	 * Calculates and sets the minimum and maximum amplitude in the complete
	 * time series. Sets ampMin and ampMax.
	 * 
	 */
	public final void calculateAndSetAmplitudeRange(boolean setOutOfRangeValuesToZero)
	{

		RangeDouble range = calculateAmplitudeRange(0, sample.length, setOutOfRangeValuesToZero);
		if (range.min < range.max)
		{
			ampMin = range.min;
			ampMax = range.max;
		}
		else
		{
			ampMin = range.min - 0.5;
			ampMax = range.max + 0.5;
		}

	}

	/**
	 * Calculates and returns the minimum and maximum amplitude in a subset of
	 * the time series samples.
	 * 
	 */
	public final RangeDouble calculateAmplitudeRange(int indexMin, int indexMax, boolean setOutOfRangeValuesToZero)
	{

		RangeFloat range = new RangeFloat(Float.MAX_VALUE, -Float.MAX_VALUE);
		for (int nsamp = indexMin; nsamp < indexMax; nsamp++)
		{
			if (sample[nsamp] > outOfRangeMin && sample[nsamp] < outOfRangeMax)
			{
				range.min = Math.min(sample[nsamp], range.min);
				range.max = Math.max(sample[nsamp], range.max);
			}
			else if (setOutOfRangeValuesToZero)
			{
				sample[nsamp] = 0.0f;
			}
		}

		RangeDouble rangeDouble = new RangeDouble((double) range.min, (double) range.max);
		return (rangeDouble);

	}

	/**
	 * Calculates and returns the rms amplitude in the time series samples.
	 * 
	 */
	public final double calculateRmsAmplitude()
	{

		double rms = 0.0;
		double value = 0.0;
		int nsum = 0;
		for (int nsamp = 0; nsamp < sample.length; nsamp++)
		{
			if (sample[nsamp] > outOfRangeMin && sample[nsamp] < outOfRangeMax)
			{
				value = sample[nsamp];
				rms += value * value;
				nsum++;
			}
		}

		if (nsum > 0)
		{
			rms /= (double) nsum;
			rms = Math.sqrt(rms);
		}

		return (rms);

	}

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

	/**
	 * Undo the last remove mean.
	 * 
	 */
	public final void unDoRemoveMean()
	{

		if (lastSampleMean == INVALID_AMPLITUDE) return;

		for (int nsamp = 0; nsamp < sample.length; nsamp++)
			sample[nsamp] += (float) lastSampleMean;

		ampMin += lastSampleMean;
		ampMax += lastSampleMean;

		// sampleMean = lastSampleMean;
		lastSampleMean = INVALID_AMPLITUDE;

	}
}
