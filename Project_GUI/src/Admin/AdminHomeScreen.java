package Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ConnectionUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminHomeScreen implements Initializable {
    private ObservableList list = FXCollections.observableArrayList();
    private ObservableList<Log> logData = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle rb) {
        fillListView();
    }

    @FXML
    private TextField txtFname;
    @FXML
    private TextField txtLname;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtExp;
    @FXML
    private TextField txtFirm;
    @FXML
    private TextField txtCertif;
    @FXML
    private TextField txtLetter;
    @FXML
    private TextArea txtCv;
    @FXML
    private Button createCand;
    @FXML
    private Button createRecr;
    @FXML
    private ListView<String> userList;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateUntil;
    @FXML
    private TextField txtFieldTitle;
    @FXML
    private TextField txtParentField;
    @FXML
    private TextArea txtxFieldDesc;
    @FXML
    private Button addField;
    @FXML
    private TextField txtSectorTitle;
    @FXML
    private TextField txtSectorParent;
    @FXML
    private TextArea txtSectorDesc;
    @FXML
    private Button addSector;
    @FXML
    private TableView<Log> LogTable;
    @FXML
    private TableColumn<Log, Integer> columnId;
    @FXML
    private TableColumn<Log, String> columnUsername;
    @FXML
    private TableColumn<Log, String> columnTime;
    @FXML
    private TableColumn<Log, String> columnType;
    @FXML
    private TableColumn<Log, String> columnTable;
    @FXML
    private TableColumn<Log, String> columnSuccess;

    /*Init the variables that we are constantly using in order to connect to the database*/
    String query;
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet resultSet;
    {
        resultSet = null;
    }


    /*When button Load is pressed the TableViews is populated with the content of the Log table
    *The output of the table depends on the date period so we have get the values of the datepicker
    *Also depends on the user who is being selected from the ListView
    * */
    @FXML
    void btnLoadTable(MouseEvent event) throws SQLException {
        logData.clear();
        LocalDate from = dateFrom.getValue();
        LocalDate until = dateUntil.getValue();
        String Sfrom = "", Suntil = "";
        if (from == null ) {
            Sfrom = "2017-04-06 7:59:10"; //Initialize with the oldest day in the database(not the proper way of doing that but is ok for our project)
            System.out.println(Sfrom);
        }else{
            Sfrom = from + " 00:00:00";
            System.out.println(Sfrom);
        }
        if (until == null ) {
            until = until.now(); //Initialize with the current time
            Suntil = until.toString();
            Suntil = Suntil + " 00:00:00";
            System.out.println(Suntil);
        }else{
            Suntil = until + " 00:00:00";
            System.out.println(Suntil);
        }
        String name = null;
        name = userList.getSelectionModel().getSelectedItem();
        openConnection();
        if (name != null) {
            try {
                String fetchLogData = "SELECT * FROM user_logs WHERE username = ? AND happened_at >= ? AND happened_at <= ?";

                statement = con.prepareStatement(fetchLogData);
                statement.setString(1, name);
                statement.setString(2, Sfrom);
                statement.setString(3,Suntil);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    logData.add(new Log(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("happened_at"),
                            resultSet.getString("action_type"),
                            resultSet.getString("table_name"),
                            resultSet.getString("success")
                    ));
                }
            } catch (Exception e) {
                System.err.println("Got an exception on list view!");
                System.err.println(e.getMessage());
            }
        }else {
            try {
                String fetchLogData = "SELECT * FROM user_logs WHERE happened_at >= ? AND happened_at <= ?";
                statement = con.prepareStatement(fetchLogData);
                statement.setString(1, Sfrom);
                statement.setString(2,Suntil);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    logData.add(new Log(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("happened_at"),
                            resultSet.getString("action_type"),
                            resultSet.getString("table_name"),
                            resultSet.getString("success")
                    ));
                }
            } catch (Exception e) {
                System.err.println("Got an exception on list view!");
                System.err.println(e.getMessage());
            }
        }

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("happened_at"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("action_type"));
        columnTable.setCellValueFactory(new PropertyValueFactory<>("table_name"));
        columnSuccess.setCellValueFactory(new PropertyValueFactory<>("success"));
        LogTable.setItems(null);
        LogTable.setItems(logData);
        closeConnection();
    }

    /*When the 'Create Candidate' buttons is pressed we call the insertNewCandidate() method
    * in order to add a new candidate to the database after we have validate that all the
    * appropriate fields is completed*/
    @FXML
    void btnCreateCandidate(MouseEvent event) {
        if (validateFieldsForCandidate()) try {
            insertNewCandidate();
            alertUser();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    /*When the 'Create Recruiter' buttons is pressed we call the insertNewRecruiter() method
     * in order to add a new candidate to the database after we have validate that all the
     * appropriate fields is completed*/
    @FXML
    void btnCreateRecruiter(MouseEvent event) {
        if (validateFieldsForRecruiter()) try {
            insertNewRecruiter();
            alertUser();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }

    }

    /*Whem the logout is pressed the Admin Panel closes and the user goes back to the Login Screen,
    also we make sure that the connection with the database is close*/
    @FXML
    void btnlogout(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Parent prevScreen = null;
        try {
            prevScreen = FXMLLoader.load(getClass().getResource("../MultiUserLoginForm/LoginForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setTitle("Login");
        newStage.setScene(new Scene(prevScreen));
        newStage.show();
        closeConnection();
    }

    @FXML
    void btnAddSector(MouseEvent event) {
        if (validateFieldsForSector()) try {
            addNewSector();
            alertUser();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void btnAddField(MouseEvent event) {
        if (validateFieldsForNewField()) try {
            openConnection();
            addNewField();
            alertUser();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        closeConnection();
    }

    /*This method is the first one that is called from the initializer in order to populate the
    * listView with the username*/
    private void fillListView() {
        list.clear();
        try {
            openConnection();
            String fetchUsers = "SELECT username FROM user ORDER BY username";
            statement = con.prepareStatement(fetchUsers);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("username"));
            }
        } catch (Exception e) {
            System.err.println("Got an exception on list view!");
            System.err.println(e.getMessage());
        }
        closeConnection();
        userList.getItems().clear();
        userList.getItems().addAll(list);
    }

    /*This method inserts a new candidate to our database*/
    private void insertNewCandidate() {
        try {
            /*Get from textFields the values that is needed in order to insert a user successfully*/
            String fname = txtFname.getText();
            String lname = txtLname.getText();
            String email = txtEmail.getText();
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String refLetter = txtLetter.getText();
            String cv = txtCv.getText();
            String certif = txtCertif.getText();

            openConnection();
            String query = " INSERT INTO user (username, usr_password, first_name, surname, email)"
                    + " VALUES (?, ?, ?, ?, ?)";

            statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fname);
            statement.setString(4, lname);
            statement.setString(5, email);
            statement.execute();

            query = " INSERT INTO candidate (username, bio, sistatikes, certificates)"
                    + " VALUES (?, ?, ?, ?)";

            statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, cv);
            statement.setString(3, refLetter);
            statement.setString(4, certif);
            statement.execute();
            closeConnection();
            clearFields();
            userList.refresh();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    /*This method inserts a new recruiter to our database*/
    private void insertNewRecruiter() {
        try {
            /*Get from textFields the values that is needed in order to insert a user successfully*/
            String fname = txtFname.getText();
            String lname = txtLname.getText();
            String email = txtEmail.getText();
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            int experience = Integer.parseInt(txtExp.getText());
            String firm = txtFirm.getText();
            openConnection();
            query = " INSERT INTO user (username, usr_password, first_name, surname, email)"
                    + " VALUES (?, ?, ?, ?, ?)";

            statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fname);
            statement.setString(4, lname);
            statement.setString(5, email);
            statement.execute();

            query = " INSERT INTO recruiter (username, exp_years, firm)"
                    + " VALUES (?, ?, ?)";

            statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setInt(2, experience);
            statement.setString(3, firm);
            statement.execute();
            closeConnection();
            clearFields();
            fillListView();
            userList.refresh();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    private void addNewField() {
        try {
            String fieldTitle = txtFieldTitle.getText();
            String fieldParent = txtParentField.getText();
            String fieldDesc = txtxFieldDesc.getText();

            String query = "INSERT INTO antikeim (title, descr, belongs_to)"
                    + "VALUES (?, ?, ?)";
            statement = con.prepareStatement(query);
            statement.setString(1, fieldTitle);
            statement.setString(3, fieldDesc);
            statement.setString(2, fieldParent);
            statement.execute();
            clearAddNewFields();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    private void addNewSector() {
        try {
            String sectorTitle = txtSectorTitle.getText();
            String sectorParent = txtSectorParent.getText();
            String sectorDesc = txtSectorDesc.getText();

            String query = "INSERT INTO sections (title, descr, belong_to)"
                    + "VALUES (?, ?, ?)";
            statement = con.prepareStatement(query);
            statement.setString(1, sectorTitle);
            statement.setString(2, sectorDesc);
            statement.setString(3, sectorParent);
            statement.execute();
            clearSectorFields();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    private boolean validateFieldsForCandidate() {

        if (txtFname.getText().isEmpty() | txtLname.getText().isEmpty() | txtEmail.getText().isEmpty() | txtUsername.getText().isEmpty() |
                txtPassword.getText().isEmpty() | txtLetter.getText().isEmpty() |
                txtCv.getText().isEmpty() | txtCertif.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please complete all the appropriate fields");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean validateFieldsForRecruiter() {
        if (txtFname.getText().isEmpty() | txtLname.getText().isEmpty() | txtEmail.getText().isEmpty() | txtUsername.getText().isEmpty() |
                txtPassword.getText().isEmpty() | txtExp.getText().isEmpty() | txtFirm.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please complete all the appropriate fields");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean validateFieldsForNewField() {

        if (txtFieldTitle.getText().isEmpty() | txtxFieldDesc.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please complete all the appropriate fields");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private boolean validateFieldsForSector() {

        if (txtSectorDesc.getText().isEmpty() | txtSectorTitle.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please complete all the appropriate fields");
            alert.showAndWait();
            return false;
        }
        return true;
    }


    private void alertUser() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("User has been created");
        alert.showAndWait();
    }

    private void clearAddNewFields() {
        txtxFieldDesc.setText("");
        txtFieldTitle.setText("");
        txtParentField.setText("");
    }

    private void clearSectorFields() {
        txtSectorDesc.setText("");
        txtSectorParent.setText("");
        txtSectorTitle.setText("");
    }

    private void clearFields() {
        txtFname.setText("");
        txtLname.setText("");
        txtEmail.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtExp.setText("");
        txtFirm.setText("");
        txtLetter.setText("");
        txtCv.setText("");
        txtCertif.setText("");
    }

    private void openConnection() {
        con = ConnectionUtils.conDB();
        System.out.println("A connection open");
    }

    private void closeConnection() {
        try {
            con.close();
            System.out.println("A connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}