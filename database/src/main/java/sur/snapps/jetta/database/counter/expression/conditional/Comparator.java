package sur.snapps.jetta.database.counter.expression.conditional;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:22
 */
public enum Comparator {

    LIKE(" LIKE "),
    EQUAL(" = "),
    IS(" IS ");

    private String sql;

    private Comparator(String sql) {
        this.sql = sql;
    }

    public String get() {
        return sql;
    }
}
