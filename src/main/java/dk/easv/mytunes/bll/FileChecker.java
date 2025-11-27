package dk.easv.mytunes.bll;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChecker {
    public File open(){
        File currentFile;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return null;
        }
        else  {
            currentFile = file;

        }
        return currentFile;

    }
}
