
package sur.snapps.jetta.metadata.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for scenarioType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="scenarioType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUCCESS"/>
 *     &lt;enumeration value="EXCEPTION"/>
 *     &lt;enumeration value="BUGFIX"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "scenarioType")
@XmlEnum
public enum ScenarioType {

    SUCCESS,
    EXCEPTION,
    BUGFIX;

    public String value() {
        return name();
    }

    public static ScenarioType fromValue(String v) {
        return valueOf(v);
    }

}
