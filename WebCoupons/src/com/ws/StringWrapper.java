package com.ws;

import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringWrapper {

	private String value;

	public String getValue() {		
		
		return value;
	}

	public void setValue(String value) {
		try
		{
			value = makeProper(value);
		}
		catch (IOException e)
		{
			
		}
		this.value = value;
	}
	
	private String makeProper(String theString) throws java.io.IOException {

		java.io.StringReader in = new java.io.StringReader(
				theString.toLowerCase());
		boolean precededBySpace = true;
		StringBuffer properCase = new StringBuffer();
		while (true) {
			int i = in.read();
			if (i == -1)
				break;
			char c = (char) i;
			if (c == ' ' || c == '"' || c == '(' || c == '.' || c == '/'
					|| c == '\\' || c == ',') {
				properCase.append(c);
				precededBySpace = true;
			} else {
				if (precededBySpace) {
					properCase.append(Character.toUpperCase(c));
				} else {
					properCase.append(c);
				}
				precededBySpace = false;
			}
		}
		return properCase.toString();
	}
	    
	         

}


