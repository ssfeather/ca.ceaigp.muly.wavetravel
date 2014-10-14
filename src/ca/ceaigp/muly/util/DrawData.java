package ca.ceaigp.muly.util;

import org.csstudio.swt.xygraph.figures.XYGraph;

public class DrawData
{
	public DrawData(String fileName, final XYGraph swtFigure)
	{
		if (swtFigure.primaryXAxis.getTraceList().size() == 0)
		{
			swtFigure.removeAllTrace(true);
		
			//画走时曲线
			DrawCurve dc = new DrawCurve(swtFigure);
			//dc.createCurve("jb", "P, S, Pn, Sn, PcP, ScS", 100, 600, 100);
			//dc.createCurve("iasp91", "P,pP,Pg,Pn,PmP,S,sS,Sg,Sn,SmS", 10, 100, 10);
			dc.createCurve("iasp91", "P,Pg,Pn,S,Sg,Sn", 10, 200, 50);
		}
		else
		{
			//移除所有与非主轴关联的Trace
			//swtFigure.removeAllTrace(false);
		}
		
		new DrawSeisData(fileName, swtFigure);
		
	}
}
