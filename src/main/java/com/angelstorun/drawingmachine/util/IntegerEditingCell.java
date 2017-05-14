package com.angelstorun.drawingmachine.util;

import java.util.regex.Pattern;

import com.angelstorun.drawingmachine.model.Player;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class IntegerEditingCell extends TableCell<Player, Number> {

	private final TextField textField  = new TextField();
	private final Pattern   intPattern = Pattern.compile("\\d+");
	private boolean         escPressed = false;

	
	private void processEdit() {
		String text = textField.getText();
		if (intPattern.matcher(text).matches()) {
			commitEdit(Integer.parseInt(text));
			
		} else {
			cancelEdit();
		}
	}

	
	@Override
	public void updateItem(Number value, boolean empty) {
		super.updateItem(value, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
			
		} else if (isEditing()) {
			setText(null);
			textField.setText(value.toString());
			setGraphic(textField);
			
		} else {
			setText(value.toString());
			setGraphic(null);
		}
	}

	@Override
	public void startEdit() {
		super.startEdit();
		Number value = getItem();
		if (value != null) {
			textField.setText(value.toString());
			setGraphic(textField);
			setText(null);
		}
	}
	
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getItem().toString());
		setGraphic(null);
	}
	
	@Override
	public void commitEdit(Number value) {
		super.commitEdit(value);
		((Player)this.getTableRow().getItem()).setNumberOfShows(value.intValue());
	}

	public IntegerEditingCell() {
		textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (!isNowFocused && !escPressed) {
				processEdit();
			}
		});
		textField.setOnAction(event -> processEdit());
		textField.setOnKeyPressed((value) -> {
			if (value.getCode() == KeyCode.ESCAPE) {
				escPressed = true;
			}
		});
	}
}
