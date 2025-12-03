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
            stmt.close();
            con.close();
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
            stmt.close();
            con.close();

        }
    }
}
