package sur.snapps.jetta.database.dummy;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import org.unitils.util.ReflectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sur.snapps.jetta.database.dummy.expression.DateExpression;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;


public class DummyExtractor {

    private String xmlFileLocation;
    
    public DummyExtractor(String xmlFileLocation) {
        this.xmlFileLocation = xmlFileLocation;
    }

    public <D> D doAction(D dummy, String action) {
        String identifier = getDummyIdentifier(dummy.getClass());
        String identifierValue = ReflectionUtils.getFieldValue(dummy, ReflectionUtils.getFieldWithName(dummy.getClass(), identifier, false));

        String xpath = xpathToSelectDummy(dummy.getClass(), identifierValue);
        Node dummyNode = getSingleNode(getDocument(), xpath);
        if (dummyNode != null) {
            return (D) initializeDummyWithAction(dummy.getClass(), dummyNode, action);
        }
        throw new IllegalArgumentException("invalid dummy locator, unable to find dummy in file (xpath = " + xpath + ")");
    }
    
    public <D> D get(Class<D> dummyClass, String identifierValue) {
        checkValidDummyClass(dummyClass);

        String xpath = xpathToSelectDummy(dummyClass, identifierValue);
        Node dummyNode = getSingleNode(getDocument(), xpath);
        if (dummyNode != null) {
            return initializeDummy(dummyClass, dummyNode);
        } 
        throw new IllegalArgumentException("invalid dummy locator, unable to find dummy in file (xpath = " + xpath + ")");
    }

    private <D> D initializeDummy(Class<D> dummyClass, Node dummyNode) {
        return initializeDummyWithAction(dummyClass, dummyNode, null);
    }
    
    private <D> D initializeDummyWithAction(Class<D> dummyClass, Node dummyNode, String action) {
        D dummy = ReflectionUtils.createInstanceOfType(dummyClass, false);
        
        Map<String, Node> childNodes = getChildNodes(dummyNode, action);

        for (Field field : ReflectionUtils.getAllFields(dummyClass)) {
            String fieldName = field.getName();

            if (childNodes.containsKey(fieldName)) {
                String nodeValue = getNodeValue(childNodes, fieldName);
                Object value = determineValue(field.getType(), nodeValue);
                ReflectionUtils.setFieldValue(dummy, field, value);
            }
        }
        
        return dummy;
    }

    private Node findAction(Node actionsNode, String action) {
        NodeList childNodes = actionsNode.getChildNodes();
        for (int index = 0; index < childNodes.getLength(); index++) {
            Node actionNode = childNodes.item(index);
            if (actionNode.getNodeType() == Node.ELEMENT_NODE
                && actionNode.getAttributes().getNamedItem("id").getTextContent().equals(action)) {
                return actionNode;
            }
        }
        return null;
    }

    private Object determineValue(Type type, String stringValue) {
        if (type.equals(boolean.class)) {
            return Boolean.parseBoolean(stringValue);
        } else if (type.equals(int.class)) {
            return Integer.valueOf(stringValue);
        } else if (type.equals(Date.class)) {
            return new DateExpression(stringValue).parse();
        } else if (type.equals(String.class)) {
            return stringValue;
        }
        return null;
    }

    private String getNodeValue(Map<String, Node> childNodes, String fieldName) {
        Node node = childNodes.get(fieldName);
        if (node.hasAttributes()) {
            Node translationAttribute = node.getAttributes().getNamedItem("translation");
            if (translationAttribute != null) {
                return translationAttribute.getTextContent();
            }
        }
        return node.getTextContent();
    }
    
    private Map<String, Node> getChildNodes(Node node, String action) {
        Map<String, Node> childNodes = Maps.newHashMap();

        putNodesInMap(childNodes, node.getChildNodes());

        if (action != null) {
            Node actionNode = findAction(childNodes.get("actions"), action);
            putNodesInMap(childNodes, actionNode.getChildNodes());
        }

        return childNodes;
    }

    private void putNodesInMap(Map<String, Node> mappedNodes, NodeList nodes) {
        for (int index = 0; index < nodes.getLength(); index++) {
            Node childNode = nodes.item(index);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, childNode.getNodeName());
                mappedNodes.put(nodeName, childNode);
            }
        }
    }

    private String xpathToSelectDummy(Class<?> dummyClass, String identifierValue) {
        String identifier = getDummyIdentifier(dummyClass);
        Dummy dummy = dummyClass.getAnnotation(Dummy.class);

        return "//dummies/" + dummy.value() + "[" + identifier + "[text()='" + identifierValue + "']]";
    }

    private String getDummyIdentifier(Class<?> dummyClass) {
        for (Field field : ReflectionUtils.getAllFields(dummyClass)) {
            if (field.isAnnotationPresent(Identifier.class)) {
                return field.getName();
            }
        }
        throw new IllegalStateException("invalid dummy class, missing @Identifier on one of fields");
    }
    
    // TODO allow to have multiple identifiers
    private Node getSingleNode(Document doc, String expression) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        try {
            XPathExpression xpathExpression = xpath.compile(expression);
            return (Node) xpathExpression.evaluate(doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("invalid dummy locator, unable to find dummy in file (xpath = " + expression);
        }
    }
    
    private Document getDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(xmlFileLocation);
            if (inputStream == null) {
                throw new IllegalArgumentException("error locating xml file: " + xmlFileLocation);
            }

            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new IllegalArgumentException("error loading xml file: " + xmlFileLocation);
        }
    }
    
    private void checkValidDummyClass(Class<?> dummyClass) {
        if (!dummyClass.isAnnotationPresent(Dummy.class)) {
            throw new IllegalArgumentException("invalid dummy class (no @Dummy annotation present): " + dummyClass);
        }
    }
}
