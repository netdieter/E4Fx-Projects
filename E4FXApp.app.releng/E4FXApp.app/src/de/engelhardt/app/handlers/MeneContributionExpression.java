 
package de.engelhardt.app.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Evaluate;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class MeneContributionExpression {
	@Inject
	EModelService modelService;

	@Inject
	@Active
	MWindow activeWindow;
	
	@Evaluate
	public boolean evaluate() {
		String activePerspective = getActivePerspectiveId();
		
		if (activePerspective.equals("e4fxapp.app.perspective.steuer")) {
			return true;
		} else {
			return false;
		}
	}

	//	@AboutToShow
//	public void aboutToShow(List<MMenuElement> items) {
//		String activePerspective = getActivePerspectiveId();
//
//		if (activePerspective.equals("e4fxapp.app.perspective.steuer")) {
//			MDirectMenuItem item = modelService.createModelElement(MDirectMenuItem.class);
//			item.setLabel("Perspective-Specific Item");
//			item.setContributionURI("bundleclass://E4FXApp.app/e4fxapp.app.menucontribution.steuer");
//			items.add(item);
//		}
//	}

	private String getActivePerspectiveId() {
		// Ist wohl noch zu früh an dieser Stelle
		MPerspective activePerspective = modelService.getActivePerspective(activeWindow);
		return activePerspective != null ? activePerspective.getElementId() : null;
	}

}
