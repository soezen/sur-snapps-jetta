package sur.snapps.jetta.core.exception;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author sur
 * @since 06/03/2015
 */
public class JettaException extends RuntimeException {

    private final ErrorType type;
    private final ImmutableList<String> params;

    public JettaException(ErrorType type, List<String> params) {
        this.type = type;
        this.params = ImmutableList.copyOf(params);
    }

    public JettaException(ErrorType type, Exception cause) {
        super(cause);
        this.type = type;
        this.params = ImmutableList.of();
    }
}
