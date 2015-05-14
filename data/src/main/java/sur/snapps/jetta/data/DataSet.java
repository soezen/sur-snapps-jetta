package sur.snapps.jetta.data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author sur
 * @since 28/02/2015
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface DataSet {

    Class<?> type()  default DataSet.class;

    /**
     * attribute or field in xml file to determine which elements from the table to select
     * to include in the dataset.
     */
    String selector() default "";
    String[] value() default "";
}
