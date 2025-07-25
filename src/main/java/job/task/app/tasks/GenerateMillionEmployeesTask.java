package job.task.app.tasks;

import job.task.app.model.Employee;
import job.task.app.repository.EmployeeRepository;
import job.task.app.util.EmployeeGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class GenerateMillionEmployeesTask implements Task
{
    private static final Logger logger = LogManager.getLogger(GenerateMillionEmployeesTask.class);

    private final EmployeeRepository repository;
    private final EmployeeGenerator generator;

    @Override
    public void execute()
    {
        logger.info("Start generate million employees");
        var list = generator.generateEmployees(1000000 - 100, false);
        list.addAll(
                generator.generateEmployees(100, true)
        );
        System.out.println(list.size());
        Employee.saveBatch(list, repository);
    }
}
