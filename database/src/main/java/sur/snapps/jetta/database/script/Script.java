package sur.snapps.jetta.database.script;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: SUR
 * Date: 25/05/14
 * Time: 15:44
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Script {
    String value();

    ScriptStrategy strategy() default ScriptStrategy.CLEAN_FIRST;
}
