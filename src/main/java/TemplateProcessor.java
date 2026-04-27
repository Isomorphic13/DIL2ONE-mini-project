import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 *
 * Class that process a result from a SQL query with Free Marker template engine.
 * @see <a href="https://freemarker.apache.org/index.html">...</a>
 */
public class TemplateProcessor {

    private final Configuration configuration;

    /**
     *
     * The constructor sets the file from which templates will be downloaded. The file is 'config' folder in 'resources'.
     */

    public TemplateProcessor() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_34);

        try {
            File templateDirectory = new File("src/main/resources/config/");
            configuration.setDirectoryForTemplateLoading(templateDirectory);
        } catch (IOException e) {
            System.err.println("The given template name was not found in the config folder: " + e.getMessage());
        }

        this.configuration.setDefaultEncoding("UTF-8");
    }

    /**
     *
     * @param data result from a SQL query given by {@link QueryProcessor}.
     * @param templateName Name of the template in which records from the SQL query result will be written.
     * @param controller contains all the input's data from the user.
     */

    public void process(List<Map<String, String>> data, String templateName, Controller controller) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("root", data);

        // Ensure the filename has the correct extension for FreeMarker
        String fileName = templateName.endsWith(".txt") ? templateName : templateName + ".txt";

        String outputFileName = controller.getResultFileName();

        try (FileWriter writer = new FileWriter(outputFileName)) {

            Template template = configuration.getTemplate(fileName);
            template.process(dataModel, writer);

            System.out.println("Success: Result written to " + outputFileName);
        } catch (IOException | TemplateException e) {
            System.err.println("Template Processing Error: " + e.getMessage());
        }
    }
}