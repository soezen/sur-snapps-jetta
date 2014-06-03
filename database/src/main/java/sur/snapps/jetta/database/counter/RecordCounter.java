package sur.snapps.jetta.database.counter;

import sur.snapps.jetta.database.counter.clause.CountStatement;

import java.sql.Connection;

public class RecordCounter {

    private Connection connection;

    public RecordCounter(Connection connection) {
        this.connection = connection;
    }

    public CountStatement count() {
        return new CountStatement(connection);
    }
}
