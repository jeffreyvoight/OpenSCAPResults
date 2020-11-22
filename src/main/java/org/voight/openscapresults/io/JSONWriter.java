package org.voight.openscapresults.io;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.voight.openscapresults.objects.Results;

import java.io.IOException;

public abstract class JSONWriter {
    protected final String elasticHost;
    protected final int elasticPort;
    protected final String elasticUser;
    protected final String elasticPass;
    protected final String indexName;
    private Logger log = Logger.getLogger(JSONWriter.class);

    public JSONWriter(String elasticHost, int elasticPort, String elasticUser, String elasticPass, String indexName) {
        this.elasticHost = elasticHost;
        this.elasticPort = elasticPort;
        this.elasticUser = elasticUser;
        this.elasticPass = elasticPass;
        this.indexName = indexName;
    }

    public abstract void write(Results Results) throws IOException;


    protected int sendPut(String toPut) throws IOException {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(elasticHost, elasticPort),
                new UsernamePasswordCredentials(elasticUser, elasticPass));
        String url = String.format("http://%s:%d/%s/_bulk", elasticHost, elasticPort, indexName);
        log.info(url);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider).build();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Content-Type", "application/x-ndjson");

        if (null != toPut) {
            HttpEntity httpEntity = new ByteArrayEntity(toPut.getBytes("UTF-8"));
            httpPut.setEntity(httpEntity);
        }
        HttpResponse httpResponse = httpClient.execute(httpPut);
        return httpResponse.getStatusLine().getStatusCode();
    }
}
