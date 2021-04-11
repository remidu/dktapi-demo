package re.duriez.demo.dktapi.stores;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parameters {

    @NotNull
    public String country;

    public String order = "desc";

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
