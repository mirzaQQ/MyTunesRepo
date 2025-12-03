package dk.easv.mytunes.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PlaylistsDAO {
    ConnectionManager conMan = new ConnectionManager();

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
    }

}
