package MultiUserLoginForm;

import Recruiter.RecruiterHomeScreen;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.fxml.Initializable;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.ConnectionUtils;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet resultSet;
    private String name;
    private String typeOfUser = "";
    @FXML
    private TextField candUsername;
    @FXML
    private PasswordField candPassword;
    @FXML
    private Button btnCandLoginAction;
    @FXML
    private TextField adminUsername;
    @FXML
    private PasswordField adminPass;
    @FXML
    private TextField recrUsername;
    @FXML
    private PasswordField recrPassword;
    @FXML
    private Button btnAdminLoginAction;
    @FXML
    private Label lblError1;
    @FXML
    private Label lblError2;
    @FXML
    private Label lblError3;
    @FXML
    private Tab tabAdmin;
    @FXML
    private Button btnRecruiterLogin;
    @FXML
    private Tab tabRecuit;
    @FXML
    private Tab tabCand;
    @FXML
    private Pane slidingPane;
    @FXML
    private TabPane tabPaneLogin;
    @FXML
    private Label lblAdmin;
    @FXML
    private Label lblRecruit;
    @FXML
    private Label lblCand;
    @FXML
    private Label lblSatus;

    {
        resultSet = null;
    }

    public LoginController() {
        con = ConnectionUtils.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        if (con == null) {
            lblError1.setTextFill(Color.TOMATO);
            lblError1.setText("Server Error : Check");
            lblError2.setTextFill(Color.TOMATO);
            lblError2.setText("Server Error : Check");
            lblError3.setTextFill(Color.TOMATO);
            lblError3.setText("Server Error : Check");
        }
    }

    @FXML
    void btnAdminLoginAction(MouseEvent event) {
        if (AdminlogIn().equals("Success")) {
            try {
                nextScreen(event, "../Admin/AdminHomeScreen.fxml");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @FXML
    void btnRecruiterLoginAction(MouseEvent event) throws IOException {
        if (RecruiterlogIn().equals("Success")) {
            try {
                typeOfUser = "recruiter";
                nextScreen(event, "../Recruiter/RecruiterHomeScreen.fxml");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @FXML
    void btnCandidateLoginAction(MouseEvent event) {
        if (CandidatelogIn().equals("Success")) {
            try {
                nextScreen(event, "../Candidate/CandidateHomeScreen.fxml");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @FXML
    void openAdminTab(MouseEvent event) {
        TranslateTransition toLeftAnimation = new TranslateTransition(new Duration(300), lblSatus);
        toLeftAnimation.setToX(slidingPane.getTranslateX());
        toLeftAnimation.play();
        toLeftAnimation.setOnFinished((ActionEvent event1) -> {
            lblSatus.setText("ADMINISTRATOR");
        });
        SingleSelectionModel<Tab> selectionModel = tabPaneLogin.getSelectionModel();
        selectionModel.select(tabAdmin);
    }

    @FXML
    void openCandTab(MouseEvent event) {
        TranslateTransition toRightAnimation = new TranslateTransition(new Duration(300), lblSatus);
        toRightAnimation.setToX(slidingPane.getTranslateX() + 2 * lblSatus.getPrefWidth());
        toRightAnimation.play();
        toRightAnimation.setOnFinished((ActionEvent event1) -> {
            lblSatus.setText("CANDIDATE");
        });
        SingleSelectionModel<Tab> selectionModel = tabPaneLogin.getSelectionModel();
        selectionModel.select(tabCand);
    }

    @FXML
    void openRecruitTab(MouseEvent event) {
        TranslateTransition toRightAnimation = new TranslateTransition(new Duration(300), lblSatus);
        toRightAnimation.setToX(slidingPane.getTranslateX() + lblSatus.getPrefWidth());
        toRightAnimation.play();
        toRightAnimation.setOnFinished((ActionEvent event1) -> {
            lblSatus.setText("RECRUITER");
        });
        SingleSelectionModel<Tab> selectionModel = tabPaneLogin.getSelectionModel();
        selectionModel.select(tabRecuit);
    }


    private String AdminlogIn() {
        name = adminUsername.getText();
        String pass = adminPass.getText();

        String sql = "SELECT * FROM user WHERE username = (SELECT username FROM admin_db WHERE username = ?) AND usr_password = ? LIMIT 1;";

        if (logIn(name, pass, sql) == "Success") {
            return "Success";
        }
        return "Failed";
    }

    private String RecruiterlogIn() {
        name = recrUsername.getText();
        String pass = recrPassword.getText();

        String sql = "SELECT * FROM user WHERE username = (SELECT username FROM recruiter WHERE username = ?) AND usr_password = ? LIMIT 1;";

        if (logIn(name, pass, sql) == "Success") {
            return "Success";
        }
        return "Failed";
    }

    private String CandidatelogIn() {
        name = candUsername.getText();
        String pass = candPassword.getText();

        String sql = "SELECT * FROM user WHERE username = (SELECT username FROM candidate WHERE username = ?) AND usr_password = ? LIMIT 1;";

        if (logIn(name, pass, sql) == "Success") {
            return "Success";
        }
        return "Failed";
    }


    private String logIn(String username, String password, String sqlStatement) {
        try {
            statement = con.prepareStatement(sqlStatement);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                lblError1.setTextFill(Color.TOMATO);
                lblError1.setText("Enter Correct Username/Password");
                lblError2.setTextFill(Color.TOMATO);
                lblError2.setText("Enter Correct Username/Password");
                lblError3.setTextFill(Color.TOMATO);
                lblError3.setText("Enter Correct Username/Password");
                return "Failed";
            } else {
                lblError1.setTextFill(Color.GREEN);
                lblError1.setText("Login Successful...");
                lblError2.setTextFill(Color.GREEN);
                lblError2.setText("Login Successful...");
                lblError3.setTextFill(Color.GREEN);
                lblError3.setText("Login Successful...");
                return "Success";
            }
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            return "Exception";
        }
    }

    private void nextScreen(MouseEvent event, String s) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Parent nextScreen;
        if (typeOfUser .equals("recruiter")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(s));
            nextScreen = loader.load();
            RecruiterHomeScreen  recrController = loader.getController();
            recrController.setName(name);
        }else {
            nextScreen = FXMLLoader.load(getClass().getResource(s));
        }
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setTitle("Home");
        newStage.setScene(new Scene(nextScreen));
        newStage.show();
    }
}
