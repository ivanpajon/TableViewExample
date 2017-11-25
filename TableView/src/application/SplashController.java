package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXSpinner;

import fxwindow.fxmove.FXMove;
import fxwindow.fxstage.FXStage;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class SplashController implements Initializable {

	@FXML private AnchorPane root;

    @FXML private JFXSpinner spinner;
    
    private FXMove fxmove;
    private FXStage fxstage;
    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
    	fxstage = new FXStage(root, "src/application/Main.fxml");  // Se declara un stage con el root actual y el url del que se abrira
    	fxstage.setUndecorated(true);
    	fxmove = new FXMove(root);
    	
    	FadeTransition ft = new FadeTransition();
		ft.setDuration(Duration.millis(1000));
		ft.setNode(root);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.setOnFinished(e -> delay(3));  // Se establece un delay antes de abrir el siguiente stage automaticamente
		ft.play();
	}
    
    private void fadeOut() {
    	FadeTransition ft = new FadeTransition();
		ft.setDuration(Duration.millis(2000));
		ft.setNode(root);
		ft.setFromValue(1);
		ft.setToValue(0);
		ft.setOnFinished(e -> {fxstage.close();fxstage.open();});  // Se cierra el stage actual y se abre uno nuevo con el fxml que se le indico
		ft.play();
    }
    
    @FXML void movePressed(MouseEvent e) {
    	fxmove.movePressed(e);
    }

    @FXML void moveDragged(MouseEvent e) {
    	fxmove.moveDragged(e);
    }
    
    private void delay(int seconds) {
    	ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    	exec.schedule(() -> fadeOut(), seconds, TimeUnit.SECONDS);  // Se establece que funcion debe ejecutarse y dentro de cuanto tiempo
    }
}
