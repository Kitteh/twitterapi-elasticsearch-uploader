package digital.jameel.twitterelasticsearch;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileParser
{
    private InputStream inputStream;
    private Elasticsearch elasticsearch;
    private JsonArray elements;

    public FileParser(InputStream inputStream){
        this.inputStream = inputStream;
        this.elasticsearch = new Elasticsearch();
    }

    public void parseFile() throws IOException{
        this.elements = this.parseFileToJson();
    }

    public void uploadDocuments(){
        for (JsonElement element : this.elements){
            JsonObject object = element.getAsJsonObject();
            String index = object.get("subject").toString().replace("#", "").replace("\"", "").toLowerCase();
            int statusCode = this.elasticsearch.putDocument(index, object.toString());
            if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR){
                System.out.println("500 Failed to upload document");
            } else {
                System.out.println(statusCode + " Uploaded successfully");
            }
        }
    }

    private JsonArray parseFileToJson() throws IOException{
        BufferedReader bf = new BufferedReader(new InputStreamReader(this.inputStream));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = bf.readLine()) != null){
            builder.append(line);
        }
        JsonElement rootElement = new JsonParser().parse(builder.toString());
        JsonArray elements = (JsonArray)  rootElement;
        return elements;
    }

}
