package com.angelstorun.drawingmachine.view;

import org.controlsfx.dialog.Dialogs;

import com.angelstorun.drawingmachine.MainApp;
import com.angelstorun.drawingmachine.model.Player;
import com.angelstorun.drawingmachine.util.IntegerEditingCell;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

//TODO: dodać możliwość wykluczenia kolumny z czynników wpływających na prawdopodobieństwo wylosowania
public class PersonChooserControler {
	
	private MainApp mainApp;

	@FXML
	private TableView<Player> playersTable;

	@FXML
	private TableColumn<Player, Boolean> isIncludedColumn;

	@FXML
	private TableColumn<Player, String> nameColumn;

	@FXML
	private TableColumn<Player, Boolean> isSubscriptionPaidColumn;

	@FXML
	private TableColumn<Player, Number> numberOfShowsColumn;

	@FXML
	private TableColumn<Player, Boolean> isActiveColumn;

	@FXML 
	private TextField numberOfPlayersTextField;

	
	@FXML
	private void initialize() {

		isIncludedColumn.setCellValueFactory(cellData -> cellData.getValue().getIsIncludedProperty());
		isIncludedColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Boolean>>() {
			@Override
			public void handle(CellEditEvent<Player, Boolean> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setIncluded(event.getNewValue());
			}
		});

		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,String>>() {	
			@Override
			public void handle(CellEditEvent<Player, String> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setName(event.getNewValue());
				
			}
		});

		isSubscriptionPaidColumn.setCellValueFactory(cellData -> cellData.getValue().getIsSubscriptionPaidProperty());
		isSubscriptionPaidColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Boolean>>() {
			@Override
			public void handle(CellEditEvent<Player, Boolean> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setSubscriptionPaid(event.getNewValue());
			}
		});

		numberOfShowsColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberOfShowsProperty());
		numberOfShowsColumn.setCellFactory(col -> new IntegerEditingCell());
		numberOfShowsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Number>>() {	
			@Override
			public void handle(CellEditEvent<Player, Number> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setNumberOfShows(event.getNewValue().intValue());
			}
		});

		isActiveColumn.setCellValueFactory(cellData -> cellData.getValue().getIsActiveProperty());
		isActiveColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Boolean>>() {
			@Override
			public void handle(CellEditEvent<Player, Boolean> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setIsActive(event.getNewValue());
			}
		});

		numberOfPlayersTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d+")) {
					Integer.parseInt(newValue);
					
				} else if (newValue.length() > 0) {
					numberOfPlayersTextField.setText(oldValue);
				}
			}
		});
	}

	@FXML
	private void handleDrowButton() {
		if (numberOfPlayersTextField.getText().length() > 0) {
			Integer t = Integer.valueOf(numberOfPlayersTextField.getText());

			boolean okClicked = mainApp.showDrowPlayerDialog(t);
			if (okClicked) {
				for (Player p : mainApp.getPlayers()) {
					if (mainApp.getPickedPlayers().contains(p)) {
						p.addOneShow();
					}
				}
			}
			
		} else {
			Dialogs.create()
				.title("Błąd")
				.masthead("Podaj liczbę graczy do wylosowania!")
				.message("Nie podano liczby graczy do wylosowania.")
				.showError();
		}
	}
	

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		playersTable.setItems(mainApp.getPlayers());
	}
}
