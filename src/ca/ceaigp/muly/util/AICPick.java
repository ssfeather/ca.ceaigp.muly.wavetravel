package ca.ceaigp.muly.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class AICPick
{
	public static int pick(double[] v, int off, int len)
	{
		if (len < 3) return -1;
		double[] aic = new double[len];
		for (int k = 0; k < len; k++)
		{
			double var1 = variance(v, off, k);
			if ((Double.isNaN(var1)) || (var1 <= 1.0E-08D))
			{
				aic[k] = (0.0D / 0.0D);
			}
			else
			{
				double var2 = variance(v, off + k, len - k);
				if ((Double.isNaN(var2)) || (var2 <= 1.0E-08D))
				{
					aic[k] = (0.0D / 0.0D);
				}
				else aic[k] = (k * Math.log(var1) + (len - k - 1) * Math.log(var2));
			}
		}
		return off + findMinIndex(aic, 0, len);
	}

	private static double variance(double[] v, int off, int len)
	{
		if (len < 2) return (0.0D / 0.0D);
		double m = mean(v, off, len);
		double ans = 0.0D;
		for (int i = off; i < off + len; i++)
			ans += (v[i] - m) * (v[i] - m);
		return ans / (len - 1);
	}

	private static double mean(double[] v, int off, int len)
	{
		if (len < 1)
		{
			return (0.0D / 0.0D);
		}
		double sum = 0.0D;
		for (int k = off; k < off + len; k++)
		{
			sum += v[k];
		}
		return sum / len;
	}

	private static int findMinIndex(double[] v, int off, int len)
	{
		int minIdx = off;
		double min = 1.7976931348623157E+308D;
		for (int i = off; i < off + len; i++)
		{
			if ((!Double.isNaN(v[i])) && (v[i] < min))
			{
				min = v[i];
				minIdx = i;
			}
		}
		return minIdx;
	}

	public static void main(String[] args) throws IOException
	{
		String fname = "TestData/AICPicker/GT_LBTB_BHZ_2.txt";
		BufferedReader br = new BufferedReader(new FileReader(fname));
		int count = 0;
		String line = null;
		while ((line = br.readLine()) != null)
			count++;
		br.close();

		double[] v = new double[count];
		br = new BufferedReader(new FileReader(fname));
		for (int i = 0; i < count; i++)
		{
			line = br.readLine();
			v[i] = Double.parseDouble(line.trim());
		}
		br.close();

		System.out.println(pick(v, 2200, 1000));
	}
}
