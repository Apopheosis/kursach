package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;

public class RegistrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button RegOkButton;

    @FXML
    private PasswordField PassField;

    @FXML
    private TextField LoginField;

    @FXML
    private Button BackButton;

    @FXML
    private TextField FirstNameField;

    @FXML
    private ChoiceBox<String> OccupationBox;

    @FXML
    private TextField LastNameField;

    void initAuthScreen() {
        RegOkButton.getScene().getWindow().hide();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/sample/authentication.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Методическая работа");
        stage.setScene(new Scene(root, 700, 300));
        stage.show();
    }

    @FXML
    void initialize() {
        ObservableList<String> jobs = FXCollections.observableArrayList("Администратор", "Преподаватель", "Типография");
        OccupationBox.setItems(jobs);
        OccupationBox.setValue("Администратор");

        DatabaseHandler dbHandler = new DatabaseHandler();

        RegOkButton.setOnAction(event -> {
            dbHandler.signUpUser(FirstNameField.getText().trim(), LastNameField.getText().trim(), LoginField.getText().trim(), PassField.getText().trim(), OccupationBox.getValue());
            initAuthScreen();
        });
        BackButton.setOnAction(event -> {
            initAuthScreen();
        });
    }
}

