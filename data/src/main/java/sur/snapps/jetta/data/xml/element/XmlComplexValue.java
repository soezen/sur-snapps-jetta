package sur.snapps.jetta.data.xml.element;

import sur.snapps.jetta.data.xml.XmlData;

/**
 * @author sur
 * @since 22/02/2015
 */
public class XmlComplexValue extends XmlElement {

    private XmlData value;

    public XmlComplexValue(String identifier, XmlData value) {
        super(Type.COMPLEX, identifier);
        this.value = value;
    }

    public XmlData getValue() {
        return value;
    }
}
