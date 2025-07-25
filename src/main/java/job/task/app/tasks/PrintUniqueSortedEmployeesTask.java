package job.task.app.tasks;

import job.task.app.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrintUniqueSortedEmployeesTask implements Task
{
    private final EmployeeRepository repository;

    @Override
    public void execute()
    {
        repository.findAllUniqueSortedByFioName().forEach(System.out::println);
    }
}
