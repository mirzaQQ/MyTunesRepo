package dk.easv.mytunes.bll;

import java.io.File;

public class FileChecker {
    /**
     * I don't know why this class is here and not in MusicFunctions.
     */

    public boolean checkFile(File file){
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

        return music.contains(".wav") || music.contains(".mp3");
    }
}