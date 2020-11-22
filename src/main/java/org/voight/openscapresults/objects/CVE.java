package org.voight.openscapresults.objects;
import org.apache.log4j.Logger;

import java.util.Formatter;

public class CVE {
    private static Logger log = Logger.getLogger(CVE.class);
    private String dateStamp;
    private String hostname;
    private String vulnerable;
    private String refId;
    private String refUrl;
    private String title;
    private String severity;
    private String description;

    public CVE(String dateStamp, String hostname, String vulnerable, String refId, String refUrl, String title, String severity, String description) {
        this.dateStamp = dateStamp;
        this.hostname = hostname;
        this.vulnerable = vulnerable;
        this.refId = refId;
        this.refUrl = refUrl;
        this.title = title;
        this.severity = normalizeSeverity(severity);
        this.description = description;
        log.debug(String.format("%s %s", refId, this.severity));
    }

    public String toJSON(){
        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder);
        formatter.format("{\"timestamp\" : \"%s\", \"hostname\": \"%s\", \"vulnerable\" : \"%s\", \"refId\" : \"%s\", ", dateStamp, hostname, vulnerable, refId);
        formatter.format("\"refURL\" : \"%s\", \"title\" : \"%s\", \"severity\" : \"%s\",  ", refUrl, title, severity);
        formatter.format("\"description\" : \"%s\"}%n", description.replaceAll(",", "\\,").replaceAll("\\n", " "));
        log.debug(builder.toString());
        return builder.toString();
    }

    public String getRefId() {
        return refId;
    }

    /**
     * Converts the severity to a normal form.
     * @param severity
     * @return String
     */
    private String normalizeSeverity(String severity){
        switch(severity){
            case "Moderate":
                return "Medium";
            case "Low":
                return "Low";
            case "Negligible":
                return "Info";
            case "Important":
                return "High";
            case "Critical":
                return "Severe";
            default:
                return severity;
        }
    }
}
