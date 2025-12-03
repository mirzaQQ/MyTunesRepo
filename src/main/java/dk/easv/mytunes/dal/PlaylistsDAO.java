package dk.easv.mytunes.dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PlaylistsDAO {
    ConnectionManager conMan = new ConnectionManager();
    public void addPlaylist(String playlistName) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            String sql = "INSERT INTO Playlist (Name) VALUES (?)";
            Statement stmt = con.createStatement();
        }
    }

}
