package re.duriez.demo.dktapi.sales;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import re.duriez.demo.dktapi.es.RequestService;

@RestController
@RequiredArgsConstructor
public class SalesController {

    private final RequestService requestService;

    @GetMapping("/sales")
    public String getSales(Parameters parameters) {
        return requestService.search("sales", "search", parameters.buildEsParams());
    }

    @GetMapping("/models")
    public String sortModels() {
        return null; // TODO
    }

    @GetMapping("/stores")
    public String sortStores() {
        return null; // TODO
    }
}
