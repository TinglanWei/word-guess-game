package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;

public class GuessingGame extends Application {
    public void start (Stage primaryStage)throws IOException{
        GuessingGamePane pane = new GuessingGamePane();
        pane.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(pane, 600, 600);
        
        primaryStage.setTitle("Guessing Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
