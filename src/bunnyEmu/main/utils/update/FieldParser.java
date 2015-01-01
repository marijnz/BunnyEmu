package bunnyEmu.main.utils.update;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import misc.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML Parser for UpdateFields, to support UpdateFields for different patches
 * 
 * @author Marijn
 *
 */
public class FieldParser {
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private boolean initalized = false;
	
	private HashMap<String, HashMap<String, Integer>> typeFields = new HashMap<String, HashMap<String, Integer>>();
	
	public FieldParser(int version){
		this.parseUpdateFields(version);
	}
	
	private void parseUpdateFields(int version){
		try {
			Document doc = parse("assets/xml/updatefields/" + version + ".xml");
			NodeList sorts = doc.getElementsByTagName("type");
			Logger.writeLog("Parsing " + sorts.getLength() + " nodes", Logger.LOG_TYPE_VERBOSE);
			for (int i = 0; i < sorts.getLength(); i++) {
				Node typeNode = sorts.item(i);
				String type = ((Element) typeNode).getAttribute("name");
				Logger.writeLog("Parsing node: " + type, Logger.LOG_TYPE_VERBOSE);
				NodeList fields = typeNode.getChildNodes();
				for (int i2 = 0; i2 < fields.getLength(); i2++) {
					NodeList nodes =  fields.item(i2).getChildNodes();
					if(nodes.getLength() > 0){
						String name = nodes.item(1).getTextContent();
						String value = nodes.item(3).getTextContent();
						add(type, name, Integer.parseInt(value));
					}
				}
			}
		} catch (IOException e) {
			Logger.writeLog("Parsing went wrong!", Logger.LOG_TYPE_WARNING);
			e.printStackTrace();
		}
	}
	
	private void add(String type, String name, int value){
		HashMap<String, Integer> typeField = typeFields.get(type);
		if (typeField == null){
			typeField = typeFields.put(type, new HashMap<String, Integer>());
			typeField = typeFields.get(type);
		}
		typeField.put(name, value);
		Logger.writeLog("Added new field: " + type + " " + name + " " + value, Logger.LOG_TYPE_VERBOSE);
	}
	
	public Integer get(String type, String name){
		return typeFields.get(type).get(name);
	}
	
	private Document parse(String s) throws IOException{
		Document doc = null;
		try {
			File file = new File(s);
			if(!initalized){
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
				initalized = true;
			}
			doc = builder.parse(file);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
		return doc;
	}
}
