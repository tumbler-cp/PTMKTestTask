package job.task.app.repository;

import job.task.app.model.Employee;

import java.util.List;

public interface EmployeeRepository
{
    void createTable();

    void save(Employee employee);

    void saveAll(List<Employee> employees);

    List<Employee> findAll();

    List<Employee> findAllUniqueSortedByFioName();
    List<Employee> findAllFioNameStartsWithF();

}
