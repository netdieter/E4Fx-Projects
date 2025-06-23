 
package de.engelhardt.app.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.engelhardt.app.Constants;

public class BankAusgabenFilter {
	static final Logger logger = LoggerFactory.getLogger(BankAusgabenFilter.class);

//	@Inject
//	IEventBroker eventBroker;
	
	@Execute
	public void execute(@Named("e4fxapp.app.commandparameter.tollbar.filter") String parameter, IEventBroker eventBroker) {
		logger.info("Execute: " + parameter);
		eventBroker.send(Constants.EVNT_FILTER, parameter);
	}
	
	
	@CanExecute
	public boolean canExecute(MWindow activeWindow,	EModelService modelService) {
		MPerspective activePerspective = modelService.getActivePerspective(activeWindow);
		return activePerspective != null ? activePerspective.getElementId().equals("e4fxapp.app.perspective.steuer") : false;
	}
		
}