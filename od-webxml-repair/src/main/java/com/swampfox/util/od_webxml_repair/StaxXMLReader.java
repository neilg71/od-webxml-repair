package com.swampfox.util.od_webxml_repair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxXMLReader {
	
	private XMLInputFactory xmlInputFactory;
	private StaxXMLParser xmlParser = null;
	
	StaxXMLReader(){
		xmlInputFactory = XMLInputFactory.newInstance();
	}
	
	public void parseXML(String fileName){
		try {
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
			while(xmlEventReader.hasNext()){
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				if(xmlEvent.isStartElement()) {
					processElement(xmlEvent.asStartElement());					
				}
				if(xmlEvent.isEndElement()) {
					processElement(xmlEvent.asEndElement());
				}
				if(xmlEvent.isCharacters()) {
					processElement(xmlEvent.asCharacters());
				}
			}
		}
		catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
		}
	}
	
	private void processElement(StartElement startElement) {
		xmlParser.parseStartElement(startElement);
		@SuppressWarnings("unchecked")
		Iterator<Attribute> attributes = startElement.getAttributes();
		while(attributes.hasNext()) {
			xmlParser.parseAttribute(attributes.next());
		}
		
	}
	
	private void processElement(EndElement endElement) {
		xmlParser.parseEndElement(endElement);		
	}
	
	private void processElement(Characters characters) {
		xmlParser.parseCharacters(characters);		
	}


	public StaxXMLParser getXmlParser() {
		return xmlParser;
	}

	public void setXmlParser(StaxXMLParser xmlParser) {
		this.xmlParser = xmlParser;
	}

	
}
