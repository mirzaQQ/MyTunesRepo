package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDAO {
    ConnectionManager conMan = new ConnectionManager();

    public List<Category> getCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection con = conMan.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM dbo.Category");
            while (rs.next()) {
                int id = rs.getInt("Category_id");
                String name = rs.getString("Name");
                categories.add(new Category(id, name));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return categories;
    }

    public void addCategory(String category) throws SQLException {
        try (Connection con = conMan.getConnection()) {
            Statement stmt = con.createStatement();
            String sql = "INSERT INTO Category (Name) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, category);
            ps.executeUpdate();
            ps.close();
            con.close();
        }
    }
}
