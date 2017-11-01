package application;
	
import application.GUI.AnimationWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
   
   //Static varibales
   public static Stage mainStage;
   
   
	@Override
	public void start(Stage primaryStage) {
		try {
		
			mainStage = primaryStage;
			AnimationWindow mainWindow = new AnimationWindow();
			AnimationWindow.show();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getActiveScene(){
	   return mainStage.getScene();
	}
	
	public static void main(String[] args) {
	   launch(args);
	   
	}

}
