package dk.easv.mytunes.gui;

import dk.easv.mytunes.bll.FileChecker;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.jfr.Category;

import java.awt.geom.Point2D;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;


public class NewSongController implements Initializable {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtArtist;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField txtTime;
    @FXML
    private TextField txtFile;
    @FXML
    private Label lblExist;
    @FXML
    private TextField txtMore;
    private File currentFile;
    @FXML
    private ComboBox category;
    private ObservableList<MenuItem> categories =  FXCollections.observableArrayList();
    FileChecker fileChecker = new FileChecker();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        categories.add(new  MenuItem("Pop"));
        categories.add(new  MenuItem("Rock"));
        categories.add(new  MenuItem("Jazz"));

        for(MenuItem item : categories){

            category.getItems().add(item.getText());
        }
    }

    public void btnClickedMore(ActionEvent actionEvent) {
        txtMore.setVisible(true);
    }

    public void txtEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String genre = txtMore.getText();
            Boolean exist = false;
            for(MenuItem item : categories){
                if(item.getText().equals(genre)){
                    exist = true;
                }
            }
            lblExist.setVisible(true);
            if(!exist){

                categories.add(new  MenuItem(genre));
                category.getItems().add(categories.getLast().getText());
                lblExist.setStyle("-fx-background-color: green");
                lblExist.setText("Added successfully");
            }
            else {
                lblExist.setStyle("-fx-background-color: red");
                lblExist.setText("Already exists");
            }
        }
    }
    public void btnChooseOnClick(ActionEvent actionEvent) {

        MusicFunctions musicFunctions = new MusicFunctions();
        /**
         * parts of this can in bll
         * new file should be created for this in the bll
         * that can be used for checking if the user choose a mp3 or wav.
         */
        currentFile = fileChecker.open();

        txtFile.setText(currentFile.getAbsolutePath());
        if(fileChecker.checkfile(currentFile)) {
            txtTime.setText(musicFunctions.getDuration());
        }
        else {
            txtTime.setText("ERROR");
        }

    }

    public void btnCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void btnSaveOnClick(ActionEvent actionEvent) {
        lblExist.setVisible(true);
        if(!isEmpty()){
            lblExist.setStyle("-fx-background-color: red");
            lblExist.setText("Some fields are empty");
        }
        else {
            if(currentFile == null){
                lblExist.setStyle("-fx-background-color: red");
                lblExist.setText("Please select a file");
            }
            else {
                if(!fileChecker.checkfile(currentFile)){
                    lblExist.setStyle("-fx-background-color: red");
                    lblExist.setText("Not mp3 or mp4");
                }
                else {
                    lblExist.setStyle("-fx-background-color: green");
                    lblExist.setText("Saved successfully");
                }
            }
        }

    }
    private boolean isEmpty(){
        if(txtTitle.getText() == null || txtArtist.getText() == null || txtFile.getText() == null || category.getValue() == null){
            return false;
        }
        else{
            return true;
        }
    }
}
