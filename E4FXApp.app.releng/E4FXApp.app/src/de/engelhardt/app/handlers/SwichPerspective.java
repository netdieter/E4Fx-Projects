 
package de.engelhardt.app.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwichPerspective {
	static final Logger logger = LoggerFactory.getLogger(SwichPerspective.class);

	
	@Execute
	public void execute(@Named("e4fxapp.app.commandparameter.perspektive") String parameter, MApplication app, EPartService partService, 
		      EModelService modelService) {
		logger.info(parameter);
		MPerspective element = (MPerspective) modelService.find(parameter, app);
		// now switch perspective
		partService.switchPerspective(element);
	}
		
}