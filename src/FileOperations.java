import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



public class FileOperations
{

    //<editor-fold desc="Fields">
    private ArrayList<Employee> _employees = new ArrayList<Employee>();
    String _fileLocation;
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public FileOperations (String fileLocation)
    {
        _fileLocation = fileLocation;
        ReadToArrayList();
    }
    //</editor-fold>

    //<editor-fold desc="Private Methods">
    //Reading file to ArrayList
    private void ReadToArrayList()
    {
        Employee employee;
        _employees.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(_fileLocation)))
        {
            String _emp;


            while((_emp = br.readLine()) != null)
            {

                employee = TryGetEmployeeFields(_emp);
                if(employee != null)
                    _employees.add(employee);

            }

        }
        catch(IOException e)
        {
            System.out.println("Error reading file");
        }
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
    public void WriteAll(ArrayList<Employee> employees)
    {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(_fileLocation)))
        {
            for(Employee employee : employees)
            {
                String newData = String.format("%d-%s-%s-%s-%s-%.2f",employee.GetIdNumber(),employee.GetFirstName(),employee.GetLastName(),
                        employee.GetJobTitle(), employee.GetLocation(),employee.GetSalary());;

                bw.write(newData);
                bw.newLine();
            }

//            ReadToArrayList();
        }
        catch(IOException e)
        {
            System.out.println("Error writing to file");
        }
    }


    //</editor-fold>


}
