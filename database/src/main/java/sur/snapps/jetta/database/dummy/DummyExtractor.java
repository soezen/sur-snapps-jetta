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
import java.util.Date;
import java.util.Map;


public class DummyExtractor {

    private String xmlFileLocation;
    
    public DummyExtractor(String xmlFileLocation) {
        this.xmlFileLocation = xmlFileLocation;
    }
    
    public <D> D get(Class<D> dummyClass, String[] identifier) {
        checkValidDummyClass(dummyClass);
        
        Document doc = getDocument();
        doc.getDocumentElement().normalize();

        String xpath = getXpathExpression(dummyClass.getAnnotation(Dummy.class), identifier);
        Node dummyNode = getSingleNode(doc, xpath);
        if (dummyNode != null) {
            return initializeDummy(dummyClass, dummyNode);
        } 
        throw new IllegalArgumentException("invalid dummy locator, unable to find dummy in file (xpath = " + xpath);
    }
    
    private <D> D initializeDummy(Class<D> dummyClass, Node dummyNode) {
        D dummy = ReflectionUtils.createInstanceOfType(dummyClass, false);
        
        Map<String, Node> childNodes = getChildNodes(dummyNode);
        
        for (Field field : ReflectionUtils.getAllFields(dummyClass)) {
            String fieldName = field.getName();
            if (childNodes.containsKey(fieldName)) {
                String nodeValue = getNodeValue(childNodes, fieldName);
                if (field.getType().equals(boolean.class)) {
                    ReflectionUtils.setFieldValue(dummy, field, Boolean.parseBoolean(nodeValue));
                } else if (field.getType().equals(int.class)) {
                    ReflectionUtils.setFieldValue(dummy, field, Integer.valueOf(nodeValue));
                } else if (field.getType().equals(Date.class)) {
                    Date dateValue = new DateExpression(nodeValue).parse();
                    ReflectionUtils.setFieldValue(dummy, field, dateValue);
                } else {
                    ReflectionUtils.setFieldValue(dummy, field, nodeValue);
                }
            }
        }
        
        return dummy;
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
    
    private Map<String, Node> getChildNodes(Node node) {
        Map<String, Node> childNodes = Maps.newHashMap();
        
        NodeList nodeList = node.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node childNode = nodeList.item(index);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, childNode.getNodeName());
                childNodes.put(nodeName, childNode);
            }
        }
        
        return childNodes;
    }
    
    private String getXpathExpression(Dummy dummy, String[] identifier) {
        if (identifier.length != 2) {
            throw new IllegalArgumentException("invalid identifier, requires key and value: " + identifier);
        }
        
        return "//dummies/" + dummy.value() + "[" + identifier[0] + "[text()='" + identifier[1] + "']]";
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
            return builder.parse(inputStream);
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException("error loading xml file: " + xmlFileLocation);
        } catch (IOException e) {
            throw new IllegalArgumentException("error loading xml file: " + xmlFileLocation);
        } catch (SAXException e) {
            throw new IllegalArgumentException("error loading xml file: " + xmlFileLocation);
        }
    }
    
    private void checkValidDummyClass(Class<?> dummyClass) {
        if (!dummyClass.isAnnotationPresent(Dummy.class)) {
            throw new IllegalArgumentException("invalid dummy class (no @Dummy annotation present): " + dummyClass);
        }
    }
}
