package org.voight.openscapresults.io;

import org.voight.openscapresults.objects.Results;
import java.io.IOException;

public class JSONXccdfWriter extends JSONWriter {

    public JSONXccdfWriter(String elasticHost, int elasticPort, String elasticUser, String elasticPass, String indexName) {
        super(elasticHost, elasticPort, elasticUser, elasticPass, indexName);
    }

    @Override
    public void write(Results Results) throws IOException {

    }


}
