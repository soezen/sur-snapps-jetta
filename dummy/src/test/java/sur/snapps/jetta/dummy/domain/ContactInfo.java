package sur.snapps.jetta.dummy.domain;

/**
 * @author sur
 * @since 22/02/2015
 */
public class ContactInfo {

    private String id;
    private Type type;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    enum Type {
        EMAIL,
        PHONE
    }
}
