package job.task.app.util;

import job.task.app.model.Employee;
import job.task.app.model.Gender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EmployeeGenerator {
    private static final Logger logger = LogManager.getLogger(EmployeeGenerator.class);

    private final List<String> allFioNames;
    private final List<String> fioNamesStartingWithF;
    private final Random random;

    public EmployeeGenerator(File fioNamesFile) throws FileNotFoundException {
        this.allFioNames = loadAllNames(fioNamesFile);
        this.fioNamesStartingWithF = filterNamesStartingWithF(allFioNames);
        this.random = new Random();

        if (allFioNames.isEmpty()) {
            logger.error("Empty file given");
            throw new IllegalStateException("Empty FIO file");
        }
    }

    private List<String> loadAllNames(File file) throws FileNotFoundException {
        List<String> names = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    names.add(line);
                }
            }
        }
        return names;
    }

    private List<String> filterNamesStartingWithF(List<String> allNames) {
        List<String> result = new ArrayList<>();
        for (String name : allNames) {
            if (name.toUpperCase().startsWith("F")) {
                result.add(name);
            }
        }
        return result;
    }

    public Employee generateRandomEmployee(boolean forceMaleF) {
        String fio;
        Gender gender;

        if (forceMaleF) {
            if (fioNamesStartingWithF.isEmpty()) {
                logger.error("No FIO starting with F in FIO pool");
                throw new IllegalStateException("No FIO starting with F");
            }
            fio = fioNamesStartingWithF.get(random.nextInt(fioNamesStartingWithF.size()));
            gender = Gender.Male;
        } else {
            fio = allFioNames.get(random.nextInt(allFioNames.size()));
            gender = random.nextBoolean() ? Gender.Male : Gender.Female;
        }

        LocalDate birthDate = generateRandomBirthDate();

        return Employee.builder()
                .fioName(fio)
                .birthDate(birthDate)
                .gender(gender)
                .build();
    }

    private LocalDate generateRandomBirthDate() {
        long minDay = LocalDate.of(1963, 4, 12).toEpochDay();
        long maxDay = LocalDate.of(2004, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public List<Employee> generateEmployees(int count, boolean maleWithF) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            employees.add(generateRandomEmployee(maleWithF));
        }
        return employees;
    }
}