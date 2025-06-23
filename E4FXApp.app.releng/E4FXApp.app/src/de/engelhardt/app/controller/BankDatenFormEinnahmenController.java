package de.engelhardt.app.controller;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.engelhardt.app.Constants;
import de.engelhardt.app.Utils;
import de.engelhardt.app.model.KontoEntity;
import javafx.collections.FXCollections;

public class BankDatenFormEinnahmenController extends BankDatenBaseForm {
	private static final Logger logger = LoggerFactory.getLogger(BankDatenFormEinnahmenController.class);

	@Inject
    @Optional
	public void handleImport(@UIEventTopic(Constants.EVNT_EINNAHMEN) List<KontoEntity> lst) {
		logger.info("handleImport: " + lst.size());
		tabKontoauszug.setItems(FXCollections.observableList(lst));
    	txtSumme.setText(Utils.toDecimal(
    	 		tabKontoauszug.getItems().stream().mapToDouble(KontoEntity::getBetrag).sum()
           	)
	    );
	}

}
