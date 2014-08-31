package sur.snapps.jetta.metadata.annotations;

import sur.snapps.jetta.metadata.xml.FailureImpact;
import sur.snapps.jetta.metadata.xml.ScenarioType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: SUR
 * Date: 23/08/2014
 * Time: 18:22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scenario {

    ScenarioType type();

    FailureImpact failureImpact();

}
