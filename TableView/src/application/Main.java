package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getResource("Main.fxml"));
	        Scene scene = new Scene(root);
	        primaryStage.setTitle("FXML Welcome");
	        primaryStage.setScene(scene);
	        primaryStage.initStyle(StageStyle.TRANSPARENT);  // Le quita los bordes a la ventana
	        scene.setFill(Color.TRANSPARENT);  // Le quita los bordes a la ventana
	        primaryStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
