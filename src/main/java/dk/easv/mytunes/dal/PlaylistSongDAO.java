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
            String sql = "SELECT ps.*, s.Title From PlaylistSong ps JOIN Songs s ON ps.Song_id = s.Song_id WHERE ps.Playlist_id = ? ORDER BY ps.Position ASC";
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
            String getMaxPos = "SELECT MAX(Position) as maxPos FROM PlaylistSong WHERE Playlist_id = ?";
            PreparedStatement stmtMax = con.prepareStatement(getMaxPos);
            stmtMax.setInt(1, playlist);
            ResultSet rsMax = stmtMax.executeQuery();

            int newPos = 1;
            if (rsMax.next() && rsMax.getInt("maxPos") > 0) {
                newPos = rsMax.getInt("maxPos") + 1;
            }

            String sql = "INSERT INTO PlaylistSong (Playlist_id, Song_id, Position) VALUES (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, playlist);
            stmt.setInt(2, song);
            stmt.setInt(3, newPos);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteSongFromPlaylist(int playlist, int position) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            String getPos = "DELETE FROM PlaylistSong WHERE Playlist_id = ? AND Position = ?";
            PreparedStatement stmtPos = con.prepareStatement(getPos);
            stmtPos.setInt(1, playlist);
            stmtPos.setInt(2, position);
            stmtPos.executeUpdate();

            String updatePos = "UPDATE PlaylistSong SET Position = Position - 1 WHERE Playlist_id = ? AND Position > ?";
            PreparedStatement stmtUpdate = con.prepareStatement(updatePos);
            stmtUpdate.setInt(1, playlist);
            stmtUpdate.setInt(2, position);
            stmtUpdate.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
