package de.engelhardt.app.controller;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import de.engelhardt.app.Constants;
import de.engelhardt.app.Utils;
import de.engelhardt.app.model.KontoEntity;
import javafx.collections.FXCollections;

public class BankDatenFormController extends BankDatenBaseForm {
	private static final Logger logger = LoggerFactory.getLogger(BankDatenFormController.class);

	@Inject
	EPartService partService;

	@Inject
    @Optional
	public void handleImport(@UIEventTopic(Constants.EVNT_IMPORT) File file) {
		logger.info("handleImport: " + file.getAbsolutePath());
		
		// File lesen und parsen.
	    try (Reader reader = new FileReader(file.getAbsolutePath())) {
	        CsvToBean<KontoEntity> cb = new CsvToBeanBuilder<KontoEntity>(reader)
	         .withType(KontoEntity.class)
	         .withSeparator(';')
	         .build();
	        lst = cb.parse();
	        tabKontoauszug.setItems(FXCollections.observableList(lst));
	        filterRechnungsEingang(lst);
	        txtSumme.setText(Utils.toDecimal(
	        		tabKontoauszug.getItems().stream().mapToDouble(KontoEntity::getBetrag).sum()
	        	)
	        );
		} catch (Exception e) {
			logger.error("Fehler beim CSV-Import!", e);
		}
	 }

	private void filterRechnungsEingang(List<KontoEntity> lst) {
		List<KontoEntity> eing_lst = lst.stream()
			.filter(KontoEntity::isRechnungseingang)
			.map(e -> {
				e.setBuchungstext(
						e.getBuchungstext().substring(0, e.getBuchungstext().indexOf("Schoene"))); 
				return e;}
			)
			.collect(Collectors.toList());
		eventBroker.post(Constants.EVNT_EINNAHMEN, eing_lst);
	}

	@Inject
	@Optional
	public void handleFilter(@UIEventTopic(Constants.EVNT_FILTER) String param) {
		logger.info("handleFilter: " + param);
		if(lst == null)
			return;
		List<KontoEntity> ausg_lst = new ArrayList();
		if(param.equals("EKST")) {
			ausg_lst = lst.stream().
					filter(KontoEntity::isAusgangEinkommensteuer)
					.map(item -> { 
						if(item.isMixedSteuer()) { 
							KontoEntity c = new KontoEntity(item.getBuchungsdatum(), item.getBetrag(), item.getUmsatzart(), item.getBuchungstext());
							c.setBetrag(getValFromMixedFormText(item.getBuchungstext(), "EINK.ST"));
							return c;
						} return item;
					})
					.map(i -> {i.setBuchungstext(stripBankTrailer(i.getBuchungstext())); return i;})
					.collect(Collectors.toList());
			
		} else if(param.equals("UST")) {
			ausg_lst = lst.stream().
					filter(KontoEntity::isAusgangUmsatzsteuer)
					.map(item -> { 
						if(item.isMixedSteuer()) { 
							KontoEntity c = new KontoEntity(item.getBuchungsdatum(), item.getBetrag(), item.getUmsatzart(), item.getBuchungstext());
							c.setBetrag(getValFromMixedFormText(item.getBuchungstext(), "UMS.ST"));
							return c;
						} return item;
					})
					.map(i -> {i.setBuchungstext(stripBankTrailer(i.getBuchungstext())); return i;})
					.collect(Collectors.toList());
		} else if(param.equals("TELEFON")) {
			ausg_lst = lst.stream()
					.filter(KontoEntity::isLastschrift)
					.filter(item -> item.getBuchungstext().contains("Vodafone Deutschland GmbH"))
					.map(item -> {
						KontoEntity c = new KontoEntity(item.getBuchungsdatum(), item.getBetrag(), item.getUmsatzart(), null);
						c.setBuchungstext(getTelefonBuchungsText(item.getBuchungstext()));
						return c;
					})
					.collect(Collectors.toList());
		}

		setAusgabePartActiv(param);
		eventBroker.post(Constants.EVNT_AUSGABEN, ausg_lst);
	}
	
	
	private void setAusgabePartActiv(String label) {
		MPart part = partService.findPart("e4fxapp.app.part.ausgaben");
		part.setLabel(label);
		partService.showPart(part, PartState.ACTIVATE);
	}
	
	/*
	 * Falls mehrere Beträge im Buchungstext enthalten sind
	 */
	private Double getValFromMixedFormText(String text, String pattern) {
		int st = text.lastIndexOf(pattern);
		int end = text.indexOf("EUR", st);
		String k1 = text.substring(st, end);
		int ko = k1.indexOf(',');
		String k2 = k1.substring(0, ko);
		String k3 = k2.replaceAll("\\.", "")  +  "." + k1.substring(ko+1).trim();;
		String k = k3.substring(k3.lastIndexOf(" "), k3.length());
		return Double.parseDouble(k) * -1;
	}
	
	private String stripBankTrailer(String str) {
		return str.indexOf("Mandatsref") >= 0? str.substring(0, str.indexOf("Mandatsref")) : str;
	}
	
	private String getTelefonBuchungsText(String str) {
		String s = str.replace(" Ihre Rechnu ng online bei www.vodafone.de/meink abel End-to-End-Ref", ":");
		return stripBankTrailer(s);
	}
}
