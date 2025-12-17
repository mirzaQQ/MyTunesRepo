package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.PlaylistSong;
import dk.easv.mytunes.be.Playlists;
import dk.easv.mytunes.be.Songs;
import dk.easv.mytunes.bll.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.transformation.FilteredList;

public class MyTunesController {
    /**
     * Initialises the controller class and global variables.
     */
    @FXML
    private Button closeButton;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnFilter;
    @FXML
    private Label lblName;
    @FXML
    private Label lblDuration;
    @FXML
    private Label lblException;
    @FXML
    private Slider sliderVolume;
    @FXML
    private TextField txtFilter;
    @FXML
    private ListView<PlaylistSong> listSongsOnPlaylist;
    @FXML
    private TableView<Songs> tableSongs;
    @FXML
    private TableColumn<Songs, String> tableSongsTitle;
    @FXML
    private TableColumn<Songs, String> tableSongsArtist;
    @FXML
    private TableColumn<Songs, Integer> tableSongsCategory;
    @FXML
    private TableColumn<Songs, String> tableSongsTime;
    @FXML
    private TableView<Playlists> tablePlaylist;
    @FXML
    private TableColumn<Playlists, String> tablePlaylistName;
    @FXML
    private TableColumn<Playlists, Integer> tablePlaylistSongs;
    @FXML
    private TableColumn<Playlists, String> tablePlaylistTime;

    private final Logic logic = new Logic();
    MusicFunctions musicFunctions = new MusicFunctions();

    private final ObservableList<Songs> songsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Playlists> playlistsObservableList = FXCollections.observableArrayList();
    private final ObservableList<PlaylistSong> playlistSongObservableList = FXCollections.observableArrayList();

    private Songs currentSong;
    private FilteredList<Songs> filteredSongs;

    boolean isPlaylistSelected = false;
    boolean isSongSelected = false;
    boolean BtnFilter = false;

    private long lastBackClickTime = 0;
    private static final long DOUBLE_CLICK_THRESHOLD = 500; // milliseconds

    /**
     * All the methods that are not "ActionEvents".
     * <p> <-- because ide won't shut up about this "blank line".
     * initialize - initializes the song and playlist tables, sets up listeners for selection changes, and binds data to the UI components.
     * updateSongTable - updates the song table with the latest data from the database.
     * updatePlaylistTable - updates the playlist table with the latest data from the database.
     * updatePlaylistSongList - updates the list of songs on the selected playlist in the playlist table.
     * setSelectedPlaylist - sets the selected playlist in the playlist table and updates the list of songs on the playlist.
     * setSelectedSong - sets the selected song in the song table.
     * playNextSong - plays the next song in the playlist.
     * stopButton - stops the music playback.
     */
    public void initialize() throws SQLException {
        initializeSongTable();

        filteredSongs = new FilteredList<>(songsObservableList, _ -> true);
        tableSongs.setItems(filteredSongs);

        initializePlaylistTable();

        listSongsOnPlaylist.setItems(playlistSongObservableList);

        tablePlaylist.getSelectionModel().selectedItemProperty().addListener(
                (_, _, _) -> setSelectedPlaylist());
        tableSongs.getSelectionModel().selectedItemProperty().addListener(
                (_, _, _) -> setSelectedSong());
    }

