package repository;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Secretary;
import model.Student;

public class RepositoryXML implements IRepository<String, String> {
	private String filePath;

	public RepositoryXML(String file) {
		filePath = file;
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

	@Override
	public ArrayList<String> getObjects() {
		ArrayList<String> secretaries = new ArrayList<String>();
		try {
			File file = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getElementsByTagName("secretaries");
			System.out.println("==========================");

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Secretary s = new Secretary();;
					s.username = getValue("username", element);
					s.password = getValue("password", element);
					System.out.println("Stock Symbol: " + getValue("username", element));
					System.out.println("Stock Price: " + getValue("password", element));
					
					secretaries.add(getValue("password", element));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return secretaries;
	}

	@Override
	public String getObject(String key) {
		try {
			File file = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getElementsByTagName("secretaries");
			System.out.println("==========================");

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					
					if (getValue("username", element).equals(key)){
						return getValue("password", element); 
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void deleteObject(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeObject(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateObject(String key, String value) {
		// TODO Auto-generated method stub
		
	}

}
