package re.duriez.demo.dktapi.es;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RequestService {

    public String search(String index, String templateId, ObjectNode params) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:9200/" + index + "/_search/template");
        String body = this.buildJson(templateId, params);
        log.info("Sending search query : " + body);
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

        CloseableHttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    private String buildJson(String templateId, ObjectNode params) throws JsonProcessingException {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.set("params", params);
        node.put("id", templateId);
        return new ObjectMapper().writeValueAsString(node);
    }
}
