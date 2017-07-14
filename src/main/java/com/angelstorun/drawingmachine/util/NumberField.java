package com.angelstorun.drawingmachine.util;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class NumberField extends TextField {
	public NumberField() {
		this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent t) {
				char ar[] = t.getCharacter().toCharArray();
				char ch = ar[t.getCharacter().toCharArray().length - 1];
				if (!(ch >= '0' && ch <= '9')) {
					//TODO: zastąpić to jakimś loggerem
					System.out.println("Wprowadzony znak nie jest cyfrą");
					t.consume();
				}
			}
		});
	}
}