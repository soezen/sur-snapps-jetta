package sur.snapps.jetta.dummy.xml;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sur.snapps.jetta.dummy.xml.element.XmlDummyComplexValue;
import sur.snapps.jetta.dummy.xml.element.XmlDummyElement;
import sur.snapps.jetta.dummy.xml.element.XmlDummyList;
import sur.snapps.jetta.dummy.xml.element.XmlDummyReference;
import sur.snapps.jetta.dummy.xml.element.XmlDummyValue;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Collection of values for one entity represented in the xml dummy file.
 *
 * Created by sur on 19/02/2015.
 */
public class XmlDummy {

    private Map<String, XmlDummyElement> values;

    public XmlDummy(Node node) {
        checkNotNull(node, "Cannot create XmlDummy with null node");
        initializeValues(node);
    }

    public XmlDummyElement get(String key) {
        return values.get(key);
    }

    private void initializeValues(Node node) {
        values = Maps.newHashMap();
        if (isReference(node)) {
            values.put(node.getNodeName(), createReference(node));
            return;
        }
        if (isValue(node)) {
            values.put(node.getNodeName(), createValue(node));
            return;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                XmlDummyElement element = createXmlDummyElement(childNode);
                values.put(element.getIdentifier(), createXmlDummyElement(childNode));
            }
        }
    }

    private XmlDummyElement createXmlDummyElement(Node node) {
        if (isReference(node)) {
            return createReference(node);
        }
        if (isList(node)) {
            return createList(node);
        }
        if (isValue(node)) {
            return createValue(node);
        }
        if (isComplexValue(node)) {
            return createComplex(node);
        }
        throw new IllegalArgumentException("Invalid xml document");
    }

    private XmlDummyElement createReference(Node node) {
        String identifier = node.getNodeName();
        Attr attribute = (Attr) node.getAttributes().item(0);
        String referenceIdentifier = attribute.getName();
        String referenceValue = attribute.getValue();
        return new XmlDummyReference(identifier, referenceIdentifier, referenceValue);
    }

    private XmlDummyElement createList(Node node) {
        List<XmlDummyElement> dummyChildList = Lists.newArrayList();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                dummyChildList.add(createXmlDummyElement(childNode));
            }
        }
        String identifier = node.getAttributes().item(0).getNodeValue();
        return new XmlDummyList(identifier, dummyChildList);
    }

    private XmlDummyElement createValue(Node node) {
        String identifier = node.getNodeName();
        String value = node.getChildNodes().item(0).getNodeValue();
        return new XmlDummyValue(identifier, value);
    }

    private XmlDummyElement createComplex(Node node) {
        String identifier = node.getNodeName();
        XmlDummy complexValue = new XmlDummy(node);
        return new XmlDummyComplexValue(identifier, complexValue);
    }

    private boolean isReference(Node node) {
        return node.getChildNodes().getLength() == 0
            && node.getAttributes().getLength() == 1;
    }

    private boolean isList(Node node) {
        return node.getNodeName().equals("list")
            && node.getAttributes().getLength() == 1
            && node.getAttributes().item(0).getNodeName().equals("of");
    }

    private boolean isValue(Node node) {
        return node.getChildNodes().getLength() == 1
            && node.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE;
    }

    private boolean isComplexValue(Node node) {
        return node.getChildNodes().getLength() > 0;
    }
}
