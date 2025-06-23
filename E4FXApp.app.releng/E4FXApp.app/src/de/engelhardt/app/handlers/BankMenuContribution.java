
package de.engelhardt.app.handlers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class BankMenuContribution {
	@Inject
	EModelService modelService;

	@Inject
	MApplication application;

	@AboutToShow
	public void aboutToShow(List<MMenuElement> items) {
		String activePerspective = getActivePerspectiveId();

		if (activePerspective.equals("e4fxapp.app.perspective.steuer")) {
			MDirectMenuItem item = modelService.createModelElement(MDirectMenuItem.class);
			item.setLabel("Perspective-Specific Item");
			item.setContributionURI("bundleclass://E4FXApp.app/e4fxapp.app.menucontribution.steuer");
			items.add(item);
		}
	}

	private String getActivePerspectiveId() {
		MPerspective activePerspective = modelService.getActivePerspective(application.getSelectedElement());
		return activePerspective != null ? activePerspective.getElementId() : null;
	}

	@AboutToHide
	public void aboutToHide(List<MMenuElement> items) {

	}

}