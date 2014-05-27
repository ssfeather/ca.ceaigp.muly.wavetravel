package ca.ceaigp.muly.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.PlotArea;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.swt.xygraph.linearscale.Range;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import edu.sc.seis.seisFile.sac.SacTimeSeries;

public class DrawSeisData
{
	//设置波形显示范围，Range大，波形显示小
	private int zoomRange = 4;
	
	public int getZoomRange()
    {
    		return zoomRange;
    }

	public void setZoomRange(int zoomRange)
    {
		this.zoomRange = zoomRange;
    }

	public DrawSeisData(String fileName, final XYGraph swtFigure)
	{
		//读波形数据
		SacTimeSeries sac2 = getSacData(fileName);
		float[] sacx2 = sac2.getX();
		float[] sacy2 = sac2.getY();
		
		CircularBufferDataProvider traceDataProvider2 = new CircularBufferDataProvider(true);
		
		//------------显示地震波形----------------------------
		traceDataProvider2.setBufferSize(sacy2.length);
		traceDataProvider2.setCurrentXDataArray(sacx2);
		traceDataProvider2.setCurrentYDataArray(sacy2);
		
		
		//-------------------------------------------------------------------------------------------------------
		Axis x2Axis = new Axis("X2", false);
		Axis y2Axis = new Axis("Y2", true);
		
		//x2Axis.setTickLableSide(LabelSide.Secondary);
		//y2Axis.setTickLableSide(LabelSide.Secondary);

		//x2Axis.setTitle("Time");
		//y2Axis.setTitle("Amplitude");
		
		//y2Axis.setAutoScale(false);
		x2Axis.setRange(new Range(swtFigure.primaryXAxis.getRange().getLower(), swtFigure.primaryXAxis.getRange().getUpper()/sac2.getHeader().getDelta()));
		y2Axis.setRange(traceDataProvider2.getYDataMinMax().getLower()*zoomRange, traceDataProvider2.getYDataMinMax().getUpper()*zoomRange);
		//y2Axis.setRange(new Range(swtFigure.primaryYAxis.getRange().getLower(), swtFigure.primaryYAxis.getRange().getUpper()*200));
		//System.out.println(swtFigure.primaryYAxis.getRange().getLower());
		//System.out.println(swtFigure.primaryYAxis.getRange().getUpper());
		
		swtFigure.addAxis(x2Axis);
		swtFigure.addAxis(y2Axis);
		
		x2Axis.setVisible(false);
		y2Axis.setVisible(false);
		
		//------------------------------------------------------------------------------------------------------------------------
		String waveName = sac2.getHeader().getKnetwk().trim() + ":" +sac2.getHeader().getKstnm().trim() + ":" + sac2.getHeader().getKcmpnm().trim();
		Trace trace2 = new Trace(waveName,x2Axis, y2Axis, traceDataProvider2);
		//trace2.setTraceColor(new Color(null, new RGB(0,0,255)));
		trace2.setEnableMove(true);
		swtFigure.addTrace(trace2);
		trace2.setSingleEnableMove(true);
		//swtFigure.setShowLegend(false);
		//this.removeTrace(trace2);
		
	}
	
	private SacTimeSeries getSacData(String fn)
	{
		SacTimeSeries sac = new SacTimeSeries();
		try
        {
	       sac.read(new File(fn));
        }
        catch (FileNotFoundException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        catch (IOException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		return sac;
	}

}
