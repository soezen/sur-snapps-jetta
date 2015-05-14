package sur.snapps.jetta.database.script;

import com.google.common.collect.Lists;
import com.google.common.io.LineReader;
import sur.snapps.jetta.core.logger.JettaLogger;

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
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> getSqlStatements(String scriptLocation) {
        List<String> statements = Lists.newLinkedList();
        JettaLogger.info(ScriptRunner.class, "Loading script : " + scriptLocation);

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(scriptLocation);
        if (inputStream == null) {
            throw new IllegalArgumentException("script not found at location: " + scriptLocation);
        }
        InputStreamReader reader = new InputStreamReader(inputStream);
        LineReader lineReader = new LineReader(reader);
        StringBuilder lineBuilder = new StringBuilder();

        try {
            String line;
            while ((line = lineReader.readLine()) != null) {
                lineBuilder.append(" ").append(line.trim());
                if (line.endsWith(";")) {
                    String statement = lineBuilder.toString();
                    statement = statement.replaceAll(";", "").trim();
                    statements.add(statement);
                    JettaLogger.debug(ScriptRunner.class, "SQL : " + statement);
                    lineBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("unable to read file: " + scriptLocation);
        }
        return statements;
    }
}
