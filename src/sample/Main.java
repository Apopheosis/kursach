package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    public static String occupationInstance; //Глобальная переменная для хранения профессии входящего, стирается после выхода.


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("authentication.fxml")); // Инициализация окна авторизации.
        primaryStage.setTitle("Методическая работа");
        primaryStage.setScene(new Scene(root, 700, 300));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

