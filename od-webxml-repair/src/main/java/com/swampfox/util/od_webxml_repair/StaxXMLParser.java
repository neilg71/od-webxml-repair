package com.swampfox.util.od_webxml_repair;

import java.util.List;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public interface StaxXMLParser {
	void parseStartElement(StartElement startElement);
	void parseAttribute(Attribute attribute);
	void parseEndElement(EndElement startElement);
	void parseCharacters(Characters characterElement);
	
	List<String> getList();

}
