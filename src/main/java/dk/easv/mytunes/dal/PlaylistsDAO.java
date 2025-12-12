package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Playlists;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistsDAO {
    ConnectionManager conMan = new ConnectionManager();

    public List<Playlists> getPlaylists() throws SQLException {
        List<Playlists> playlists = new ArrayList<>();

        try (Connection con = conMan.getConnection()) {
            String sql = "SELECT * FROM Playlists";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("Playlist_id");
                String name = rs.getString("Name");
                int songsnumber = rs.getInt("SongsNumber");
                String totaltime = rs.getString("TotalTime");

                playlists.add(new Playlists(id, name, songsnumber, totaltime));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return playlists;
    }

    public void addPlaylist(String playlistName) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            String sql = "INSERT INTO Playlists (Name, SongsNumber, TotalTime) VALUES (?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, playlistName);
            stmt.setInt(2, 0);
            stmt.setString(3, "0:00");
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void deletePlaylist(int playlist) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            String sql = "DELETE FROM Playlists WHERE Playlist_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, playlist);
            stmt.executeUpdate();
        }
    }

    public void updatePlaylist(int playlistId) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            String getSongs = "SELECT s.Time FROM PlaylistSong ps JOIN Songs s ON ps.Song_id = s.Song_id WHERE ps.Playlist_id = ?";
            PreparedStatement stmt = con.prepareStatement(getSongs);
            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            int totalSeconds = 0;
            int totalSongs = 0;

            while (rs.next()) {
                String time = rs.getString("Time");
                String[] parts = time.split(":");
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                totalSeconds += minutes * 60 + seconds;
                totalSongs++;
            }

            int totalMinutes = totalSeconds / 60;
            int totalSecondsLeft = totalSeconds % 60;
            String totalTime = totalMinutes + ":" + String.format("%02d", totalSecondsLeft);

            String update = "UPDATE Playlists SET SongsNumber = ?, TotalTime = ? WHERE Playlist_id = ?";
            PreparedStatement stmt2 = con.prepareStatement(update);
            stmt2.setInt(1, totalSongs);
            stmt2.setString(2, totalTime);
            stmt2.setInt(3, playlistId);
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
