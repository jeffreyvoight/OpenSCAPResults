package org.voight.openscapresults;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.voight.openscapresults.io.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class SCAPResults {
    private static final Logger log = Logger.getLogger(SCAPResults.class);

    public enum FileType {
        OVAL,
        XCCDF;
    }

    public SCAPResults(String inputFileName, String elasticHost, int elasticPort, String elasticUser, String elasticPass, String indexName, FileType fileType) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        File inputFile = new File(inputFileName);
        OSCAPFileReader fileReader = null;
        ElasticWriter elasticWriter = new ElasticWriter(elasticHost, elasticPort, elasticUser, elasticPass, indexName);
        log.info(String.format("Reading from file: %s", inputFile.getName()));
        switch(fileType){
            case OVAL:
                fileReader = new OvalFileReader(inputFile, elasticWriter);
                break;
            case XCCDF:
                fileReader = new XCCDFFileReader(inputFile, elasticWriter);
                break;
            default:
                usageInstructions();
        }
        log.info("Processed " + fileReader.getCount() + " entries.");
    }

    public static void main(String[] argv){
        if(argv.length!=5){
            usageInstructions();
        }
        if(!argv[4].equals("oval")&&!argv[4].equals("xccdf")){
            log.error("File Type must be oval or xccdf.");
            usageInstructions();
        }
        try {
            String[] hostParts = argv[1].split(":");
            String hostName = hostParts[0];
            int hostPort = Integer.parseInt(hostParts[1]);
            String[] elasticParts = argv[2].split(":");
            String user = elasticParts[0];
            String password = elasticParts[1];
            new SCAPResults(argv[0], hostName, hostPort, user, password, argv[3], FileType.valueOf(argv[4].toUpperCase()));
        } catch(Exception e){
            log.error("Something bad happened:", e);
            usageInstructions();
        }
    }

    private static void usageInstructions() {
        log.error("Usage: SCAPResults inputFile elasticHost:elasticPort elasticUser:elasticPass indexName oval|xccdf");
        System.exit(-1);
    }
}
