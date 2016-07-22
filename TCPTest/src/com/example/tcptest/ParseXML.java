package com.example.tcptest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



import android.os.Environment;
import android.util.Log;

public class ParseXML {

	public static List<Parameter> getParameters()
	{
		List<Parameter>params = new ArrayList<Parameter>();
		File sdcard = Environment.getExternalStorageDirectory();
	
		//Get the text file
		File file = new File(sdcard + "/project/","config.xml");    
		Document doc = null;
		try {
			InputStream is = new FileInputStream(file.getPath());
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db;
			db = dbf.newDocumentBuilder();
		    doc = db.parse(new InputSource(is));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	    NodeList nl = doc.getElementsByTagName("Task");
	
	    for (int i = 0; i < nl.getLength(); i++) {
	    	Parameter param = new Parameter();
	    	Element el = (Element) nl.item(i);
	    	NodeList childnodes = ((Node) el).getChildNodes();
	    	for(int j = 0; j < childnodes.getLength(); j ++)
	    	{
	    		Node node = childnodes.item(j);
	    		if(node.getNodeName().equals("BackgroundFilename"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setBackgroundFilename(node.getTextContent());
	    			Log.d(ProgramAutoRun.TAG, node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("Filename"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setFilename(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("FTPFilename"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setFTPFilename(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("BackgroundPORT"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setBackgroundPORT(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("PORT"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setPORT(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("WaitTime"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setWaitTime(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("MessageSize"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setMessageSize(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("ServerIP"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setServerIP(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("rmem_min"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setrmem_min(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("rmem_default"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setrmem_default(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("rmem_max"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setrmem_max(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("ECR"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setECR(node.getTextContent());
	    		}}    		
	    		else if(node.getNodeName().equals("ECN"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setECN(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("Timestamp"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setTimestamp(node.getTextContent());
	    		}}
	    		else if(node.getNodeName().equals("MTU"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setMTU(node.getTextContent());
	    		}}
	    	}
	    	params.add(param);
	    	
	    	

	    }
	
	    
	    return params;
	}
	
	

	public static List<Parameter> getParameters(String msg)
	{
		List<Parameter>params = new ArrayList<Parameter>();
		//File sdcard = Environment.getExternalStorageDirectory();
	
		//Get the text file
		//File file = new File(sdcard + "/project/","config.xml");    
		Document doc = null;
		try {
			//InputStream is = new FileInputStream(file.getPath());
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			doc = db.parse(new ByteArrayInputStream(msg.getBytes()));
		    //doc = db.parse(new InputSource(is));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	    NodeList nl = doc.getElementsByTagName("Task");
	
	    for (int i = 0; i < nl.getLength(); i++) {
	    	Parameter param = new Parameter();
	    	Element el = (Element) nl.item(i);
	    	NodeList childnodes = ((Node) el).getChildNodes();
	    	for(int j = 0; j < childnodes.getLength(); j ++)
	    	{
	    		Node node = childnodes.item(j);
	    		if(node.getNodeName().equals("BackgroundFilename"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setBackgroundFilename(node.getTextContent());
	    			Log.d(ProgramAutoRun.TAG, node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("Filename"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setFilename(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("BackgroundPORT"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setBackgroundPORT(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("PORT"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setPORT(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("WaitTime"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setWaitTime(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("MessageSize"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setMessageSize(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("ServerIP"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setServerIP(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("rmem_min"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setrmem_min(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("rmem_default"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setrmem_default(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("rmem_max"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setrmem_max(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("ECR"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setECR(node.getTextContent());
	    		}}    		
	    		if(node.getNodeName().equals("ECN"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setECN(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("Timestamp"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setTimestamp(node.getTextContent());
	    		}}
	    		if(node.getNodeName().equals("MTU"))
	    		{if (!node.getTextContent().equals("null")){
	    			param.setMTU(node.getTextContent());
	    		}}
	    	}
	    	params.add(param);
	    	
	    	

	    }
	
	    
	    return params;
	}
}
