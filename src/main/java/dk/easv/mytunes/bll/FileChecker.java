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
        String fileName = file.getName();
        String regex = "[\\\\]";
        String[] splitedBip = fileName.split(regex);
        String music = splitedBip[splitedBip.length - 1];

        if(music.contains(".wav") || music.contains(".mp3")){
            //System.out.println("yesss");
            return true;
        }else {
            //System.out.println("Not mp3 or wav");
            return false;
        }


        //return file.isFile() && file.isFile() && file.canRead();
    }
}
