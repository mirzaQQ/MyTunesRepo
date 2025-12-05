package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.PlaylistSong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSongDAO {
    ConnectionManager conMan = new ConnectionManager();

    public List<PlaylistSong> getPlaylistSongsFromDB(int playlistId) throws SQLException {
        List<PlaylistSong> playlistSongs = new ArrayList<>();

        try (Connection con = conMan.getConnection()) {
            String sql = "SELECT ps.*, s.Title From PlaylistSong ps JOIN Songs s ON ps.Song_id = s.Song_id WHERE ps.Playlist_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int playlist_id = rs.getInt("Playlist_id");
                int song_id = rs.getInt("Song_id");
                int position = rs.getInt("Position");
                String title = rs.getString("Title");
                playlistSongs.add(new PlaylistSong(playlist_id, song_id, position, title));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return playlistSongs;
    }

    public void addSongToPlaylist(int playlist, int song) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            String sql = "INSERT INTO PlaylistSong (Playlist_id, Song_id) VALUES (?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, playlist);
            stmt.setInt(2, song);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
