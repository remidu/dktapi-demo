package re.duriez.demo.dktapi.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DataInitializer {
    
    private final static String PATH = "es/init";
    
    @PostConstruct
    public void init() {
        try {
            this.uploadTemplate("sales_template.json");
            this.createIndex("sales");
            this.insertData("bulk.json");
            log.info("Initialized index sales");
        } catch (IOException e) {
            log.error("Could not connect to Elasticsearch, is it running ?", e);
        }
    }

    private void uploadTemplate(String templateName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
            this.getClass().getClassLoader().getResourceAsStream(PATH + "/" + templateName)));
        String fileContent = br.lines().collect(Collectors.joining());

        HttpPost request = new HttpPost("http://localhost:9200/_index_template/" + templateName);
        request.setEntity(new StringEntity(fileContent, ContentType.APPLICATION_JSON));
        HttpClients.createDefault().execute(request);
    }

    private void createIndex(String name) throws IOException {
        HttpPut request = new HttpPut("http://localhost:9200/sales");
        HttpClients.createDefault().execute(request);
    }
    
    private void insertData(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
            this.getClass().getClassLoader().getResourceAsStream(PATH + "/" + filename)));
        String fileContent = br.lines().collect(Collectors.joining("\n", "", "\n"));

        HttpPost request = new HttpPost("http://localhost:9200/_bulk");
        request.setEntity(new StringEntity(fileContent, ContentType.APPLICATION_JSON));
        HttpClients.createDefault().execute(request);
    }
}
