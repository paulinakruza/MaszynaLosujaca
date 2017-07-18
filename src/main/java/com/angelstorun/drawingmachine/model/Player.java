package com.angelstorun.drawingmachine.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Player {
	/**
	 * Liczba pokazów, w których uczestniczył dany gracz.
	 */
	private final IntegerProperty numberOfShows;

	/**
	 * Umożliwia użytkownikowi wykluczenie gracza z losowania.
	 */
	private final BooleanProperty included = new SimpleBooleanProperty(true);
	
	/**
	 * Informacja o opłaconej (lub nieopłaconej) składce członkowskiej.
	 */
	private final BooleanProperty isSubscriptionPaid;

	/**
	 * Informacja o uczestniczeniu w treningach. Jeśli zawodnik regularnie uczestniczy w treningach
	 * wartość tego pola jest równa TRUE.
	 */
	private final BooleanProperty isActive = new SimpleBooleanProperty(true);
	
	/**
	 * Imię i nazwisko gracza.
	 */
	private final StringProperty name;

	/**
	 * Pole techniczne. Informacja o prawdopodobieństwie wylosowania gracza. 
	 * Wartość pola wyznaczana na podstawie liczby pokazów, w których brał udział zawodnik,
	 * aktywności na treningach i opłacania składki członkowskiej.
	 */
	private Double probability;

	/**
	 * Właściwość techniczna wykorzystywana przy losowaniu graczy. 
	 * Uniemożliwia wybranie kilka razy tego samego gracza w jednym losowaniu.
	 */
	private boolean isAccessible;


	public Player(String playersName, int showsCount, boolean subscriptionPaid) {
		isAccessible         = true;
		probability        = 1.0;
		name               = new SimpleStringProperty(playersName);
		numberOfShows      = new SimpleIntegerProperty(showsCount);
		isSubscriptionPaid = new SimpleBooleanProperty(subscriptionPaid);
	}
	
	public Player(String name, int showsCount) {
		this(name, showsCount, true);
	}

	public Player(String name) {
		this(name, 0);
	}

	public Player() {
		this("Jan Kowalski");
	}

	public void recalculateProbability(int maxShows) {
		if (isAccessible()) {
			
			//zewiększanie prawdopodobieństwa jeśli składka nie jest opłacona
			if (!isSubscriptionPaid()) {
				addProbability(2.0);
			}
			
			if (!isActive()) {
				addProbability(1.0);
			}
			
			//zwiększanie prawdopodobieństa ze względu na ilość pokazów
			int diff = maxShows - getNumberOfShows();
			
			addProbability(diff*0.1);
			
		} else {
			setProbability(0.0);
		}
	}

	public void recalculateProbability(
			boolean isActiveConsidered,
			boolean isSubscriptionPaidConsidered,
			boolean isNumberOfShowsConsidered, 
			int maxShows
		) {
		if (isAccessible()) {
			//Ustalenie początkowej wartości prawdopodobieństwa
			setProbability(1.0);
			
			//zewiększanie prawdopodobieństwa jeśli składka nie jest opłacona
			if (!isSubscriptionPaid() && isSubscriptionPaidConsidered) {
				addProbability(2.0);
			}
			
			if (!isActive() && isActiveConsidered) {
				addProbability(1.0);
			}
			
			if (isNumberOfShowsConsidered) {
				//zwiększanie prawdopodobieństa ze względu na ilość pokazów
				int diff = maxShows - getNumberOfShows();
				
				addProbability(diff*0.1);
			}
			
		} else {
			setProbability(0.0);
		}
	}

	public void addProbability(double value) {
		probability+=value;
	}

	public void addOneShow() {
		this.numberOfShows.set(getNumberOfShows() + 1);
	}

	
	public IntegerProperty getNumberOfShowsProperty() {
		return numberOfShows;
	}

	/**
	 * @return the numberOfShows
	 */
	public int getNumberOfShows() {
		return numberOfShows.get();
	}

	/**
	 * @param numberOfShows the numberOfShows to set
	 */
	public void setNumberOfShows(int numberOfShows) {
		this.numberOfShows.set(numberOfShows);
	}
	

	public BooleanProperty getIsIncludedProperty() {
		return included; 
	}

	public boolean isIncluded() {
		return included.get();
	}

	public void setIncluded(Boolean included) {
		this.included.set(included);
	}
	

	public BooleanProperty getIsSubscriptionPaidProperty() {
		return isSubscriptionPaid;
	}

	/**
	 * @return the isSubscriptionPaid
	 */
	public boolean isSubscriptionPaid() {
		return isSubscriptionPaid.get();
	}

	/**
	 * @param isSubscriptionPaid the isSubscriptionPaid to set
	 */
	public void setSubscriptionPaid(boolean isSubscriptionPaid) {
		this.isSubscriptionPaid.set(isSubscriptionPaid);
	}
	

	public BooleanProperty getIsActiveProperty() {
		return isActive;
	}

	public Boolean isActive() {
		return isActive.get();
	}

	public void setIsActive(Boolean isActive) {
		this.isActive.set(isActive);
	}
	

	public StringProperty getNameProperty() {
		return name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name.set(name);
	}
	

	/**
	 * @return the probability
	 */
	public Double getProbability() {
		return probability;
	}

	/**
	 * @param probability the probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	
	/**
	 * @return the isAccessible
	 */
	public boolean isAccessible() {
		return (isAccessible && isIncluded());
	}

	/**
	 * @param isAccessible the isAccessible to set
	 */
	public void setIsAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "================\n"
				+ name.get() + "\n\t"
				+ "isAccessible = " + isAccessible + "\n\t"
				+ "numberOfShows = " + numberOfShows.get() + "\n\t"
				+ "isSubscriptionPaid = " + isSubscriptionPaid.get() + "\n\t"
				+ "probability = " + probability + "\n";
	}
}
