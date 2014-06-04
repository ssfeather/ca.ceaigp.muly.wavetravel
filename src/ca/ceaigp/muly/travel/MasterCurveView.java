package ca.ceaigp.muly.travel;

import org.csstudio.swt.xygraph.figures.ToolbarArmedXYGraph;
import org.csstudio.swt.xygraph.figures.XYGraph;
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
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class MasterCurveView extends ViewPart
{
	public static final String ID = "ca.ceaigp.muly.travel.view";
	private LightweightSystem lws;

	public MasterCurveView()
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
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent)
	{
		createActions();
		initializeToolBar();
		initializeMenu();

		// use LightweightSystem to create the bridge between SWT and draw2D
		lws = new LightweightSystem(new Canvas(parent, SWT.NONE));

		// create a new XY Graph.
		// XYGraph xyGraph = new XYGraph();
		XYGraph swtFigure = new XYGraph();

		ToolbarArmedXYGraph toolbarArmedXYGraph = new ToolbarArmedXYGraph(swtFigure);
		// toolbarArmedXYGraph.setShowToolbar(false);

		swtFigure.setTitle("Seismic Wave");
		// set it as the content of LightwightSystem
		lws.setContents(toolbarArmedXYGraph);

		swtFigure.setFont(XYGraphMediaFactory.getInstance().getFont(XYGraphMediaFactory.FONT_TAHOMA));
		swtFigure.primaryXAxis.setTitle("Time");
		swtFigure.primaryYAxis.setTitle("Dist");
		swtFigure.primaryYAxis.setAutoScale(true);
		swtFigure.primaryXAxis.setAutoScale(true);
		swtFigure.primaryXAxis.setShowMajorGrid(true);
		swtFigure.primaryYAxis.setShowMajorGrid(true);
		// swtFigure.primaryXAxis.setAutoFormat(true);
		swtFigure.getPlotArea().setShowBorder(true);
		
		// -----------------------------------------------------------------------------------------------------

		/*
		 * //Test log4j Properties prop = new Properties(); try {
		 * prop.load(Activator.class.getResourceAsStream("log4j.properties")); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } PropertyConfigurator.configure(prop); Logger
		 * logger = Logger.getLogger(MasterCurveView.class);
		 * logger.log(Level.WARN, "This is a test...");
		 */

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

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		//IViewPart scView = getViewSite().getPage().findView("ca.ceaigp.muly.travel.SlaveCurveView");
	}
}