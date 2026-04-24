package kg.tursunbek;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        //SQLite database creating
        DataBase dataBase = new DataBase();
//        dataBase.createDataBase();

        //getting the user's input
//        Scanner scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//        scanner.close();

        //Generates latex code with a template, which depending on the input
        LatexGenerator latexGenerator = new LatexGenerator();

        List<Map<String, String>> datas = Queries.queryFreeMakerData(dataBase, """
                    SELECT REGION, SUM(IF(Policy_Type = 'Life', 1, 0)) AS Life_Policy_Count,
                           SUM(IF(Policy_Type = 'Auto', 1, 0)) AS Auto_Policy_Count,
                           SUM(IF(Policy_Type = 'Home', 1, 0)) AS Home_Policy_Count
                    FROM  insurance_dataset
                    GROUP by Region;
                """);

        // tempalte1.txt is element 0
//        latexGenerator.process(datas, "template1", "output.tex");
        latexGenerator.process(datas, "template2", "output.tex");

//        for (Map<String, String> kg.tursunbek.data : datas) {
//            latexGenerator.process(kg.tursunbek.data, "result" + input + ".tex");
//        }

        //Closing the connection to the created database.
//        dataBase.getConnection().close();
    }
}
