package org.voight.openscapresults.io;

import org.voight.openscapresults.objects.CVE;
import org.voight.openscapresults.objects.OvalResults;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OvalFileReader extends OSCAPFileReader {
    Logger log = Logger.getLogger(OvalFileReader.class);

    public OvalFileReader(File inputFile) throws IOException, ParserConfigurationException, SAXException {
        super(inputFile);
    }

    public OvalResults parse() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        OvalResults r = new OvalResults();
        XPath xpath = XPathFactory.newInstance().newXPath();
        String resultsPath="/oval_results/results/system/definitions/definition[@result='true']";
        String titlePath = "metadata/title";
        String referencePath = "metadata/reference";
        String severityPath = "metadata/advisory/severity";
        String descriptionPath = "metadata/description";

        NodeList resultsList = (NodeList) xpath.compile(resultsPath).evaluate(document, XPathConstants.NODESET);
        int m = resultsList.getLength();
        log.info(String.format("There are %d results.", m));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dateStamp = sdf.format(cal.getTime());
        String hostname = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("hostname").getInputStream())).readLine();

        for(int i = 0; i < m; i++){
            Node node = resultsList.item(i);
            String id = node.getAttributes().getNamedItem("definition_id").getNodeValue();
            String vulnerable = node.getAttributes().getNamedItem("result").getNodeValue();
            if(id.equals("oval:com.ubuntu.focal:def:100")){ // Test for Ubuntu. Since there's no vulnerability, this would fail the severity check
                log.info("Ubuntu test. Skipping because there's no severity key.");
                break;
            }
            String ovalDefinitionPath = "/oval_results/oval_definitions/definitions/definition[@id='"+id+"']";

            Node cve=((Node)xpath.compile(ovalDefinitionPath).evaluate(document, XPathConstants.NODE));
            String title = ((Node) xpath.compile(titlePath).evaluate(cve, XPathConstants.NODE)).getTextContent();
            String severity = ((Node) xpath.compile(severityPath).evaluate(cve, XPathConstants.NODE)).getTextContent();
            String description = ((Node) xpath.compile(descriptionPath).evaluate(cve, XPathConstants.NODE)).getTextContent();
            String refUrl = ((Node) xpath.compile(referencePath).evaluate(cve, XPathConstants.NODE)).getAttributes().getNamedItem("ref_url").getNodeValue();
            String refId = ((Node) xpath.compile(referencePath).evaluate(cve, XPathConstants.NODE)).getAttributes().getNamedItem("ref_id").getNodeValue();
            CVE c = new CVE(dateStamp, hostname, vulnerable, refId, refUrl, title, severity, description);
            r.putCVE(c);
        }
        return r;
    }
}
