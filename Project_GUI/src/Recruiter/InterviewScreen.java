package Recruiter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.ConnectionUtils;

import java.sql.*;

public class InterviewScreen {
    private ObservableList jobIds = FXCollections.observableArrayList();
    private ObservableList candidates = FXCollections.observableArrayList();
    private ObservableList interId = FXCollections.observableArrayList();

    String user, query, id, selected_id;
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet resultSet;

    {
        resultSet = null;
    }

    @FXML
    private TextField txtStarted;
    @FXML
    private TextField txtFinished;
    @FXML
    private TextField txtPers;
    @FXML
    private TextField txtEduc;
    @FXML
    private TextField txtExp;
    @FXML
    private ComboBox<String> comboBoxIdIn;
    @FXML
    private ComboBox<String> comboBoxCand;
    @FXML
    private ComboBox<String> comboBoxInterview;
    @FXML
    private TextField txtUpRecUsername;
    @FXML
    private TextField txtUpJobId;
    @FXML
    private TextField txtUpCandUsername;
    @FXML
    private TextField txtUpCreatedAt;
    @FXML
    private TextField txtUpStarted;
    @FXML
    private TextField txtUpFinished;
    @FXML
    private TextField txtUpDuration;
    @FXML
    private TextField txtUpRers;
    @FXML
    private TextField txtUpExp;
    @FXML
    private TextField txtUpEdu;
    @FXML
    private TextField txtUpId;


    @FXML
    void btnDelete(MouseEvent event) {
        try {
            openConnection();
            query = "DELETE FROM interviews WHERE id = ?";
            statement = con.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
            clearInterviewFieldsOnUpdate();
            alertUser("The interview deleted successfully");
        }catch (Exception e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }finally {
            closeConnection();
        }
    }


    @FXML
    void btnUpdate(MouseEvent event) {
        try {
            openConnection();
            query = "UPDATE interviews SET start_time = ?, finish_time = ?, " +
                    "personality_score = ?, education_score = ?, experience_score = ? WHERE id = ?";
            statement = con.prepareStatement(query);
            statement.setInt(6, Integer.parseInt(id));
            statement.setString(1, txtUpStarted.getText());
            statement.setString(2, txtUpFinished.getText());
            statement.setString(3, txtUpRers.getText());
            statement.setString(4, txtUpEdu.getText());
            statement.setString(5, txtUpExp.getText());
            statement.executeUpdate();

            clearInterviewFieldsOnUpdate();
            alertUser("The interview updated successfully");
        }catch (Exception e){
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }finally {
            closeConnection();
        }
    }

    @FXML
    void btnSave(MouseEvent event) {
        addNewInterview();
        alertUser("The interview added successfully");
    }

    @FXML
    void btnLoadIds(MouseEvent event) {
        fillComboBoxId();
    }


    @FXML
    void btnLoadIdForInterviews(MouseEvent event) {
        fillInterIds();
    }

    @FXML
    void btnLoadInterview(MouseEvent event) {
        try {
            openConnection();
            id = String.valueOf(comboBoxInterview.getSelectionModel().getSelectedItem());
            query = "SELECT * FROM interviews WHERE id = ?";
            statement = con.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(id));
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                txtUpId.setText(resultSet.getString("id"));
                txtUpJobId.setText(resultSet.getString("job_id"));
                txtUpRecUsername.setText(resultSet.getString("cand_username"));
                txtUpCandUsername.setText(resultSet.getString("recruiter_username"));
                txtUpCreatedAt.setText(resultSet.getString("created_at"));
                txtUpStarted.setText(resultSet.getString("start_time"));
                txtUpFinished.setText(resultSet.getString("finish_time"));
                txtUpDuration.setText(resultSet.getString("duration"));
                txtUpRers.setText(resultSet.getString("personality_score"));
                txtUpEdu.setText(resultSet.getString("education_score"));
                txtUpExp.setText(resultSet.getString("experience_score"));
            }
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @FXML
    void btnLoadTable(MouseEvent event) {

    }

