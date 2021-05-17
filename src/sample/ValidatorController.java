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
import javafx.util.Callback;

import static sample.Const.*;
import static sample.Main.occupationInstance;

public class ValidatorController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField TitleField;

    @FXML
    private TextField YearField;

    @FXML
    private Button ProceedButton;

    @FXML
    private TableView<Manual> ManualTable;

    @FXML
    private TableColumn<Manual, String> AuthorCol;

    @FXML
    private TableColumn<Manual, String> YearCol;

    @FXML
    private TableColumn<Manual, String> TitleCol;

    @FXML
    private TableColumn<Manual, String> PercentCol;

    @FXML
    private TableColumn TypoCol;

    @FXML
    private TextField AuthorField;

    @FXML
    private Button BackButton;

    @FXML
    private TextField PercentField;

    @FXML
    private Button ScheduleButton;

    @FXML
    private Button TypoButton;

    ObservableList<Manual> list = FXCollections.observableArrayList();
    DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void initialize() {


        try {
            Connection con = dbHandler.getDbConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM " + MANUAL_TABLE);

            while (rs.next()) {
                list.add(new Manual(rs.getString("idnew_table"), rs.getString("title"), rs.getString("year"), rs.getString("author"), rs.getString("ready")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        YearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        PercentCol.setCellValueFactory(new PropertyValueFactory<>("ready"));

        ProceedButton.setOnAction(event-> {
            Manual manual = new Manual(TitleField.getText(), YearField.getText(), AuthorField.getText(), PercentField.getText());
            AddManualToTable(manual);
            reOpenWindow();
        });

        ScheduleButton.setOnAction(event -> {
            ManualTable.getScene().getWindow().hide();

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
        });

        Callback<TableColumn<Manual, String>, TableCell<Manual, String>> cellFactory = (param) -> {
            final TableCell<Manual, String> ManualCell = new TableCell<Manual, String>() {
                @Override
                public void updateItem(String Item, boolean empty) {
                    super.updateItem(Item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button typoButton = new Button("Сдать");
                        typoButton.setOnAction(event -> {
                            Manual m = getTableView().getItems().get(getIndex());
                            if (m.getReady().equals("100")) {
                                sendToTypo(m.getTitle());
                                deleteRow(m.getTitle());
                                reOpenWindow();
                            } else {
                                setGraphic(null);
                                setText(null);
                            }
                        });
                        setGraphic(typoButton);
                        setText(null);
                    }
                }
            };
            return ManualCell;
        };
        TypoCol.setCellFactory(cellFactory);

        BackButton.setOnAction(event -> {
            ManualTable.getScene().getWindow().hide();
            occupationInstance = "";

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

        ManualTable.setItems(list);
    }

    private void sendToTypo(String manualTitle) {

        try (
                Connection con = dbHandler.getDbConnection();
                PreparedStatement stmt = con.prepareStatement("INSERT INTO " + TYPO_TABLE + " SELECT * FROM " + MANUAL_TABLE + " WHERE " +
                        MANUAL_TITLE + "=?");
        ) {

            stmt.setString(1, manualTitle);
            stmt.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Error");
            ex.printStackTrace(System.err);
        }
    }

    private void deleteRow(String manualTitle) {

        try (
                Connection con = dbHandler.getDbConnection();
                PreparedStatement stmt = con.prepareStatement("DELETE FROM " + Const.MANUAL_TABLE + " WHERE " + Const.MANUAL_TITLE + "=? ");
        ) {

            stmt.setString(1, manualTitle);
            stmt.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Error");
            ex.printStackTrace(System.err);
        }
    }

    public void AddManualToTable(Manual manual) {
        String insert = "INSERT INTO " + MANUAL_TABLE + "(" +
                MANUAL_TITLE + "," + Const.MANUAL_YEAR
                + "," + Const.MANUAL_AUTHOR + "," + Const.MANUAL_READY  + ")" + "VALUES(?,?,?,?)";
        try {
            PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(insert);
            prSt.setString(1, manual.getTitle());
            prSt.setString(2, manual.getYear());
            prSt.setString(3, manual.getAuthor());
            prSt.setString(4, manual.getReady());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void reOpenWindow() {
        ManualTable.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ValidatorScreen.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ответственный за методическую работу");
        stage.show();
    }
}
