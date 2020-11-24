package org.voight.openscapresults.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;

public abstract class OSCAPFileReader {
    protected ElasticWriter output;
    protected Document document;
    protected int count = 0;

    public OSCAPFileReader(File _file, ElasticWriter output) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(_file);
        this.output = output;
        parse();
    }

    protected abstract void parse() throws IOException, XPathExpressionException;

    public int getCount(){
        return count;
    }


    protected String getNamedNodeValue(XPath xpath, Node cve, String referencePath, String name) throws XPathExpressionException {
        return ((Node) xpath.compile(referencePath).evaluate(cve, XPathConstants.NODE)).getAttributes().getNamedItem(name).getNodeValue();
    }

    protected String getNamedTextContent(XPath xpath, Node cve, String name) throws XPathExpressionException {
        return ((Node) xpath.compile(name).evaluate(cve, XPathConstants.NODE)).getTextContent();
    }

    protected NodeList getNodeList(XPath xpath, String resultsPath) throws XPathExpressionException {
        return (NodeList) xpath.compile(resultsPath).evaluate(document, XPathConstants.NODESET);
    }
}
