package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.PlaylistSong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSongDAO {
    ConnectionManager conMan = new ConnectionManager();

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
