package sur.snapps.jetta.database.counter.expression;

import sur.snapps.jetta.database.DatabaseDialect;

/**
 * User: SUR
 * Date: 15/08/14
 * Time: 18:07
 */
public class Functions {

    public static String dateColumnToString(DatabaseDialect dialect, String columnName) {
        switch (dialect) {
            case MYSQL:
                return "date_format(" + columnName + ", '%d-%m-%Y')";
            case ORACLE:
                return "to_char(" + columnName + ", 'dd-MM-yyyy')";
            default:
                throw new IllegalArgumentException("unknown database dialect: " + dialect);
        }
    }

}
