package sur.snapps.jetta.database.script;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.easymock.annotation.Mock;
import org.unitils.easymock.util.Order;
import sur.snapps.jetta.database.script.ScriptRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.easymock.EasyMock.expect;
import static org.unitils.easymock.EasyMockUnitils.replay;

/**
 * User: SUR
 * Date: 25/05/14
 * Time: 22:00
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ScriptRunnerTest {

    @Mock
    private Connection connection;
    @Mock(order = Order.STRICT)
    private Statement statement;

    @Test
    public void testExecuteScriptWithOneStatement() throws SQLException {
        expect(connection.createStatement()).andReturn(statement);
        statement.addBatch("delete from users");
        expect(statement.executeBatch()).andReturn(new int[0]);
        connection.commit();
        replay();

        ScriptRunner.executeScript(connection, "scripts/testExecuteScriptWithOneStatement.sql");
    }

    @Test
    public void testExecuteScriptWithMultipleStatements() throws SQLException {
        expect(connection.createStatement()).andReturn(statement);
        statement.addBatch("delete from users");
        statement.addBatch("delete from entities");
        statement.addBatch("delete from tokens");
        expect(statement.executeBatch()).andReturn(new int[0]);
        connection.commit();
        replay();

        ScriptRunner.executeScript(connection, "scripts/testExecuteScriptWithMultipleStatements.sql");
    }

    @Test
    public void testExecuteScriptWithOneMultiLineStatement() throws SQLException {
        expect(connection.createStatement()).andReturn(statement);
        statement.addBatch("delete from users where id = 'test' and name like 'this'");
        expect(statement.executeBatch()).andReturn(new int[0]);
        connection.commit();
        replay();

        ScriptRunner.executeScript(connection, "scripts/testExecuteScriptWithOneMultiLineStatement.sql");
    }

    @Test
    public void testExecuteScriptWithMultipleMultiLineStatements() throws SQLException {
        expect(connection.createStatement()).andReturn(statement);
        statement.addBatch("delete from users where id = 'test' and name like 'this'");
        statement.addBatch("delete from entities where user_id in ( select id from users )");
        statement.addBatch("delete from tokens where id = 'this'");
        expect(statement.executeBatch()).andReturn(new int[0]);
        connection.commit();
        replay();

        ScriptRunner.executeScript(connection, "scripts/testExecuteScriptWithMultipleMultiLineStatements.sql");
    }
}
