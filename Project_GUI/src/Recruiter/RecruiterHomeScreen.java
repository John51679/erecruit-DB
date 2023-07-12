package Recruiter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ConnectionUtils;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class RecruiterHomeScreen {

    public String user;
    private ObservableList<Jobs> jobs = FXCollections.observableArrayList();
    private ObservableList jobIds = FXCollections.observableArrayList();

    @FXML
    private Button compInfo;
    @FXML
    private Button accInfo;
    @FXML
    private Button JobPositions;
    @FXML
    private Button addJob;
    @FXML
    private TextField txtSubDate;
    @FXML
    private TextField txtStartDate;
    @FXML
    private TextField txtSalary;
    @FXML
    private TextArea txtPosition;
    @FXML
    private TextArea txtEdra;
    @FXML
    private TextArea txtField;
    @FXML
    private ComboBox<String> comboBoxId;
    @FXML
    private TableView<Jobs> jobTable;
    @FXML
    private TableColumn<Jobs, Integer> columnId;
    @FXML
    private TableColumn<Jobs, String> columnPosition;
    @FXML
    private TableColumn<Jobs, String> columnEdra;
    @FXML
    private TableColumn<Jobs, String> columnRecr;
    @FXML
    private TableColumn<Jobs, Double> columnSalary;
    @FXML
    private TableColumn<Jobs, String> columnStartingDay;
    @FXML
    private TableColumn<Jobs, String> columnAnnDay;
    @FXML
    private TableColumn<Jobs, String> columnSubDay;
    @FXML
    private TableColumn<Jobs, String> columnField;
    @FXML
    private TableColumn<?,String> columnStatus;

    String query;
    String id, fieldsOld, fieldsNew;
    String[] valuesOld;
    String[] valuesNew;
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet resultSet;

    {
        resultSet = null;
    }

    @FXML
    void btnAcccInfo(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AccInfo.fxml"));
        Parent popup1 = loader.load();
        AccInfo acc = loader.getController();
        acc.populateAccountInfo(user);
        Stage popupStage1 = new Stage();
        popupStage1.initStyle(StageStyle.DECORATED);
        popupStage1.setTitle("Info");
        popupStage1.setScene(new Scene(popup1));
        popupStage1.show();
    }


    @FXML
    void btnCompanyInfo(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompInfo.fxml"));
        Parent popup2 = loader.load();
        CompInfo comp = loader.getController();
        comp.populateCompanyInfo(user);
        Stage popupStage2 = new Stage();
        popupStage2.initStyle(StageStyle.DECORATED);
        popupStage2.setTitle("Info");
        popupStage2.setScene(new Scene(popup2));
        popupStage2.show();
    }

    @FXML
    void btnInterviews(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InterviewScreen.fxml"));
        Parent popup3 = loader.load();
        InterviewScreen inter = loader.getController();
        inter.setName(user);
        Stage popupStage3 = new Stage();
        popupStage3.initStyle(StageStyle.DECORATED);
        popupStage3.setTitle("Info");
        popupStage3.setScene(new Scene(popup3));
        popupStage3.show();
    }

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
    }


    @FXML
    void btnAddJob(MouseEvent event) {
        if (validateFieldsJob()) try {
            addNewJob();
            alertUser();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void btnLoad(MouseEvent event) {
        id = String.valueOf(comboBoxId.getSelectionModel().getSelectedItem());
        try {
            openConnection();
            query = "SELECT start_date, salary, position, edra, submission_date, GROUP_CONCAT(requires.antikeim_title SEPARATOR ', ') AS 'antikeim_title' FROM job " +
                    "INNER JOIN requires ON requires.job_id = job.id WHERE id = ?";
            statement = con.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(id));
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                txtStartDate.setText(resultSet.getString("start_date"));
                txtPosition.setText(resultSet.getString("position"));
                txtSubDate.setText(resultSet.getString("submission_date"));
                txtField.setText(resultSet.getString("antikeim_title"));
                txtEdra.setText(resultSet.getString("edra"));
                txtSalary.setText(String.valueOf(resultSet.getFloat("salary")));

                fieldsOld = txtField.getText();
                valuesOld = fieldsOld.split(", ");
            }
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }


    @FXML
    void btnSave(MouseEvent event) {
        if (validateFieldsJob()) try {
            editJob();
            alertUserOnSave();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void btnShowAllJobs(MouseEvent event) {
        try {
            jobIds.clear();
            jobTable.getItems().clear();
            String firm = "";
            openConnection();
            query = "SELECT * FROM recruiter WHERE  username = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, user);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                firm = resultSet.getString("firm");
            }
            String fetchLogData = "SELECT id, start_date, salary, position, edra, recruiter, announce_date, submission_date, " +
                                    "GROUP_CONCAT(requires.antikeim_title SEPARATOR ', ') AS 'antikeim_title' FROM requires " +
                                    "INNER JOIN job ON requires.job_id = job.id INNER JOIN ( Select username FROM recruiter " +
                                    "where firm = ?) AS recr ON recruiter = recr.username GROUP BY job_id";

            statement = con.prepareStatement(fetchLogData);
            statement.setString(1, firm);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                jobs.add(new Jobs(
                        resultSet.getInt("id"),
                        resultSet.getString("start_date"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("position"),
                        resultSet.getString("edra"),
                        resultSet.getString("recruiter"),
                        resultSet.getString("announce_date"),
                        resultSet.getString("submission_date"),
                        resultSet.getString("antikeim_title")
                ));
            }
        } catch (Exception e) {
            System.err.println("Got an exception on list view!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }

        showContent();
    }


    @FXML
    void btnYourJobs(MouseEvent event) {
        try {
            jobIds.clear();
            jobTable.getItems().clear();
            openConnection();
            query = "SELECT id, start_date, salary, position, edra, recruiter, announce_date, submission_date, GROUP_CONCAT(requires.antikeim_title SEPARATOR ', ') AS 'antikeim_title' FROM requires" +
                    " INNER JOIN job ON requires.job_id = job.id WHERE recruiter = ? GROUP BY job_id";

            statement = con.prepareStatement(query);
            statement.setString(1, user);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                jobs.add(new Jobs(
                        resultSet.getInt("id"),
                        resultSet.getString("start_date"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("position"),
                        resultSet.getString("edra"),
                        resultSet.getString("recruiter"),
                        resultSet.getString("announce_date"),
                        resultSet.getString("submission_date"),
                        resultSet.getString("antikeim_title")
                ));
            }
        } catch (Exception e) {
            System.err.println("Got an exception on list view!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }

        showContent();
    }

    private void showContent() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAnnDay.setCellValueFactory(new PropertyValueFactory<>("announce_date"));
        columnStartingDay.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        columnEdra.setCellValueFactory(new PropertyValueFactory<>("edra"));
        columnSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        columnSubDay.setCellValueFactory(new PropertyValueFactory<>("submission_date"));
        columnPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        columnRecr.setCellValueFactory(new PropertyValueFactory<>("recruiter"));
        columnField.setCellValueFactory(new PropertyValueFactory<>("antikeim_title"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        jobTable.setItems(null);
        jobTable.setItems(jobs);
    }

    private void fillComboBox() {
        jobIds.clear();
        try {
            openConnection();
            query = "SELECT * FROM job WHERE recruiter = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, user);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                jobIds.add(resultSet.getInt("id"));
            }
        } catch (Exception e) {
            System.err.println("Got an exception on list view!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }
        comboBoxId.getItems().clear();
        comboBoxId.getItems().setAll(jobIds);
    }

    private void addNewJob() {
        try {
            String subDate = txtSubDate.getText();
            String startDate = txtStartDate.getText();
            Double salary = Double.parseDouble(txtSalary.getText());
            String position = txtPosition.getText();
            String edra = txtEdra.getText();
            String field = txtField.getText();
            int id = -1;

            openConnection();
            query = " INSERT INTO job (start_date, salary, position, edra, recruiter, submission_date)"
                    + " VALUES (?, ?, ?, ?, ?, ?)";

            statement = con.prepareStatement(query);
            statement.setString(1, startDate);
            statement.setDouble(2, salary);
            statement.setString(3, position);
            statement.setString(4, edra);
            statement.setString(5, user);
            statement.setString(6, subDate);
            statement.execute();

            query = "SELECT id FROM job WHERE start_date = ? and recruiter = ? AND edra = ? AND position = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, startDate);
            statement.setString(2, user);
            statement.setString(3, edra);
            statement.setString(4, position);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            valuesNew = field.split(", ");
            for (int j = 0; j < valuesNew.length; j++) {
                System.out.println(valuesNew[j]);

                query = " INSERT INTO requires (job_id, antikeim_title)"
                        + " VALUES (?, ?)";

                statement = con.prepareStatement(query);
                statement.setInt(1, id);
                statement.setString(2, valuesNew[j]);
                statement.execute();
            }
            closeConnection();
            fillComboBox();
            clearJobFields();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    private void editJob(){
        try {
            openConnection();
            query = "UPDATE job SET start_date = ?, salary = ?, position = ?, edra = ?, submission_date = ?" +
                    "WHERE id = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, (String) txtStartDate.getText());
            statement.setDouble(2, Double.parseDouble(txtSalary.getText()));
            statement.setString(3, (String) txtPosition.getText());
            statement.setString(4, (String) txtEdra.getText());
            statement.setString(5, (String) txtSubDate.getText());
            statement.setInt(6, Integer.parseInt(id));
            statement.executeUpdate();

            fieldsNew = txtField.getText();
            valuesNew = fieldsNew.split(", ");

            for (int j = 0; j < valuesNew.length; j++) {
                if (Arrays.stream(valuesOld).anyMatch(valuesNew[j]::equals)) {
                    continue;
                } else {
                    String query = "INSERT INTO requires (job_id, antikeim_title)"
                            + "VALUES (?, ?)";
                    statement = con.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(id));
                    statement.setString(2, valuesNew[j]);
                    statement.execute();
                }
            }

            for (int j = 0; j < valuesOld.length; j++) {
                if (Arrays.stream(valuesNew).anyMatch(valuesOld[j]::equals)) {
                    continue;
                } else {
                    query = "DELETE FROM requires WHERE job_id = ? AND antikeim_title = ?";
                    statement = con.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(id));
                    statement.setString(2, valuesOld[j]);
                    statement.execute();
                }
            }

        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void clearJobFields() {
        txtField.setText("");
        txtEdra.setText("");
        txtPosition.setText("");
        txtSalary.setText("");
        txtSubDate.setText("");
        txtStartDate.setText("");
    }

    private boolean validateFieldsJob() {

        if (txtStartDate.getText().isEmpty() | txtSubDate.getText().isEmpty() | txtSalary.getText().isEmpty()
                | txtPosition.getText().isEmpty() | txtEdra.getText().isEmpty() | txtField.getText().isEmpty()) {
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
        alert.setContentText("New Job added successfully");
        alert.showAndWait();
    }

    private void alertUserOnSave() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("User's information updated successfully");
        alert.showAndWait();
    }

    private void openConnection() {
        con = ConnectionUtils.conDB();
        System.out.println("A connection open");
    }

    public void setName(String name) {
        this.user = name;
        fillComboBox();
    }

    private void closeConnection() {
        try {
            con.close();
            System.out.println("A connection closed");
        } catch (SQLException e) {
            System.err.println("Got an exception!");
        }
        try {
            statement.close();
        } catch (SQLException e) {
            System.err.println("Got an exception!");
        }
        try {
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Got an exception!");
        }
    }
}
