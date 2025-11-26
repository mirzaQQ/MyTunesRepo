package dk.easv.mytunes.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import jdk.jfr.Category;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class NewSongController implements Initializable {

    @FXML
    private ComboBox category;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<MenuItem> categories =  FXCollections.observableArrayList();
        categories.add(new  MenuItem("Pop"));
        categories.add(new  MenuItem("Rock"));
        categories.add(new  MenuItem("Jazz"));

        category.setItems(categories);
    }
}