    @FXML
    void btnTakeId(MouseEvent event) {
        try {
            selected_id = String.valueOf(comboBoxIdIn.getSelectionModel().getSelectedItem());
            fillComboBoxCand();
        } catch (Exception e) {
            return;
        }

    }

    private void fillInterIds() {
        interId.clear();
        openConnection();
        try {
            query = "SELECT * FROM interviews WHERE recruiter_username = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, user);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                interId.add(resultSet.getInt("id"));
            }

        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }
        comboBoxInterview.getItems().clear();
        comboBoxInterview.getItems().setAll(interId);
    }

    private void addNewInterview() {
        try {
            openConnection();
            id = String.valueOf(comboBoxIdIn.getSelectionModel().getSelectedItem());
            query = "INSERT INTO interviews (job_id, cand_username, recruiter_username, start_time,   " +
                    "finish_time, personality_score , education_score, experience_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            statement = con.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(id));
            statement.setString(2, comboBoxCand.getSelectionModel().getSelectedItem());
            statement.setString(3, user);
            statement.setString(4, txtStarted.getText());
            statement.setString(5, txtFinished.getText());
            if (txtExp.getText().equals("")) {
                txtExp.setText("-1");
            }
            if (txtEduc.getText().equals("")) {
                txtEduc.setText("-1");
            }
            if (txtPers.getText().equals("")) {
                txtPers.setText("-1");
            }
            statement.setInt(6, Integer.parseInt(txtPers.getText()));
            statement.setInt(7, Integer.parseInt(txtEduc.getText()));
            statement.setInt(8, Integer.parseInt(txtExp.getText()));
            statement.execute();

        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void fillComboBoxId() {
        jobIds.clear();
        try {
            openConnection();
            query = "SELECT * FROM job WHERE recruiter = ? AND submission_date <= CURDATE()";
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
        comboBoxIdIn.getItems().clear();
        comboBoxIdIn.getItems().setAll(jobIds);
    }

    private void fillComboBoxCand() {
        candidates.clear();
        try {
            openConnection();
            query = "SELECT * FROM applies WHERE job_id = ?";
            statement = con.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(selected_id));
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                candidates.add(resultSet.getString("cand_usrname"));
            }
        } catch (Exception e) {
            return;
        } finally {
            closeConnection();
        }
        comboBoxCand.getItems().clear();
        comboBoxCand.getItems().setAll(candidates);
    }

    /*We were't able to call the store procedure from java but our store
    *procedure runs perfectly in mysql Workbench */
//    public void callStoreProcedure(){
//        try {
//            openConnection();
//            CallableStatement call = con.prepareCall("{ call getInterviewsAboutJob(?, ?) }");
//            call.setInt(1, 2);
//            call.registerOutParameter(2, Types.VARCHAR);
//            call.execute();
//            resultSet = call.getResultSet();
//            boolean isResultSet = call.execute();
//            int updateCount = call.getUpdateCount();
//
//            while (isResultSet || updateCount != -1) {
//                if (isResultSet) {
//                    resultSet = call.getResultSet();
//                    System.out.println(resultSet.getString(2));
//                }
//                isResultSet = call.getMoreResults();
//                updateCount = call.getUpdateCount();
//            }
//            } catch(SQLException e){
//                e.printStackTrace();
//            }
//
//        }

        public void setName(String name) {
        this.user = name;
    }

    private void alertUser(String mess) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.showAndWait();
    }

    private void clearInterviewFieldsOnUpdate() {
        txtUpDuration.setText("");
        txtUpExp.setText("");
        txtUpRers.setText("");
        txtUpFinished.setText("");
        txtUpCreatedAt.setText("");
        txtUpCandUsername.setText("");
        txtUpRecUsername.setText("");
        txtUpJobId.setText("");
        txtUpId.setText("");
        txtUpStarted.setText("");
        txtUpEdu.setText("");
    }


    private void openConnection() {
        con = ConnectionUtils.conDB();
        System.out.println("A connection open");
    }


    private void closeConnection(){
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
