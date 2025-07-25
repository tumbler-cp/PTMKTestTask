package job.task.app.tasks;

import job.task.app.model.Employee;
import job.task.app.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PrintSortedEmployeeFTask implements Task
{
    private final EmployeeRepository repository;

    @Override
    public void execute()
    {
        List<Employee> employees = repository.findAllFioNameStartsWithF();
        int maxRows = 10;
        employees.stream().limit(maxRows).forEach(System.out::println);
        System.out.println("+" + (employees.size() - maxRows) + "more");
    }
}
