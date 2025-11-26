package dk.easv.mytunes.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import jdk.jfr.Category;

import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;


public class NewSongController implements Initializable {

    @FXML
    private TextField txtMore;

    @FXML
    private ComboBox category;
    private ObservableList<MenuItem> categories =  FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        categories.add(new  MenuItem("Pop"));
        categories.add(new  MenuItem("Rock"));
        categories.add(new  MenuItem("Jazz"));

        Dropdownmenu();


    }

    public void btnClickedMore(ActionEvent actionEvent) {
        txtMore.setVisible(true);


    }

    public void txtEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String genre = txtMore.getText();
            categories.add(new  MenuItem(genre));
            Dropdownmenu();


        }

    }
    private void Dropdownmenu(){
        /**
        ObservableList<MenuItem> results = FXCollections.observableArrayList();
        Boolean duplicate = false;
        for (MenuItem item : categories) {
            for (MenuItem item2 : categories) {
                if (item.getText().equals(item2.getText())) {}
            }
        }*/

        for(MenuItem item : categories){

            category.getItems().add(item.getText());


        }

    }

}
