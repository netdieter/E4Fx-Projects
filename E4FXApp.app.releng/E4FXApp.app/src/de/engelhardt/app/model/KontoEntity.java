/**
 * 
 */
package de.engelhardt.app.model;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 */
public class KontoEntity {

	@CsvDate(value = "dd.MM.yyyy")
	@CsvBindByName(column = "Wertstellung" )
	private LocalDate date;
	private  ObjectProperty<LocalDate>  buchungsdatum = new SimpleObjectProperty<>();

	@CsvNumber(value = "#0.00")
	@CsvBindByName(column = "Betrag", required = true)
	private  Double wert;
	private DoubleProperty betrag = new SimpleDoubleProperty();

	@CsvBindByName(column = "Umsatzart", required = true)
	private String umart;
	private StringProperty umsatzart = new SimpleStringProperty();
	
	@CsvBindByName(column = "Buchungstext", required = true)
	private String text;
	private StringProperty buchungstext = new SimpleStringProperty();

	
	
	/**
	 * 
	 */
	public KontoEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param date
	 * @param wert
	 * @param umart
	 * @param text
	 */
	public KontoEntity(LocalDate date, Double wert, String umart, String text) {
		super();
		this.setDate(date);
		this.setWert(wert);
		this.setUmart(umart);
		this.setText(text);
	}

	public Double getWert() {
		return wert;
	}

	public void setWert(Double wert) {
		setBetrag(wert);
	}

	public String getUmart() {
		return umart;
	}

	public void setUmart(String umart) {
		setUmsatzart(umart);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		setBuchungstext(text);
	}

	public void setBetrag(DoubleProperty betrag) {
		this.betrag = betrag;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		setBuchungsdatum(date);
	}
	
	public final ObjectProperty<LocalDate> buchungsdatumProperty() {
		return this.buchungsdatum;
	}
	
	public final LocalDate getBuchungsdatum() {
		return this.buchungsdatumProperty().get();
	}
	
	public final void setBuchungsdatum(final LocalDate buchungsdatum) {
		this.buchungsdatumProperty().set(buchungsdatum);
	}
	
	public final DoubleProperty betragProperty() {
		return this.betrag;
	}
	
	public final double getBetrag() {
		return this.betragProperty().get();
	}
	
	public final void setBetrag(final double betrag) {
		this.betragProperty().set(betrag);
	}
	
	public final StringProperty umsatzartProperty() {
		return this.umsatzart;
	}
	
	public final String getUmsatzart() {
		return this.umsatzartProperty().get();
	}
	
	public final void setUmsatzart(final String umsatzart) {
		this.umsatzartProperty().set(umsatzart);
	}
	
	public final StringProperty buchungstextProperty() {
		return this.buchungstext;
	}
	
	public final String getBuchungstext() {
		return this.buchungstextProperty().get();
	}
	
	public final void setBuchungstext(final String buchungstext) {
		this.buchungstextProperty().set(buchungstext);
	}
	
	// Filterdefinitionen
	
	public boolean isUeberweisung() {
		return getUmsatzart().equals("Überweisung");
	}

	public boolean isLastschrift() {
		return getUmsatzart().equals("Lastschrift");
	}

	public boolean isUST() {
		return getBuchungstext().contains("UMS.ST");
	}
	
	public boolean isEKST() {
		return getBuchungstext().contains("EINK.ST");
	}

	public boolean isMixedSteuer() {
		return isUST() && isEKST();
	}

	public boolean isRechnungseingang() {
		return isUeberweisung() && getBuchungstext().startsWith("duragIS IT-Consulting");
	}

	public boolean isAusgangEinkommensteuer() {
		return isLastschrift() && getBuchungstext().contains("STEUERNR 257/215/00406") && isEKST();
	}
	
	public boolean isAusgangUmsatzsteuer() {
		return isLastschrift() && getBuchungstext().contains("STEUERNR 257/215/00406") && isUST();
	}
	
}
