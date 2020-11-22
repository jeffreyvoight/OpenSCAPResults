package org.voight.openscapresults.io;

import org.voight.openscapresults.objects.Results;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;

public abstract class OSCAPFileReader {

    protected Document document;
    public OSCAPFileReader(File _file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant  df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(_file);
    }

    public abstract Results parse() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException;
}
