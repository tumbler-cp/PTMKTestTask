package job.task.app.tasks;

import job.task.app.repository.EmployeeRepository;
import job.task.app.util.EmployeeGenerator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TaskFactory
{
    private static final Logger logger = LogManager.getLogger(TaskFactory.class);
    public static Task createTask(String[] args, EmployeeRepository repository, EmployeeGenerator generator)
    {
        if (args.length < 1)
        {
            logger.error("Invalid number of arguments");
            throw new IllegalArgumentException("Define working mode");
        }

        int mode;

        try
        {
            mode = Integer.parseInt(args[0]);
        } catch (NumberFormatException e)
        {
            logger.error("Invalid number format");
            throw new IllegalArgumentException("Invalid working mode type");
        }

        return switch (mode)
        {
            case 1 -> new CreateTableTask(repository);
            case 2 -> new AddEmployeeTask(args, repository);
            case 3 -> new PrintUniqueSortedEmployeesTask(repository);
            case 4 -> new GenerateMillionEmployeesTask(repository, generator);
            case 5 -> new PrintSortedEmployeeFTask(repository);
            default -> {
                logger.error("Invalid mode");
                throw new IllegalArgumentException("Invalid working mode type");
            }
        };
    }
}
