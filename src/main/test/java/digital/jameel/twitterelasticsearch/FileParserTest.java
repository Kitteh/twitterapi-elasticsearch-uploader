package digital.jameel.twitterelasticsearch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FileParserTest {
    private static final String filePath = "twitterapi-stream-parsed.json";
    private static FileParser fileParser;

    @BeforeAll
    public static void setup() throws IOException, URISyntaxException {
        URL url = FileParserTest.class.getClassLoader().getResource(filePath);
        URI uri = url.toURI();
        FileInputStream file = new FileInputStream(url.getFile());
        fileParser = new FileParser(file);
    }

    @Test
    @Order(1)
    public void parseFileTest() throws IOException{
        try {
            fileParser.parseFile(); // it runs without error
        } catch (Exception e){
            throw e;
        }
    }

    @Test
    @Order(2)
    public void uploadDocumentsTest(){
        fileParser.uploadDocuments();
    }
}