    public void initializeSongTable() throws SQLException {
        tableSongsTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableSongsArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tableSongsCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tableSongsTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        try {
            List<Songs> songs = logic.getAllSongsFromDB();
            songsObservableList.addAll(songs);
            tableSongs.setItems(songsObservableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializePlaylistTable() throws SQLException {
        tablePlaylistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablePlaylistSongs.setCellValueFactory(new PropertyValueFactory<>("songsNumber"));
        tablePlaylistTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));

        try {
            List<Playlists> playlists = logic.getAllPlaylistsFromDB();
            playlistsObservableList.addAll(playlists);
            tablePlaylist.setItems(playlistsObservableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSongTable() throws SQLException {
        songsObservableList.clear();
        List<Songs> songs = logic.getAllSongsFromDB();
        songsObservableList.addAll(songs);
    }

    public void updatePlaylistTable() throws SQLException {
        playlistsObservableList.clear();
        List<Playlists> playlists = logic.getAllPlaylistsFromDB();
        playlistsObservableList.addAll(playlists);
    }

    public void updatePlaylistSongList() throws SQLException {
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            playlistSongObservableList.clear();
            return;
        }
        playlistSongObservableList.clear();
        List<PlaylistSong> playlistSongs = logic.getAllPlaylistSongsFromDB(selectedPlaylist.getPlaylist_id());
        playlistSongObservableList.addAll(playlistSongs);
    }

    public void setSelectedPlaylist() {
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            isPlaylistSelected = true;
            try {
                lblException.setVisible(true);
                lblException.setStyle("-fx-text-fill: black;");
                lblException.setText("Selected playlist: " + selectedPlaylist.getName());
                updatePlaylistSongList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            isPlaylistSelected = false;
            playlistSongObservableList.clear();
            lblException.setVisible(false);
        }
    }

    public void setSelectedSong() {
        Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();
        isSongSelected = selectedSong != null;
    }

    public void playNextSong() throws SQLException {
        int currentIndex = listSongsOnPlaylist.getSelectionModel().getSelectedIndex();
        int nextIndex = currentIndex + 1;

        if (nextIndex < playlistSongObservableList.size()) {
            listSongsOnPlaylist.getSelectionModel().select(nextIndex);
            btnPlayOnClick(null);
        }
        else {
            listSongsOnPlaylist.getSelectionModel().select(0);
            currentSong = null;
            btnPlay.setText("▶");
            btnPlay.setFont(new Font(24));
            lblName.setText("(none) ...");
            lblDuration.setText("");
        }
    }

    public void stopButton() {
        musicFunctions.pauseMusic();
        btnPlay.setText("▶");
        btnPlay.setFont(new Font(24));
    }

    /**
     * All the methods that are in the top part of the app.
     * <p>
     * btnPlayOnClick - plays the selected song or toggles play/pause if a song is already playing.
     * sliderOnClick - changes the volume when the slider is clicked.
     * sliderOnMouseDrag - changes the volume when the slider is dragged.
     * btnBckOnClick - restarts the current song or goes back to the previous song in the playlist.
     * btnFwdOnClick - plays the next song in the playlist.
     * btnFilterOnClick - filters the songs in the table based on the text in the text field.
     */

    public void btnPlayOnClick(ActionEvent actionEvent) throws SQLException {
        Songs song = null;
        PlaylistSong playlistSong = listSongsOnPlaylist.getSelectionModel().getSelectedItem();
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null && !playlistSongObservableList.isEmpty()) {
            if (playlistSong == null) {
                playlistSong = playlistSongObservableList.getFirst();
                listSongsOnPlaylist.getSelectionModel().select(0);
            }

            for (Songs s : songsObservableList) {
                if (s.getTitle().equals(playlistSong.getTitle())) {
                    song = s;
                    break;
                }
            }
        } else {
            return;
        }
        if (song == null) {
            return;
        }

        if (!song.equals(currentSong)) {
            if (currentSong != null) {
                musicFunctions.stopMusic();
            }
            musicFunctions.song(song.getFilepath(), () -> {
                try {
                    playNextSong();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            musicFunctions.playMusic();
            currentSong = song;
            btnPlay.setText("⏸");
            btnPlay.setFont(new Font(20));
            lblName.setText(currentSong.getTitle());
            lblDuration.setText(song.getTime());
            return;
        }

        if (musicFunctions.getStatus().equals("PLAYING")) {
            stopButton();
        } else {
            musicFunctions.playMusic();
            btnPlay.setText("⏸");
            btnPlay.setFont(new Font(20));
        }
    }

    public void sliderOnClick(MouseEvent mouseEvent) {
        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void sliderOnMouseDrag(MouseEvent mouseEvent) {
        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void btnBckOnClick(ActionEvent actionEvent) throws SQLException {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - lastBackClickTime;

        if (timeDiff < DOUBLE_CLICK_THRESHOLD && lastBackClickTime != 0) {
            int currentIndex = listSongsOnPlaylist.getSelectionModel().getSelectedIndex();
            int previousIndex = currentIndex - 1;

            if (previousIndex >= 0) {
                listSongsOnPlaylist.getSelectionModel().select(previousIndex);
                btnPlayOnClick(null);
            }
            lastBackClickTime = 0;
        }
        else {
            musicFunctions.restartMusic();
            lastBackClickTime = currentTime;
        }
    }

    public void btnFwdOnClick(ActionEvent actionEvent) throws SQLException{
        int currentIndex = listSongsOnPlaylist.getSelectionModel().getSelectedIndex();
        int nextIndex = currentIndex + 1;

        if (nextIndex < playlistSongObservableList.size()) {
            listSongsOnPlaylist.getSelectionModel().select(nextIndex);
            btnPlayOnClick(null);
        }
        else {
            listSongsOnPlaylist.getSelectionModel().select(0);
            currentSong = null;
            if (musicFunctions.getStatus().equals("PLAYING")) {
                musicFunctions.stopMusic();
            }
            btnPlay.setText("▶");
            btnPlay.setFont(new Font(24));
            lblName.setText("(none) ...");
            lblDuration.setText("");
        }
    }

    public void btnFilterOnClick(ActionEvent actionEvent) {
        if (!BtnFilter) {
            String filterText = txtFilter.getText().toLowerCase().trim();
            if (filterText.isEmpty()) {
                return;
            }
            filteredSongs.setPredicate(song -> song.getTitle().toLowerCase().contains(filterText) || song.getArtist().toLowerCase().contains(filterText));
            btnFilter.setText("X");
            BtnFilter = true;
        }
        else {
            filteredSongs.setPredicate(song -> true);
            txtFilter.clear();
            btnFilter.setText("\uD83D\uDD0E");
            BtnFilter = false;
        }
    }

    /**
     * All the buttons that are connected with the Playlist table.
     * <p>
     * btnNewPlaylistOnClick - opens a new window to add a new playlist.
     * btnEditPlaylistOnClick - opens a new window to edit the selected playlist.
     */

    public void btnNewPlaylistOnClick(ActionEvent actionEvent) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("MyTunesPlaylistView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add playlist");
        stage.setResizable(false);
        stage.showAndWait();
        updatePlaylistTable();
    }

    public void btnEditPlaylistOnClick(ActionEvent actionEvent) throws IOException, SQLException{
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist == null) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" No playlist selected for editing ");
            return;
        }

        if (musicFunctions.getStatus().equals("PLAYING")) {
            stopButton();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyTunesPlaylistView.fxml"));
        Parent root = loader.load();
        NewPlaylistController controller = loader.getController();
        controller.setPlaylistToEdit(selectedPlaylist);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit playlist");
        stage.setResizable(false);
        stage.showAndWait();

        updatePlaylistTable();
        updatePlaylistSongList();

        int selectedIndex = tablePlaylist.getSelectionModel().getSelectedIndex();
        tablePlaylist.getSelectionModel().select(selectedIndex);
    }

    public void btnDeletePlaylistOnClick (ActionEvent actionEvent) throws SQLException {
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            logic.deletePlaylistFromDB(selectedPlaylist.getPlaylist_id());
            playlistsObservableList.remove(selectedPlaylist);
        }
        else {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" No playlist selected for deletion ");
        }
    }

    /**
     * All the buttons that are connected with the Song list on the Playlist.
     * <p>
     * BtnMoveSongUpOnClick - moves the selected song up in the list.
     * BtnMoveSongDownOnClick - moves the selected song down in the list.
     * BtnDeleteSongInPlaylistOnClick - deletes the selected song from the playlist.
     * btnMoveSongToPlaylistOnClick - moves the selected song to the selected playlist.
     */

    public void BtnMoveSongUpOnClick(ActionEvent actionEvent) {
        int selectedIndex = listSongsOnPlaylist.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Please select a song ");
            return;
        }

        if (selectedIndex == 0) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Song is already on top ");
            return;
        }

        try {
            Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                int position = selectedIndex + 1;
                logic.moveSongUpInDB(selectedPlaylist.getPlaylist_id(), position);
                updatePlaylistSongList();
                listSongsOnPlaylist.getSelectionModel().select(selectedIndex - 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void BtnMoveSongDownOnClick(ActionEvent actionEvent) {
        int selectedIndex = listSongsOnPlaylist.getSelectionModel().getSelectedIndex();
        int maxIndex = listSongsOnPlaylist.getItems().size() - 1;

        if (selectedIndex < 0) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Please select a song ");
            return;
        }

        if (selectedIndex == maxIndex) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Song is already on bottom ");
            return;
        }

        try {
            Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                int position = selectedIndex + 1;
                int maxPosition = listSongsOnPlaylist.getItems().size();
                logic.moveSongDownInDB(selectedPlaylist.getPlaylist_id(), position, maxPosition);
                updatePlaylistSongList();
                listSongsOnPlaylist.getSelectionModel().select(selectedIndex + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void BtnDeleteSongInPlaylistOnClick(ActionEvent actionEvent) {
        PlaylistSong selectedSong = listSongsOnPlaylist.getSelectionModel().getSelectedItem();
        int selectedIndex = listSongsOnPlaylist.getSelectionModel().getSelectedIndex();

        if (selectedSong == null) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Please select song to delete ");
        }
        else {
            try {
                Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
                if (selectedPlaylist != null) {
                    int position = selectedIndex + 1;
                    int selectedPlaylistIndex = tablePlaylist.getSelectionModel().getSelectedIndex();

                    logic.deleteSongFromPlaylist(selectedPlaylist.getPlaylist_id(), position);
                    logic.updatePlaylist(selectedPlaylist.getPlaylist_id());
                    updatePlaylistSongList();
                    updatePlaylistTable();

                    tablePlaylist.getSelectionModel().select(selectedPlaylistIndex);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void btnMoveSongToPlaylistOnClick(ActionEvent actionEvent) {
        if (!isPlaylistSelected) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Please select playlist ");
        }
        else if (!isSongSelected) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Please select song ");
        }
        else {
            try {
                Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
                Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();
                int selectedPlaylistIndex = tablePlaylist.getSelectionModel().getSelectedIndex();

                logic.addSongToPlaylist(selectedSong.getSong_id(), selectedPlaylist.getPlaylist_id());
                logic.updatePlaylist(selectedPlaylist.getPlaylist_id());
                updatePlaylistSongList();
                updatePlaylistTable();

                tablePlaylist.getSelectionModel().select(selectedPlaylistIndex);

                lblException.setVisible(true);
                lblException.setStyle("-fx-text-fill: black;");
                lblException.setText(" Song \"" + selectedSong.getTitle() + "\" moved to \"" + selectedPlaylist.getName() + "\" playlist");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * All the buttons that are connected with the Song table.
     * <p>
     * btnNewSongOnClick - opens a new window to add a new song.
     * BtnEditSongOnClick - opens a new window to edit the selected song.
     * BtnDeleteSongOnClick - deletes the selected song from the database.
     */

    public void btnNewSongOnClick(ActionEvent actionEvent) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("MyTunesSongView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add song");
        stage.setResizable(false);
        stage.showAndWait();
        updateSongTable();
    }

    public void BtnEditSongOnClick(ActionEvent actionEvent) throws IOException, SQLException {
        Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();

        if (selectedSong == null) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" No song selected for editing ");
            return;
        }

        if (musicFunctions.getStatus().equals("PLAYING")) {
            stopButton();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyTunesSongView.fxml"));
        Parent root = loader.load();

        NewSongController controller = loader.getController();
        controller.setSongToEdit(selectedSong);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit song");
        stage.setResizable(false);
        stage.showAndWait();

        updateSongTable();
        updatePlaylistTable();
        updatePlaylistSongList();

        int selectedIndex = tableSongs.getSelectionModel().getSelectedIndex();
        tableSongs.getSelectionModel().select(selectedIndex);
    }

    public void BtnDeleteSongOnClick(ActionEvent actionEvent) throws SQLException {
        Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            logic.deleteSongFromDB(selectedSong.getSong_id());
            songsObservableList.remove(selectedSong);
        }
        else {
            lblException.setVisible(true);
            lblException.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" No song selected for deletion ");
        }
    }

    /**
     * This one doesn't have friends :<
     * <p>
     * btnCloseClick - closes the application window. duh
     */

    public void btnCloseClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
