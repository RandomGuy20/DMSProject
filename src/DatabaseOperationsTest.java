//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//class DatabaseOperationsTest
//{
//
//    private FileOperations fileOps;
//    private DatabaseOperations databaseOps;
//    ArrayList<Employee> employees = new ArrayList<>();
//
//    @org.junit.jupiter.api.BeforeEach
//    void setUp()
//    {
//        fileOps = new FileOperations("unittests.txt");
//
//
//        employees.add(new Employee(1,"lastNameOne","firstNameOne","testJobTitleOne","testLocationOne",1));
//        employees.add(new Employee(2,"lastNameTwo","firstNameTwo","testJobTitleTwo","testLocationTwo",1));
//        employees.add(new Employee(3,"lastNameThree","firstNameThree","testJobTitleThree","testLocationThree",1));
//        employees.add(new Employee(4,"lastNameFour","firstNameFour","testJobTitleFour","testLocationFour",1));
//        employees.add(new Employee(5,"lastNameFive","firstNameFive","testJobTitleFive","testLocationFive",1));
//        employees.add(new Employee(6,"lastNameSix","firstNameSix","testJobTitleSix","testLocationSix",1));
//        employees.add(new Employee(7,"lastNameSeven","firstNameSeven","testJobTitleSeven","testLocationSeven",1));
//        employees.add(new Employee(8,"lastNameEight","firstNameEight","testJobTitleEight","testLocationEight",1));
//        employees.add(new Employee(9,"lastNameNine","firstNameNine","testJobTitleNine","testLocationNine",1));
//        employees.add(new Employee(10,"lastNameTen","firstNameTen","testJobTitleTen","testLocationTen",1));
//        employees.add(new Employee(11,"lastNameEleven","firstNameEleven","testJobTitleEleven","testLocationEleven",1));
//        employees.add(new Employee(12,"lastNameTwelve","firstNameTwelve","testJobTitleTwelve","testLocationTwelve",1));
//        employees.add(new Employee(13,"lastNameThirteen","firstNameThirteen","testJobTitleThirteen","testLocationThirteen",1));
//        employees.add(new Employee(14,"lastNameFourteen","firstNameFourteen","testJobTitleFourteen","testLocationFourteen",1));
//        employees.add(new Employee(15,"lastNameFifteen","firstNameFifteen","testJobTitleFifteen","testLocationFifteen",1));
//        employees.add(new Employee(16,"lastNameSixteen","firstNameSixteen","testJobTitleSixteen","testLocationSixteen",1));
//        employees.add(new Employee(17,"lastNameSeventeen","firstNameSeventeen","testJobTitleSeventeen","testLocationSeventeen",1));
//        employees.add(new Employee(18,"lastNameEighteen","firstNameEighteen","testJobTitleEighteen","testLocationEighteen",1));
//        employees.add(new Employee(19,"lastNameNineteen","firstNameNineteen","testJobTitleNineteen","testLocationNineteen",1));
//        employees.add(new Employee(20,"lastNameTwenty","firstNameTwenty","testJobTitleTwenty","testLocationTwenty",1));
//
//
//        fileOps.WriteAll(employees);
//    }
//
//    @org.junit.jupiter.api.Test
//    void addEmployee()
//    {
//        String emp = "21\n" +
//                "AddEmployeeLast\n" +
//                "AddEmployeeFirst\n" +
//                "addJobTitle\n" +
//                "adddlocation\n" +
//                "6000\n";
//        System.setIn((new ByteArrayInputStream(emp.getBytes())));
//
//        databaseOps = new DatabaseOperations(fileOps);
//        databaseOps.AddEmployee();
//        ArrayList<Employee> list = fileOps.GetEmployees();
//
//        assertTrue(list.stream().anyMatch(e -> e.GetFirstName().equals("AddEmployeeLast")));
//    }
//
//    @org.junit.jupiter.api.Test
//    void readAll()
//    {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(out));
//
//        databaseOps = new DatabaseOperations(fileOps);
//        databaseOps.ReadAll();
//
//        String output = out.toString();
//        assertTrue(output.contains("lastNameOne"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void sortEmployees()
//    {
//        String input = "2\n" +
//                "testJobTitleOne\n" +
//                "3\n"; // choose "2" for position, then enter "Developer", then exit
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(out));
//
//        databaseOps = new DatabaseOperations(fileOps);
//        databaseOps.SortEmployees();
//
//        String output = out.toString();
//        assertTrue(output.contains("testJobTitleOne"));
//        assertFalse(output.contains("testJobTitleTwo"));
//
//    }
//
//    @org.junit.jupiter.api.Test
//    void updateEmployee()
//    {
//        String input = "1\n" +
//                        "1\n" +
//                        "unitnametested\n" +
//                        "6\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//
//        databaseOps = new DatabaseOperations(fileOps);
//        employees = fileOps.GetEmployees();
//        databaseOps.UpdateEmployee();
//
//
//        for (Employee employee : employees)
//
//        assertEquals("unitnametested", employees.get(0).GetFirstName());
//        //the failure point
//        assertEquals("firstNameOne", employees.get(0).GetFirstName());
//    }
//
//    @org.junit.jupiter.api.Test
//    void deleteEmployee()
//    {
//        String input = "2\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//
//        databaseOps = new DatabaseOperations(fileOps);
//        databaseOps.DeleteEmployee();
//    }
//}