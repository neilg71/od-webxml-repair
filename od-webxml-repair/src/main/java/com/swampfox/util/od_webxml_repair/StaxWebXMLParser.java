package com.swampfox.util.od_webxml_repair;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;


public class StaxWebXMLParser implements StaxXMLParser{
	public static final String SERVLET_NAME = "servlet-name";
	
	private List<String> servletNames = new ArrayList<String>();
	
	private String name = null;
	
	private String lastStartElement = null;
	
	public void parseStartElement(StartElement startElement) {
		if(startElement.getName().getLocalPart().equals(SERVLET_NAME)) {
			lastStartElement = SERVLET_NAME;
		}
	}
	
	public void parseEndElement(EndElement endElement) {
		if(endElement.getName().getLocalPart().equals(SERVLET_NAME)) {
			if(name == null) {
				System.out.println("ERROR - no name found");
				return;
			}
			if(servletNames.stream().anyMatch(t->t.equals(name)) == false) {
				System.out.println("Added servlet to webxml list:" + name);
				servletNames.add(name);
				name = null;
			}
		}
	}
	
	@Override
	public void parseCharacters(Characters characterElement) {
		if(lastStartElement != null && lastStartElement.equals(SERVLET_NAME)) {
			name = characterElement.getData();
			lastStartElement = null;
		}
		
	}
	
	@Override
	public void parseAttribute(Attribute attribute) {
		// TODO Auto-generated method stub
		
	}
	
	public List<String >getList() {
		return servletNames;
	}
	
	
	
}
