package PMB;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			VBox root = (VBox)loader.load(DashboardController.class.getResourceAsStream("Dashboard.fxml"));
			Scene scene = new Scene(root,820,600);
			scene.getStylesheets().add(getClass().getResource("/ressources/application.css").toExternalForm());
			
			final DashboardController controller = (DashboardController)loader.getController();
			
	        //remove window decoration
	        primaryStage.initStyle(StageStyle.UNDECORATED);		
	        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>()
	        {
	            @Override
	            public void handle(WindowEvent window)
	            {
	            	controller.handleWindowShownEvent();
	            }
	        });	        
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
