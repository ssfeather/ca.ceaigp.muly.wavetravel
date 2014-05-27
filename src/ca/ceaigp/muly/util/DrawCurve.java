package ca.ceaigp.muly.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.Trace.TraceType;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.swt.xygraph.util.XYGraphMediaFactory;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import edu.sc.seis.TauP.MatTauP_Curve;
import edu.sc.seis.TauP.TT_Curve;

public class DrawCurve
{
	private XYGraph swtFigure;
	private Map<String,RGB> curveColor = new HashMap<String, RGB>();
	int[][] DEFAULT_CURVE_COLOR = 
	{ 		
		{128,0,0}, 		// Maroon
		{0,0,128}, 		// Navy
		{0,128,0}, 		// Green
		{128,128,0}, 	// Olive
		{128,0,128}, 	// Purple
	    {0,128,128}, 	// Teal
	    {128,128,128}, 	// Gray
	    {0,0,0}			// Black
	};
	
	//private final static String[] PPHASES ={"p", "Pg", "Pn", "PcP", "Pdiff", "PKP","PKiKP","PKIKP"};
	//private final static String[] SPHASES ={"s", "Sg", "Sn", "ScS", "Sdiff", "SKS","SKiKS","SKIKS"};
	
	
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
		// Taup DrawData Curve
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
			
			//swtFigure.primaryYAxis.setRange(traceDataProvider.getYDataMinMax().getUpper(), traceDataProvider.getYDataMinMax().getLower());
			Trace trace1 = new Trace(ttc.phaseName + "_" + ttc.sourceDepth, swtFigure.primaryXAxis, swtFigure.primaryYAxis, traceDataProvider);
			
			//如果是S震相（震相名首字母是S或s），用虚线绘制
			String firstPN = ttc.phaseName.substring(0,1);
			//System.out.println(firstPN);
			if(firstPN.equals("s") || firstPN.equals("S")) 
			{
				trace1.setTraceType(TraceType.DASH_LINE);
			}
			
			trace1.setTraceColor(XYGraphMediaFactory.getInstance().getColor(curveColor.get(ttc.phaseName+ "_" + ttc.sourceDepth)));
			this.swtFigure.addTrace(trace1);
		}
		//改变Y轴方向
		swtFigure.primaryYAxis.setRange(swtFigure.primaryYAxis.getTraceDataRange().getUpper(), swtFigure.primaryYAxis.getTraceDataRange().getLower());
		//swtFigure.primaryYAxis.setRange(30,5);
	}
	
	public void createCurve(String model, String phaseList, int depthMin, int depthMax, int step)
	{
		String[] travelArgs = new String[3];
		
		configColor(phaseList, depthMin, depthMax, step);
		
		for(int i = depthMin; i <= depthMax; i+=step )
		{
			travelArgs[0] = model;
			travelArgs[1] = String.valueOf(i);
			travelArgs[2] = phaseList;
			
			createCurve(travelArgs);
		}
		
	}
	
	private void configColor(String phaseList, int depthMin, int depthMax, int step)
	{
		String[] phases = phaseList.split(",");
		List<String> pPhases = new ArrayList<String>();
		List<String> sPhases = new ArrayList<String>();
		
		//将p，s震相分类
		for(int i=0; i < phases.length; i++)
		{
			String firstPN = phases[i].substring(0,1);
			if(firstPN.equals("p") || firstPN.equals("P")) 
			{
				pPhases.add(phases[i]);
			}
			if(firstPN.equals("s") || firstPN.equals("S")) 
			{
				sPhases.add(phases[i]);
			}
		}
		
		int colorStep = (255-128)/((depthMax-depthMin)/step);
		int tempColorStep = 0;
		int colorType = DEFAULT_CURVE_COLOR.length;
		//给P震相走时曲线分配颜色
		for(int i=0; i < pPhases.size(); i++)
		{
			for(int j = depthMin; j <= depthMax; j+=step )
			{
				Double depth = new Double(j);
				String tempPhaseName = pPhases.get(i) + "_"+ depth.toString();
				int[] rgbValue = DEFAULT_CURVE_COLOR[i%colorType];
				int redColor = rgbValue[0] + tempColorStep;
				int greenColor = rgbValue[1] + tempColorStep;
				int blueColor = rgbValue[2] + tempColorStep;
				curveColor.put(tempPhaseName, new RGB(redColor,greenColor,blueColor));
				tempColorStep = tempColorStep + colorStep;
			}
			tempColorStep = 0;
		}
		//给S震相走时曲线分配颜色
		for(int i=0; i < sPhases.size(); i++)
		{
			for(int j = depthMin; j <= depthMax; j+=step )
			{
				Double depth = new Double(j);
				String tempPhaseName = sPhases.get(i) + "_"+ depth.toString();
				int[] rgbValue = DEFAULT_CURVE_COLOR[i%colorType];
				int redColor = rgbValue[0] + tempColorStep;
				int greenColor = rgbValue[1] + tempColorStep;
				int blueColor = rgbValue[2] + tempColorStep;
				curveColor.put(tempPhaseName, new RGB(redColor,greenColor,blueColor));
				tempColorStep = tempColorStep + colorStep;
			}
			tempColorStep = 0;
		}
	}
	
}
