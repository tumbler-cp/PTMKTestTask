package job.task.app.model;

import job.task.app.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Employee
{
    private static final Logger logger = LogManager.getLogger(Employee.class);

    private Long id;
    private String fioName;
    private LocalDate birthDate;
    private Gender gender;

    public void save(EmployeeRepository repository)
    {
        logger.info("Sending to repository: {}", this.toString());
        repository.save(this);
    }

    public static void saveBatch(List<Employee> employees, EmployeeRepository repository)
    {
        repository.saveAll(employees);
    }

    public int getAge()
    {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String toString()
    {
        return String.format("%-30s | %-12s | %-6s | %3d years",
                fioName,
                birthDate,
                gender.name(),
                getAge());
    }
}
