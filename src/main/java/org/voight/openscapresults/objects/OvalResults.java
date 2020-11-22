package org.voight.openscapresults.objects;

import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.Collection;

public class OvalResults extends Results {
    public OvalResults() throws IOException, SAXException, ParserConfigurationException {
        super();
    }

    public Collection<CVE> getCVECollection(){
        return resultTable.values();
    }

}
