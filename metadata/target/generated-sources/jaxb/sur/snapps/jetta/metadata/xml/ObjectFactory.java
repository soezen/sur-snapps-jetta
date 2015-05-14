
package sur.snapps.jetta.metadata.xml;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sur.snapps.jetta.metadata.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sur.snapps.jetta.metadata.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Scenario }
     * 
     */
    public Scenario createScenario() {
        return new Scenario();
    }

    /**
     * Create an instance of {@link UseCase }
     * 
     */
    public UseCase createUseCase() {
        return new UseCase();
    }

    /**
     * Create an instance of {@link JettaMetaDataReport }
     * 
     */
    public JettaMetaDataReport createJettaMetaDataReport() {
        return new JettaMetaDataReport();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

}
