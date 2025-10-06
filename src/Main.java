import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    public static void main(String[] args)
    {

        //region Fields
        Scanner scanner = new Scanner(System.in);
        DatabaseOperations databaseOperations;
        FileOperations fileOperations;
        boolean isRunning = true;
        int userChoice = 0;
        //endregion




        System.out.print("Enter pathway to the Employee.txt file, if you enter an incorrect path the system will tell you and then" +
                "it will terminate. Also make sure your data, if you have any, is separated by a - otherwise it will not read it: \n");

        String input = scanner.nextLine().trim();
        //String input = "D:\\DMStests\\employees.rtf"; //This was just used to streamline my testing

        //Verifying correct input to allow program to proceed
        if(input.isEmpty())
        {
            System.out.println("You entered an empty string, exiting.....");
            System.exit(1);
        }
        if(Files.exists(Paths.get(input)))
            System.out.println("File Path and File name found, entering hamburger menu");
        else
        {
            System.out.println("You entered an incorrect pathway and/or filename, exiting...");
            System.exit(1);
        }

        //Instantiate the instances of the classes
        fileOperations = new FileOperations(input);
        databaseOperations = new DatabaseOperations(fileOperations);



        //Start the Hamburger Menu Operations
        do
        {

            System.out.println
            (
                    "DMS Operations" +
                            "\n1. Create an Employee"  +
                            "\n2. Read Employee Data"  +
                            "\n3. Update an Employee"  +
                            "\n4. Delete an Employee"  +
                            "\n5. Search for Employees"  +
                            "\n6. Exit"  +
                            "\nEnter your choice: \n"
            );

            //Verify the user entered an integer
            if(scanner.hasNextInt())
            {
                userChoice = scanner.nextInt();
                scanner.nextLine();
            }
            else //If they Did NOT, make them enter again
            {
                System.out.println("Please enter a valid choice");
                scanner.nextLine();
                continue;
            }

            //Loop through the Menu Options
            switch(userChoice)
            {
                case 1:
                    System.out.println("Create and Employee");
                    databaseOperations.AddEmployee();
                    break;
                case 2:
                    System.out.println("Read Employee Data");
                    databaseOperations.ReadAll();
                    break;
                case 3:
                    System.out.println("Update an Employee");
                    databaseOperations.UpdateEmployee();
                    break;
                case 4:
                    System.out.println("Delete an Employee");
                    databaseOperations.DeleteEmployee();
                    break;
                case 5:
                    databaseOperations.SortEmployees();
                    break;
                case 6:
                    System.out.println("Exiting....");
                    isRunning = false;
                    break;

            }

        }
        while(isRunning);


    }
}































