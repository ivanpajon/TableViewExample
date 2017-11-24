package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import fxwindow.fxmove.FXMove;
import fxwindow.fxstage.FXStage;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ManualController implements Initializable {

    @FXML private AnchorPane root, pane1, pane2, pane3;
    
    @FXML private JFXButton btnNext, btnPrevious;
    
    @FXML private Label countLabel;
    
    private FXStage fxstage;
    private FXMove fxmove;
    
    private int showSlide = 0;
    
    @Override
	public void initialize(URL url, ResourceBundle rb) {
    	translateAnimation(0.1, pane2, 600);
		translateAnimation(0.1, pane3, 600);
		
		fxstage = new FXStage(root, "src/application/Splash.fxml");  // Se declara un stage con el root actual y el url del que se abrira
		fxstage.setUndecorated(true);
		
		fxmove = new FXMove(root);
		
		btnPrevious.setDisable(true);
	}
    
    @FXML
    void next(ActionEvent e) throws InterruptedException {
    	if (showSlide == 0) {
    		btnPrevious.setDisable(false);
			translateAnimation(0.5, pane2, -600);
			showSlide++;
			countLabel.setText("2/3");
		}
    	else if (showSlide == 1) {
			translateAnimation(0.5, pane3, -600);
			showSlide++;
			countLabel.setText("3/3");
			btnNext.setText("x");
		}
    	else {
    		FadeTransition ft = new FadeTransition();
    		ft.setDuration(Duration.millis(1000));
    		ft.setNode(root);
    		ft.setFromValue(1);
    		ft.setToValue(0);
    		ft.setOnFinished(e1 -> {fxstage.close();fxstage.open();});  // Se cierra el stage actual y se abre uno nuevo con el fxml que se le indico
    		ft.play();
		}
    }
    
    @FXML
    void previous(ActionEvent e) {
    	if (showSlide == 1) {
    		btnPrevious.setDisable(true);
			translateAnimation(0.5, pane2, 600);
			showSlide--;
			countLabel.setText("1/3");
		}
		else if (showSlide == 2) {
			translateAnimation(0.5, pane3, 600);
			showSlide--;
			countLabel.setText("2/3");
			btnNext.setText(">");
		}
    }
    
    @FXML
    void movePressed(MouseEvent e) {
    	fxmove.movePressed(e);
    }
    
    @FXML
    void moveDragged(MouseEvent e) {
    	fxmove.moveDragged(e);
    }
	
	private void translateAnimation(double duration, Node node, double byX) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), node);
		translateTransition.setByX(byX);
		translateTransition.play();
	}

}