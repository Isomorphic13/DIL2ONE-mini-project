import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 *
 * Class which represents a SQLite database parsed from a CSV file. The database is created by JDBC API.
 * @see <a href="https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc">...</a>
 * OpenCSV library is used as the CSV reader
 * @see <a href="https://opencsv.sourceforge.net/">...</a>
 */
public class DataBase {

    private String dataBaseName;
    private String url;
    private String tableName;
    private Connection conn;
    Controller controller;

    boolean useDataFromProject;

    /**
     * The difference between the following constructors is that the firsts one created from the project's 'insurance_dataset.csv'
     * and the second one from the CSV file given from as user's input.
     */
    public DataBase () {
        this.dataBaseName = "insurance_dataset";
        this.tableName = "insurance_dataset";
        this.url = "jdbc:sqlite:" + dataBaseName;

        useDataFromProject = true;

        try {
            this.conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            System.err.println("Connection to the database by createDataBase went wrong: " + e.getMessage());
        }

    }

    public DataBase (Controller controller) {
        this.controller = controller;
        this.dataBaseName = controller.getDataBaseName();
        this.tableName = controller.getDataBaseName();
        this.url = "jdbc:sqlite:" + dataBaseName;

        useDataFromProject = false;

        try {
            this.conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            System.err.println("Connection to the database by createDataBase went wrong: " + e.getMessage());
        }
    }

    /**
     * The method creates a SQLite database in the project folder depending on the user's choice.
     */

    public void createDataBase()  {
        Reader reader = null;
        if (this.useDataFromProject) {
            reader = new InputStreamReader(getClass().getClassLoader().getSystemResourceAsStream("insurance_dataset.csv"));
        } else {
            try {
                reader = new InputStreamReader(new FileInputStream(this.controller.getPathToCSV()));
            } catch (FileNotFoundException e) {
                System.err.println("Could not find the path to the CSV:" + e.getMessage());
            }
        }
        createDataBase(new CSVReader(reader));
    }

    /**
     * The actual method to create a database. It creates table with same name as the database.
     * After that it inserts all records as single String to the database.
     * @param csvReader instance of CSVReader from OpenCSV library. csvReader reads the given file line by line
     */

    private void createDataBase(CSVReader csvReader) {
        try {
            // Getting the column names and creating a table
            String tableCreation = "CREATE TABLE IF NOT EXISTS " + this.getTableName() + " (" + getColumnNames(csvReader.readNext()) + ");";

            // Collecting all records in single String line by line with Iterator
            StringBuilder rows = new StringBuilder("INSERT INTO " + this.getTableName() + " VALUES \n");

            Iterator<String[]> it = csvReader.iterator();
            while (it.hasNext()) {
                rows.append("(")
                        .append(rowAppender(it.next()))
                        .append(")");
                if (it.hasNext()) {
                    rows.append(",\n");
                } else {
                    rows.append(";");
                }
            }


            try {
                Statement statement = this.conn.createStatement();
                statement.execute(tableCreation);
                statement.execute(rows.toString());
                statement.close();
            } catch (SQLException e) {
                System.err.println("Table creating error: " + e.getMessage());
            }

        } catch (IOException | CsvValidationException e) {
            System.err.println("Reading the CSV file error: " + e.getMessage());
        }
    }



    /**
     * Helping method to get the column names from a CSV file for the database.
     * @param s Array of strings that contains the column names as elements.
     * @return Single String to be passed to a SQL statement for creating a table in the SQl database.
     */
    private static String getColumnNames(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i]);
            if (i < s.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Same as getColumNames(String[] s), but gets the content of a row as a single String
     * @param s Array of strings that contains the values of a record from a CSV file as its elements.
     * @return Single String to be passed to createDataBase(CSVReader csvReader) for creating a table in the SQl database.
     */
    private static String rowAppender(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null || s[i].isEmpty()) {
                sb.append("NULL");
            } else {
                sb.append("'").append(s[i]).append("'");
            }
            if (i < s.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }



    public Connection getConnection() {
        return this.conn;
    }

    public String getDataBaseName() {
        return this.dataBaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getUrl() {
        return this.url;
    }
}
