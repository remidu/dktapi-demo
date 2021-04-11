package re.duriez.demo.dktapi.stores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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
public class StoresController {

    private final RequestService requestService;

    @GetMapping("/stores")
    public List<Store> getStores(@Valid Parameters parameters) throws JsonMappingException, JsonProcessingException, IOException {
        List<Store> stores = new ArrayList<>();
        String result = requestService.search("sales", "stores.json", parameters.buildEsParams());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode buckets = mapper.readTree(result).findValue("aggregations").findValue("sorted-stores").findValue("buckets");
        for (JsonNode bucket : buckets) {
            stores.add(mapper.readValue(bucket.toString(), Store.class));
        }
        return stores;
    }
}
