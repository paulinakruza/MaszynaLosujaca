package com.angelstorun.drawingmachine.view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.angelstorun.drawingmachine.MainApp;
import com.angelstorun.drawingmachine.model.Player;

public class DrowPlayerDialogControler {
	private MainApp mainApp;
	private Stage   dialogStage;
	private Boolean okClicked  = false;
	private String  textResult = new String("Losowanie wygrywają:\n");

	@FXML
	private TextArea textAreaResult;

	@FXML
	private void initialize() {
		textAreaResult.setText(textResult);
	}

	@FXML
	private void handleOk() {
		okClicked = true;
		mainApp.setClipboardContentString(textAreaResult.getText());
		dialogStage.close();
		
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void showPickedPlayers(List<Player> pickedPlayers) {
		for (Player player : pickedPlayers) {
			textResult += "\n" + player.getName();
		}
		textAreaResult.setText(textResult);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void handleEsc() {
		handleCancel();
	}

	public void handleEnter() {
		handleOk();
	}

	public boolean isOkClicked() {
		return okClicked;
	}
}
