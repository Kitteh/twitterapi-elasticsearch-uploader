package digital.jameel.twitterelasticsearch;

import com.amazonaws.*;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.*;
import com.amazonaws.util.IOUtils;
import org.apache.http.HttpStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Elasticsearch {

    private String endpoint;
    private final AWSCredentials credentials = new DefaultAWSCredentialsProviderChain().getCredentials();


    public Elasticsearch(){
        this.endpoint = System.getenv("ELASTICSEARCH_URL");
    }

    public Elasticsearch(String endpoint){
        this.endpoint = endpoint;
    }

    public int putDocument(String index, String jsonPayload) {
        String urlString = "https://" + this.endpoint + "/" + index + "/_doc";
        try {
            URL url = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(jsonPayload.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            return responseCode;
        } catch (MalformedURLException e){
            // do something
            System.out.println(e);
        } catch (IOException e ){
            // do something
            System.out.println(e);
        }
        return HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }


}
