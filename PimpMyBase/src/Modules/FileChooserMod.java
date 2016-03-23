package Modules;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Modules.DecryptSQL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*********************************************************************************************************************************************
 * 			
 * 			Affiche une fenetre pour la sélection d'un document
 * 
 * 
 *********************************************************************************************************************************************/
public final class FileChooserMod extends Application {
 
    private Desktop desktop = Desktop.getDesktop();
    private static String WindowsTitle="";
    private DecryptSQL SQLmanager=new DecryptSQL();
    
    @Override
    public void start(final Stage stage) {
        stage.setTitle("File Chooser Sample");
 
        final FileChooser fileChooser = new FileChooser();
 
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openFile(file);
        }
    }
    public File start(final Stage stage, String Title) {

    	stage.setTitle(Title);
        WindowsTitle=Title;
        final FileChooser fileChooser = new FileChooser();
        
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);

        return file;
    } 
    
    public static void main(String[] args) {
        Application.launch(args);
    }
         
    private static void configureFileChooser(final FileChooser fileChooser){                           
        fileChooser.setTitle(WindowsTitle);
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        ); 
    }
            
 
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                FileChooserMod.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
}
