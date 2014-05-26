package sur.snapps.unitils.modules.database;

import com.google.common.collect.Lists;
import com.google.common.io.LineReader;
import org.unitils.core.UnitilsException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * User: SUR
 * Date: 25/05/14
 * Time: 20:32
 */
public class ScriptRunner {

    // TODO transactions?
    public static void executeScript(Connection connection, String scriptLocation) {
        List<String> sqlStatements = getSqlStatements(scriptLocation);

        try {
            Statement statement = connection.createStatement();
            for (String sqlStatement : sqlStatements) {
                statement.addBatch(sqlStatement);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new UnitilsException(e);
        }
    }

    private static List<String> getSqlStatements(String scriptLocation) {
        List<String> statements = Lists.newLinkedList();

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(scriptLocation);
        if (inputStream == null) {
            throw new UnitilsException("script not found at location: " + scriptLocation);
        }
        InputStreamReader reader = new InputStreamReader(inputStream);
        LineReader lineReader = new LineReader(reader);
        StringBuilder lineBuilder = new StringBuilder();

        try {
            String line;
            while ((line = lineReader.readLine()) != null) {
                lineBuilder.append(" ").append(line.trim());
                if (line.endsWith(";")) {
                    statements.add(lineBuilder.toString().trim());
                    lineBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            throw new UnitilsException("Error loading file: " + scriptLocation, e);
        }
        return statements;
    }
}
