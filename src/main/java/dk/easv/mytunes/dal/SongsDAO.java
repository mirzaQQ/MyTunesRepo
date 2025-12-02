package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Songs;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import dk.easv.mytunes.bll.Logic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsDAO {
    ConnectionManager conMan = new ConnectionManager();

    public List<Songs> getSongs() throws SQLException {
        List<Songs> songs = new ArrayList<>();

        try (Connection con = conMan.getConnection()) {
            String sql = "SELECT Song_id, Title, Artist, C.Name AS 'Category', Time, Filepath FROM Songs JOIN dbo.Category C on C.Category_id = Category";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("Song_id");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String category = rs.getString("Category");
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
    public void insertSongs(String title, String artist,String time,String category, String file) throws SQLException {
        try (Connection con = conMan.getConnection()) {

            String sql_categID = "SELECT Category_id FROM Category WHERE Name = ?";
            PreparedStatement stmtid = con.prepareStatement(sql_categID);
            stmtid.setString(1, category);
            ResultSet rs = stmtid.executeQuery();
            rs.next();
            int id = rs.getInt("Category_id");

            String sql = "INSERT INTO Songs (Title, Artist, Category, Time ,Filepath) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,title);
            stmt.setString(2, artist);
            stmt.setInt(3,id);
            stmt.setString(4,time);
            stmt.setString(5,file);
            stmt.executeUpdate();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public void deleteSong(int id) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            String sql = "DELETE FROM Songs WHERE Song_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}