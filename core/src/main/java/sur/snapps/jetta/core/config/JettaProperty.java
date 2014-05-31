package sur.snapps.jetta.core.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: SUR
 * Date: 31/05/14
 * Time: 9:44
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JettaProperty {

    // TODO validation: can only be placed on Strings or booleans

    String property();

    boolean required() default false;

    String defaultValue() default "";
}
