 
package de.engelhardt.app.parts;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.fx.core.di.LocalInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class BankDatenEinnahmen {
	static final Logger logger = LoggerFactory.getLogger(BreakEvenCalc.class);

	@Inject
	public BankDatenEinnahmen() {
	}
	
	@PostConstruct
	public void postConstruct(BorderPane parent,@LocalInstance FXMLLoader fxmlLoader) {
//		logger.debug("@PostConstruct");
		try {
			fxmlLoader.setLocation(getClass().getResource("/de/engelhardt/app/controller/BankDatenFormEinnahmen.fxml"));

			Node load = fxmlLoader.load();
			parent.setCenter(load);
		} catch (IOException e) {
			logger.error("FXMLLoader.load()  failed:", e);
		}

	
		
	}
	
}