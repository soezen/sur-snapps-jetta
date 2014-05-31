package sur.snapps.jetta.core.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 9:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JettaConfiguration {

    // TODO create annotation to specify constraints between jetta-properties?

    String value();
}
