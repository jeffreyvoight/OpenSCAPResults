package org.voight.openscapresults.io;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import java.io.IOException;

public class ElasticWriter {
    protected final String elasticHost;
    protected final int elasticPort;
    protected final String elasticUser;
    protected final String elasticPass;
    protected final String indexName;
    private Logger log = Logger.getLogger(ElasticWriter.class);

    public ElasticWriter(String elasticHost, int elasticPort, String elasticUser, String elasticPass, String indexName) {
        this.elasticHost = elasticHost;
        this.elasticPort = elasticPort;
        this.elasticUser = elasticUser;
        this.elasticPass = elasticPass;
        this.indexName = indexName;
    }

    protected int sendOne(String toPut) throws IOException {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(elasticHost, elasticPort),
                new UsernamePasswordCredentials(elasticUser, elasticPass));
        String url = String.format("http://%s:%d/%s", elasticHost, elasticPort, indexName);
        log.info(url);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Content-Type", "application/x-json");

        if (null != toPut) {
            StringBuilder builder = new StringBuilder();
            builder.append("{\"index\": {\"_index\": \"" + indexName + "\" }}\n");
            builder.append(toPut + "\n");
            HttpEntity httpEntity = new ByteArrayEntity(builder.toString().getBytes("UTF-8"));
            httpPut.setEntity(httpEntity);
        }
        HttpResponse httpResponse = httpClient.execute(httpPut);
        return httpResponse.getStatusLine().getStatusCode();
    }
}
