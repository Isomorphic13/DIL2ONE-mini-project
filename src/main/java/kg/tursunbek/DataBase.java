package kg.tursunbek;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

/**
 * Class which represents the SQL database parsed from
 *
 * @see <a href="https://www.kaggle.com/datasets/archanagajendra/insurance-customer-data">...</a>
 * Instantiating this class initializes the database if it does not already exist
 */
public class DataBase {

    private final String dbName = "kg.tursunbek.DataBase";
    private final String url = "jdbc:sqlite:" + this.dbName;
    private Connection conn;

    /**
     * The constructor creates local SQLite database.
     * The databased is created by JDBC API.
     *
     * @see <a href="https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc">...</a>
     */
    public DataBase() {
        try {
            this.conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            System.err.println("Connection to the database by createDataBase went wrong: " + e.getMessage());
        }
    }

    /**
     * Helping method to get the column names from a CSV file for the database.
     *
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
     *
     * @param s Array of strings that contains the column names as elements.
     * @return Single String to be passed to a SQL statement for creating a table in the SQl database.
     */
    private static String rowAppender(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null || s[i].isEmpty()) {
                sb.append("NULL");
            } else {
                if (i == 2) {              //normalization of the Gender column
                    if (s[i].equals("Female") || s[i].equals("F")) sb.append("'").append("F").append("'");
                    else {
                        if (s[i].equals("Male") || s[i].equals("M")) sb.append("'").append("M").append("'");
                        else sb.append("'").append("Other").append("'");
                    }
                } else {
                    sb.append("'").append(s[i]).append("'");
                }
            }

            if (i < s.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * The method takes the content of the insurance_dataset.csv and creates a table in the created SQLite database.
     * Important part of the method is OpenCSV library, that enables reading a CSV file line by line
     *
     * @see <a href="https://mvnrepository.com/artifact/com.opencsv/opencsv">...</a>
     */
    public void createDataBase() {
        try {
            Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream("insurance_dataset.csv")); //insurance_dataset.csv is not empty
            CSVReader csvReader = new CSVReader(reader);
            String tableCreation = "CREATE TABLE IF NOT EXISTS " + this.getTableName() + " (" + getColumnNames(csvReader.readNext()) + ");";

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

    public Connection getConnection() {
        return this.conn;
    }

    public String getDbName() {
        return this.dbName;
    }

    public String getTableName() {
        return "insurance_dataset";
    }

    public String getUrl() {
        return this.url;
    }
}
