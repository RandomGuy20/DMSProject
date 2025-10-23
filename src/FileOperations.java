import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;



public class FileOperations
{

    //<editor-fold desc="Fields">
    private ArrayList<Employee> _employees = new ArrayList<Employee>();
    String _fileLocation;
    String sqdBase ;
    Statement statement;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public FileOperations (String fileLocation)
    {
        //_fileLocation = fileLocation;
        _fileLocation = fileLocation.replace("\"", "").trim();

        ReadToArrayList();
    }
    //</editor-fold>

    //<editor-fold desc="Private Methods">
    //Reading file to ArrayList
    private void ReadToArrayList()
    {
        Employee employee;
        _employees.clear();
       // Class.forName("org.sqlite.JDBC");
         sqdBase = "jdbc:sqlite:" + _fileLocation;
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error class.forName: " + e.getMessage());
        }

        try(Connection c = DriverManager.getConnection(sqdBase))
        {
            statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employees");


            while(resultSet.next())
            {
                Employee emp = new Employee
                (
                    resultSet.getInt("id"),
                    resultSet.getString("lastName"),
                    resultSet.getString("firstName"),
                    resultSet.getString("jobTitle"),
                    resultSet.getString("location"),
                    resultSet.getDouble("salary")
                );

                _employees.add(emp);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Error reading from database is: " + e.getMessage());
        }


        // OLD FILE BASED CODE
//        try(BufferedReader br = new BufferedReader(new FileReader(_fileLocation)))
//        {
//            String _emp;
//
//
//            while((_emp = br.readLine()) != null)
//            {
//
//                employee = TryGetEmployeeFields(_emp);
//                if(employee != null)
//                    _employees.add(employee);
//
//            }
//
//        }
//        catch(IOException e)
//        {
//            System.out.println("Error reading file");
//        }
    }

    //Get the employee fields
    private Employee TryGetEmployeeFields(String fileLine)
    {
        String[] _fields =  fileLine.split("-");

        if (_fields.length != 6)
            return null;

        try
        {
            int _id = Integer.parseInt(_fields[0]);
            String _lastName = _fields[1];
            String _firstName = _fields[2];
            String _position = _fields[3];
            String _location = _fields[4];
            double _salary =  Double.parseDouble(_fields[5].replace(",",""));

            return new Employee(_id, _lastName, _firstName, _position, _location, _salary);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //</editor-fold>

    //<editor-fold desc="Public Methods">

    //Returns an arrayList of all employee objects
    public ArrayList<Employee> GetEmployees()
    {
        ReadToArrayList();
        return _employees;
    }

    //Completely overwrite the file with the employees parameter
    //Updateing for Database vs file based ops
    public void WriteAll(ArrayList<Employee> employees)
    {

        String deleteAll = "DELETE FROM Employees";
        String insertAll = "INSERT INTO Employees (Id, FirstName, LastName, JobTitle, Location, Salary) VALUES (?, ?, ?, ?, ?, ?)";

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(sqdBase);
            conn.setAutoCommit(false);

            statement = conn.createStatement();
            statement.executeUpdate(deleteAll);

            PreparedStatement preparedStatement = conn.prepareStatement(insertAll);

            employees.stream().forEach(employee ->
            {
                try
                {
                    preparedStatement.setInt(1, employee.GetIdNumber());
                    preparedStatement.setString(2, employee.GetFirstName());
                    preparedStatement.setString(3, employee.GetLastName());
                    preparedStatement.setString(4, employee.GetJobTitle());
                    preparedStatement.setString(5, employee.GetLocation());
                    preparedStatement.setDouble(6, employee.GetSalary());

                    preparedStatement.addBatch();
                }
                catch (SQLException e)
                {
                    System.out.println("Error in inserting employee is: " + e.getMessage());
                }
            });

            preparedStatement.executeBatch();
            conn.commit();
            preparedStatement.close();
            conn.close();

        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Driver not found: " + e.getMessage());
        }
        catch (SQLException e)
        {
            System.out.println("Database error: " + e.getMessage());
        }



        //Old File based stuffs
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(_fileLocation)))
//        {
//            for(Employee employee : employees)
//            {
//                String newData = String.format("%d-%s-%s-%s-%s-%.2f",employee.GetIdNumber(),employee.GetFirstName(),employee.GetLastName(),
//                        employee.GetJobTitle(), employee.GetLocation(),employee.GetSalary());;
//
//                bw.write(newData);
//                bw.newLine();
//            }
//
////            ReadToArrayList();
//        }
//        catch(IOException e)
//        {
//            System.out.println("Error writing to file");
//        }
    }


    //</editor-fold>


}
