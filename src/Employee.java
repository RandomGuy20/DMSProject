/**
 * This is th Employee object that the data will be structured and destructured into
 */
public class Employee
{


    //<editor-fold desc="Fields">
    private int _idNumber;
    private String _lastName;
    private String _firstName;
    private String _jobTitle;
    private String _location;
    private double _salary;
    //</editor-fold>








    //<editor-fold desc="Props">
    public Integer GetIdNumber()
    {
        return _idNumber;
    }
    public String GetLastName()
    {
        return _lastName;
    }
    public String GetFirstName()
    {
        return _firstName;
    }
    public String GetJobTitle()
    {
        return _jobTitle;
    }
    public String GetLocation()
    {
        return _location;
    }
    public double GetSalary()
    {
        return _salary;
    }
    //</editor-fold>





    //<editor-fold desc="Constructor">
    public Employee(int idNumber, String lastName, String firstName, String jobTitle, String location, double salary)
    {
        _idNumber = idNumber;
        _lastName = lastName;
        _firstName = firstName;
        _jobTitle = jobTitle;
        _location = location;
        _salary = salary;
    }
    //</editor-fold>



    //<editor-fold desc="Setters">
    public void SetNewIdNumber(int newIdNumber)
    {
        _idNumber = newIdNumber;
    }

    public void SetLastName(String newLastName)
    {
        _lastName = newLastName;
    }

    public void

    SetFirstName(String newFirstName)
    {
        _firstName = newFirstName;
    }

    public void SetJobTitle(String newJobTitle)
    {
        _jobTitle = newJobTitle;
    }

    public void SetLocation(String newLocation)
    {
        _location = newLocation;
    }

    public void SetSalary(double newSalary)
    {
        _salary = newSalary;
    }
    //</editor-fold>



    @Override
    public String toString ()
    {
        return String.format("%d %s %s %s %s %.2f", _idNumber, _firstName, _lastName, _jobTitle, _location, _salary);
    }


}
