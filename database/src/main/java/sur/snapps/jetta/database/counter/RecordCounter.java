package sur.snapps.jetta.database.counter;

import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.clause.CountStatement;

import java.sql.Connection;

public class RecordCounter {

    private Connection connection;
    private DatabaseDialect dialect;

    public RecordCounter(Connection connection, DatabaseDialect dialect) {
        this.connection = connection;
        this.dialect = dialect;
    }

    public CountStatement count() {
        return new CountStatement(connection, dialect);
    }
}
