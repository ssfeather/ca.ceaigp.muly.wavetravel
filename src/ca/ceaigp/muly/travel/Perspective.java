package ca.ceaigp.muly.travel;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory
{

	public void createInitialLayout(IPageLayout layout)
	{
		layout.setEditorAreaVisible(false);
		//layout.setFixed(true);
		//layout.addView("ca.ceaigp.muly.travel.MasterCurveView", IPageLayout.LEFT, 0.5f, "ca.ceaigp.muly.travel.SlaveCurveView");
		//layout.addView("ca.ceaigp.muly.travel.SlaveCurveView", IPageLayout.BOTTOM, 0.69f, "ca.ceaigp.muly.travel.MasterCurveView");
	}
}
