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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Const;
import sample.DatabaseHandler;
import sample.Manual;
import sample.User;

import static sample.Main.occupationInstance;

public class TypoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Manual> ManualsTable;

    @FXML
    private TableColumn<Manual, String> TitleCol;

    @FXML
    private TableColumn<Manual, String> YearCol;

    @FXML
    private TableColumn<Manual, String> AuthorCol;

    @FXML
    private TableColumn<Manual, String> ReadyCol;


    @FXML
    private Button BackButton;

    @FXML
    private TableColumn TypoCol;
    ObservableList<Manual> data = FXCollections.observableArrayList();
    DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void initialize() {
        try {
            Connection con = dbHandler.getDbConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM " + Const.TYPO_TABLE);

            while (rs.next()) {
                data.add(new Manual(rs.getString("title"), rs.getString("year"), rs.getString("author"), rs.getString("ready")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        YearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        ReadyCol.setCellValueFactory(new PropertyValueFactory<>("ready"));

        Callback<TableColumn<Manual, String>, TableCell<Manual, String>> cellFactory = (param) -> {
            final TableCell<Manual, String> ManualCell = new TableCell<Manual, String>() {
                @Override
                public void updateItem(String Item, boolean empty) {
                    super.updateItem(Item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button typoButton = new Button("Отправить в печать");
                        typoButton.setOnAction(event -> {
                            Manual m = getTableView().getItems().get(getIndex());
                            if (m.getReady().equals("100")) {
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
        ManualsTable.setItems(data);

        BackButton.setOnAction(event -> {
            ManualsTable.getScene().getWindow().hide();
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
    }

    private void deleteRow(String manualTitle) {

        try (
                Connection con = dbHandler.getDbConnection();
                PreparedStatement stmt = con.prepareStatement("DELETE FROM " + Const.TYPO_TABLE + " WHERE " + Const.TYPO_TITLE + "=? ");
        ) {

            stmt.setString(1, manualTitle);
            stmt.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("Error");
            ex.printStackTrace(System.err);
        }
    }

    public void reOpenWindow() {
        ManualsTable.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TypoScreen.fxml"));

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
