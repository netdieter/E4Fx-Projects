package de.engelhardt.app.controller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;

import de.engelhardt.serpapi.DataService;
import de.engelhardt.serpapi.DataServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class SerpApiFormController {
	 private static final Logger logger = LoggerFactory.getLogger(SerpApiFormController.class);
	 @FXML private AnchorPane anchor;
	 
//	 Stage stage = (Stage) ap.getScene().getWindow();
	 
	 @FXML
	private TextField txtSymbol;
	@FXML
	private ChoiceBox<IntervalData> cbInterval;
	@FXML
	private Button btnLoad;

//	@Inject 
	DataService dataservice;
	
//	public SerpApiFormController(DataService dataservice) {
//		super();
//		this.dataservice = dataservice;
//	}

	@FXML
	public void initialize() {
		logger.info("Starte SerpApiForm");
		dataservice = new DataServiceImpl();
		initChoiseBox();
		txtSymbol.setText("DAX:INDEXDB");
	}

	// Event Listener on Button[#btnLoad].onAction
	@FXML
	public void onLoad(ActionEvent event) { 
		logger.info("SerpApiFormController.load");
		dataservice.setSymbol(txtSymbol.getText());
		JsonArray jar = dataservice.load();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save SerpApi File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/DataFiles/"));
		fileChooser.setInitialFileName(txtSymbol.getText().replace(':', '_') +".json");
		File path = fileChooser.showSaveDialog(anchor.getScene().getWindow());
		dataservice.writeJSOANArray(jar, path);
		
	}

	public void initChoiseBox() {
		cbInterval.getItems().add(new IntervalData("1D", "Tag (default)"));
		cbInterval.getItems().add(new IntervalData ("5D", "5 Days"));
		cbInterval.getItems().add(new IntervalData ("1M", "1 Month"));
		cbInterval.getItems().add(new IntervalData ("6M", "6 Months"));
		cbInterval.getItems().add(new IntervalData ("YTD", "Year to Date"));
		cbInterval.getItems().add(new IntervalData ("1Y", "1 Year"));
		cbInterval.getItems().add(new IntervalData ("5Y", "5 Years"));
		cbInterval.getItems().add(new IntervalData ("MAX", "Maximum"));
		cbInterval.getSelectionModel().selectFirst();
		
	}

	
	private class IntervalData {
		private String key;
		private String value;
		

		public IntervalData(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}


		public String getKey() {
			return key;
		}


		public void setKey(String key) {
			this.key = key;
		}


		public String getValue() {
			return value;
		}


		public void setValue(String value) {
			this.value = value;
		}


		@Override
		public String toString() {
			return value;
		}
		
	}
}
