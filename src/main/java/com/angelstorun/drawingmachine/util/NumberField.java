package com.angelstorun.drawingmachine.util;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

//TODO: rozważyć, czy nie wprowadzić limitu znaków dla pola
public class NumberField extends TextField {
	public NumberField() {
		this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent t) {
				if (
						t.getCharacter().length() != 1   || 
						t.getCharacter().charAt(0) < '0' || 
						t.getCharacter().charAt(0) > '9'
					) {
						t.consume();
				}
			}
		});
	}
}