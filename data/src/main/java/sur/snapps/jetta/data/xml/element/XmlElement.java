package sur.snapps.jetta.data.xml.element;

/**
 * @author sur
 * @since 22/02/2015
 */
public abstract class XmlElement {

    private Type type;
    private String identifier;

    public XmlElement(Type type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isValue() {
        return Type.VALUE.equals(type);
    }

    public boolean isList() {
        return Type.LIST.equals(type);
    }

    public boolean isReference() {
        return Type.REFERENCE.equals(type);
    }

    public boolean isComplexType() {
        return Type.COMPLEX.equals(type);
    }

    enum Type {
        VALUE,
        LIST,
        COMPLEX,
        REFERENCE
    }
}
