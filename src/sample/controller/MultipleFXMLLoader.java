package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import sample.Main;

import java.net.URL;

public class MultipleFXMLLoader {

    private Pane view;

    public  Pane getPage(String fileName){

        try {

            URL fileURL = Main.class.getResource("/sample/view/" + fileName.trim() + ".fxml");
            if (fileURL == null) {
                throw new java.io.FileNotFoundException("FXML file not found");
            }
            view = new FXMLLoader().load(fileURL);
        }
        catch (Exception e){
            //System.out.println("No page" + fileName + " please ckeck FXML Loader");
        }

        return view;
    }
}
