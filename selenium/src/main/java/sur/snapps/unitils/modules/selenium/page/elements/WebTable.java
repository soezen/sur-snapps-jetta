package sur.snapps.unitils.modules.selenium.page.elements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: SUR
 * Date: 26/05/14
 * Time: 16:57
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebTable {
    String id();

    Column[] columns();
}
