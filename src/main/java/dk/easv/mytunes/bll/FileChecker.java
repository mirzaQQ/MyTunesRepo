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
    public void checkfile(File file){

        System.out.println(file);
        /**
        String regex = "[\\\\]";
        String[] splitedBip = bip.split(regex);
        String music = splitedBip[splitedBip.length - 1];*/
        //return file.isFile() && file.isFile() && file.canRead();
    }
}
