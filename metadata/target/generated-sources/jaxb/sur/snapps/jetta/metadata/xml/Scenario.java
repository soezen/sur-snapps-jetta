
package sur.snapps.jetta.metadata.xml;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for scenario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scenario">
 *   &lt;complexContent>
 *     &lt;extension base="{}itemWithDescription">
 *       &lt;sequence>
 *         &lt;element name="type" type="{}scenarioType"/>
 *         &lt;element name="failureImpact" type="{}failureImpact" minOccurs="0"/>
 *         &lt;element name="executionTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="properties">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="properties" type="{}property" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="failureCause" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="status" type="{}status" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scenario", propOrder = {
    "type",
    "failureImpact",
    "executionTime",
    "duration",
    "properties",
    "failureCause"
})
public class Scenario
    extends ItemWithDescription
{

    @XmlElement(required = true)
    protected ScenarioType type;
    @XmlElement(defaultValue = "MEDIUM")
    protected FailureImpact failureImpact;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar executionTime;
    protected Long duration;
    protected String failureCause;
    @XmlAttribute(name = "status")
    protected Status status;
    @XmlElementWrapper(name = "properties", required = true)
    @XmlElement(name = "properties")
    protected List<Property> properties;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ScenarioType }
     *     
     */
    public ScenarioType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScenarioType }
     *     
     */
    public void setType(ScenarioType value) {
        this.type = value;
    }

    /**
     * Gets the value of the failureImpact property.
     * 
     * @return
     *     possible object is
     *     {@link FailureImpact }
     *     
     */
    public FailureImpact getFailureImpact() {
        return failureImpact;
    }

    /**
     * Sets the value of the failureImpact property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailureImpact }
     *     
     */
    public void setFailureImpact(FailureImpact value) {
        this.failureImpact = value;
    }

    /**
     * Gets the value of the executionTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getExecutionTime() {
        return executionTime;
    }

    /**
     * Sets the value of the executionTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExecutionTime(Calendar value) {
        this.executionTime = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDuration(Long value) {
        this.duration = value;
    }

    /**
     * Gets the value of the failureCause property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureCause() {
        return failureCause;
    }

    /**
     * Sets the value of the failureCause property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureCause(String value) {
        this.failureCause = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Property>();
        }
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

}
