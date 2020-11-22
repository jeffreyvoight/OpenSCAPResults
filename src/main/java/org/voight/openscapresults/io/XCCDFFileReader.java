package org.voight.openscapresults.io;

import org.voight.openscapresults.objects.XCCDFResults;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XCCDFFileReader extends OSCAPFileReader {
    public XCCDFFileReader(File inputFile) throws IOException, ParserConfigurationException, SAXException {
        super(inputFile);
    }

    public XCCDFResults parse(){
        return null;
    }
}
