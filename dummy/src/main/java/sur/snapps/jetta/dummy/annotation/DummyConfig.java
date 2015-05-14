package sur.snapps.jetta.dummy.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sur
 * @since 20/02/2015
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DummyConfig {

    String path();
    Class<?> type();
    String identifier() default "id";
}
