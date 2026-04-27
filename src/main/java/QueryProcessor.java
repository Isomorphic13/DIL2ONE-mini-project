import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Class with static method queryFreeMarkerData(DataBase dataBase, String query) that gives the result from a query.
 */

public class QueryProcessor {

    /**
     *
     * @param dataBase is an instance of a database from which data will be passed to the TemplateProcessor.
     * @param query is a query to be processed.
     * @return data, whose structure is designed to passed to Free Marker template engine.
     * Map<String, String> contains as the key a variable in the template to which will be a passed a value.
     * For example: Dear &<r.name>, you just bought %<root.product>. Name and product are keys, the values depend on the person and his purchase
     */
    public static List<Map<String, String>> queryFreeMarkerData(DataBase dataBase, String query) {
        SqlResult result = getResult(dataBase, query);

        List<Map<String, String>> data = new ArrayList<>();
        List<String> columnNames = result.getColumnNames();

        for (List<String> temp : result.getData()) {
            Map<String, String> subRoot = new HashMap<>(); //important point. Collects

            for (int i = 0; i < columnNames.size(); i++) {
                subRoot.put(columnNames.get(i), temp.get(i));
            }

            data.add(subRoot);
        }

        return data;
    }

    /**
     *
     * @param dataBase is given from queryFreeMarkerData(DataBase dataBase, String query)
     * @param query is given from queryFreeMarkerData(DataBase dataBase, String query)
     * @return a instance of the SqlResult class that contains the data from the query.
     */

    private static SqlResult getResult(DataBase dataBase, String query) {
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
            System.err.println("Running Statement in Queries went wrong: " + s.getMessage());
        }

        return new SqlResult(result, columnNames);
    }

    /**
     *
     * @param resultSet contains the result from the JDBC API from a query.
     * @return List of all rows. Each sublist contain the content of a single row.
     * @throws SQLException
     */

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

    public static String queryTemporary(DataBase dataBase) {
        return  "SELECT Customer_ID, Policy_Type, Region " +
                "FROM " + dataBase.getTableName() + " " +
                "WHERE Policy_Type IS NOT NULL " +
                "AND Premium IS NOT NULL " +
                "AND Premium >= 250 " +
                "AND Customer_Satisfaction IS NOT NULL " +
                "AND Customer_Satisfaction <= 5";
    }
}
