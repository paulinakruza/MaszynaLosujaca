package com.angelstorun.drawingmachine.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.angelstorun.drawingmachine.model.Player;


public class DomParser {
	Document dom;

	
	private void parseXmlFile(String filename) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		dom = db.parse(new File(filename));
	}

	private void parseDocument(List<Player> myPlayers) {
		Element docEle = dom.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("gracz");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				Player p = getPlayer(el);

				myPlayers.add(p);
			}
		}
	}

	private Player getPlayer(Element player) {
		String name = getTextValue(player, "nazwisko");
		int showsCount = getIntValue(player, "liczbaPokazow");
		boolean subscriptionPaid = getTextValue(player, "czyOplaconaSkladka").equals("tak");

		Player p = new Player(name, showsCount, subscriptionPaid);
		return p;
	}

	private String getTextValue(Element elem, String tagName) {
		String textVal = null;
		NodeList nl = elem.getElementsByTagName(tagName);
		if(nl != null && nl.getLength()>0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
		
	}

	private int getIntValue(Element elem, String tagName) {
		return Integer.parseInt(getTextValue(elem, tagName));
	}

	
	public void runXmlParser(String filename, List<Player> myPlayers) throws SAXException, IOException, ParserConfigurationException {
		parseXmlFile(filename);
		
		myPlayers.clear();
		parseDocument(myPlayers);
	}

	public void saveAsXmlFile(File file, List<Player> players) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		// root element
		Element rootElement = doc.createElement("zawodnicy");
		doc.appendChild(rootElement);

		for (Player p : players) {
			Element playerElement = doc.createElement("gracz");

			Element name = doc.createElement("nazwisko");
			name.appendChild(doc.createTextNode(p.getName()));

			playerElement.appendChild(name);

			Element showCouter = doc.createElement("liczbaPokazow");
			showCouter.appendChild(doc.createTextNode(Integer.toString(p.getNumberOfShows())));

			playerElement.appendChild(showCouter);

			Element subscribtion = doc.createElement("czyOplaconaSkladka");
			subscribtion.appendChild(doc.createTextNode(p.isSubscriptionPaid() ? "tak" : "nie"));
			playerElement.appendChild(subscribtion);
			rootElement.appendChild(playerElement);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);

		transformer.transform(source, result);
	}
}
