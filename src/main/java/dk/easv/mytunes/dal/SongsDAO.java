package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Songs;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsDAO {
    ConnectionManager conMan = new ConnectionManager();

    public List<Songs> getSongs() throws SQLException {
        List<Songs> songs = new ArrayList<>();
        try (Connection con = conMan.getConnection()) {
            String sql = "SELECT * FROM Songs";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("Song_id");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                int category = rs.getInt("Category");
                String time = rs.getString("Time");
                String filepath = rs.getString("Filepath");

                songs.add(new Songs(title, artist, category, time, filepath, id));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return songs;
    }
}
