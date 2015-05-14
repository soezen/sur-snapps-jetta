package sur.snapps.jetta.dummy.xml;

import org.junit.Test;
import sur.snapps.jetta.dummy.xml.element.XmlDummyComplexValue;
import sur.snapps.jetta.dummy.xml.element.XmlDummyElement;
import sur.snapps.jetta.dummy.xml.element.XmlDummyList;
import sur.snapps.jetta.dummy.xml.element.XmlDummyReference;
import sur.snapps.jetta.dummy.xml.element.XmlDummyValue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static sur.snapps.jetta.dummy.xml.XmlDummyDocument.getSingleNode;

public class XmlDummyTest {

    @Test
    public void getValue() {
        XmlDummy xmlDummy = new XmlDummy(getSingleNode("//test/objects/object_with_value"));
        XmlDummyElement xmlDummyElement = xmlDummy.get("test_identifier");
        assertTrue(xmlDummyElement instanceof XmlDummyValue);

        XmlDummyValue xmlDummyValue = (XmlDummyValue) xmlDummyElement;
        assertEquals("test_identifier", xmlDummyElement.getIdentifier());
        assertEquals("test_value", xmlDummyValue.getValue());
    }

    @Test
    public void getReference() {
        XmlDummy xmlDummy = new XmlDummy(getSingleNode("//test/objects/object_with_reference"));
        XmlDummyElement xmlDummyElement = xmlDummy.get("test_identifier");
        assertTrue(xmlDummyElement instanceof XmlDummyReference);

        XmlDummyReference xmlDummyReference = (XmlDummyReference) xmlDummyElement;
        assertEquals("test_identifier", xmlDummyReference.getIdentifier());
        assertEquals("references", xmlDummyReference.getReferenceIdentifier());
        assertEquals("reference_id", xmlDummyReference.getReferenceIdentifierValue());
    }

    @Test
    public void getComplexValue() {
        XmlDummy xmlDummy = new XmlDummy(getSingleNode("//test/objects/object_with_complex_value"));
        XmlDummyElement xmlDummyElement = xmlDummy.get("complex_type");
        assertTrue(xmlDummyElement instanceof XmlDummyComplexValue);

        XmlDummyComplexValue xmlDummyComplexValue = (XmlDummyComplexValue) xmlDummyElement;
        assertEquals("complex_type", xmlDummyComplexValue.getIdentifier());
        assertNotNull(xmlDummyComplexValue.getValue());
        assertEquals("value_one", ((XmlDummyValue) xmlDummyComplexValue.getValue().get("property_one")).getValue());
    }

    @Test
    public void getListWithSimpleValues() {
        XmlDummy xmlDummy = new XmlDummy(getSingleNode("//test/objects/object_with_simple_list"));
        XmlDummyElement xmlDummyElement = xmlDummy.get("values");
        assertTrue(xmlDummyElement instanceof XmlDummyList);

        XmlDummyList xmlDummyList = (XmlDummyList) xmlDummyElement;
        assertEquals("values", xmlDummyList.getIdentifier());
        assertEquals(3, xmlDummyList.getValues().size());
        assertEquals("value", ((XmlDummyValue) xmlDummyList.getValues().get(0)).getValue());
    }

    @Test
    public void getListWithReferences() {
        XmlDummy xmlDummy = new XmlDummy(getSingleNode("//test/objects/object_with_list_of_references"));
        XmlDummyElement xmlDummyElement = xmlDummy.get("references");
        assertTrue(xmlDummyElement instanceof XmlDummyList);

        XmlDummyList xmlDummyList = (XmlDummyList) xmlDummyElement;
        assertEquals("references", xmlDummyList.getIdentifier());
        assertEquals(2, xmlDummyList.getValues().size());
        assertEquals("references", ((XmlDummyReference) xmlDummyList.getValues().get(0)).getReferenceIdentifier());
        assertEquals("reference_id", ((XmlDummyReference) xmlDummyList.getValues().get(0)).getReferenceIdentifierValue());
    }

    @Test
    public void getListWithComplexValues() {
        XmlDummy xmlDummy = new XmlDummy(getSingleNode("//test/objects/object_with_list_of_complex_values"));
        XmlDummyElement xmlDummyElement = xmlDummy.get("complex_values");
        assertTrue(xmlDummyElement instanceof XmlDummyList);

        XmlDummyList xmlDummyList = (XmlDummyList) xmlDummyElement;
        assertEquals("complex_values", xmlDummyList.getIdentifier());
        assertEquals(2, xmlDummyList.getValues().size());
        assertEquals("value_one", ((XmlDummyValue) ((XmlDummyComplexValue) xmlDummyList.getValues().get(0)).getValue().get("property_one")).getValue());
    }
}