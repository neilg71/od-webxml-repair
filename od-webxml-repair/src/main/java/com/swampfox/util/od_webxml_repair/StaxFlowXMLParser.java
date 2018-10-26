package com.swampfox.util.od_webxml_repair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class StaxFlowXMLParser implements StaxXMLParser {
	
	private static final String PROPERTY = "Property";
	private static final String FLOWNODE = "FlowNode";
	private static final String CLASS = "class";
	private static final String LABEL_CLASS = "com.avaya.sce.callflow.internal.appflow.Label";
	private static final String BOOKMARK_CLASS = "com.avaya.sce.callflow.internal.appflow.Bookmark";
	private static final String LABEL = "Label";
	private static final String BOOKMARK = "Bookmark";
	private static final String NAME = "Name";
	private static final String _NAME = "name";
	private static final String MAIN = "main.flow";
	
	private String flowName = null;
	private String preamble = "";
	
	private Stack<String> nodeStack = new Stack<String>();
	String parent = null;
	private List<String> servletNames = new ArrayList<String>();
	
	private String lastAttrib = null;
	
	public StaxFlowXMLParser(String flowName) {
		if(!flowName.equals(MAIN)) {
			this.flowName = parseFlowName(flowName);
			preamble = this.flowName + "-";
		}
	}
	
	private String parseFlowName(String flowName) {
		int dx = flowName.indexOf(".");
		return flowName.substring(0,dx);
	}

	public void parseStartElement(StartElement startElement) {
		parent=(nodeStack.empty()?null:nodeStack.peek());
		String elementName = startElement.getName().getLocalPart();
		if(elementName.equals(FLOWNODE)) {
			Attribute attribute = startElement.getAttributeByName(new QName(CLASS));
			//we change the node name of a label to avoid collision with servlet nodes
			if(attribute != null && attribute.getValue().equals(LABEL_CLASS)) {
				elementName += LABEL;
			}
			else if(attribute != null && attribute.getValue().equals(BOOKMARK_CLASS)) {
				elementName += BOOKMARK;
			}
		}
		nodeStack.push(elementName);
	}
	
	@Override
	public void parseAttribute(Attribute attribute) {
		if(lastAttrib != null && lastAttrib.equals(NAME)) {
			String servletName = attribute.getValue();
			if(servletNames.stream().anyMatch(t->t.equals(servletName)) == false) {
				System.out.println("Added servlet to flow list:" + preamble + servletName);
				servletNames.add(preamble + servletName);
			}
			lastAttrib = null;
		}
		if(nodeStack.peek().equals(PROPERTY) && parent.equals(FLOWNODE)) {
			if(attribute.getName().equals(new QName(_NAME))) {
				lastAttrib = attribute.getValue();
			}
		}
		
	}
	
	public void parseEndElement(EndElement endElement) {
		nodeStack.pop();
	}
	
	@Override
	public void parseCharacters(Characters characterElement) {
		// TODO Auto-generated method stub
		
	}
	
	public List<String >getList() {
		return servletNames;
	}
	
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

}
