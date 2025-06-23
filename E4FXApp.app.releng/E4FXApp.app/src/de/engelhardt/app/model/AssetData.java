/**
 * 
 */
package de.engelhardt.app.model;

import java.time.LocalDate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * 
 */
public class AssetData {

	private  ObjectProperty<LocalDate>  datum = new SimpleObjectProperty<>();
	private LongProperty anzahl = new SimpleLongProperty();
	private DoubleProperty preis = new SimpleDoubleProperty();

	
	
	public AssetData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssetData(LocalDate datum, long anzahl, double preis) {
		super();
		this.setDatum(datum);
		this.setAnzahl(anzahl);
		this.setPreis(preis);
	}

	public final ObjectProperty<LocalDate> datumProperty() {
		return this.datum;
	}
	
	public final LocalDate getDatum() {
		return this.datumProperty().get();
	}
	
	public final void setDatum(final LocalDate datum) {
		this.datumProperty().set(datum);
	}
	
	public final LongProperty anzahlProperty() {
		return this.anzahl;
	}
	
	public final long getAnzahl() {
		return this.anzahlProperty().get();
	}
	
	public final void setAnzahl(final long anzahl) {
		this.anzahlProperty().set(anzahl);
	}
	
	public final DoubleProperty preisProperty() {
		return this.preis;
	}
	
	public final double getPreis() {
		return this.preisProperty().get();
	}
	
	public final void setPreis(final double preis) {
		this.preisProperty().set(preis);
	}
	

	
}
