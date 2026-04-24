package kg.tursunbek.data;

import java.util.List;

public class SqlResult {

    private final List<List<String>> data;
    private final List<String> columnNames;

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    public SqlResult(List<List<String>> data, List<String> columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public List<List<String>> getData() {
        return this.data;
    }

}
