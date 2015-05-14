package sur.snapps.jetta.data.xml.element;

import java.util.List;

/**
 * @author sur
 * @since 22/02/2015
 */
public class XmlList extends XmlElement {

    private List<XmlElement> xmlDummies;

    public XmlList(String identifier, List<XmlElement> xmlDummies) {
        super(Type.LIST, identifier);
        this.xmlDummies = xmlDummies;
    }

    public List<XmlElement> getValues() {
        return xmlDummies;
    }
}
