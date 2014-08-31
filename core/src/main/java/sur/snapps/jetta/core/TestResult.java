package sur.snapps.jetta.core;

/**
 * User: SUR
 * Date: 24/08/14
 * Time: 10:13
 */
public class TestResult {

    private Type type;
    private Throwable failureCause;
    private Long duration;

    private TestResult(Type type) {
        this.type = type;
    }

    public Long duration() {
        return duration;
    }

    public String failureCause() {
        return failureCause == null ? null : failureCause.getMessage();
    }

    public boolean testSkipped() {
        return type == Type.SKIPPED;
    }

    public boolean testSucceeded() {
        return type == Type.SUCCESS;
    }

    public boolean testFailed() {
        return type == Type.FAILURE;
    }

    public static TestResult successResult(long milliseconds) {
        TestResult testResult = new TestResult(Type.SUCCESS);
        testResult.duration = milliseconds;
        return testResult;
    }

    public static TestResult failureResult(Throwable throwable, long milliseconds) {
        TestResult result = new TestResult(Type.FAILURE);
        result.failureCause = throwable;
        result.duration = milliseconds;
        return result;
    }

    public static TestResult skippedResult() {
        return new TestResult(Type.SKIPPED);
    }

    private enum Type {
        SUCCESS,
        FAILURE,
        SKIPPED
    }
}
