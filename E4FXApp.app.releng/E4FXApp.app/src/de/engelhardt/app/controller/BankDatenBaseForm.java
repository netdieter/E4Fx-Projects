package de.engelhardt.app.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.engelhardt.app.model.KontoEntity;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * 
 */
public class BankDatenBaseForm {
	protected static final Logger logger = LoggerFactory.getLogger(BankDatenBaseForm.class);

	@Inject
	@Optional
	protected IEventBroker eventBroker;

	@FXML // ResourceBundle that was given to the FXMLLoader
	protected ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	protected URL location;

	@FXML // fx:id="anchor"
	protected AnchorPane anchor; // Value injected by FXMLLoader
	@FXML // fx:id="scrPane"
	protected ScrollPane scrPane; // Value injected by FXMLLoader
	@FXML
	protected TableView<KontoEntity> tabKontoauszug;
	@FXML
	protected TableColumn<KontoEntity, LocalDate> colBuchngsdate;
	@FXML
	protected TableColumn<KontoEntity, Double> colBetrag;
	@FXML
	protected TableColumn<KontoEntity, String> colUmsatzart;
	@FXML
	protected TableColumn<KontoEntity, String> colBuchungstext;
	@FXML // fx:id="txtSumme"
	protected TextField txtSumme; // Value injected by FXMLLoader

	@FXML
	private ContextMenu tabContextMenue;
	@FXML
	private MenuItem ctxMenueItemCopy;

	protected List<KontoEntity> lst;

	@FXML
	public void initialize() {
		logger.info("Starte BankDatenFormController");
		assert anchor != null : "fx:id=\"anchor\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		assert colBetrag != null : "fx:id=\"colBetrag\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		assert colBuchngsdate != null
				: "fx:id=\"colBuchngsdate\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		assert colBuchungstext != null
				: "fx:id=\"colBuchungstext\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		assert colUmsatzart != null
				: "fx:id=\"colUmsatzart\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		assert scrPane != null : "fx:id=\"scrPane\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		assert tabKontoauszug != null
				: "fx:id=\"tabKontoauszug\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		assert txtSumme != null : "fx:id=\"txtSumme\" was not injected: check your FXML file 'BankDatenForm.fxml'.";
		colBuchngsdate.setCellValueFactory(new PropertyValueFactory<>("buchungsdatum"));
		colBetrag.setCellValueFactory(new PropertyValueFactory<>("betrag"));
		colUmsatzart.setCellValueFactory(new PropertyValueFactory<>("umsatzart"));
		colBuchungstext.setCellValueFactory(new PropertyValueFactory<>("buchungstext"));
		// Alternierende Farben für die Lesbarkeit des Buchungstextes
		colBuchungstext.setCellFactory(column -> {
			return new TableCell<KontoEntity, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setGraphic(null);
					} else {
						VBox vbox = new VBox();
						List<String> textList = Arrays.asList(item.split("(?<=\\G.{100})"));
						String[] colors = { "#3E50B4", "#FF3F80", "#727272" };
						int colorCount = colors.length;
						for (int i = 0; i < textList.size(); i++) {
							Label lbl = new Label(textList.get(i));
							lbl.setStyle("-fx-text-fill: " + colors[i % colorCount]);
							vbox.getChildren().add(lbl);
						}
						setGraphic(vbox);
					}
				}
			};
		});
		// Zeile in das Clipboard kopieren
		tabContextMenue = new ContextMenu();
		ctxMenueItemCopy = new MenuItem("Copy");
		ctxMenueItemCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		ctxMenueItemCopy.setOnAction(event -> {
			ObservableList<KontoEntity> selectedItems = tabKontoauszug.getSelectionModel().getSelectedItems();
			// Create a StringBuilder to store the copied data
			StringBuilder clipboardContent = new StringBuilder();
			for (KontoEntity entity : selectedItems) {
				clipboardContent.append(entity.getBuchungsdatum()).append("\t").append(entity.getBuchungstext())
						.append("\t").append(entity.getBetrag()).append("\n");
			}
			// Copy the data to the system clipboard
			ClipboardContent content = new ClipboardContent();
			content.putString(clipboardContent.toString());
			Clipboard.getSystemClipboard().setContent(content);
		});

		tabContextMenue.getItems().add(ctxMenueItemCopy);
		// Set the context menu for the TableView
		tabKontoauszug.setContextMenu(tabContextMenue);
	}
}
