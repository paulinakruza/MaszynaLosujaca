package com.angelstorun.drawingmachine.util;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;


public class NumberFieldTableCellFactory<T,N> implements Callback<TableColumn<T,N>, TableCell<T,N>> {

	//FIXME: po co s� te zmienne?
	private final int                maxChars;
	private final int                decimals;
	private final boolean            negativesAllowed;
	private final StringConverter<N> convert;

	@Override
	public TableCell<T,N> call( TableColumn<T,N> param) {
		return new TableCell<T,N>() {
			private NumberField    numFld;
			private ContentDisplay prevCD;
			private Node           oldGraphic;
			
			
			private void createEditingField() {
				numFld = new NumberField(/*maxChars, decimals*/);
				// numFld.setAllowNegative( negativesAllowed );
				numFld.setOnKeyReleased(new EventHandler<KeyEvent>() {
					@Override 
					public void handle(KeyEvent KE) {
						if (KE.getCode() == KeyCode.ENTER || KE.getCode() == KeyCode.TAB) {
							try {
								commitEdit(convert.fromString(numFld.getText()));
								
							} catch (Exception EX){
								//Do nothing
							}
							
						} else if (KE.getCode() == KeyCode.ESCAPE){ 
							cancelEdit();
						}
					}
				});
			}
			
			private void removeEditingField() {
				setGraphic( oldGraphic );
				setContentDisplay( prevCD );
				oldGraphic = null;
			}

			
			@Override
			public void startEdit() {
				if (!isEmpty()) {
					super.startEdit();
					prevCD = getContentDisplay();
					setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					oldGraphic = getGraphic();
					if (numFld == null) {
							createEditingField();
					}
					numFld.setText(getText());
					setGraphic(numFld);
					numFld.selectAll();
				}
			}

			@Override
			public void cancelEdit() {
				removeEditingField();
				super.cancelEdit();
			}

			@Override
			public void commitEdit(N newValue) {
				removeEditingField();
				super.commitEdit(newValue);
			}

			@Override
			public void updateItem(N item, boolean empty) {
				super.updateItem(item, empty);
				if (empty){
					setText(null);
					setGraphic(null); 
					
				} else {
					if (!isEditing()) {
						setText(convert.toString(item));
						
					} else {
						numFld.setText(convert.toString(item));
					}
				}
			}
		};
	}
	
	public NumberFieldTableCellFactory(int maxNoChars, int decimalPlaces, boolean allowNegative, StringConverter<N> converter) {
		negativesAllowed = allowNegative;
		decimals         = (decimalPlaces >= 0 ? decimalPlaces : 0);
		maxChars         = maxNoChars;
		convert          = converter;
	}
}
