package sur.snapps.jetta.data.xml.element;

/**
 * @author sur
 * @since 22/02/2015
 */
public class XmlReference extends XmlElement {

    private String referenceIdentifier;
    private String referenceIdentifierValue;

    public XmlReference(String identifier, String referenceIdentifier, String referenceIdentifierValue) {
        super(Type.REFERENCE, identifier);
        this.referenceIdentifier = referenceIdentifier;
        this.referenceIdentifierValue = referenceIdentifierValue;
    }

    public String getReferenceIdentifier() {
        return referenceIdentifier;
    }

    public String getReferenceIdentifierValue() {
        return referenceIdentifierValue;
    }

}
