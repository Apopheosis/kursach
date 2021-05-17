package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Const;
import sample.DatabaseHandler;
import sample.User;

import static sample.Main.occupationInstance;

public class AdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<User> TeacherTable;

    @FXML
    private TableColumn<User, String> UserCol;

    @FXML
    private TableColumn<User, String> PassCol;

    @FXML
    private TableColumn<User, String> FirstCol;

    @FXML
    private TableColumn<User, String> LastCol;

    @FXML
    private TableColumn<User, String> OccCol;

    @FXML
    private Button BackButton;

    @FXML
    private TableColumn ButtCol;
    ObservableList<User> list = FXCollections.observableArrayList();
    DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void initialize() {



        try {
            Connection con = dbHandler.getDbConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM " + Const.USER_TABLE);

            while (rs.next()) {
                list.add(new User(rs.getString("Firstname"), rs.getString("Lastname"), rs.getString("Login"),
                        rs.getString("Password"), rs.getString("Occupation"), rs.getString("idUsers")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        UserCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        PassCol.setCellValueFactory(new PropertyValueFactory<>("userPass"));
        FirstCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        LastCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        OccCol.setCellValueFactory(new PropertyValueFactory<>("occupation"));

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory = (param) -> {
            final TableCell<User, String> cell = new TableCell<User, String>() {
                @Override
                public void updateItem(String Item, boolean empty) {
                    super.updateItem(Item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button assignButton = new Button("Назначить ответственность");
                        assignButton.setOnAction(event -> {
                            User u = getTableView().getItems().get(getIndex());
                            if (u.getOccupation().equals("Преподаватель")) {
                                updateData("Occupation", Const.VALIDATOR, u.getId());
                                reOpenWindow();
                            } else if (u.getOccupation().equals(Const.VALIDATOR)) {
                                updateData("Occupation", "Преподаватель", u.getId());
                                reOpenWindow();
                            } else {
                                setGraphic(null);
                                setText(null);
                            }
                        });
                        setGraphic(assignButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        ButtCol.setCellFactory(cellFactory);
        TeacherTable.setItems(list);

        BackButton.setOnAction(event -> {
            occupationInstance = "";

            BackButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("authentication.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Авторизация");
            stage.show();
        });
    }

    private void updateData(String column, String newValue, String id) {

        try (
                Connection con = dbHandler.getDbConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE " + Const.USER_TABLE + " SET "+column+" = ? WHERE " + Const.USERS_ID + " = ? ");
        ) {

            stmt.setString(1, newValue);
            stmt.setString(2, id);
            stmt.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Error");
            ex.printStackTrace(System.err);
        }
    }

    public void reOpenWindow() {
        TeacherTable.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AdminScreen.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

