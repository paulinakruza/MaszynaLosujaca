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
import javafx.util.converter.NumberStringConverter;

//TODO: dodać możliwość wykluczenia kolumny z czynników wpływających na prawdopodobieństwo wylosowania
public class PersonChooserControler {
	
	private MainApp mainApp;

	@FXML
	private TableView<Player> playersTable;

	@FXML
	private TableColumn<Player, Boolean> jestWPuliColumn;

	@FXML
	private TableColumn<Player, String> nazwiskoColumn;

	@FXML
	private TableColumn<Player, Boolean> oplaconaSkladkaColumn;

	@FXML
	private TableColumn<Player, Number> liczbaPokazowColumn;

	@FXML
	private TableColumn<Player, Boolean> chodziNaTreningiColumn;

	@FXML 
	private TextField numberOfPlayersTextField;

	
	@FXML
	private void initialize() {

		jestWPuliColumn.setCellValueFactory(cellData -> cellData.getValue().jestWPuliProperty());
		jestWPuliColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Boolean>>() {
			@Override
			public void handle(CellEditEvent<Player, Boolean> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setIncluded(event.getNewValue());
			}
		});

		nazwiskoColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		nazwiskoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nazwiskoColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,String>>() {	
			@Override
			public void handle(CellEditEvent<Player, String> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setName(event.getNewValue());
				
			}
		});

		oplaconaSkladkaColumn.setCellValueFactory(cellData -> cellData.getValue().oplaconaSkladkaProperty());
		oplaconaSkladkaColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Boolean>>() {
			@Override
			public void handle(CellEditEvent<Player, Boolean> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setSubscriptionPaid(event.getNewValue());
			}
		});

		liczbaPokazowColumn.setCellValueFactory(cellData -> cellData.getValue().liczbaPokazowProperty());
		liczbaPokazowColumn.setCellFactory(col -> new IntegerEditingCell());
		liczbaPokazowColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Number>>() {	
			@Override
			public void handle(CellEditEvent<Player, Number> event) {
				((Player)event.getTableView().getItems().get(event.getTablePosition().getRow())).setNumberOfShows(event.getNewValue().intValue());
			}
		});

		chodziNaTreningiColumn.setCellValueFactory(cellData -> cellData.getValue().chodziNaTreningiProperty());
		chodziNaTreningiColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Player,Boolean>>() {
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
