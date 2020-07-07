package re.duriez.demo.dktapi.sales;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class Parameters {

    public String country;

    public String store;

    public String minimum;

    public ObjectNode buildEsParams() {
        try {
            ObjectNode node = JsonNodeFactory.instance.objectNode();
            for (Field f : this.getClass().getFields()) {
                String name = f.getName();
                String value = (String) new PropertyDescriptor(name, this.getClass()).getReadMethod().invoke(this);
                if (value != null) {
                    node.put(name, value);
                }
            }
            log.info("Parameters as node : " + node);
            return node;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}
