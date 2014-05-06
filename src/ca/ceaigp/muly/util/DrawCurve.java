package ca.ceaigp.muly.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.List;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.XYGraph;

import edu.sc.seis.TauP.MatTauP_Curve;
import edu.sc.seis.TauP.TT_Curve;

public class DrawCurve
{
	private XYGraph swtFigure;

	public DrawCurve(final XYGraph swtFigure)
    {
		/*
		x1Axis.setTitle("Time");
		y1Axis.setTitle("Dist");
		x1Axis.setAutoScale(true);
		y1Axis.setAutoScale(true);
		x1Axis.setShowMajorGrid(true);
		y1Axis.setShowMajorGrid(true);
		
		swtFigure.addAxis(x1Axis);
		swtFigure.addAxis(y1Axis);
		//swtFigure.primaryXAxis.setAutoFormat(true);
		swtFigure.getPlotArea().setShowBorder(true);
		*/
		swtFigure.primaryXAxis.setTitle("Time");
		swtFigure.primaryYAxis.setTitle("Dist");
		swtFigure.primaryYAxis.setAutoScale(true);
		swtFigure.primaryXAxis.setAutoScale(true);
		swtFigure.primaryXAxis.setShowMajorGrid(true);
		swtFigure.primaryYAxis.setShowMajorGrid(true);
		//swtFigure.primaryXAxis.setAutoFormat(true);
		swtFigure.getPlotArea().setShowBorder(true);
		
		this.swtFigure = swtFigure;
    }
	
	public void createCurve(String[] travelArgs)
	{
		// Taup Draw Curve
		String[] curveArgs = new String[6];
		curveArgs[0] = "-mod";
		curveArgs[1] = travelArgs[0];
		curveArgs[2] = "-h";
		curveArgs[3] = travelArgs[1];
		curveArgs[4] = "-ph";
		curveArgs[5] = travelArgs[2];
				
		TT_Curve[] ttcurve = null;
		try
		{
			ttcurve = MatTauP_Curve.run_curve(curveArgs);
		}
		catch (OptionalDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (StreamCorruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (TT_Curve ttc : ttcurve)
		{
			float[] dists = new float[ttc.dist.length];
			float[] times = new float[ttc.time.length];

			double[] tempfd = ttc.dist;
			for (int i = 0; i < ttc.dist.length; i++)
			{
				Double fd = new Double(tempfd[i]);
				dists[i] = fd.floatValue();
			}
			tempfd = ttc.time;
			for (int i = 0; i < ttc.time.length; i++)
			{
				Double fd = new Double(tempfd[i]);
				times[i] = fd.floatValue();
				//System.out.println("Time: " + times[i]);
			}

			CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(false);
			traceDataProvider.setBufferSize(9000);
			traceDataProvider.setCurrentXDataArray(times);
			traceDataProvider.setCurrentYDataArray(dists);

			Trace trace1 = new Trace(ttc.phaseName + "_" + ttc.sourceDepth, swtFigure.primaryXAxis, swtFigure.primaryYAxis, traceDataProvider);
			//Trace trace1 = new Trace(ttc.phaseName + "_" + ttc.sourceDepth, x1Axis, y1Axis, traceDataProvider);
			this.swtFigure.addTrace(trace1);
		}
	}
	
	public void createCurve(String model, String phaseList, int depthMin, int depthMax, int step)
	{
		String[] travelArgs = new String[3];
		
		for(int i = depthMin; i <= depthMax; i+=step )
		{
			travelArgs[0] = model;
			travelArgs[1] = String.valueOf(i);
			travelArgs[2] = phaseList;
			createCurve(travelArgs);
		}
	}
}
