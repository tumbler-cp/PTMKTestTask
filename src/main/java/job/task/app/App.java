package job.task.app;

import job.task.app.config.Database;
import job.task.app.repository.EmployeeRepository;
import job.task.app.repository.impl.PostgresEmployeeRepository;
import job.task.app.tasks.TaskFactory;
import job.task.app.util.EmployeeGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Starting Application...");

        logger.info("Loading properties...");
        Properties properties = loadProperties();
        if (properties == null) {
            return;
        }

        File namesFile = new File(properties.getProperty("generator.namelist.file"));
        if (!namesFile.exists()) {
            logger.error("Generate names list file does not exist");
            return;
        }

        logger.info("Initialising Employee generator...");
        EmployeeGenerator generator = createEmployeeGenerator(namesFile);
        if (generator == null) {
            return;
        }

        logger.info("Initialising Employee repository...");
        Database db = createDatabase(properties);
        EmployeeRepository repository = new PostgresEmployeeRepository(db);

        logger.info("Parsing task");
        TaskFactory.createTask(args, repository, generator).execute();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./app.properties")) {
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            logger.error("Properties file not found: {}", e.getMessage());
            return null;
        }
    }

    private static EmployeeGenerator createEmployeeGenerator(File namesFile) {
        try {
            return new EmployeeGenerator(namesFile);
        } catch (FileNotFoundException e) {
            logger.error("Generate names list file does not exist: {}", e.getMessage());
            return null;
        }
    }

    private static Database createDatabase(Properties properties) {
        return new Database(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }
}