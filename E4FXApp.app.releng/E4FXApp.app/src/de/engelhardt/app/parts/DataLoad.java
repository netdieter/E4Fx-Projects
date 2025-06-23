 
package de.engelhardt.app.parts;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.fx.core.di.LocalInstance;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class DataLoad {
//	static final Logger logger = LoggerFactory.getLogger(DataLoad.class);

	
	@PostConstruct
	public void postConstruct(BorderPane parent,@LocalInstance FXMLLoader fxmlLoader) {
//		logger.debug("@PostConstruct");
		try {
			fxmlLoader.setLocation(getClass().getResource("/de/engelhardt/app/controller/SerpApiForm.fxml"));
//			logger.debug(ds.toString());
			Node load = fxmlLoader.load();
			parent.setCenter(load);
		} catch (IOException e) {
//			logger.error("FXMLLoader.load()  failed:", e);
		}

	}
}