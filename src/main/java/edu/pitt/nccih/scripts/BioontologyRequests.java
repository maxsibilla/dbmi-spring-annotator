package edu.pitt.nccih.scripts;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BioontologyRequests {
    private static List<String> parentVocab = new ArrayList<>();
    private static List<String> grandParentVocab = new ArrayList<String>();


//    public static void main(String[] args) {
//        String searchterm = "genetics";
//        String result = ontologyRequest(searchterm, "MESH", null);
//        parseGetRequest(result);
//
//        result = ontologyRequest(searchterm, "NCIT", null);
//        parseGetRequest(result);
//
//        System.out.println(Arrays.toString(parentVocab.toArray()));
//        System.out.println(Arrays.toString(grandParentVocab.toArray()));
//    }

    private static final String apiKey = "f060b29e-fc9a-4dcd-b3be-9741466dbc4a";
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();


    public static String ontologyRequest(String searchTerm, String ontology, String url) {
        HttpGet request;

        if (url != null) {
            request = new HttpGet(url + "?apikey=" + apiKey);
        } else {
            try {
                request = new HttpGet("http://data.bioontology.org/search?q=" +  URLEncoder.encode(searchTerm, String.valueOf(StandardCharsets.UTF_8)) + "&ontologies=" + ontology + "&apikey=" + apiKey);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void parseGetRequest(String jsonString) {
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        JsonObject links = jsonObject.get("collection").getAsJsonArray().get(0).getAsJsonObject().get("links").getAsJsonObject();
        String parentLink = links.get("ancestors").getAsString();

        try {
            JsonObject parentJsonObject = new JsonParser().parse(ontologyRequest(null, null, parentLink)).getAsJsonArray().get(0).getAsJsonObject();
            parentVocab.add("(" + parentJsonObject.get("prefLabel").getAsString() + ")");
            links = parentJsonObject.getAsJsonObject().get("links").getAsJsonObject();
            String grandParentLink = links.get("ancestors").getAsString();

            try {
                JsonObject grandParentJsonObject = new JsonParser().parse(ontologyRequest(null, null, grandParentLink)).getAsJsonArray().get(0).getAsJsonObject();
                grandParentVocab.add("(" + grandParentJsonObject.get("prefLabel").getAsString() + ")" );
            } catch (IndexOutOfBoundsException e) {
                grandParentVocab.add(null);
            }
        } catch (IndexOutOfBoundsException ex) {
            parentVocab.add(null);
        }
    }

}
