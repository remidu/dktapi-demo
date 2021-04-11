package re.duriez.demo.dktapi.sales;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parameters {

    public String country;

    public String store;

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
            return node;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}
