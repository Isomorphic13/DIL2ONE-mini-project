import java.util.List;

/**
 *
 * Instance of this class contain the result of a SQL query.
 */

public class SqlResult {

    private final List<List<String>> data;
    private final List<String> columnNames;


    /**
     *
     * @param data List of all rows. Each sublist contain the content of a single row.
     * @param columnNames names of columns of the result.
     */
    public SqlResult(List<List<String>> data, List<String> columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public List<List<String>> getData() {
        return this.data;
    }
    public List<String> getColumnNames() {
        return this.columnNames;
    }

}
