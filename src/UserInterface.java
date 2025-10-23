import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.sql.*;


/// Do List
/// 1. Load File bveing correct, and if incorrect exiting
/// 2. Upon the FIRST Succesful load, show everyone
/// 3. See All button Shows all users
/// 4. Search Button
///     4.1 ID
///     4.2 Location
///     4.3 Position
/// 4.4 if nothing is real, will them flash a warning side
/// 5 Double Clicking a user will allow Title location and salary to be updated

public class UserInterface extends JFrame
{
    FileOperations fileOperations;
    DatabaseOperations databaseOperations;
    ArrayList<Employee> employees;

    JTextField filePathField ;
    JButton getFilePathButton;

    boolean systemHasValidFileLocation;

    Employee editingEmployee;

    //Create Center STuff
    private JTable table;
    private DefaultTableModel tableMOdel;


    //Search fields
    JTextField searchField ;
    JButton searchButton;
    JButton seeAllButton;

    JRadioButton idButton;
    JRadioButton locationButton ;
    JRadioButton positionButton;
    ButtonGroup searchGroup;


    //Editing PANE
    JTextField[] fields;
    JButton saveUserButton;
    JButton clearUserButton;
    String[] employeeDatalabels;
    JButton deleteUserButton;


    /**
     * Constructor to build the UI
     */
    public UserInterface()
    {
        setTitle("Employee Database management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        //<editor-fold desc="Build HEader">

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        //</editor-fold>



        //<editor-fold desc="Load Data area">
        JPanel loadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePathField= new JTextField(80);
        getFilePathButton = new JButton("Load File");

        loadPanel.add(getFilePathButton);
        loadPanel.add(filePathField);

        getFilePathButton.addActionListener(e -> GetFilePathData());

        //</editor-fold>



        //<editor-fold desc="Create The Center Display Table">


        String[] columnNames = {"ID", "Last Name", "First Name", "Position", "Location", "Salary"};

        tableMOdel  = new DefaultTableModel(columnNames, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table = new JTable(tableMOdel);
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);

        JScrollPane tableScroll = new JScrollPane(table);
        add(tableScroll, BorderLayout.CENTER);


        table.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event)
            {
                int tableRow = table.rowAtPoint(event.getPoint());
                int tableColumn = table.columnAtPoint(event.getPoint());
                if(tableColumn > 0)
                {
                    FlashWarningMessage("You need to double click the ID field to edit a user");
                    return;
                }
                if(event.getClickCount() == 2)
                {
                    String[] tableRowData = new String[table.getColumnCount()];
                    for ( int i = 0; i < table.getColumnCount(); i++ )
                        tableRowData[i] = table.getValueAt(tableRow, i).toString();


                    //Make into an employee
                    Employee employee =
                            new Employee( Integer.parseInt(tableRowData[0]),
                                          tableRowData[1],
                                          tableRowData[2],
                                          tableRowData[3],
                                          tableRowData[4],
                                          Double.parseDouble(tableRowData[5]));

                    //Now Pass Data to a helper method.
                    PopulateEditPane(employee);
                }
            }

        });

        //</editor-fold>

        //<editor-fold desc="Search Build">
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        JPanel searchInputField = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchBarlabel = new JLabel("Search:");
        searchField = new JTextField(20);

        searchButton = new JButton("Search");
        seeAllButton = new JButton("See All");

        searchInputField.add(searchBarlabel);
        searchInputField.add(searchField);
        searchInputField.add(searchButton);
        searchInputField.add(seeAllButton);

        searchButton.addActionListener(e -> SearchButtonPress());
        seeAllButton.addActionListener(e -> SeeAllButtonPress());

        //<editor-fold desc="Radio Search Buttons">
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        locationButton = new JRadioButton("Location");
        positionButton = new JRadioButton("Position");
        searchGroup = new ButtonGroup();
        searchGroup.add(idButton);
        searchGroup.add(locationButton);
        searchGroup.add(positionButton);

        radioPanel.add(locationButton);
        radioPanel.add(positionButton);

        //</editor-fold>

        searchPanel.add(searchInputField);
        searchPanel.add(radioPanel);

        //</editor-fold>



        // Add All Stuff to header
        headerPanel.add(loadPanel);
        headerPanel.add(searchPanel);
        add(headerPanel, BorderLayout.NORTH);





        //<editor-fold desc="Edit the Employee Area">

        JPanel leftPanel = new  JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.insets = new Insets(5,5,5,5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;

        employeeDatalabels = new String[] {"ID", "Last Name", "First Name", "Position", "Location", "Salary"};
        fields = new JTextField[employeeDatalabels.length];

        for (int i = 0; i < employeeDatalabels.length; i++)
        {
            gridBagConstraints.gridx = 0;
            JLabel label = new JLabel(employeeDatalabels[i]);
            leftPanel.add(label, gridBagConstraints);

            gridBagConstraints.gridx = 1;
            fields[i] = new JTextField(15);
            leftPanel.add(fields[i], gridBagConstraints);



//            if(employeeDatalabels[i].equalsIgnoreCase("id"))
//                fields[i].setEditable(false);

            gridBagConstraints.gridy++;
        }

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        clearUserButton = new JButton("Clear User");
        clearUserButton.addActionListener(e -> ClearUserInfo());
        leftPanel.add(clearUserButton,gridBagConstraints);

        //gridBagConstraints.gridy++;

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        saveUserButton = new JButton("Save");
        saveUserButton.addActionListener(e -> UpdateUserInfo());
        leftPanel.add(saveUserButton,gridBagConstraints);


        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy++;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(e -> DeleteUser());
        leftPanel.add(deleteUserButton,gridBagConstraints);


        add(leftPanel, BorderLayout.WEST);
        //</editor-fold>

    }

    /**
     * verifies File path and instantiates the fileOperations and databaseOperations instances
     */
    private void GetFilePathData()
    {
        String filePathText =  filePathField.getText();
        if(filePathText.isEmpty())
            FlashWarningMessage("You need to put in a valid file path");

        systemHasValidFileLocation = Files.exists(Paths.get(filePathText));
        if(systemHasValidFileLocation)
        {
            fileOperations = new FileOperations(filePathText);
            databaseOperations = new DatabaseOperations(fileOperations);


            FillCenterData(databaseOperations.GetAllEmployees());
        }
        else
        {
            FlashWarningMessage("No File exists at the supplied pathway");
        }
    }

    /**
     * Helper Method to fill the Center Data Field
     * @param employees Pass in the Array that is going to populate the CenterData
     */
    private void FillCenterData(ArrayList<Employee> employees)
    {

        tableMOdel.setRowCount(0);
        employees.sort((id1,id2) -> Integer.compare(id1.GetIdNumber(), id2.GetIdNumber()));

        for (Employee employee : employees)
        {
            Object [] centerTableRows =
            {
                    employee.GetIdNumber(),
                    employee.GetLastName(),
                    employee.GetFirstName(),
                    employee.GetJobTitle(),
                    employee.GetLocation(),
                    employee.GetSalary()
            };
            tableMOdel.addRow(centerTableRows);
        };
    }

    /**
     * Updates the selected Users data, then clears the Data fields
     */
    private void UpdateUserInfo()
    {
        if(!systemHasValidFileLocation)
        {
            FlashWarningMessage("No File exists at the supplied pathway");
            return;
        }

        if (fields[0].getText().isEmpty() || !fields[0].getText().matches("\\d+"))
        {
            FlashWarningMessage("Please enter a valid ID");
            return;
        }

        if(!databaseOperations.EmployeeExists(Integer.parseInt(fields[0].getText())) && !fields[0].getText().isEmpty())
        {
            editingEmployee = new Employee(Integer.parseInt(fields[0].getText()),"","","","",0);
        }

        if(editingEmployee == null)
        {
            FlashWarningMessage("You need to select an employee");
            return;
        }

//        if(databaseOperations.EmployeeExists(Integer.parseInt(fields[0].getText())))
//        {
            for (int i = 1; i < employeeDatalabels.length ; i++)
            {
                String alteredEmployeeData = fields[i].getText();

                if (alteredEmployeeData.isEmpty() )
                {
                    FlashWarningMessage("You need to Enter correctly formatted data for: " + employeeDatalabels[i]);
                    return;
                }


                if(i < 5)
                {
                    if(!databaseOperations.EmployeeStringDataIsCorrectlyFormatted(alteredEmployeeData))
                    {
                        FlashWarningMessage("You need to Enter correctly formatted data for: " + employeeDatalabels[i]);
                        return;
                    }
                }
                else
                {
                    try
                    {
                        if(databaseOperations.VerifySalaryIsDoubles(fields[5].getText()) < 0)
                        {
                            FlashWarningMessage("You need to Enter correctly formatted data for: " + employeeDatalabels[i]);
                            return;
                        }
                    }
                    catch (Exception e)
                    {
                        FlashWarningMessage("You need to Enter correctly formatted data for: " + employeeDatalabels[i]);
                        return;
                    }

                }



            }

            editingEmployee.SetLastName(fields[1].getText());
            editingEmployee.SetFirstName(fields[2].getText());
            editingEmployee.SetLocation(fields[3].getText());
            editingEmployee.SetJobTitle(fields[4].getText());
            editingEmployee.SetSalary(Double.parseDouble(fields[5].getText()));


            if (!databaseOperations.EmployeeExists(Integer.parseInt(fields[0].getText())))
            {
                if(!databaseOperations.AddEmployee(editingEmployee))
                    FlashWarningMessage("Employee addition failed Clearing System");
                else
                    FillCenterData(databaseOperations.GetAllEmployees());


                ClearEditingEmployeeAndUpdateField();
            }
            else
            {
                if(!databaseOperations.UpdateEmployee(editingEmployee))
                    FlashWarningMessage("Employee update failed Clearing System");
                else
                    FillCenterData(databaseOperations.GetAllEmployees());


                ClearEditingEmployeeAndUpdateField();
            }


//        }
//        else
//        {
//
//        }



    }

    /**
     * Extra helper method to clear user data and error check
     */
    private void ClearUserInfo()
    {
        if(!systemHasValidFileLocation)
        {
            FlashWarningMessage("No File exists at the supplied pathway");
            return;
        }

        if(editingEmployee == null)
        {
            FlashWarningMessage("No Data to clear");
            return;
        }

        ClearEditingEmployeeAndUpdateField();
    }

    /**
     * Clear out editing Employee obj, and set Text to empty in JFields
     */
    private void ClearEditingEmployeeAndUpdateField()
    {
        editingEmployee = null;

        for (int i = 0; i < fields.length; i++)
            fields[i].setText("");

        if(fields[0].getText().isEmpty())
            fields[0].setEditable(true);
    }

    /**
     * Populates the Jpanels for user editing
     * @param employee this is the employee that we want to populate the eidting Pane with
     */
    private void PopulateEditPane(Employee employee)
    {
        editingEmployee = employee;
        //Need to populate the left side fields, and only let certain be able to be edited
        fields[0].setText(String.valueOf(editingEmployee.GetIdNumber()));
        fields[1].setText(editingEmployee.GetLastName());
        fields[2].setText(editingEmployee.GetFirstName());
        fields[3].setText(editingEmployee.GetJobTitle());
        fields[4].setText(editingEmployee.GetLocation());
        fields[5].setText(String.valueOf(editingEmployee.GetSalary()));

        fields[0].setEditable(fields[0].getText().isEmpty());
    }

    /**
     * Flash warning message when incorrect formatted data
     * @param message This is teh message you want displayed
     */
    private void FlashWarningMessage(String message)
    {
        JOptionPane.showMessageDialog(
                this,
                message
        );
    }

    /**
     * Searches based on criteria selected
     */
    private void SearchButtonPress()
    {

        ArrayList<Employee> employees ;
        if(!systemHasValidFileLocation)
        {
            FlashWarningMessage("No File exists at the supplied pathway");
            return;
        }


        if(searchField.getText().isEmpty())
        {
            FlashWarningMessage("You need to enter search parameters");
            return;
        }



        employees = databaseOperations.SortEmployees(searchField.getText(),locationButton.isSelected());

        if (employees.isEmpty())
            FlashWarningMessage("No Matches on your search parameters");
        else
            FillCenterData(employees);


    }

    /**
     * Lets user return to seeing all employees
     */
    private void SeeAllButtonPress()
    {
        if(!systemHasValidFileLocation)
            FlashWarningMessage("No File exists at the supplied pathway");
        else
            FillCenterData(databaseOperations.GetAllEmployees());
    }

    /**
     * This will delete the currently selected user
     */
    private void DeleteUser()
    {

        if(!systemHasValidFileLocation)
        {
            FlashWarningMessage("No File exists at the supplied pathway");
            return;
        }
        if (fields[0].getText().isEmpty())
        {
            FlashWarningMessage("There is no Employee to delete");
        }
        else
        {
            if(!databaseOperations.DeleteEmployee(Integer.parseInt(fields[0].getText())))
            {
                FlashWarningMessage("Employee deletion failed");
                ClearUserInfo();
            }
            else
            {
                FillCenterData(databaseOperations.GetAllEmployees());
                ClearUserInfo();
            }



        }
    }



}
