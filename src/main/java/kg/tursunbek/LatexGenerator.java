package kg.tursunbek;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class that generates a latex code from the given templates.
 * The methodMap holds all similar methods of the class. Enables flexibility by writing new actions with other templates.
 */
public class LatexGenerator {
    private final static String FIRST_LINE = "\\documentclass{article}\n\\usepackage[top=2cm, bottom=2cm, right=2cm, left=2cm]{geometry}\n\\begin{document}\n";
    private final static String LAST_LINE = "\\end{document}";

    private final Map<String, Template> templates = new HashMap<>();

    /**
     * The constructor configures the tools Free Marker library
     *
     * @param input user input, depending on will be used different templates for Free Marker
     * @link package.freemarker.template
     * @see <a href="https://freemarker.apache.org/index.html">...</a>
     */
    public LatexGenerator() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_34);
        configuration.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "config/");
        configuration.setDefaultEncoding("UTF-8");

        try {
            URL resources = this.getClass().getClassLoader().getResource("config/");
            File file = new File(Objects.requireNonNull(resources).toURI());
            Stream<Path> list = Files.list(file.toPath());
            list.map(Path::toFile).map(File::getName).forEach(fileName -> this.loadTemplate(fileName, configuration));
            list.close();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTemplate(String fileName, Configuration configuration) {
        try {
            this.templates.put(fileName.replace(".txt", ""), configuration.getTemplate(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void process(List<Map<String, String>> data, String inputFile, String fileName) { // , String input
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("root", data);
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(LatexGenerator.FIRST_LINE);

            this.templates.get(inputFile.replace(".txt", "")).process(dataModel, writer);

            writer.write(LAST_LINE);
        } catch (IOException | TemplateException e) {
            System.err.println("Creating the result file error:" + e.getMessage());
        }
    }

}