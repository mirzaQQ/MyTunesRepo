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
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.media.Media;

import java.io.File;
import java.io.IOException;
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
            boolean exist = categoriesObservableList.contains(genre);

            lblExist.setVisible(true);
            if (genre.isEmpty()) {
                lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                lblExist.setText(" Category can not be empty ");
            }
            else if (!exist) {
                categoriesObservableList.add(genre);
                lblExist.setStyle("-fx-text-fill: green; -fx-border-color: green; -fx-border-radius: 5px;");
                lblExist.setText(" Added category successfully ");
                category.setValue(genre);
                logic.addCategory(genre);
            }
            else {
                lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                lblExist.setText(" Category already exists ");
            }
        }
    }
    public void btnChooseOnClick(ActionEvent actionEvent) throws IOException {
        MusicFunctions musicFunctions = new MusicFunctions();
        currentFile = fileChecker.open();

        if (currentFile != null) {
            txtFile.setText(currentFile.getCanonicalPath());
            if (fileChecker.checkfile(currentFile)) {
                try {
                    Media media = new Media(currentFile.toURI().toString());
                    MediaPlayer tempPlayer = new MediaPlayer(media);
                    media.durationProperty().addListener((_, _, newDurration) -> {
                        if (newDurration != null && !newDurration.isUnknown()) {
                            long minutes = (long) newDurration.toMinutes();
                            long seconds = (long) newDurration.toSeconds() % 60;
                            txtTime.setText(String.format("%02d:%02d", minutes, seconds));
                            tempPlayer.dispose();
                        }
                    });
                } catch (Exception e) {
                    txtTime.setText("ERROR");
                }
            } else {
                txtTime.setText("ERROR");
            }
        }
    }

    private String getRelativePath(File file) {
        String absolutePath = file.getAbsolutePath();
        String resourcesPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "music";

        int index = absolutePath.indexOf(resourcesPath);
        if (index != -1) {
            return absolutePath.substring(index).replace(file.separator, "/");
        }
        return absolutePath;
    }

    public void btnCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void btnSaveOnClick(ActionEvent actionEvent) throws SQLException {

        lblExist.setVisible(true);
        if(!isEmpty()){
            lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblExist.setText(" Some fields are empty ");
        }
        else {
            if(currentFile == null){
                lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                lblExist.setText(" Please select a file ");
            }
            else {
                if(!fileChecker.checkfile(currentFile)){
                    lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                    lblExist.setText(" Not a valid format ");
                }
                else {
                    lblExist.setStyle("-fx-text-fill: green; -fx-border-color: green; -fx-border-radius: 5px;");
                    lblExist.setText(" Saved successfully ");
                    String categoryString = category.getValue().toString();
                    String relativePath = getRelativePath(currentFile);
                    logic.getInfo(txtTitle.getText(), txtArtist.getText(), txtTime.getText(), categoryString, relativePath);
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
