package org.voight.openscapresults;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.voight.openscapresults.io.*;
import org.voight.openscapresults.objects.OvalResults;
import org.voight.openscapresults.objects.Results;
import org.voight.openscapresults.objects.XCCDFResults;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;


public class SCAPResults {
    private static final Logger log = Logger.getLogger(SCAPResults.class);

    public enum FileType {
        oval,
        xccdf;
    }
    private final File inputFile;
    private final FileType fileType;
    private final String elasticHost;
    private final int elasticPort;
    private final String elasticUser;
    private final String elasticPass;
    private final String indexName;

    public SCAPResults(String inputFile, String elasticHost, int elasticPort, String elasticUser, String elasticPass, String indexName, FileType fileType) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        this.inputFile = new File(inputFile);
        this.elasticHost = elasticHost;
        this.elasticPort = elasticPort;
        this.elasticUser = elasticUser;
        this.elasticPass = elasticPass;
        this.indexName = indexName;
        this.fileType = fileType;
        OSCAPFileReader fileReader;
        JSONWriter jsonWriter;
        Results results;
        switch(fileType){
            case oval:
                log.info(String.format("Reading from file: %s", this.inputFile.getName()));
                fileReader = new OvalFileReader(this.inputFile);
                jsonWriter = new JSONOvalWriter(elasticHost, elasticPort, elasticUser, elasticPass, indexName);
                results = fileReader.parse();
                jsonWriter.write((OvalResults)results);
                break;
            case xccdf:
                fileReader = new XCCDFFileReader(this.inputFile);
                jsonWriter = new JSONXccdfWriter(elasticHost, elasticPort, elasticUser, elasticPass, indexName);
                results = fileReader.parse();
                jsonWriter.write((XCCDFResults)results);
                break;
            default:
                usageInstructions();
        }
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
            new SCAPResults(argv[0], hostName, hostPort, user, password, argv[3], FileType.valueOf(argv[4]));
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
