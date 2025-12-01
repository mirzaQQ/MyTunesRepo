package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.Songs;
import dk.easv.mytunes.dal.SongsDAO;

import java.sql.SQLException;
import java.util.List;

public class Logic {
   SongsDAO songsDAO = new SongsDAO();

   public List<Songs> getAllSongsFromDB() throws SQLException {
       return songsDAO.getSongs();
   }
}