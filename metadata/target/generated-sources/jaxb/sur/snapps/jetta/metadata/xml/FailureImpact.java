
package sur.snapps.jetta.metadata.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for failureImpact.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="failureImpact">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HIGH"/>
 *     &lt;enumeration value="MEDIUM"/>
 *     &lt;enumeration value="LOW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "failureImpact")
@XmlEnum
public enum FailureImpact {

    HIGH,
    MEDIUM,
    LOW;

    public String value() {
        return name();
    }

    public static FailureImpact fromValue(String v) {
        return valueOf(v);
    }

}
