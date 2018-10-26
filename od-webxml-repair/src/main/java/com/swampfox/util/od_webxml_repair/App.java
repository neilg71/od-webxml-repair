package com.swampfox.util.od_webxml_repair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;


public class App 
{
	final static String SUBFLOWDIR = "subflowdir";
    public static void main( String[] args )
    {
    	Properties prop = new Properties();
    	InputStream input = null;
    	
    	try {
    		input = new FileInputStream("settings.properties");
    		prop.load(input);
    	}
    	catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
    	
    	String dir = prop.getProperty(SUBFLOWDIR);
    	
        StaxXMLReader staxWebXMLReader = new StaxXMLReader();
        staxWebXMLReader.setXmlParser(new StaxWebXMLParser());
        staxWebXMLReader.parseXML("web.xml");
        
                
        File folder = new File(dir);
        File[] files = folder.listFiles();
        
        for(File file:files) {
            System.out.println("\n");
        	System.out.println("Processing file:" + dir + "\\" + file.getName());
        	StaxXMLReader staxFlowXMLReader = new StaxXMLReader();
        	staxFlowXMLReader.setXmlParser(new StaxFlowXMLParser(file.getName()));
        	staxFlowXMLReader.parseXML(dir + "\\" + file.getName());
        	compareFiles(staxWebXMLReader.getXmlParser().getList(), staxFlowXMLReader.getXmlParser().getList());
        }
        
    }
    
    private static boolean predCompare(String t, List<String>webxmlServlets) {
    	boolean found = webxmlServlets.contains(t);
    	if(found==false) {
    		System.err.println("flowServlet:" + t + " not found in web.xml");
    	}
    	return found;
    }
    
    public static void compareFiles(List<String>webxmlServlets, List<String>flowServlets) {
        Predicate<String> pred = t->predCompare(t, webxmlServlets);
    	flowServlets.stream().allMatch(pred);
    }
}
