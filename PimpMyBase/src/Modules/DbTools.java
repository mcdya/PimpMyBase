package Modules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;

/**
*
* @author Jean-Paul Lasy
*/
public class DbTools {
	
	private String StrDriver="";
	private String DBurl="";
	private String MsgDriverErr="";
	private Connection connection;
    /************************************************************************************************************************************
     * Se connecter
     */
    public Connection getConnection(String file,String DbType) {   	
        Properties connectionProps = new Properties();
        connectionProps.put("user", "");
        connectionProps.put("password", "");
        
        LoadDriver(DbType);//Chargement du pilote en fonction du format de base (Sql,SQlite,...)
        DBurl=GetDBurl();//Renvoi la chaine de caractère utilisée pour la connexion à la base de données
        
        try {
			this.connection = DriverManager.getConnection(DBurl + file,connectionProps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("La connexion a échoué (DbTools.getConnection)");
		}
        
        return this.connection;
    }
    /************************************************************************************************************************************
     * Se connecter via un datasource
     * @throws NamingException 
     */
    public Connection getDataSourceConnect(String file,String DbType) throws NamingException {   	
    	
        LoadDriver(DbType);
        DBurl=GetDBurl();//Renvoi la chaine de caractère utilisée pour la connexion à la base de données

      //initialisation du contexte 
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, 
              "com.sun.jndi.fscontext.RefFSContextFactory"); 
        InitialContext ic = new InitialContext(); 
          
        // création d'une référence sur la DataSource 
        Reference ref = new Reference("javax.sql.DataSource", 
              "org.apache.commons.dbcp.BasicDataSourceFactory", 
              null); 
        ref.add(new StringRefAddr("driverClassName", StrDriver)); 
        ref.add(new StringRefAddr("url", DBurl+file)); 
        ref.add(new StringRefAddr("username", "")); 
        ref.add(new StringRefAddr("password", "")); 
          
        //liaison de la DataSource au contexte 
        ic.rebind("jdbc/MaDataSource", ref);

      //récupération de la DataSource à partir du contexte 
        Context ctx = new InitialContext(); 
        DataSource source = (DataSource)ctx.lookup("jdbc/MaDataSource"); 
          
        //récupération d'une Connection 
        try {
        	this.connection = source.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("La connexion via datasource a échoué (source.getConnection)");
		}         
        
        return this.connection;
    } 
    /************************************************************************************************************************************
     * Fermer la connexion
     */
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DecryptSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
	/* *********************************************************************************************************
	 * LoadDriver
	 * Chargement du pilote en fonction du format de base (Sql,SQlite,...)
	 * 
	 */	
	public void LoadDriver(String DbType){		
		
		MsgDriverErr="";
		DbType=DbType.toLowerCase();
		
    	switch (DbType)
    	{
    	  case "sql"://Sql
    		  System.out.println("base sql");
    		  StrDriver="sun.jdbc.odbc.JdbcOdbcDriver";
    		  DBurl="jdbc:odbc:";
    		  
    		  //Message en cas d'erreur
    		  MsgDriverErr="Impossible de charger le pilote jdbc:odbc";
    		  break;     
    		  
    	  case "db3"://SQlite
    		  System.out.println("base SQlite");
    		  StrDriver="org.sqlite.JDBC";
    		  DBurl="jdbc:sqlite:";
    		  
    		  //Message en cas d'erreur
    		  MsgDriverErr="Impossible de charger le pilote org.sqlite.JDBC";    		  
    		  break; 
    		  
    	  case "dbf"://Java DB
    		  System.out.println("base Java DB");
    		  StrDriver="org.apache.derby.jdbc.EmbeddedDriver";
    		  DBurl="jdbc:derby:";
    		  
    		  //Message en cas d'erreur
    		  MsgDriverErr="Impossible de charger le pilote org.apache.derby.jdbc.EmbeddedDriver";    		  
    		  break;     	
    		  
    	  case "frm "://mysql
    		  System.out.println("base mysql - schéma ");
    		  StrDriver="com.mysql.jdbc.Driver";
    		  DBurl="jdbc:mysql:";
    		  
    		  //Message en cas d'erreur
    		  MsgDriverErr="Impossible de charger le pilote com.mysql.jdbc.Driver";    		  
    		  break; 
    		  
    	  case "myd"://mysql
    		  System.out.println("base mysql - données des tables");
    		  StrDriver="com.mysql.jdbc.Driver";
    		  DBurl="jdbc:mysql:";
    		  
    		  //Message en cas d'erreur
    		  MsgDriverErr="Impossible de charger le pilote com.mysql.jdbc.Driver";    		  
    		  break;     		  
    		  
    	  default:
    		  MsgDriverErr="Type de connexion introuvable";              
    	} 	
    	
    	//StrDriver="com.mysql.jdbc.driver";
    	//DBurl="jdbc:derby:";
		// chargement du pilote
    	if(!StrDriver.isEmpty()){
			try {
				Class.forName(StrDriver);
			} catch (ClassNotFoundException e) {
				arret(MsgDriverErr);
			}    		
    	}else{
    		arret(MsgDriverErr);
    	}
		
	}
	/* *********************************************************************************************************
	 * GetStrDriver
	 * Renvoi la chaine de caractère utilisée pour définir le pilote 
	 * Remarque: la methode LoadDriver doit être lancée au préalable
	 * 
	 */
	public String GetStrDriver(){
		return StrDriver;
	}
	/* *********************************************************************************************************
	 * GetDBurl
	 * Renvoi la chaine de caractère utilisée pour la connexion à la base de données 
	 * 
	 */
	public String GetDBurl(){
		
		if(DBurl.isEmpty()){
			MsgDriverErr="Chaine de caractère utilisée pour la connexion à la base de données introuvable (DBurl)";
			arret(MsgDriverErr);
		}
		return DBurl;
	}
	public static void arret(String message) {
		System.err.println(message);
		System.exit(99);
	}	
}
