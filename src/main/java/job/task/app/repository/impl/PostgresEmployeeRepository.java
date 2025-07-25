package job.task.app.repository.impl;

import job.task.app.config.Database;
import job.task.app.model.Employee;
import job.task.app.model.Gender;
import job.task.app.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresEmployeeRepository implements EmployeeRepository
{
    private static final Logger logger = LogManager.getLogger(PostgresEmployeeRepository.class);
    private final Database db;

    public PostgresEmployeeRepository(Database db)
    {
        this.db = db;
    }

    @Override
    public void createTable()
    {
        logger.info("Creating employee table...");
        String query = """
                CREATE TABLE IF NOT EXISTS employee (
                 id SERIAL PRIMARY KEY,
                 fioName TEXT NOT NULL,
                 birthDate DATE NOT NULL,
                 gender VARCHAR(6) NOT NULL
                )
                """;
        try (Connection conn = db.getConnection(); Statement stmt = conn.createStatement())
        {
            stmt.execute(query);
        } catch (Exception e)
        {
            throw new RuntimeException("Error while creating table", e);
        }
    }

    @Override
    public void save(Employee employee)
    {
        logger.info("Saving employee...");
        String query = """
                INSERT INTO employee (fioName, birthDate, gender)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setString(1, employee.getFioName());
            stmt.setDate(2, Date.valueOf(employee.getBirthDate()));
            stmt.setString(3, employee.getGender().name());
            stmt.executeUpdate();
        } catch (Exception e)
        {
            throw new RuntimeException("Error while saving object", e);
        }
    }

    @Override
    public void saveAll(List<Employee> employees)
    {
        logger.info("Saving employees batch...");
        String query = """
                INSERT INTO employee (fioName, birthDate, gender)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
            for (var employee : employees)
            {
                stmt.setString(1, employee.getFioName());
                stmt.setDate(2, Date.valueOf(employee.getBirthDate()));
                stmt.setString(3, employee.getGender().name());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e)
        {
            throw new RuntimeException("Error while saving batch", e);
        }
    }

    @Override
    public List<Employee> findAll()
    {
        String query = """
                SELECT * FROM employee;
                """;
        return getEmployees(query);
    }

    @Override
    public List<Employee> findAllUniqueSortedByFioName()
    {
        String query = """
                SELECT * FROM (
                    SELECT DISTINCT ON (fioName, birthDate) *
                    FROM employee
                ) AS unique_employees
                ORDER BY fioName
                """;
        return getEmployees(query);
    }

    private List<Employee> getEmployees(String query)
    {
        List<Employee> employees = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try (Connection conn = db.getConnection(); Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(query))
        {
            long duration = System.currentTimeMillis() - startTime;

            logger.info("Query execution time: {} ms.", duration);

            while (res.next())
            {
                employees.add(
                        Employee.builder()
                                .fioName(res.getString("fioName"))
                                .birthDate(res.getDate("birthDate").toLocalDate())
                                .gender(Gender.valueOf(res.getString("gender"))).build()
                );
            }
        } catch (Exception e)
        {
            throw new RuntimeException("Error while getting batch", e);
        }

        return employees;
    }

    @Override
    public List<Employee> findAllFioNameStartsWithF()
    {
        String query = """
                SELECT * FROM employee
                WHERE gender = 'Male'
                AND fioName LIKE 'F%'
                ORDER BY fioName
                """;

        return getEmployees(query);
    }
}
