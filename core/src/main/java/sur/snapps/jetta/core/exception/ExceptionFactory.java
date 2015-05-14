package sur.snapps.jetta.core.exception;

import static java.util.Arrays.asList;

/**
 * @author sur
 * @since 06/03/2015
 */
public class ExceptionFactory {

    private ExceptionFactory() {}

    public static JettaException create(ErrorType type, String... params) {
        return new JettaException(type, asList(params));
    }
}
