package de.engelhardt.app.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.engelhardt.app.model.AssetData;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LongStringConverter;

public class BreakEvenCalcFormController {
	private static final Logger logger = LoggerFactory.getLogger(BreakEvenCalcFormController.class);
	@FXML
	private AnchorPane ancohr;
	@FXML
	private Button btnSell;
	@FXML
	private Button btnBuy;
	@FXML
	private TableView<AssetData> tabAssets;
	
	@FXML
	private TableColumn<AssetData, LocalDate> colDatum;
	@FXML
	private TableColumn<AssetData, Double> colPreis;
	@FXML
	private TableColumn<AssetData, Long> colAnzahl;
	
	@FXML
	private TextField txtGesAnzahl;
	@FXML
	private TextField txtBrealEven;
	@FXML
	private TextField txtKosten;
	@FXML
	private TextField txtInputDatum;
	@FXML
	private TextField txtInputPreis;
	
	@FXML
	private TextField txtInputAnzahl;
	
	private long sumAnzahl;
	private double sumKosten;

	@FXML
	public void initialize() {
		logger.info("Starte BreakEvenCalcForm");
		 DecimalFormat dfGerman = new DecimalFormat("#,###.##",
	                new DecimalFormatSymbols(Locale.GERMAN));
		tabAssets.setEditable(true);
		colDatum.setCellValueFactory( new PropertyValueFactory<>("datum"));
		colPreis.setCellValueFactory( new PropertyValueFactory<>("preis"));
		colAnzahl.setCellValueFactory( new PropertyValueFactory<>("anzahl"));	
		
		txtInputDatum.setTextFormatter(new TextFormatter<LocalDate>(new LocalDateStringConverter(), LocalDate.now()));
		txtInputPreis.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		txtInputAnzahl.setTextFormatter(new TextFormatter<Long>(new LongStringConverter()));
		txtKosten.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		txtGesAnzahl.setTextFormatter(new TextFormatter<Long>(new LongStringConverter()));
		txtBrealEven.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
		
		btnBuy.disableProperty().bind(
			Bindings.isEmpty(txtInputDatum.textProperty())
			.or(Bindings.isEmpty(txtInputPreis.textProperty()))
			.or(Bindings.isEmpty(txtInputAnzahl.textProperty()))
		);
		btnSell.disableProperty().bind(Bindings.isEmpty(
			tabAssets.getSelectionModel().getSelectedItems())
		);
		
	}

	// Event Listener on Button[#btnSell].onAction
	@FXML
	public void onSell(ActionEvent event) {
		tabAssets.getItems().remove(tabAssets.getSelectionModel().getSelectedItem());
		calcColumns();
	}

	// Event Listener on Button[#btnBuy].onAction
	@FXML
	public void onBuy(ActionEvent event) {
		tabAssets.getItems().add(
			new AssetData(
					LocalDate.parse(txtInputDatum.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy")), 
					Long.parseLong(txtInputAnzahl.getText()), 
					Double.parseDouble(txtInputPreis.getText()))
		);
		txtInputAnzahl.clear();
		txtInputPreis.clear();
		calcColumns();
	}
	
	public void calcColumns() {
		tabAssets.getItems().stream().forEach(el -> {
			sumAnzahl += el.getAnzahl(); 
			sumKosten += el.getAnzahl() *el.getPreis();
			}
		); 
		txtKosten.setText(String.valueOf(sumKosten));
		txtBrealEven.setText(String.valueOf(sumKosten / sumAnzahl));
		txtGesAnzahl.setText(String.valueOf(sumAnzahl));
	}
}
