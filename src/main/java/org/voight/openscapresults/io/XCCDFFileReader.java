package org.voight.openscapresults.io;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

public class XCCDFFileReader extends OSCAPFileReader {
    public XCCDFFileReader(File inputFile, ElasticWriter elasticWriter) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        super(inputFile, elasticWriter);
    }

    @Override
    protected void parse() throws IOException, XPathExpressionException {

    }
}
