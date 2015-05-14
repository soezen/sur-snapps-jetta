package sur.snapps.jetta.dummy.xml.element;

/**
 * @author sur
 * @since 22/02/2015
 */
public class XmlDummyReference extends XmlDummyElement {

    private String referenceIdentifier;
    private String referenceIdentifierValue;

    public XmlDummyReference(String identifier, String referenceIdentifier, String referenceIdentifierValue) {
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
