package re.duriez.demo.dktapi.stores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class Store {

    public String key;

    public Amount amount;
    
}
