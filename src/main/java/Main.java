import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {

        // Getting the user's input
        Controller controller = new Controller();
        controller.getUserInput();

        // Creating a database depending on the users input whether it is 'yes' or 'no'
        DataBase dataBase;
        dataBase = controller.getUseDataFromProject() ? new DataBase() : new DataBase(controller);
        dataBase.createDataBase();

        // Creating the result from the user's query or prewritten query
        String query = controller.useDataFromProject ? QueryProcessor.queryTemporary(dataBase) : controller.getQuery();
        List<Map<String,String>> queryResult = QueryProcessor.queryFreeMarkerData(dataBase, query);

        // Passing the result from the query to the FreeMarker template engine
        TemplateProcessor templateProcessor = new TemplateProcessor();
        templateProcessor.process(queryResult, controller.getTemplateName(), controller);
    }
}
