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
    
	/* *********************************************************************************************************
	 * GetEntitiesFromSQL
	 * Affiche la liste des tables
	 * 
	 */ 
    public ResultSet GetEntitiesFromSQL(String file, String DbType){

    	ResultSet results = null;    	
    	int numCols = 0;
    	System.out.print("start connection ");
    	//connection a la base de donn�es
    	connection=ToolsDataBase.getConnection(file, DbType); 	
    	
    	//Liste des �l�ments de la base (table, view...)
    	DatabaseMetaData dma;
    	
		try {
			
			dma = connection.getMetaData();
			
	    	String[] types = new String[1];
	    	types[0] = "TABLE"; //set table type mask (une valeur null permet de retrouver toutes les tables.)

	    	results = dma.getTables(null, null, "%", types);
	    				
	    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//DbTools.arret("Echec de la methode GetEntityFromSQL");
		}
		
		return results;
    	    		
    }
	/* *********************************************************************************************************
	 * GetEntity
	 * Creation et execution d'une requete SELECT � partir d'une table indiqu�e en param�tre
	 * 
	 */    
    public ResultSet GetEntity(String Entity){
    	
    	ResultSet rs = null;    	
    	String requete = "";
    	
		System.out.println("creation et execution de la requ�te SELECT pour la table "+Entity);

		requete = "SELECT * FROM "+Entity;

		try {

			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(requete);
			
		} catch (SQLException e) {
			DbTools.arret("Anomalie lors de l'execution de la requ�te");
		}

    	return rs;
    }
}
