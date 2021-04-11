package re.duriez.demo.dktapi.sales;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import re.duriez.demo.dktapi.es.RequestService;

@RestController
@RequiredArgsConstructor
public class SalesController {

    private final RequestService requestService;

    @GetMapping("/sales")
    public List<Sale> getSales(Parameters parameters) throws JsonMappingException, JsonProcessingException, IOException {
        List<Sale> sales = new ArrayList<>();
        String result = requestService.search("sales", "sales.json", parameters.buildEsParams());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode hits = mapper.readTree(result).findValue("hits").findValue("hits");
        for (JsonNode hit : hits) {
            JsonNode source = mapper.readTree(hit.toString()).findValue("_source");
            sales.add(mapper.readValue(source.toString(), Sale.class));
        }
        return sales;
    }

    @GetMapping("/stores")
    public String sortStores() {
        return null; // TODO
    }
}
