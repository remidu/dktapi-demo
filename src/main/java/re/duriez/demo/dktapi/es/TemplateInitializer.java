package re.duriez.demo.dktapi.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class TemplateInitializer {

    private final static String PATH = "es/templates";
    
    @PostConstruct
    public void init() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(PATH);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String templateFilename;
        while ((templateFilename = br.readLine()) != null) { // for each file in directory
            this.uploadTemplate(templateFilename);
        }
    }

    private void uploadTemplate(String templateName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
            this.getClass().getClassLoader().getResourceAsStream(PATH + "/" + templateName)));
        String jsonRequest = br.lines().collect(Collectors.joining());

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:9200/_scripts/" + templateName);
        request.setEntity(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON));
        try {
            CloseableHttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() < 400) {
                log.info("Uploaded template {}", templateName);
            } else {
                log.error("Failed to upload template {} : {}", templateName, response.getStatusLine().getReasonPhrase());
            }            
        } catch (ConnectException e) {
            log.error("Could not connect to Elasticsearch, is it running ?", e);
        }
    }
}
