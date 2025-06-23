 
package de.engelhardt.app.handlers;

import java.io.File;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OpenDataHandler {
	@Execute
	public void execute( @Named("primaryStage") Stage primaryStage) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Daten File wählen");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("SerpApi Daten file", "*.srp"));
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Alle", "*.*"));
//		URL location = Platform.getInstanceLocation().getURL();
//		fc.setInitialDirectory(new File(location.getPath()));
		fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\DataFiles"));
		File f = fc.showOpenDialog(primaryStage);
	}
		
}