package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.*;
import dk.easv.mytunes.dal.CategoriesDAO;
import dk.easv.mytunes.dal.PlaylistsDAO;
import dk.easv.mytunes.dal.SongsDAO;
import dk.easv.mytunes.dal.PlaylistSongDAO;

import java.sql.SQLException;
import java.util.List;

public class Logic {
    /**
     * Initializes the DAO objects for database access.
     */
    SongsDAO songsDAO = new SongsDAO();
    CategoriesDAO categoriesDAO = new CategoriesDAO();
    PlaylistsDAO playlistsDAO = new PlaylistsDAO();
    PlaylistSongDAO playlistSongDAO = new PlaylistSongDAO();

    /**
     * All the methods related to Songs.
     * <p>
     * getAllSongsFromDB - retrieves all songs from the database.
     * getInfo - inserts a new song into the database with title, artist, time, category, and file path.
     * deleteSongFromDB - deletes a song from the database by its ID.
     * updateSong - updates the title, artist, and category of a song in the database.
     */
    public List<Songs> getAllSongsFromDB() throws SQLException {
        return songsDAO.getSongs();
    }
    public void getInfo(String title, String artist, String time, String category, String file) throws SQLException {
        songsDAO.insertSongs(title, artist, time, category, file);
    }
    public void deleteSongFromDB(int id) throws SQLException {
        songsDAO.deleteSong(id);
    }
    public void updateSong(int songId, String title, String artist, String category) throws SQLException {
        songsDAO.updateSong(songId, title, artist, category);
    }

    /**
     * All the methods related to Categories.
     * <p>
     * getAllCategoryFromDB - retrieves all categories from the database.
     * addCategory - adds a new category to the database.
     */
    public List<Category> getAllCategoryFromDB() throws SQLException {
        return categoriesDAO.getCategories();
    }
    public void addCategory(String category) throws SQLException {
        CategoriesDAO categoriesDAO = new CategoriesDAO();
        categoriesDAO.addCategory(category);
    }

    /**
     * All the methods related to Playlists.
     * <p>
     * addPlaylist - adds a new playlist to the database.
     * deletePlaylistFromDB - deletes a playlist from the database by its ID.
     * getAllPlaylistsFromDB - retrieves all playlists from the database.
     * updatePlaylist - updates playlist information in the database.
     * updatePlaylistName - updates the name of a playlist in the database.
     * checkIfPlaylistExists - checks if a playlist with the given name already exists in the database.
     */
    public void addPlaylist(String playlist) throws SQLException {
        playlistsDAO.addPlaylist(playlist);
    }
    public void deletePlaylistFromDB(int playlist) throws SQLException {
        playlistsDAO.deletePlaylist(playlist);
    }
    public List<Playlists> getAllPlaylistsFromDB() throws SQLException {
        return playlistsDAO.getPlaylists();
    }
    public void updatePlaylist(int playlistId) throws SQLException {
        playlistsDAO.updatePlaylist(playlistId);
    }
    public void updatePlaylistName(int playlistId, String newName) throws SQLException {
        playlistsDAO.updatePlaylistName(playlistId, newName);
    }
    public boolean checkIfPlaylistExists(String playlistName) throws SQLException {
        List<Playlists> playlists = getAllPlaylistsFromDB();
        return playlists.stream().anyMatch(playlist -> playlist.getName().equalsIgnoreCase(playlistName));
    }

    /**
     * All the methods related to Playlist Songs.
     * <p>
     * getAllPlaylistSongsFromDB - retrieves all songs from a specific playlist by playlist ID.
     * addSongToPlaylist - adds a song to a playlist by song ID and playlist ID.
     * deleteSongFromPlaylist - removes a song from a playlist by position.
     * moveSongUpInDB - moves a song up one position in the playlist.
     * moveSongDownInDB - moves a song down one position in the playlist.
     */
    public List<PlaylistSong> getAllPlaylistSongsFromDB(int playlistId) throws SQLException {
        return playlistSongDAO.getPlaylistSongsFromDB(playlistId);
    }
    public void addSongToPlaylist(int songId, int playlistId) throws SQLException {
        playlistSongDAO.addSongToPlaylist(playlistId, songId);
    }
    public void deleteSongFromPlaylist(int playlistId, int position) throws SQLException {
        playlistSongDAO.deleteSongFromPlaylist(playlistId, position);
    }
    public void moveSongUpInDB(int playlistId, int position) throws SQLException {
        playlistSongDAO.moveSongUpInDB(playlistId, position);
    }
    public void moveSongDownInDB(int playlistId, int position, int maxPosition) throws SQLException {
        playlistSongDAO.moveSongDownInDB(playlistId, position, maxPosition);
    }
}