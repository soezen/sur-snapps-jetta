package sur.snapps.jetta.database.counter.clause;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import sur.snapps.jetta.database.DatabaseDialect;
import sur.snapps.jetta.database.counter.expression.Expression;
import sur.snapps.jetta.database.counter.table.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.unitils.easymock.EasyMockUnitils.replay;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class CountStatementTest {

    @Mock
    private Table table;
    @Mock
    private Expression expression;
    @Mock
    private Connection connection;
    @Mock
    private Statement statement;
    @Mock
    private ResultSet resultSet;
    
    @Test
    public void testGetWithWhereClause() throws SQLException {
        expect(table.alias()).andReturn("a").times(2);
        expect(table.primaryKey()).andReturn("id");
        expect(table.nameWithJoins()).andReturn("name");
        expect(expression.expression(DatabaseDialect.MYSQL)).andReturn("expr");
        expect(connection.createStatement()).andReturn(statement);
        expect(statement.executeQuery("SELECT count(a.id) as count FROM name WHERE expr")).andReturn(resultSet);
        expect(resultSet.next()).andReturn(true);
        expect(resultSet.getInt("count")).andReturn(1);
        replay();
        
        CountStatement countStatement = new CountStatement(connection, DatabaseDialect.MYSQL);
        countStatement.from(table);
        countStatement.where(expression);
        int count = countStatement.get();

        assertEquals(1, count);
    }
    
    @Test
    public void testGetWithoutWhereClause() throws SQLException {
        expect(table.alias()).andReturn("a").times(2);
        expect(table.primaryKey()).andReturn("id");
        expect(table.nameWithJoins()).andReturn("name");
        expect(connection.createStatement()).andReturn(statement);
        expect(statement.executeQuery("SELECT count(a.id) as count FROM name")).andReturn(resultSet);
        expect(resultSet.next()).andReturn(true);
        expect(resultSet.getInt("count")).andReturn(1);
        replay();
        
        CountStatement countStatement = new CountStatement(connection, DatabaseDialect.MYSQL);
        countStatement.from(table);
        int count = countStatement.get();

        assertEquals(1, count);
    }
    
    @Test
    public void testGetWithoutAlias() throws SQLException {
        expect(expression.expression(DatabaseDialect.MYSQL)).andReturn("expr");
        expect(connection.createStatement()).andReturn(statement);
        expect(statement.executeQuery("SELECT count(*) as count FROM table WHERE expr")).andReturn(resultSet);
        expect(resultSet.next()).andReturn(true);
        expect(resultSet.getInt("count")).andReturn(1);
        replay();
        
        CountStatement countStatement = new CountStatement(connection, DatabaseDialect.MYSQL);
        countStatement.from("table");
        countStatement.where(expression);
        int count = countStatement.get();

        assertEquals(1, count);
    }
}
