package sur.snapps.jetta.metadata.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test is no more applicable to the current application.
 *
 * User: SUR
 * Date: 23/08/2014
 * Time: 18:22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Deprecate {

}
