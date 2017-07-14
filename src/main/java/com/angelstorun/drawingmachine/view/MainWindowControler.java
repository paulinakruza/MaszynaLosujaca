package com.angelstorun.drawingmachine.view;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.controlsfx.dialog.Dialogs;

import com.angelstorun.drawingmachine.MainApp;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.FileChooser;

public class MainWindowControler {

	// Reference to the main application
	private MainApp mainApp;

	@FXML
	private CheckMenuItem numberOfShows;

	@FXML
	private CheckMenuItem isActive;

	@FXML
	private CheckMenuItem isSubscriptionPaid;

	
	@FXML
	private void handleExitWithSave() {
		mainApp.closeAppWithSave();
		System.out.println("handleExitWithSave");
	}

	@FXML
	private void handleExitWhidoutSave() {
		 System.exit(0);
	}

	@FXML
	private void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Ustalanie rozszerzenia plików
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki XML (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialDirectory(new File(mainApp.getDefaultDataPath()));

		// Wyświetlanie okna dialogowego
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null) {
			mainApp.loadDataFromFile(file);
		}
	}

	@FXML
	private void handleOptions() {
		//TODO obsluga tej metody
	}
	
	@FXML
	private void handleSave(){
		FileChooser fileChooser = new FileChooser();
		
		// ustalanie rozszerzenia pliku
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki XML (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		fileChooser.setInitialDirectory(new File(mainApp.getDefaultDataPath()));

		//Wyświetlanie okna dialogowego
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			try {
				mainApp.saveDataToFile(file);
				
			} catch (ParserConfigurationException | TransformerException e) {
				Dialogs.create()
					.title("Błąd")
					.masthead("Błąd zapisywania pliku xml!")
					.message(e.getMessage())
					.showError();
			}
		}
	}

	@FXML
	private void handleNumberOfShows() {
		mainApp.setNumberOfShowsConsidered(numberOfShows.isSelected());
	}

	@FXML
	private void handleIsActive() {
		 mainApp.setActiveConsidered(isActive.isSelected());
	}

	@FXML
	private void handleIsSubscriptionPaid() {
		mainApp.setSubscriptionPaidConsidered(isSubscriptionPaid.isSelected());
	}
	
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public boolean getNumberOfShowsConsidered() {
		return numberOfShows.isSelected();
	}

	public boolean getSubscriptionPaid() {
		return isSubscriptionPaid.isSelected();
	}

	public boolean getActive() {
		return isActive.isSelected();
	}
}
