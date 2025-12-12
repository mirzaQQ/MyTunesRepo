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
    private ListView<String> listSongsOnPlaylist;
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
    private final ObservableList<Songs> songsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Playlists> playlistsObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> playlistSongObservableList = FXCollections.observableArrayList();
    MusicFunctions musicFunctions = new MusicFunctions();
    private Songs currentSong;
    private FilteredList<Songs> filteredSongs;
    boolean isPlaylistSelected = false;
    boolean isSongSelected = false;
    boolean BtnFilter = false;

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

        for (PlaylistSong ps : playlistSongs)
            playlistSongObservableList.add(ps.getTitle());
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

    public void btnPlayOnClick(ActionEvent actionEvent) throws SQLException {
        Songs song = null;
        String playlistSongTitle = listSongsOnPlaylist.getSelectionModel().getSelectedItem();
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null && !playlistSongObservableList.isEmpty()) {
            if (playlistSongTitle == null) {
                playlistSongTitle = playlistSongObservableList.getFirst();
            }

            for (Songs s : songsObservableList) {
                if (s.getTitle().equals(playlistSongTitle)) {
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
            musicFunctions.song(song.getFilepath());
            musicFunctions.playMusic();
            currentSong = song;
            btnPlay.setText("⏸");
            btnPlay.setFont(new Font(20));
            lblName.setText(currentSong.getTitle());
            lblDuration.setText(song.getTime());
            return;
        }

        // Toggle play/pause for the same song
        if (musicFunctions.getStatus().equals("PLAYING")) {
            musicFunctions.pauseMusic();
            btnPlay.setText("▶");
            btnPlay.setFont(new Font(24));
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

    public void btnBckOnClick(ActionEvent actionEvent) {
        /**
         * !!!!!!TODO!!!!!!!!!
         * First click restarts the music
         * and the second one goes back one track
         * if there is none then nothing happens or goes the last element of the playlist
         */
        musicFunctions.restartMusic();
    }

    public void btnNewSongOnClick(ActionEvent actionEvent) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("MyTunesSongView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add song");
        stage.setResizable(false);
        stage.showAndWait();
        updateSongTable();
    }

    public void btnCloseClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void btnNewPlaylistOnClick(ActionEvent actionEvent) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("MyTunesPlaylistView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add playlist");
        stage.setResizable(false);
        stage.showAndWait();
        updatePlaylistTable();
    }

    public void btnDeletePlaylistOnClick (ActionEvent actionEvent) throws SQLException {
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
        /**
         *ToDo
         *Ask user if he wants to delete playlist
         */
        if (selectedPlaylist != null) {
            logic.deletePlaylistFromDB(selectedPlaylist.getPlaylist_id());
            playlistsObservableList.remove(selectedPlaylist);
        }
        else {
            /**
             *ToDo
             *Implement label that will informs user if no playlist is selected for deletion
             */
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

    public void btnEditPlaylistOnClick(ActionEvent actionEvent) {
    }

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
        String selectedSong = listSongsOnPlaylist.getSelectionModel().getSelectedItem();
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

    public void BtnEditSongOnClick(ActionEvent actionEvent) {
    }

    public void BtnDeleteSongOnClick(ActionEvent actionEvent) throws SQLException {
        Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();
        /**
         *ToDo
         *Ask user if he wants to delete song
         */
        if (selectedSong != null) {
            logic.deleteSongFromDB(selectedSong.getSong_id());
            songsObservableList.remove(selectedSong);
        }
        else {
            /**
             *ToDo
             *Implement label that will informs user if no song is selected for deletion
             */
        }
    }
}
