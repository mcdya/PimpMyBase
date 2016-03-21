package PMB;

import com.sun.glass.ui.Application;

import Modules.FileChooserMod;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController {
	@FXML private Button BtnMenuClose;
	@FXML private MenuItem MenuItemClose;
	@FXML private MenuItem MenuItemOpenDB;
	@FXML private MenuBar MainMenuBar;	
	@FXML private VBox MainRoot;
	
	public DashboardController() {// Constructor
		
	}
	public void GetDBFile(){
		Stage stage = (Stage) BtnMenuClose.getScene().getWindow();
		FileChooserMod FileChoose = new FileChooserMod();
		
		FileChoose.start(stage,"Ouverture d'une base");
	}
    public void handleWindowShownEvent()
    {
    	/*
    	System.out.println("BtnMenuClose pos "+BtnMenuClose.getLayoutX());
    	this.BtnMenuClose.setLayoutX(MainRoot.getPrefWidth()-BtnMenuClose.getMaxWidth()+100);
    	System.out.println("BtnMenuClose pos "+BtnMenuClose.getLayoutX());
    	*/
    }	
	public void Close_DashBoard(){
	    // close the dialog.
	    Stage stage = (Stage) BtnMenuClose.getScene().getWindow();
	    stage.close();
	    System.out.println("Fermeture de l'application");
		System.exit(0);
		
	}
}
