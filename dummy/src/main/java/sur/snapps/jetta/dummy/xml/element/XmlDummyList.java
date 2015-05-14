package sur.snapps.jetta.dummy.xml.element;

import java.util.List;

/**
 * @author sur
 * @since 22/02/2015
 */
public class XmlDummyList extends XmlDummyElement {

    private List<XmlDummyElement> xmlDummies;

    public XmlDummyList(String identifier, List<XmlDummyElement> xmlDummies) {
        super(Type.LIST, identifier);
        this.xmlDummies = xmlDummies;
    }

    public List<XmlDummyElement> getValues() {
        return xmlDummies;
    }
}
