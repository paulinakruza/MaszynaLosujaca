package com.angelstorun.drawingmachine;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import com.angelstorun.drawingmachine.model.InvalidNumberPlayersException;
import com.angelstorun.drawingmachine.model.Player;
import com.angelstorun.drawingmachine.model.TeamManager;
import com.angelstorun.drawingmachine.view.DrowPlayerDialogControler;
import com.angelstorun.drawingmachine.view.MainWindowControler;
import com.angelstorun.drawingmachine.view.PersonChooserControler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


// TODO: sprawdzić czy zapis i odczyt z pliku zawsze działa dobrze
/**
 * TODO: być może powinna istnieć możliwość dodania gracza z poziomu aplikacji ...
 * jeśli tak, to możliwość usunięcia też powinna istnieć
 * 
 * DONE: Do zrealizowania w kolejnej wersji programu.
 */
public class MainApp extends Application {
	private Stage                  primaryStage;
	private BorderPane             rootLayout;
	
	private TeamManager            manager   = new TeamManager();
	private final Clipboard        clipboard = Clipboard.getSystemClipboard();
	private final ClipboardContent content   = new ClipboardContent();
	
	//TODO: być może wygodniej byłoby przechowywać ścieżki jako zmienną typu File?
	private String                 defaultDataPath     = System.getProperty("user.dir") + System.getProperty("file.separator") + "data"; 
	private String                 defaultDataFilePath = defaultDataPath + System.getProperty("file.separator") + "zawodnicy_angels.xml";


	private void loadData() {
		loadDataFromFile(new File(getDefaultDataFilePath()));
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Maszyna losująca");
		Platform.setImplicitExit(false);
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent t) {
				t.consume();
				closeAppWithSave();
			}
		});
		
		initRootLayout();
		
		showPersonChooser();
	}

	public String getDefaultDataFilePath() {
		return defaultDataFilePath;
	}

	public void setDefaultDataFilePath(String defaultDataFilePath) {
		this.defaultDataFilePath = defaultDataFilePath;
	}

	public String getDefaultDataPath() {
		return defaultDataPath;
	}

	public void setDefaultDataPath(String defaultDataPath) {
		this.defaultDataPath = defaultDataPath;
	}

	public void closeAppWithSave() {
		try {
			saveDataToFile(new File(getDefaultDataFilePath()));
			System.exit(0);
			
		} catch (ParserConfigurationException | TransformerException e) {
			Action response = Dialogs.create()
									.owner(getPrimaryStage())
									.title("Błąd zapisu danych do pliku!")
									.masthead("Zapis danych do pliku nie powiódł się!")
									.message("Czy chcesz zamknąć program? Zmiany dotyczące graczy nie zostaną zapisane.")
									.actions(Dialog.Actions.OK, Dialog.Actions.CANCEL)
									.showConfirm();

			if (response == Dialog.Actions.OK) {
				System.exit(0);
				
			} else {
				//Użytkownik chce kontynuować pracę. Brak reakcji programu.
			}
		}
	}

	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/MainWindow.fxml"));
			rootLayout = (BorderPane)loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			MainWindowControler controler = loader.getController();
			controler.setMainApp(this);
			manager.setNumberOfShowsConsidered(controler.getNumberOfShowsConsidered());
			manager.setActiveConsidered(controler.getActive());
			manager.setSubscriptionPaidConsidered(controler.getSubscriptionPaid());

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showPersonChooser() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonChooser.fxml"));
			AnchorPane personChooser = (AnchorPane)loader.load();
			rootLayout.setCenter(personChooser);
			
			PersonChooserControler controller = loader.getController();
			controller.setMainApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public ObservableList<Player> getPlayers() {
		return manager.getMyPlayers();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public MainApp() {
		loadData();
	}

	public boolean showDrowPlayerDialog(Integer i) {
		try {
			manager.recalculateProbability();
			
//			//testy poprawnej zmiany wartości za pomocą tabeli.
//			for(Player p: manager.getMyPlayers()){
//				System.out.format("%20s: %d, %b\n", p.getName(), p.getNumberOfShows(), p.isActive());
//			}
//			System.out.println("########################");
//			//koniec testów

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/DrowPlayerDialog.fxml"));
			AnchorPane page = (AnchorPane)loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Wylosowano zawodników");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			DrowPlayerDialogControler controller = loader.getController();
			controller.setMainApp(this);

			scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
				switch (e.getCode()) {
						case ESCAPE:
							controller.handleEsc();
							break;

						case ENTER:
							controller.handleEnter();
							break;

						default:
							break;
				}
			});

		//List<Player> pickedPlayers = null;
			try {
				manager.pickPlayers(i);

				controller.showPickedPlayers(manager.getChosenPlayers());
				controller.setDialogStage(dialogStage);

				dialogStage.showAndWait();
				return controller.isOkClicked();

			} catch (InvalidNumberPlayersException e) {
				Dialogs.create()
						.title("Błąd")
						.masthead("Brak graczy do wylosowania!")
						.message(e.getMessage())
						.showError();
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Player> getPickedPlayers() {
		return manager.getChosenPlayers();
	}

	public void loadDataFromFile(File file) {
		try {
			setDefaultDataFilePath(file.getAbsoluteFile().toString());
			setDefaultDataPath(file.getParent());
			manager.parseXml(file.toString());
			
		} catch (Exception e) { // catches ANY exception
			Dialogs.create()
					.title("Błąd")
					.masthead("Błąd wczytywania pliku!")
					.message(e.getMessage())
					.showError();
		}
	}

	public void saveDataToFile(File file) throws ParserConfigurationException, TransformerException {
			manager.saveXMLFile(file);
			setDefaultDataFilePath(file.getAbsoluteFile().toString());
			setDefaultDataPath(file.getParent());
	}

	public void setClipboardContentString(String str) {
		content.putString(str);
		clipboard.setContent(content);
	}

	public void setNumberOfShowsConsidered(boolean value) {
		manager.setNumberOfShowsConsidered(value);
	}

	public void setSubscriptionPaidConsidered(boolean value) {
		manager.setSubscriptionPaidConsidered(value);
	}

	public void setActiveConsidered(boolean value) {
		manager.setActiveConsidered(value);
	}

	public void getMainAppFilePath() {
		//FIXME: po co ta zmienna?
		Preferences pref = Preferences.userNodeForPackage(MainApp.class);
		//String filePath = pref.get
	}
}
