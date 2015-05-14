package sur.snapps.jetta.data.xml.element;

import sur.snapps.jetta.core.el.expression.DateExpression;

import java.util.Date;

/**
 * @author sur
 * @since 22/02/2015
 */
public class XmlValue extends XmlElement {

    private String value;

    public XmlValue(String identifier, String value) {
        super(Type.VALUE, identifier);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public <C> C getValue(Class<C> clazz) {
        if (clazz.equals(String.class)) {
            return (C) value;
        }
        if (clazz.isEnum()) {
            Class<Enum> enumClass = (Class<Enum>) clazz;
            return (C) Enum.valueOf(enumClass, value);
        }
        if (clazz.equals(Date.class)) {
            return (C) new DateExpression(value).parse();
        }
        if (clazz.equals(Integer.class)
            || clazz.equals(int.class)) {
            return (C) Integer.valueOf(value);
        }
        if (clazz.equals(Boolean.class)
            || clazz.equals(boolean.class)) {
            return (C) Boolean.valueOf(value);
        }
        return null;
    }

    // TODO allow user to plugin custom converters

}
