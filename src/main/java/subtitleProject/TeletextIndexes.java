package subtitleProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jfree.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses through xml file via SAXParser and makes a list with teleteext subtitlepages and timestamps for each list
 * @author Jacob
 *
 */
public class TeletextIndexes extends DefaultHandler {

	private List<TimePeriod> timePeriods;
	private String xmlPath;

	private TimePeriod timePtmp; 
	String tmpValue;

	public TeletextIndexes(String xmlPath) {
		super();
		timePeriods = new ArrayList<TimePeriod>();
		this.xmlPath = xmlPath;
		parseDocument();
		Collections.sort(timePeriods);
	}

	public List<TimePeriod> getTimePeriodes(){
		return timePeriods;
	}

	/**
	 * Method to start parsing xml
	 */
	private void parseDocument() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(xmlPath, this);
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfig error");
		} catch (SAXException e) {
			System.out.println("SAXException : xml not well formed");
		} catch (IOException e) {
			System.out.println("IO error");
		}
	}

	@Override
	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
		if (elementName.equalsIgnoreCase("timePeriod")) {
			timePtmp = new TimePeriod();
		}
	}

	@Override
	public void endElement(String s, String s1, String element) throws SAXException {
		if (element.equals("timePeriod")) {
			timePeriods.add(timePtmp);
		}
		if (element.equalsIgnoreCase("airDay")) {
			timePtmp.setAirDay(tmpValue);
		}
		if (!element.equalsIgnoreCase("teletextIndexes")||!element.equalsIgnoreCase("/timePeriod")||!element.equalsIgnoreCase("/teletextIndexes")||!element.equalsIgnoreCase("timePeriod")||!element.equalsIgnoreCase("airDay")){
			timePtmp.getIndexes().put(element, tmpValue);
		}
	}

	@Override
	public void characters(char[] ac, int i, int j) throws SAXException {
		tmpValue = new String(ac, i, j);
	}
}

/**
 * Class to contain teletext pages
 * @author Jacob
 *
 */
class TimePeriod implements  Comparable<TimePeriod>{

	String airDay;
	Map<String, String> indexes;

	public TimePeriod() {
		super();
		indexes = new HashMap<String, String>();
	}

	public Map<String, String> getIndexes(){
		return indexes;
	}

	public String getAirDay() {
		return airDay;
	}

	public void setAirDay(String airDay) {
		this.airDay = airDay;
	}

	@Override
	public String toString(){
		String s = airDay.toString()+"\n";
		Iterator<String> channels = indexes.keySet().iterator();
		while(channels.hasNext()){
			String tmp = channels.next();
			s += tmp +": "+indexes.get(tmp)+"\n";
		}
		return s;
	}

	/**
	 * Compare method used for transportstream namesegment
	 * @param date String[] to compare
	 * @return comparison
	 */
	public int compareTo(String[] date) {
		String[] thisDate = this.airDay.split("-");
		String[] comDate = date;
		int i = 0;
		if(thisDate[0].compareTo(comDate[0])==0){
			if(thisDate[1].compareTo(comDate[1])==0){
				if(thisDate[2].compareTo(comDate[2])==0){
					Log.error("xml has timeperiods with equal stamps");
				}
				else if(thisDate[2].compareTo(comDate[2])<0){
					i=-1;
				}
				else{
					i=1;
				}
			}
			else if(thisDate[1].compareTo(comDate[1])<0){
				i=-1;
			}
			else{
				i=1;
			}
		}
		else if(thisDate[0].compareTo(comDate[0])<0){
			i=-1;
		}
		else{
			i=1;
		}
		return i;
	}

	/**
	 * Same as above but with TimePeriod as argument
	 * @param arg0 timePeriod to compare with
	 * @return comparison
	 */
	public int compareTo(TimePeriod arg0) {

		String[] thisDate = this.airDay.split("-");
		String[] comDate = arg0.getAirDay().split("-");
		int i = 0;
		if(thisDate[0].compareTo(comDate[0])==0){
			if(thisDate[1].compareTo(comDate[1])==0){
				if(thisDate[2].compareTo(comDate[2])==0){
					Log.error("xml has timeperiods with equal stamps");
				}
				else if(thisDate[2].compareTo(comDate[2])<0){
					i=-1;
				}
				else{
					i=1;
				}
			}
			else if(thisDate[1].compareTo(comDate[1])<0){
				i=-1;
			}
			else{
				i=1;
			}
		}
		else if(thisDate[0].compareTo(comDate[0])<0){
			i=-1;
		}
		else{
			i=1;
		}
		return i;

	}
}