package Recruiter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccInfo {

    private String user;
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet resultSet;
    {
        resultSet = null;
    }

    @FXML
    private TextField txtFname;

    @FXML
    private TextField txtLname;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtRegDate;

    @FXML
    private TextField txtFirm;

    @FXML
    private TextField txtExp;

    @FXML
    private Button save;

    @FXML
    void btnSave(MouseEvent event) {
        try {
            openConnection();

            String query = "UPDATE user SET usr_password = ?, first_name = ?, surname = ?, email = ?" +
                    "WHERE username = ?";

            statement = con.prepareStatement(query);
            statement.setString(1, txtPassword.getText());
            statement.setString(2, txtFname.getText());
            statement.setString(3, txtLname.getText());
            statement.setString(4, txtEmail.getText());
            statement.setString(5, txtUsername.getText());
            statement.executeUpdate();

            alertUser();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }finally {
            closeConnection();
        }
    }

    public void populateAccountInfo(String user) {
        try {
            openConnection();

            String fetchLogData = "SELECT * FROM user  WHERE username = ?";
            statement = con.prepareStatement(fetchLogData);
            statement.setString(1, user);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                txtFname.setText(resultSet.getString("first_name"));
                txtLname.setText(resultSet.getString("surname"));
                txtEmail.setText(resultSet.getString("email"));
                txtPassword.setText(resultSet.getString("usr_password"));
                txtUsername.setText(resultSet.getString("username"));
                txtRegDate.setText(resultSet.getString("reg_date"));
            }

            fetchLogData = "SELECT * FROM recruiter  WHERE username = ?";
            statement = con.prepareStatement(fetchLogData);
            statement.setString(1, user);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                txtFirm.setText(resultSet.getString("firm"));
                txtExp.setText(resultSet.getString("exp_years"));
            }

        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }finally {
            closeConnection();
        }
    }

    private void alertUser() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Account information has been updated successfully");
        alert.showAndWait();
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