package sur.snapps.jetta.modules.selenium.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: SUR
 * Date: 26/05/14
 * Time: 18:10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    int index();
    String name();
    boolean optional() default false;
}
