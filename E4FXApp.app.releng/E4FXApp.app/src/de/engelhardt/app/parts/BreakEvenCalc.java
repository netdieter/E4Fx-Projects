 
package de.engelhardt.app.parts;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.fx.core.di.LocalInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class BreakEvenCalc {
	static final Logger logger = LoggerFactory.getLogger(BreakEvenCalc.class);

	
	@PostConstruct
	public void postConstruct(BorderPane parent,@LocalInstance FXMLLoader fxmlLoader) {
//		logger.debug("@PostConstruct");
		try {
			fxmlLoader.setLocation(getClass().getResource("/de/engelhardt/app/controller/BreakEvenCalcForm.fxml"));

			Node load = fxmlLoader.load();
			parent.setCenter(load);
		} catch (IOException e) {
//			logger.error("FXMLLoader.load()  failed:", e);
		}

	}
}