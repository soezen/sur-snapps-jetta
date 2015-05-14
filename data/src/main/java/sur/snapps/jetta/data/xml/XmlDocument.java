package sur.snapps.jetta.data.xml;

import com.google.common.collect.Lists;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sur.snapps.jetta.core.config.JettaConfigurations;
import sur.snapps.jetta.data.module.DataConfiguration;

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
import java.util.List;

/**
 * Created by sur on 19/02/2015.
 */
public class XmlDocument {

    private static List<Document> documents = Lists.newArrayList();

    static {
        DataConfiguration config = JettaConfigurations.get(DataConfiguration.class);
        String[] fileNames = config.dummiesFile().split(",");
        for (String fileName : fileNames) {
            loadDocument(config, fileName.trim());
        }
    }

    private static void loadDocument(DataConfiguration config, String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("error locating xml file: " + config.dummiesFile());
            }

            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
            documents.add(document);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new IllegalArgumentException("error loading xml file: " + config.dummiesFile());
        }
    }

    public static List<Node> getNodes(String expression) {
        List<NodeList> nodeLists = getByXPath(expression, XPathConstants.NODESET);
        List<Node> nodes = Lists.newArrayList();
        for (NodeList nodeList : nodeLists) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                nodes.add(nodeList.item(i));
            }
        }
        return nodes;
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> getByXPath(String expression, QName qname) {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        List<T> items = Lists.newArrayList();
        for (Document document : documents) {
            try {
                XPathExpression xpathExpression = xpath.compile(expression);
                items.add((T) xpathExpression.evaluate(document, qname));
            } catch (XPathExpressionException e) {
                throw new IllegalArgumentException("invalid dummy path, unable to find dummy by xpath " + expression);
            }
        }
        return items;
    }

}
