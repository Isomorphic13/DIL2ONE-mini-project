import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 *
 *  Class with the method that takes user's input.
 * The key input is user's choice to process prewritten CSV file, query and template or to process his custom ones.
 * An instance of the class contains user's input and pass the information to other Object of the code.
 */

public class Controller {
    private final Scanner scanner = new Scanner(System.in);

    private String dataBaseName;
    private String templateName;
    private String query;

    private String pathToCSV;
    private String pathToTemplate;

    private boolean DataFromProjectUsed;

    private boolean dataBaseExisting;

    private String resultFileName;

    private static final String[] VALID_FIRST_INPUT = {"yes", "no"};

    public void getUserInput() {

        List<String> temp = Arrays.asList(VALID_FIRST_INPUT);
        String input = "";

        do {
            System.out.println("Do you want to process a template from the project ('yes') " +
                    "\nAlternatively you can process your own template and CSV-file ('no'):");
            try {
                input = scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("invalid inpuit");
            }
        } while (!temp.contains(input));

        if (input.equals("yes")) {
            DataFromProjectUsed = true;
            this.templateName = "template1.txt";
        }

        if (input.equals("no")) {
            DataFromProjectUsed = false;
            this.getUserData(scanner);
        }

        System.out.println("Name the result file");
        resultFileName = scanner.nextLine();

        scanner.close();
    }

    private void getUserData(Scanner scanner) {
        System.out.println("Name your database \nIf a database with the given name doesn't exist in project folder, a new database will be created: ");
        this.dataBaseName = scanner.nextLine();

        File dbFile = new File(dataBaseName);
        if (!dbFile.exists()) {
            System.out.println("Give path to your CSV-file");
            this.dataBaseExisting = false;
            this.pathToCSV = scanner.nextLine();
        } else {
            this.dataBaseExisting = true;
        }

        System.out.println("Give your full template name with its extension \nIf a template with the given name doesn't exist in resources folder in the project, it will be added: ");
        this.templateName = scanner.nextLine();
        this.addTemplateToResources(templateName, scanner);

        System.out.println("Write your SQL-query: ");
        this.query = scanner.nextLine();
    }

    private void addTemplateToResources(String templateName, Scanner scanner) {
        String pathInResources = "config/" + templateName;
        URL resource = getClass().getClassLoader().getResource(pathInResources);

        if (resource == null) {
            System.out.println("Give the path to the template:");
            this.pathToTemplate = scanner.nextLine();
            try {

                Path sourcePath = Paths.get(pathToTemplate);
                Path destinationPath = Paths.get("src/main/resources/config/").resolve(sourcePath.getFileName());
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                System.err.println("Could not copy file: " + e.getMessage());
            }
        }
    }


    public String getPathToCSV() {
        return pathToCSV;
    }

    public String getPathToTemplate() {
        return pathToTemplate;
    }

    public String getQuery() {
        return query;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public boolean isDataFromProjectUsed() {
        return DataFromProjectUsed;
    }

    public String getResultFileName() {
        return resultFileName;
    }

    public boolean isDataBaseExisting() {
        return dataBaseExisting;
    }

}
