package job.task.app.tasks;

import job.task.app.model.Employee;
import job.task.app.model.Gender;
import job.task.app.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddEmployeeTask implements Task
{
    private static final Logger logger = LogManager.getLogger(AddEmployeeTask.class);

    private final String[] args;
    private final EmployeeRepository repository;
    private final DateTimeFormatter formatter;

    public AddEmployeeTask(String[] args, EmployeeRepository repository)
    {
        if (args.length != 4)
        {
            logger.error("Incorrect number of arguments. Correct input format: \"<Name> <birthDate in yyyy-MM-dd format> <Gender: Male | Female>\"");
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        this.args = args;
        this.repository = repository;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Override
    public void execute()
    {
        try
        {
            Employee.builder()
                    .fioName(args[1])
                    .birthDate(LocalDate.parse(args[2], formatter))
                    .gender(Gender.valueOf(args[3]))
                    .build()
                    .save(repository);
        } catch (Exception e)
        {
            throw new RuntimeException("Failed object validation: ", e);
        }
    }
}
