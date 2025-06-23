 
package de.engelhardt.app.handlers;

import java.io.File;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import de.engelhardt.app.Constants;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BankAuszugImport {
	
	@Inject
	@Optional
	private IEventBroker eventBroker;
	
	@Execute
//	public void execute(IEventBroker eventBroker) {
	public void execute(Stage stage) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Kontoauszug File wählen");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-File", "*.csv"));
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Alle", "*.*"));
//		URL location = Platform.getInstanceLocation().getURL();
//		fc.setInitialDirectory(new File(location.getPath()));
		fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\DataFiles"));
		File f = fc.showOpenDialog(stage);

		eventBroker.send(Constants.EVNT_IMPORT, f);
//		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}
	
	
	@CanExecute
	public boolean canExecute(MWindow activeWindow,	EModelService modelService) {
		MPerspective activePerspective = modelService.getActivePerspective(activeWindow);
		return activePerspective != null ? activePerspective.getElementId().equals("e4fxapp.app.perspective.steuer") : false;
	}
		
}