package sample;
import java.io.IOException;
import java.net.URL;

import sample.DatabaseHandler;
import sample.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static sample.Main.occupationInstance;

public class Controller {



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem ExitButton;

    @FXML
    private Label AuthLabel;

    @FXML
    private PasswordField AuthPasswordText;

    @FXML
    private Label PasswordAuthLabel;

    @FXML
    private TextField AuthLoginText;

    @FXML
    private Label LoginAuthLabel;

    @FXML
    private Button AuthOkButton;

    @FXML
    private Button RegButton;

    @FXML
    private Label NotifierLabel;


    @FXML
    void exitProgram(ActionEvent event) {

    }


    @FXML
    void initialize() {
        AuthOkButton.setOnAction(this::handle);
        RegButton.setOnAction(event -> {

            AuthOkButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/registration.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });
    }




    private void handle(ActionEvent event) {

        String loginText = AuthLoginText.getText().trim();
        String passText = AuthPasswordText.getText().trim();

        if (!loginText.equals("") && (!passText.equals(""))) {
            loginUser(loginText, passText);
        } else {
            NotifierLabel.setText("Fields are empty.");
        }
    }

    public void openNewWindow(String window, String title) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window + ".fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.showAndWait();
    }

    public void onEnter(User user) {
        AuthOkButton.getScene().getWindow().hide();
        switch (user.getOccupation()) {
            case "Администратор": {
                occupationInstance = "Администратор";
                openNewWindow("AdminScreen", "Администратор");
                break;
            }
            case "Преподаватель": {
                occupationInstance = "Преподаватель";
                openNewWindow("TeacherScreen", "Преподаватель");
                break;
            }
            case "Ответственный за методическую работу": {
                occupationInstance = "Ответственный за методическую работу";
                openNewWindow("ValidatorScreen", "Ответственный за методическую работу");
                break;
            }
            case "Типография": {
                occupationInstance = "Типография";
                openNewWindow("TypoScreen", "Типография");
                break;
            }
            default: {
                System.out.println("Такого окна не существует");
                break;
            }
        }
    }

    private void loginUser(String loginText, String passText) {

        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setUserName(loginText);
        user.setUserPass(passText);
        ResultSet result = dbHandler.getUser(user);
        int count = 0;
        while (true) {
            try {
                if (!result.next()) {
                    break;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (user.getUserName().equals(AuthLoginText.getText()) && user.getUserPass().equals(AuthPasswordText.getText())) {
                try {
                    User ActiveUser = new User(result.getString("Login"), result.getString("Password"),
                            result.getString("Firstname"), result.getString("Lastname"), result.getString("Occupation"), result.getString("idUsers"));
                    count++;
                    onEnter(ActiveUser);
                    break;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        if (count==0) {
            NotifierLabel.setText("Access denied.");
        }
    }
}
