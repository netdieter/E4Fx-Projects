 
package de.engelhardt.app.handlers;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

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

public class PdfView {
	@Inject
	@Optional
	private IEventBroker eventBroker;

	@Execute
	public void execute( @Named("primaryStage") Stage primaryStage) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Pdf File wählen");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pdf file", "*.pdf"));
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Alle", "*.*"));
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		File f = fc.showOpenDialog(primaryStage);
		eventBroker.send(Constants.EVNT_SHOWPDF, f);
	}
	
	
	@CanExecute
	public boolean canExecute(MWindow activeWindow,	EModelService modelService) {
		MPerspective activePerspective = modelService.getActivePerspective(activeWindow);
		return activePerspective != null ? activePerspective.getElementId().equals("e4fxapp.app.perspective.pdfviewer") : false;
	}
		
}