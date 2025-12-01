package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Category;
import dk.easv.mytunes.be.Songs;
import dk.easv.mytunes.dal.CategoriesDAO;
import dk.easv.mytunes.dal.SongsDAO;

import java.sql.SQLException;
import java.util.List;

public class Logic {
   SongsDAO songsDAO = new SongsDAO();
   CategoriesDAO categoriesDAO = new CategoriesDAO();

   public List<Songs> getAllSongsFromDB() throws SQLException {
       return songsDAO.getSongs();
   }

   public List<Category> getAllCategoryFromDB() throws SQLException {
       return categoriesDAO.getCategories();
   }
}