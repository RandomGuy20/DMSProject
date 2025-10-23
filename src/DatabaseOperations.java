import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DatabaseOperations
{


    //<editor-fold desc="Fields">
    private ArrayList<Employee> _employees;
    private final FileOperations _fileOperations;
    private final Scanner _scanner = new Scanner(System.in);
    //</editor-fold>


    //<editor-fold desc="Constructor">
    public DatabaseOperations(FileOperations fileOperations)
    {
        _fileOperations = fileOperations;
        _employees = _fileOperations.GetEmployees();
    }
    //</editor-fold>

   //<editor-fold desc="Private Methods">

    //Verify an employee object exists
    private Boolean EmployeeExists(int input)
    {
        return _employees.stream().anyMatch(id -> id.GetIdNumber() == input);
    }

    //Helper Method to get the employees ID
    private int GetEmployeeID(String burgerPhrase)
    {
        while(true)
        {
            System.out.println(burgerPhrase);
            if(_scanner.hasNextInt())
            {
                int id = _scanner.nextInt();
                _scanner.nextLine();
                return id;
            }
            System.out.println("Incorrect formatting, you need to enter only numbers");
            _scanner.nextLine();
        }
    }

    //Helper method to obtain string data
    private String GetEmployeeStringData(String burgerPhrase)
    {
        while(true)
        {
            System.out.println(burgerPhrase);
            String input = _scanner.nextLine().trim();
            if(input.chars().allMatch(ch -> Character.isLetter(ch) || Character.isWhitespace(ch)))
            {
                return input;
            }

            System.out.println("Incorrect formatting, you need to enter only letters and white spaces");
//            _scanner.nextLine();
        }
    }

    //New helper method to make sure string data is correctly entered
    public  boolean EmployeeStringDataIsCorrectlyFormatted(String data)
    {
        return (data.chars().allMatch(ch -> Character.isLetter(ch) || Character.isWhitespace(ch)));
    }

    //helper method to get the salary
    private double GetEmployeeSalary(String burgerPhrase)
    {
        while(true)
        {
            System.out.println(burgerPhrase);
            String salary = _scanner.nextLine().trim().replace(",","");

            try
            {
                return Double.parseDouble(salary);
            }
            catch(Exception e)
            {
                System.out.println("Incorrect formatting, you need to enter only numbers, commas, and dots");
                _scanner.nextLine();
            }
        }
    }

    //New version to get salary
    public double VerifySalaryIsDoubles(String data)
    {
        String salary = data.trim().replace(",","");

        try
        {
            return Double.parseDouble(salary);
        }
        catch(Exception e)
        {
            return -1;
        }
    }

    //Call this when you need to update the _employees arrayList
    private void ReSetEmployeeList()
    {
        _employees = _fileOperations.GetEmployees();
    }

    // Getting index of instance of employee by ID
    private int GetEmployeeByIdNumber(int ID)
    {
        OptionalInt index = IntStream.range(0,_employees.size())
                .filter(e -> _employees.get(e).GetIdNumber() == ID)
                .findFirst();

        if(index.isPresent())
            return index.getAsInt();
        else
            return -1;

    }

    //Sort By Location
    private ArrayList<Employee> SortLocation(String location)
    {
        return _employees.stream().filter(emp -> emp.GetLocation().equalsIgnoreCase(location))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //Sort by position
    private ArrayList<Employee> SortPosition(String position)
    {
        return _employees.stream().filter(emp ->  emp.GetJobTitle().equalsIgnoreCase(position))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //Print employees after being sorted
    private void PrintSortedEmployees(ArrayList<Employee> sortedEmployees)
    {
        if (sortedEmployees.isEmpty())
            System.out.println("No employees found");
        else
        {
            sortedEmployees.forEach(emp -> System.out.println(emp.toString()));
        }
    }
    //</editor-fold>


    //<editor-fold desc="Public Methods -> These are the exposed methods to update the database with VRUD operations, and the special operation">
    ///Was used for Burger Menu, will not be using going forward
    public void AddEmployee()
    {
        ReSetEmployeeList();
        int employeeID = GetEmployeeID("Please Enter Employee ID");
        if(EmployeeExists(employeeID))
        {
            System.out.println("Employee already exists exiting....");
            return;
        }
        String employeeFirstName = GetEmployeeStringData("Please Enter Employee First Name: ");
        String employeeLastName = GetEmployeeStringData("Please Enter Employee Last name:  ");
        String employeePosition = GetEmployeeStringData("Please Enter Employee Position: ");
        String employeeLocation = GetEmployeeStringData("Please Enter Employee Location: ");
        double employeeSalary = GetEmployeeSalary("Please Enter Employee Salary: ");

        Employee employee = new Employee(employeeID, employeeFirstName, employeeLastName, employeePosition, employeeLocation, employeeSalary);
        _employees.add(employee);
        System.out.println("You added: " + employee);
        _fileOperations.WriteAll(_employees);
        ReSetEmployeeList();
    }

    ///Was used for Burger Menu, will not be using going forward
    public void ReadAll()
    {
        for(Employee employee : _employees)
            System.out.println(employee.toString());
    }

    public ArrayList<Employee> GetAllEmployees()
    {
        return _employees;
    }

    ///Was used for Burger Menu, will not be using going forward
    public void SortEmployees()
    {

        boolean isRunning = true;
        String sortText;
        ArrayList<Employee> sortedEmployees;
        while(isRunning)
        {
            int answer = GetEmployeeID("Sort Options" +
                    "\n1. By Location"  +
                    "\n2. By Position"  +
                    "\n3. Exit" );

            switch(answer)
            {
                case 1:
                    sortText = GetEmployeeStringData("Enter the Location");
                    sortedEmployees = SortLocation(sortText);
                    PrintSortedEmployees(sortedEmployees);
                    break;
                case 2:
                    sortText = GetEmployeeStringData("Enter the Position");
                    sortedEmployees = SortPosition(sortText);
                    PrintSortedEmployees(sortedEmployees);
                    break;
                case 3:
                    return;
            }
        }
    }

    /**
     * This sort via location or position
     * @param searchParams the data inside of teh JtextField for search purposes
     * @param isLocation Flag to let method know if it is location or position
     * @return True on success false on Failure
     */
    public ArrayList<Employee> SortEmployees(String searchParams , Boolean isLocation)
    {
        if (isLocation)
            return SortLocation(searchParams);
        else
            return SortPosition(searchParams);
    }

    /**
     * Altered for Db Operations This will tell FileOps to Write the new data into the database
     * @param employee the employee we want to update
     * @return true on success, false on failure
     */
    public Boolean UpdateEmployee(Employee employee)
    {

        for(Employee emp : _employees)
        {
            if (emp.GetIdNumber() == employee.GetIdNumber())
            {
                emp.SetFirstName(employee.GetFirstName());
                emp.SetLastName(employee.GetLastName());
                emp.SetJobTitle(employee.GetJobTitle());
                emp.SetSalary(employee.GetSalary());
                emp.SetLocation(employee.GetLocation());
                _fileOperations.WriteAll(_employees);

                return true;
            }
        }

        return false;

    }

    ///Was used for Burger Menu, will not be using going forward
    public void UpdateEmployee()
    {
        boolean isupdated = false;
        int userChoice;
        ReSetEmployeeList();


        int input = GetEmployeeID("Please Enter Employee ID that you want to update.");

        if(!EmployeeExists(input))
        {
            System.out.println("Employee ID does not exist. Exiting...");
            return;
        }
        else
        {
            int index = GetEmployeeByIdNumber(input);

            while(!isupdated)
            {
                //Ask what they wanna do
                //Hamburger option menu
                //then go to that method
                System.out.println
                        (
                                "Update Options" +
                                        "\n1. First Name"  +
                                        "\n2. Last Name"  +
                                        "\n3. Position"  +
                                        "\n4. Location"  +
                                        "\n5. Salary"  +
                                        "\n6. Exit" +
                                        "\nEnter your choice: \n"
                        );

                if(_scanner.hasNextInt())
                {
                    userChoice = _scanner.nextInt();
                    _scanner.nextLine();
                }
                else
                {
                    System.out.println("Please enter a valid choice");
                    _scanner.nextLine();
                    continue;
                }


                switch(userChoice)
                {
                    case 1:
                        _employees.get(index).SetFirstName(GetEmployeeStringData("Please Enter The New First Name: "));
                        break;
                    case 2:
                        _employees.get(index).SetLastName(GetEmployeeStringData("Please Enter The New First Name: "));
                        break;
                    case 3:
                        _employees.get(index).SetJobTitle(GetEmployeeStringData("Please Enter The New Position: "));
                        break;
                    case 4:
                        _employees.get(index).SetLocation(GetEmployeeStringData("Please Enter The New Location: "));
                        break;
                    case 5:
                        _employees.get(index).SetSalary(GetEmployeeSalary("Please Enter The New Salary: "));
                        break;
                    case 6:
                        _fileOperations.WriteAll(_employees);
                        isupdated = true;
                        break;

                }
            }

        }
    }

    ///Was used for Burger Menu, will not be using going forward
    public boolean DeleteEmployee()
    {

        boolean successful = false;

        while (!successful)
        {
            int input = GetEmployeeID("Enter the employees Id you want to delete: \n");

            if(!EmployeeExists(input))
            {
                System.out.println("No Such employee exists, exiting...");
                break;
            }
            else
            {
                int index = GetEmployeeByIdNumber(input);
                if(index > -1)
                {
                    _employees.remove(index);
                    _fileOperations.WriteAll(_employees);
                    ReSetEmployeeList();
                    System.out.println("Succesfully deleted employee ID:" + input);
                    successful = true;
                }




            }

        }

        return false;
    }
    //</editor-fold>
}
