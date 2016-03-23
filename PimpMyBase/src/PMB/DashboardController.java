package PMB;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.glass.ui.Application;

import Modules.DecryptSQL;
import Modules.FileChooserMod;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController {
	@FXML private Button BtnMenuClose;
	@FXML private MenuItem MenuItemClose;
	@FXML private MenuItem MenuItemOpenDB;
	@FXML private MenuBar MainMenuBar;	
	@FXML private VBox MainRoot;
	@FXML private TreeView<String> ElementsRoot;
	
	private DecryptSQL SQLmanager=new DecryptSQL();
	private ResultSet TablesSet = null;  
	public DashboardController() {// Constructor
		
	}
	public void GetDBFile(){
		Stage stage = (Stage) BtnMenuClose.getScene().getWindow();
		FileChooserMod FileChoose = new FileChooserMod();
		String SuffixDoc="";
		
		File file = FileChoose.start(stage,"Ouverture d'une base");
		
        if (file != null) {
        	SuffixDoc=file.getName().substring(file.getName().length()-3);
        	
        	switch (SuffixDoc)
        	{
        	  case "sql":
        		  System.out.println("base sql");
        		  System.out.println("SuffixDoc : "+SuffixDoc);
        		  try {
        			  TablesSet=SQLmanager.GetEntitiesFromSQL(file.getCanonicalPath(), SuffixDoc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	    break; 
        	  case "db3":
        		  System.out.println("base sql");
        		  System.out.println("SuffixDoc : "+SuffixDoc);
        		  try {
        			  TablesSet=SQLmanager.GetEntitiesFromSQL(file.getCanonicalPath(), SuffixDoc);
        			  GetTreeViewFromResultSet(TablesSet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	    break;        	    
        	  default:
        	    /*Action*/;    
        	    System.out.println("Cas par défaut");
        	}       	

        }		

	}
	private void GetTreeViewFromResultSet(ResultSet EntitiesSet){
		
		//Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("home.ico")));
        TreeItem<String> rootItem = new TreeItem<String> ("Base");
        rootItem.setExpanded(true);
        int numCols=0;
        
        try {

	    	while (EntitiesSet.next()) {
	    		numCols= EntitiesSet.getMetaData().getColumnCount();
	    		
	    	   for (int i = 1; i <= numCols; i++)
	    		   try{
		    		   if(!EntitiesSet.getString(i).isEmpty()){
		   	            	TreeItem<String> item = new TreeItem<String> (EntitiesSet.getString(i));            
		   	            	rootItem.getChildren().add(item);		    			   
		    		   }	    			   
	    		   } catch (java.lang.NullPointerException e) {}    	      
	    	}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        ElementsRoot = new TreeView<String> (rootItem);	

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
