package org.voight.openscapresults.objects;

import org.apache.log4j.Logger;
import java.util.Formatter;

public class CVE {
    private static final Logger log = Logger.getLogger(CVE.class);
    private final String dateStamp;
    private final String hostname;
    private final String vulnerable;
    private final String refId;
    private final String refUrl;
    private final String title;
    private final String severity;
    private final String description;

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
