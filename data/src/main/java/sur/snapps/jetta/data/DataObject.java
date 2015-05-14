package sur.snapps.jetta.data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author sur
 * @since 28/02/2015
 */
@Target({FIELD, METHOD, TYPE})
@Retention(RUNTIME)
public @interface DataObject {

    Class<?> type()  default DataObject.class;
    String identifier() default "";
    String value();
}
