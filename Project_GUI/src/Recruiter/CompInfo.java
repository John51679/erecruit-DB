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


public class CompInfo{

    private String user;
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet resultSet;
    {
        resultSet = null;
    }

    @FXML
    private TextField txtAfm;

    @FXML
    private TextField txtDoy;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtNum;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtCountry;

    @FXML
    private Button save;

    @FXML
    void btnSave(MouseEvent event){
        try {
            openConnection();

            String query = "UPDATE etaireia SET name = ?, tel = ?, street = ?, num = ?, city = ?, country = ?" +
                    "WHERE AFM = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, (String) txtName.getText());
            statement.setString(2, (String) txtTel.getText());
            statement.setString(3, (String) txtStreet.getText());
            statement.setString(4, (String) txtNum.getText());
            statement.setString(5, (String) txtCity.getText());
            statement.setString(6, (String) txtCountry.getText());
            statement.setString(7, (String) txtAfm.getText());
            statement.executeUpdate();

            alertUser();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }finally {
            closeConnection();
        }
    }


    public void populateCompanyInfo(String user) {
        try {
            openConnection();

            String fetchLogData = "SELECT * FROM etaireia INNER JOIN recruiter ON etaireia.AFM = recruiter.firm" +
                    " WHERE recruiter.username = ?";
            statement = con.prepareStatement(fetchLogData);
            statement.setString(1, user);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                txtAfm.setText(resultSet.getString("AFM"));
                txtDoy.setText(resultSet.getString("DOY"));
                txtName.setText(resultSet.getString("name"));
                txtTel.setText(resultSet.getString("tel"));
                txtStreet.setText(resultSet.getString("street"));
                txtNum.setText(resultSet.getString("num"));
                txtCity.setText(resultSet.getString("city"));
                txtCountry.setText(resultSet.getString("country"));
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
        alert.setContentText("Company's information have been updated successfully");
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
