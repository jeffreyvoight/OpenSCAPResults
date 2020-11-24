package org.voight.openscapresults.io;

import org.voight.openscapresults.objects.CVE;
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
    private final String resultsPath="/oval_results/results/system/definitions/definition[@result='true']";
    private final String titlePath = "metadata/title";
    private final String referencePath = "metadata/reference";
    private final String severityPath = "metadata/advisory/severity";
    private final String descriptionPath = "metadata/description";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final Calendar cal = Calendar.getInstance();
    private final String dateStamp = sdf.format(cal.getTime());
    private final String hostname = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("hostname").getInputStream())).readLine();

    public OvalFileReader(File inputFile, ElasticWriter output) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        super(inputFile, output);
    }

    @Override
    protected void parse() throws IOException, XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList resultsList = getNodeList(xpath, resultsPath);
        int m = resultsList.getLength();
        log.info(String.format("There are %d OVAL results.", m));

        for(int i = 0; i < m; i++){
            CVE c = getCVE(xpath, resultsList, i);
            if (c == null) break;
            output.sendOne(c.toJSON());
            count++;
        }
    }

    private CVE getCVE(XPath xpath, NodeList resultsList, int i) throws XPathExpressionException {
        Node node = resultsList.item(i);
        String id = node.getAttributes().getNamedItem("definition_id").getNodeValue();
        if(id.equals("oval:com.ubuntu.focal:def:100")){ // Test for Ubuntu. Since there's no vulnerability, this would fail the severity check
            log.debug("Ubuntu test. Skipping because there's no severity key.");
            return null;
        }
        String ovalDefinitionPath = "/oval_results/oval_definitions/definitions/definition[@id='"+id+"']";

        Node cve=((Node) xpath.compile(ovalDefinitionPath).evaluate(document, XPathConstants.NODE));
        String title = getNamedTextContent(xpath, cve, titlePath);
        String vulnerable = node.getAttributes().getNamedItem("result").getNodeValue();
        String severity = getNamedTextContent(xpath, cve, severityPath);
        String description = getNamedTextContent(xpath, cve, descriptionPath);
        String refUrl = getNamedNodeValue(xpath, cve, referencePath, "ref_url");
        String refId = getNamedNodeValue(xpath, cve, referencePath,"ref_id");
        return new CVE(dateStamp, hostname, vulnerable, refId, refUrl, title, severity, description);
    }
}
