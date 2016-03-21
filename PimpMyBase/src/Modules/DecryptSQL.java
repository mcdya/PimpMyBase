package Modules;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jean-Paul Lasy
 */

public class DecryptSQL {
	
    private Connection connection= null; 
    private final String DB_PATH = System.getProperty("user.dir").toString();
    private final String DB_NAME = ""; //ex: DataBase\\CriterBase.db3
    private DbTools ToolsDataBase=new DbTools();

    public void GetEntitiesFromSQL(String file, String DbType){

    	ResultSet results = null;    	
    	int numCols = 0;
    	System.out.print("start connection ");
    	//connection a la base de données
    	connection=ToolsDataBase.getConnection(file, DbType);
    	

    	//Liste des éléments de la base (table, view...)
    	DatabaseMetaData dma;
		try {
			
			dma = connection.getMetaData();
			
	    	String[] types = new String[1];
	    	types[0] = "TABLE"; //set table type mask (une valeur null permet de retrouver toutes les tables.)

	    	results = dma.getTables(null, null, "%", types);
	    	
	    	while (results.next()) {
	    		numCols= results.getMetaData().getColumnCount();
	    		
	    	   for (int i = 1; i <= numCols; i++)
	    		   try{
		    		   if(!results.getString(i).isEmpty()){
		    			   System.out.print(results.getString(i)+" ");
		    		   }	    			   
	    		   } catch (java.lang.NullPointerException e) {}    	      

	    	}			
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//DbTools.arret("Echec de la methode GetEntityFromSQL");
		}
    	    	
  	
    }
	/* *********************************************************************************************************
	 * GetEntity
	 * Creation et execution d'une requete SELECT à partir d'une table indiquée en paramètre
	 * 
	 */    
    private void GetEntity(String Entity){
    	
    	ResultSet résultats = null;    	
    	String requete = "";
    	
		System.out.println("creation et execution de la requête SELECT pour la table "+Entity);

		requete = "SELECT * FROM "+Entity;

		try {

			Statement stmt = connection.createStatement();
			résultats = stmt.executeQuery(requete);

		} catch (SQLException e) {
			DbTools.arret("Anomalie lors de l'execution de la requête");
		}

		//Parcours des données retournées
		try {

			ResultSetMetaData rsmd = résultats.getMetaData();
			int nbCols = rsmd.getColumnCount();
			boolean encore = résultats.next();

			while (encore) {

				for (int i = 1; i <= nbCols; i++)

					System.out.print(résultats.getString(i) + " ");

				System.out.println();

				encore = résultats.next();

			}

			résultats.close();

		} catch (SQLException e) {

			DbTools.arret(e.getMessage());

		}     	
    }
}
