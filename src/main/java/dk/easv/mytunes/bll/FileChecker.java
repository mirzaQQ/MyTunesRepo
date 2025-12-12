package dk.easv.mytunes.bll;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChecker {
    public File open(){
        File currentFile;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/music"));
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
    public boolean checkfile(File file){
        /**
         * The method is made for checking the file if it is an mp3 or wav file,
         * by getting the name of the file and turning it into a string, and then
         * splitting it up by every backslash, and then checks if the last element of the array
         * contains .wav or .mp3
         */
        String fileName = file.getName();
        String regex = "[\\\\]";
        String[] splitedBip = fileName.split(regex);
        String music = splitedBip[splitedBip.length - 1];

        if(music.contains(".wav") || music.contains(".mp3")){

            return true;
        }else {

            return false;
        }



    }
}
