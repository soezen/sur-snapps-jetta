package sur.snapps.jetta.dummy.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sur.snapps.jetta.core.config.JettaConfigurations;
import sur.snapps.jetta.dummy.module.DummyConfiguration;

import javax.xml.namespace.QName;
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

/**
 * Created by sur on 19/02/2015.
 */
public class XmlDummyDocument {

    private static Document document;

    static {
        DummyConfiguration config = JettaConfigurations.get(DummyConfiguration.class);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(config.dummiesFile());
            if (inputStream == null) {
                throw new IllegalArgumentException("error locating xml file: " + config.dummiesFile());
            }

            document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new IllegalArgumentException("error loading xml file: " + config.dummiesFile());
        }
    }

    public static Node getSingleNode(String expression) {
        return getByXPath(expression, XPathConstants.NODE);
    }

    public static NodeList getNodeList(String expression) {
        return getByXPath(expression, XPathConstants.NODESET);
    }

    private static <T> T getByXPath(String expression, QName qname) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        try {
            XPathExpression xpathExpression = xpath.compile(expression);
            return (T) xpathExpression.evaluate(document, qname);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("invalid dummy path, unable to find dummy by xpath " + expression);
        }
    }
}
