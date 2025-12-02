package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.bll.FileChecker;
import dk.easv.mytunes.bll.Logic;
import dk.easv.mytunes.bll.MusicFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class NewSongController implements Initializable {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtArtist;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
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
    private ComboBox<String> category;
    private final ObservableList<String> categoriesObservableList = FXCollections.observableArrayList();
    FileChecker fileChecker = new FileChecker();
    Logic logic = new Logic();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            List<Category> categories = logic.getAllCategoryFromDB();
            for(Category cat : categories){
                categoriesObservableList.add(cat.getName());
            }
            category.setItems(categoriesObservableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnClickedMore(ActionEvent actionEvent) {
        txtMore.setVisible(true);
    }

    public void txtEnterPressed(KeyEvent keyEvent) throws SQLException {

        if (keyEvent.getCode() == KeyCode.ENTER) {
            String genre = txtMore.getText();
            Boolean exist = false;

            lblExist.setVisible(true);
            if (!exist) {

                categoriesObservableList.add(genre);
                lblExist.setStyle("-fx-background-color: green");
                lblExist.setText("Added successfully");
                category.setValue(genre);
            }
            else {
                lblExist.setStyle("-fx-background-color: red");
                lblExist.setText("Already exists");
            }
            logic.addCategory(category.getValue().toString());
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

    public void btnSaveOnClick(ActionEvent actionEvent) throws SQLException {

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
                    String categoryString = category.getValue().toString();
                    logic.getInfo(txtTitle.getText(), txtArtist.getText(), txtTime.getText(), categoryString, txtFile.getText());
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                }
            }
        }

    }
    private boolean isEmpty(){
        String text = txtTitle.getText().trim();
       String artist = txtArtist.getText().trim();
        if(txtTitle.getText() == null || txtArtist.getText() == null || txtFile.getText() == null || category.getValue() == null){
            return false;
        }
        else{
            if(text.isEmpty() || artist.isEmpty()){
                return false;
            }
            return true;
        }
    }
}
