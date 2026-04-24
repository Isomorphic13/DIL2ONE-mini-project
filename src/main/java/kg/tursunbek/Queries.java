package kg.tursunbek;

import kg.tursunbek.data.SqlResult;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Class with static method query(kg.tursunbek.DataBase db, String input) that gives result from the query as root kg.tursunbek.data model.
 * The methodMap holds all similar methods of the class. Enables flexibility by writing new actions with other templates.
 */

public class Queries {

    /**
     * // TODO - 23.04.26 - tjorven: Create javadoc
     *
     * @param dataBase the database
     * @return kg.tursunbek.data
     */
    public static List<Map<String, String>> queryFreeMakerData(DataBase dataBase, String query) {
        SqlResult result = getResult(dataBase, query);

        List<Map<String, String>> data = new ArrayList<>();
        List<String> columnNames = result.getColumnNames();

        for (List<String> temp : result.getData()) {
            Map<String, String> subRoot = new HashMap<>();

            for (int i = 0; i < columnNames.size(); i++) {
                subRoot.put(columnNames.get(i), temp.get(i));
            }

            data.add(subRoot);
        }

        return data;
    }

    public static SqlResult getResult(DataBase dataBase, String query) {
        List<List<String>> result = null;
        List<String> columnNames = new ArrayList<>();

        try (Statement statement = dataBase.getConnection().createStatement()) {
            statement.execute(query);

            ResultSet resultSet = statement.getResultSet();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i < columnCount + 1; i++) {
                columnNames.add(metaData.getColumnLabel(i));
            }

            result = getRows(resultSet);
        } catch (SQLException s) {
            System.err.println("Running Statement in kg.tursunbek.Queries went wrong: " + s.getMessage());
        }

        return new SqlResult(result, columnNames);
    }

    private static List<List<String>> getRows(ResultSet resultSet) throws SQLException {
        List<List<String>> result = new ArrayList<>();
        int columnNumber = resultSet.getMetaData().getColumnCount();

        while (resultSet.next()) {
            List<String> row = new ArrayList<>();

            for (int i = 1; i <= columnNumber; i++) {
                row.add(resultSet.getString(i));
            }

            result.add(row);
        }
        return result;
    }

    public String queryTemporary(String table) {
        return  "SELECT Customer_ID, Policy_Type, Region " +
                "FROM %s ".formatted(table) +
                "WHERE Policy_Type IS NOT NULL " +
                "AND Premium IS NOT NULL " +
                "AND Premium >= 250 " +
                "AND Customer_Satisfaction IS NOT NULL " +
                "AND Customer_Satisfaction <= 5";
    }
}
