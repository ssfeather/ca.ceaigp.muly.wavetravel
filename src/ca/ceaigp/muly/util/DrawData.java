package ca.ceaigp.muly.util;

import org.csstudio.swt.xygraph.figures.XYGraph;

public class DrawData
{
	public DrawData(String fileName, final XYGraph swtFigure)
	{
		if (swtFigure.primaryXAxis.getTraceList().size() == 0)
		{
			swtFigure.removeAllTrace(true);
		
			//����ʱ����
			DrawCurve dc = new DrawCurve(swtFigure);
			//dc.createCurve("jb", "P, S, Pn, Sn, PcP, ScS", 100, 600, 100);
			dc.createCurve("iasp91", "Pg,Pn,PmP,Sg,Sn,SmS", 10, 110, 50);
		}
		else
		{
			//�Ƴ�����������������Trace
			//swtFigure.removeAllTrace(false);
		}
		
		new DrawSeisData(fileName, swtFigure);
		
	}
}
