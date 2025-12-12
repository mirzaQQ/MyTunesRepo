package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.*;
import dk.easv.mytunes.dal.CategoriesDAO;
import dk.easv.mytunes.dal.PlaylistsDAO;
import dk.easv.mytunes.dal.SongsDAO;
import dk.easv.mytunes.dal.PlaylistSongDAO;

import java.sql.SQLException;
import java.util.List;

public class Logic {
   SongsDAO songsDAO = new SongsDAO();
   CategoriesDAO categoriesDAO = new CategoriesDAO();
   PlaylistsDAO playlistsDAO = new PlaylistsDAO();
   PlaylistSongDAO playlistSongDAO = new PlaylistSongDAO();

   public List<Songs> getAllSongsFromDB() throws SQLException {
       return songsDAO.getSongs();
   }
    public void getInfo(String title, String artist, String time,String category, String file) throws SQLException{
        songsDAO.insertSongs(title, artist, time, category, file);
    }
    public void deleteSongFromDB(int id) throws SQLException {
        songsDAO.deleteSong(id);
    }
   public List<Category> getAllCategoryFromDB() throws SQLException {
       return categoriesDAO.getCategories();
   }
   public void addCategory(String category) throws SQLException {
       CategoriesDAO categoriesDAO = new CategoriesDAO();
       categoriesDAO.addCategory(category);
   }
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
    public boolean checkIfPlaylistExists(String playlistName) throws SQLException {
       List<Playlists> playlists = getAllPlaylistsFromDB();
       return playlists.stream().anyMatch(playlist -> playlist.getName().equalsIgnoreCase(playlistName));
   }
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