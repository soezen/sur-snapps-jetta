package sur.snapps.jetta.dummy.xml.element;

import sur.snapps.jetta.dummy.xml.XmlDummy;

/**
 * @author sur
 * @since 22/02/2015
 */
public class XmlDummyComplexValue extends XmlDummyElement {

    private XmlDummy value;

    public XmlDummyComplexValue(String identifier, XmlDummy value) {
        super(Type.COMPLEX, identifier);
        this.value = value;
    }

    public XmlDummy getValue() {
        return value;
    }
}
