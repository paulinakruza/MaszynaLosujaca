package com.angelstorun.drawingmachine.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.angelstorun.drawingmachine.util.DomParser;


public class TeamManager {
	private ObservableList <Player> myPlayers     =  FXCollections.observableArrayList();
	private ArrayList <Player>      chosenPlayers = null;

	private boolean                 isNumberOfShowsConsidered;
	private boolean                 isSubscriptionPaidConsidered;
	private boolean                 isActiveConsidered;

	
	private int countPlayers() {
		int i = 0;
		for (Player p : myPlayers) {
			if (p.isIncluded()) {
				i++;
			}
		}
		return i;
	}


	public void parseXml(String filename) throws SAXException, IOException, ParserConfigurationException {
		DomParser dom = new DomParser();
		dom.runXmlParser(filename, myPlayers);
	}

	public void saveXMLFile(File file) throws ParserConfigurationException, TransformerException {
		DomParser dom = new DomParser();
		dom.saveAsXmlFile(file, myPlayers);
	}

	public void recalculateProbability() {
		int maxShows = -1;

		if (isNumberOfShowsConsidered()) {
			for (Player p : myPlayers) {
				if ((p.getNumberOfShows() > maxShows) && p.isCoutning()) {
					maxShows = p.getNumberOfShows();
				}
			}
		}	

		for (Player p : myPlayers) {
			if (p.isCoutning()) {
				p.recalculateProbability(isActiveConsidered(), isSubscriptionPaidConsidered(), isNumberOfShowsConsidered(), maxShows);
			}
		}
	}

	public void pickPlayers(int n) throws InvalidNumberPlayersException {
		if (n <= countPlayers()) {
			ArrayList<Player> chosenPlayers = new ArrayList<Player>(n);
			ArrayList<Player> tmpPlayers = new ArrayList<>(myPlayers);

			// Sortowanie listy:
			// 1. tworzenie comparator
			Comparator<Player> comparePlayer = (d1, d2) -> d1.getProbability()
					.compareTo(d2.getProbability());

			// 2. sortowanie w kolejności malejącej
			Collections.sort(tmpPlayers,
					Collections.reverseOrder(comparePlayer));

			// Przygotowanie listy list zawodników o tym samym
			// prawdopodobieństwie.
			// TODO: może da się to prościej zrobić?
			ArrayList<ArrayList<Player>> sortedPlayers = new ArrayList<ArrayList<Player>>();

			Double lastProbability = -1.0;

			ArrayList<Player> tempList = new ArrayList<Player>();

			for (Player p : tmpPlayers) {
				if (!p.getProbability().equals(lastProbability)) {
					if (tempList.size() > 0) {
						sortedPlayers.add(tempList);
						tempList = new ArrayList<Player>();
					}
				}
				if (p.isIncluded()) {
					tempList.add(p);
					lastProbability = p.getProbability();
				}
			}

			if (tempList.size() > 0) {
				sortedPlayers.add(tempList);
			}

			// Wybór graczy
			int countChoosen = 0;
			Random generator = new Random();

			while (countChoosen < n) {
				if (sortedPlayers.get(0).size() <= (n - countChoosen)) {
					chosenPlayers.addAll(sortedPlayers.get(0));
					countChoosen += sortedPlayers.get(0).size();
					sortedPlayers.remove(0);
					
				} else {
					int l = (n - countChoosen);
					for (int k = 0; k < l; k++) {
						int rand = generator.nextInt(sortedPlayers.get(0)
								.size());
						chosenPlayers.add(sortedPlayers.get(0).get(rand));
						countChoosen++;
						sortedPlayers.get(0).remove(rand);
					}
				}
			}

			this.chosenPlayers = chosenPlayers;

		} else {
			this.chosenPlayers = null;
			throw new InvalidNumberPlayersException(
					"Za mało zawodników do wylosowania żądanej liczby graczy na pokaz!\n\n"
							+ "Zmniejsz liczbę zawodników do wylosowania lub wybierz większą pulę graczy oznaczając ich odpowiednio w polu \"Jest w puli\".");
		}
	}
	
	
	public ObservableList<Player> getMyPlayers() {
		return myPlayers;
	}

	public List<Player> getChosenPlayers() {
		return this.chosenPlayers;
	}
	
	public boolean isNumberOfShowsConsidered() {
		return isNumberOfShowsConsidered;
	}
	
	public void setNumberOfShowsConsidered(boolean isNumberOfShowsConsidered) {
		this.isNumberOfShowsConsidered = isNumberOfShowsConsidered;
	}
	
	
	public boolean isSubscriptionPaidConsidered() {
		return isSubscriptionPaidConsidered;
	}

	public void setSubscriptionPaidConsidered(boolean isSubscriptionPaidConsidered) {
		this.isSubscriptionPaidConsidered = isSubscriptionPaidConsidered;
	}

	
	public boolean isActiveConsidered() {
		return isActiveConsidered;
	}

	public void setActiveConsidered(boolean isActiveConsidered) {
		this.isActiveConsidered = isActiveConsidered;
	}

	
	public String toString() {
		String value = new String();
		Iterator<Player> it = myPlayers.iterator();
		while (it.hasNext()) {
			value += it.next().toString() + "\n";
		}
		return value;
	}
}
