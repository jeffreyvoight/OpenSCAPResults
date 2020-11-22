package org.voight.openscapresults.io;

import org.voight.openscapresults.io.JSONWriter;
import org.voight.openscapresults.objects.CVE;
import org.voight.openscapresults.objects.OvalResults;
import org.voight.openscapresults.objects.Results;

import java.io.IOException;
import java.util.Collection;

public class JSONOvalWriter extends JSONWriter {

    public JSONOvalWriter(String elasticHost, int elasticPort, String elasticUser, String elasticPass, String indexName) {
        super(elasticHost, elasticPort, elasticUser, elasticPass, indexName);
    }

    @Override
    public void write(Results results) throws IOException {
        Collection<CVE> cveCollection = ((OvalResults) results).getCVECollection();
        StringBuilder builder = new StringBuilder();
        for (CVE cve : cveCollection) {
            builder.append("{\"index\": {\"_index\": \"" + indexName + "\", \"_type\":\"scap\"}}\n");
            builder.append(cve.toJSON() + "\n");
        }
        sendPut(builder.toString());

    }

}
