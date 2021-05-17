package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import static sample.Main.occupationInstance;

public class TeacherScheduleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<String, PlannedManual> TitleCol;

    @FXML
    private TableColumn<String, PlannedManual> AuthorCol;

    @FXML
    private TableColumn<String, PlannedManual> DateCol;

    @FXML
    private TableView<PlannedManual> PlannedManualTable;

    @FXML
    private TextField TitleField;

    @FXML
    private TextField AuthorField;

    @FXML
    private ChoiceBox<String> DayBox;

    @FXML
    private ChoiceBox<String> MonthBox;

    @FXML
    private ChoiceBox<String> YearBox;

    @FXML
    private Button OKButton;

    @FXML
    private Button BackButton;

    ObservableList<PlannedManual> list = FXCollections.observableArrayList();
    DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        try {
            Connection con = dbHandler.getDbConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM " + Const.PLANNED_MANUAL_TABLE);

            while (rs.next()) {
                list.add(new PlannedManual(rs.getString("title"), rs.getString("author"), rs.getString("date")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        ObservableList<String> year = FXCollections.observableArrayList("2020", "2021", "2022", "2023", "2025", "2026", "2027",
                "2028", "2029", "2030");
        ObservableList<String> month = FXCollections.observableArrayList("01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12");
        ObservableList<String> day = FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31");
        YearBox.setItems(year);
        MonthBox.setItems(month);
        DayBox.setItems(day);

        OKButton.setOnAction(event -> {
            PlannedManual pm = new PlannedManual(TitleField.getText().trim(), AuthorField.getText().trim(), DayBox.getValue()+"."+MonthBox.getValue()+"."+
                    YearBox.getValue());
            AddManualToTable(pm);
            reOpenWindow();
        });

        BackButton.setOnAction(event -> {
            BackButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            switch (occupationInstance) {
                case ("Преподаватель"): {
                    loader.setLocation(getClass().getResource("TeacherScreen.fxml"));
                    break;
                }
                case ("Ответственный за методическую работу"): {
                    loader.setLocation(getClass().getResource("ValidatorScreen.fxml"));
                    break;
                }
            }

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            switch (occupationInstance) {
                case ("Преподаватель"): {
                    stage.setTitle("Преподаватель");
                    break;
                }
                case ("Ответственный за методическую работу"): {
                    stage.setTitle("Ответственный за методическую работу");
                    break;
                }
            }
            stage.show();
        });

        PlannedManualTable.setItems(list);
    }

    public void AddManualToTable(PlannedManual manual) {
        String insert = "INSERT INTO " + Const.PLANNED_MANUAL_TABLE + "(" +
                Const.PLANNED_MANUAL_TITLE + "," + Const.PLANNED_MANUAL_AUTHOR + "," + Const.PLANNED_MANUAL_DATE  + ")" + "VALUES(?,?,?)";
        try {
            PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(insert);
            prSt.setString(1, manual.getTitle());
            prSt.setString(2, manual.getAuthor());
            prSt.setString(3, manual.getDate());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void reOpenWindow() {
        PlannedManualTable.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TeacherScheduleScreen.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Расписание написания");
        stage.show();
    }
}
