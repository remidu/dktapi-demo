package re.duriez.demo.dktapi.sales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class Sale {

    public String country;

    public String store;

    public String item;

    public Double price;
}
