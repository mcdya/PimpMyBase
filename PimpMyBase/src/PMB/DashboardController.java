package PMB;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sun.glass.ui.Application;

import Modules.DbTools;
import Modules.DecryptSQL;
import Modules.FileChooserMod;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DashboardController {
	@FXML private Button BtnMenuClose;
	@FXML private MenuItem MenuItemClose;
	@FXML private MenuItem MenuItemOpenDB;
	@FXML private MenuBar MainMenuBar;	
	@FXML private VBox MainRoot;
	@FXML private TreeView<String> ElementsRoot;
	
	@FXML private TableView<ObservableList<String>> ElementsTable;
	
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
	@SuppressWarnings({ "unchecked", "rawtypes", "rawtypes" })
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
		    			   if(!EntitiesSet.getString(i).equals("TABLE")){
			   	            	TreeItem<String> item = new TreeItem<String> (EntitiesSet.getString(i));            
			   	            	rootItem.getChildren().add(item);		    				   
		    			   }		    			   
		    		   }	    			   
	    		   } catch (java.lang.NullPointerException e) {}    	
	    	   
	    	}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	
        ElementsRoot.setRoot(rootItem);
        ElementsRoot.setShowRoot(true);
        ElementsRoot.setEditable(false);//mode edition désactivé
        ElementsRoot.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);//Selection unique
        
        //Ajout du listener pour gérer l'event click
        ElementsRoot.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                System.out.println("Selected Text : " + selectedItem.getValue());
                if(!selectedItem.getValue().isEmpty()){
                	 SetTableToTableView(selectedItem.getValue());
                }
               
            }

          });        
	}
	/* *********************************************************************************************************
	 * SetTableToTableView
	 * Creation et execution d'une requete SELECT pour afficher une table indiquée en paramètre 
	 * dans la TableView
	 * 
	 */ 	
	@SuppressWarnings("unchecked")
	public void SetTableToTableView(String TableSelect){

		ResultSet rs = SQLmanager.GetEntity(TableSelect);
		ObservableList<TableColumn> Cols = FXCollections.observableArrayList(); 
		List<String> ListRws = new ArrayList<String>();
		ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
		
		//Parcours des données retournées
		try {

			ResultSetMetaData rsmd = rs.getMetaData();
			int nbCols = rsmd.getColumnCount();
			boolean encore = rs.next();
			
			//Insertion des colonnes
			ElementsTable.getColumns().clear();//reinitialisation des colonnes
			ElementsTable.getItems().clear();//reinitialisation des lignes
			
			for (int i = 0; i <= nbCols; i++){
				final int j = i;
				TableColumn NameCol = new TableColumn(rsmd.getColumnName(i+1));				
                
				NameCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });

				ElementsTable.getColumns().addAll(NameCol);
				Cols.add(NameCol);	
				
			}							
			
			while (encore) {
				
				ObservableList<String> row = FXCollections.observableArrayList();
				String Line="";
				
				for (int i = 1; i <= nbCols; i++){
					System.out.print(rs.getString(i) + " ");	
					Line=Line+rs.getString(i) + " ";
					row.add(rs.getString(i));
				}
				data.add(row);
				encore = rs.next();

			}
			ElementsTable.setItems(data);
			rs.close();

		} catch (SQLException e) {
			DbTools.arret(e.getMessage());
		} 
		
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
