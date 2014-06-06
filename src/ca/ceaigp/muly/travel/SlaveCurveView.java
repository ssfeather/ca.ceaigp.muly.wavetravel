package ca.ceaigp.muly.travel;

import java.util.List;

import org.csstudio.swt.xygraph.dataprovider.CircularBufferDataProvider;
import org.csstudio.swt.xygraph.figures.Axis;
import org.csstudio.swt.xygraph.figures.ToolbarArmedXYGraph;
import org.csstudio.swt.xygraph.figures.Trace;
import org.csstudio.swt.xygraph.figures.XYGraph;
import org.csstudio.swt.xygraph.linearscale.Range;
import org.csstudio.swt.xygraph.util.XYGraphMediaFactory;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ca.ceaigp.muly.util.DrawCurve;

public class SlaveCurveView extends ViewPart
{

	public static final String ID = "ca.ceaigp.muly.travel.SlaveCurveView"; //$NON-NLS-1$
	private LightweightSystem lws;
	private XYGraph swtFigure = new XYGraph();

	public SlaveCurveView()
	{
	}
	
	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider
	{
		public void inputChanged(Viewer v, Object oldInput, Object newInput)
		{
		}

		public void dispose()
		{
		}

		public Object[] getElements(Object parent)
		{
			if (parent instanceof Object[])
			{
				return (Object[]) parent;
			}
			return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider
	{
		public String getColumnText(Object obj, int index)
		{
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index)
		{
			return getImage(obj);
		}

		public Image getImage(Object obj)
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}


	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent)
	{
		createActions();
		initializeToolBar();
		initializeMenu();
		
		// use LightweightSystem to create the bridge between SWT and draw2D
		lws = new LightweightSystem(new Canvas(parent, SWT.NONE));

		// create a new XY Graph.
		// XYGraph xyGraph = new XYGraph();

		ToolbarArmedXYGraph toolbarArmedXYGraph = new ToolbarArmedXYGraph(swtFigure);
		// toolbarArmedXYGraph.setShowToolbar(false);

		//swtFigure.setTitle("Seismic Wave");
		// set it as the content of LightwightSystem
		lws.setContents(toolbarArmedXYGraph);

		swtFigure.setFont(XYGraphMediaFactory.getInstance().getFont(XYGraphMediaFactory.FONT_TAHOMA));
		//swtFigure.primaryXAxis.setTitle("Time");
		//swtFigure.primaryYAxis.setTitle("Dist");
		swtFigure.primaryYAxis.setAutoScale(true);
		swtFigure.primaryXAxis.setAutoScale(true);
		swtFigure.primaryXAxis.setShowMajorGrid(true);
		swtFigure.primaryYAxis.setShowMajorGrid(true);
		swtFigure.primaryXAxis.setShowTitle(false);
		swtFigure.primaryYAxis.setShowTitle(false);
		swtFigure.setShowLegend(false);
		swtFigure.setShowTitle(false);
		// swtFigure.primaryXAxis.setAutoFormat(true);
		swtFigure.getPlotArea().setShowBorder(true);
	}

	/**
	 * Create the actions.
	 */
	private void createActions()
	{
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar()
	{
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu()
	{
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus()
	{
		// Set the focus
		//setViewState(IWorkbenchPage.STATE_RESTORED);
		//System.out.println("Test");
	}
	
	public void displayWave(XYGraph xyGraph)
	{
		List<Trace> traceList = xyGraph.getPlotArea().getTraceList();
		for(Trace trace : traceList)
		{
			if(trace.isEnableMove())
			{
				if (swtFigure.primaryXAxis.getTraceList().size() == 0)
				{
					swtFigure.removeAllTrace(true);
				
					//画走时曲线
					DrawCurve dc = new DrawCurve(swtFigure);
					//dc.createCurve("jb", "P, S, Pn, Sn, PcP, ScS", 100, 600, 100);
					dc.createCurve("iasp91", "Pg,Pn,PmP,Sg,Sn,SmS", 10, 110, 50);
				}
				else
				{
					//移除所有与非主轴关联的Trace
					//swtFigure.removeAllTrace(false);
				}
				
				CircularBufferDataProvider traceDataProvider = (CircularBufferDataProvider)trace.getDataProvider();
				
				Axis x2Axis = new Axis("X2", false);
				Axis y2Axis = new Axis("Y2", true);
				
				x2Axis.setRange(new Range(swtFigure.primaryXAxis.getRange().getLower(), swtFigure.primaryXAxis.getRange().getUpper()/0.01));
				y2Axis.setRange(traceDataProvider.getYDataMinMax().getLower(), traceDataProvider.getYDataMinMax().getUpper());
				
				swtFigure.addAxis(x2Axis);
				swtFigure.addAxis(y2Axis);
				
				x2Axis.setVisible(false);
				y2Axis.setVisible(false);
				
				//------------------------------------------------------------------------------------------------------------------------
				Trace trace2 = new Trace("waveName",x2Axis, y2Axis, traceDataProvider);
				trace2.setEnableMove(true);
				swtFigure.addTrace(trace2);
				trace2.setSingleEnableMove(true);
			}
		}
		
	}
	
	/**
	 * @param state  one of the IWorkbenchPage STATE_* values: STATE_MAXIMIZED, 
     * STATE_MINIMIZED, STATE_RESTORED
	 * @throws PartInitException 
	 */
	/*
	private void setViewState(int state)
	{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		//IWorkbenchPage page = getViewSite().getPage();
		IViewPart scView = page.findView("ca.ceaigp.muly.travel.SlaveCurveView");
		int currentState = page.getPartState(page.getReference(this));
		if(currentState != state) 
		{
			page.activate(this);
			page.setPartState(page.getReference(scView), state);
		}
		System.out.println("test");
	}
	*/

}
