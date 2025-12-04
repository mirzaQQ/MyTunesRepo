package dk.easv.mytunes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/MyTunesGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MyTunes");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(810);
        stage.setMinHeight(300);
        stage.show();
    }
}
