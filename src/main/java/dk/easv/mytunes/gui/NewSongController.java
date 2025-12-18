package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Songs;
import dk.easv.mytunes.bll.FileChecker;
import dk.easv.mytunes.bll.Logic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class NewSongController implements Initializable {
    /**
     * Initialises the controller class and global variables.
     */
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
    @FXML
    private ComboBox<String> category;

    private File currentFile = null;
    private final ObservableList<String> categoriesObservableList = FXCollections.observableArrayList();
    private Songs songToEdit = null;

    FileChecker fileChecker = new FileChecker();
    Logic logic = new Logic();

    /**
     * All the methods that are not buttons of any kind.
     * <p>
     * initialize - initializes the combobox with all the categories from the database.
     * setSongToEdit - sets the song to edit if the user clicked the edit button.
     * getRelativePath - gets the relative path of the file.
     * isEmpty - checks if the fields are empty.
     */
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

    public void setSongToEdit(Songs song) {
        this.songToEdit = song;
        txtTitle.setText(song.getTitle());
        txtArtist.setText(song.getArtist());
        category.setValue(song.getCategory());
        txtTime.setText(song.getTime());
        txtFile.setText(song.getFilepath());

        txtTime.setDisable(true);
        txtFile.setDisable(true);
        txtFile.setOpacity(1);
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

    private boolean isEmpty(){
        String text = txtTitle.getText().trim();
        String artist = txtArtist.getText().trim();
        if(txtTitle.getText() == null || txtArtist.getText() == null || txtFile.getText() == null || category.getValue() == null){
            return false;
        }
        else{
            return !text.isEmpty() && !artist.isEmpty();
        }
    }

    /**
     * All the button methods.
     * <p>
     * btnClickedMore - shows the text field for adding more categories.
     * txtEnterPressed - adds the category to the combobox if the text field is not empty.
     * btnChooseOnClick - opens a file chooser to select a file.
     * btnCancelClick - closes the window.
     * btnSaveOnClick - saves the song to the database.
     */

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Song File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.m4a"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File musicFolder = new File("src/main/resources/music");
        if (musicFolder.exists() && musicFolder.isDirectory()) {
            fileChooser.setInitialDirectory(musicFolder);
        }

        File selectedFile = fileChooser.showOpenDialog(txtFile.getScene().getWindow());

        if (selectedFile != null) {
            currentFile = selectedFile;
            txtFile.setText(selectedFile.getCanonicalPath());

            if (fileChecker.checkFile(currentFile)) {
                try {
                    Media media = new Media(currentFile.toURI().toString());
                    MediaPlayer tempPlayer = new MediaPlayer(media);
                    media.durationProperty().addListener((_, _, newDuration) -> {
                        if (newDuration != null && !newDuration.isUnknown()) {
                            long minutes = (long) newDuration.toMinutes();
                            long seconds = (long) newDuration.toSeconds() % 60;
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

    public void btnCancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void btnSaveOnClick(ActionEvent actionEvent) throws SQLException {
        lblExist.setVisible(true);

        if (songToEdit != null) {
            if (!isEmpty()) {
                lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                lblExist.setText(" Some fields are empty ");
            } else {
                lblExist.setStyle("-fx-text-fill: green; -fx-border-color: green; -fx-border-radius: 5px;");
                lblExist.setText(" Updated successfully");
                logic.updateSong(songToEdit.getSong_id(), txtTitle.getText(), txtArtist.getText(), category.getValue());
            }
        } else {
            if (!isEmpty()) {
                lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                lblExist.setText(" Some fields are empty ");
            } else {
                if (currentFile == null) {
                    lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                    lblExist.setText(" Please select a file ");
                } else {
                    if (!fileChecker.checkFile(currentFile)) {
                        lblExist.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
                        lblExist.setText(" Not a valid format ");
                    } else {
                        lblExist.setStyle("-fx-text-fill: green; -fx-border-color: green; -fx-border-radius: 5px;");
                        lblExist.setText(" Saved successfully ");
                        String categoryString = category.getValue();
                        String relativePath = getRelativePath(currentFile);
                        logic.getInfo(txtTitle.getText(), txtArtist.getText(), txtTime.getText(), categoryString, relativePath);
                    }
                }
            }
        }
    }
}
