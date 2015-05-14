package sur.snapps.jetta.database.counter.clause;

import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;
import sur.snapps.jetta.database.counter.table.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CountStatement {

    private Connection connection;
    private DatabaseDialect dialect;
    private SelectClause selectClause;
    private FromClause fromClause;
    private WhereClause whereClause;

    public CountStatement(Connection connection, DatabaseDialect dialect) {
        this.connection = connection;
        this.dialect = dialect;
    }

    public CountStatement from(String tableName) {
        Table table = new Table(tableName);
        return from(table);
    }
    
    public CountStatement from(Table table) {
        selectClause = new SelectClause(table);
        fromClause = new FromClause(table);
        return this;
    }
    
    public CountStatement where(Expression expression) {
        whereClause = new WhereClause(expression);
        return this;
    }
    
    public int get() {
        String sql = selectClause.get(dialect)
                .concat(fromClause.get(dialect))
                .concat((whereClause == null ? "" : whereClause.get(dialect)));

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("C");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("sql error: " + sql, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("invalid sql: " + sql);
    }
}
