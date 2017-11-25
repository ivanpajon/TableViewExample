package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import fxwindow.fxmove.FXMove;
import fxwindow.fxresize.FXResize;
import fxwindow.fxtoolbar.FXToolbar;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MainController implements Initializable {
	
	@FXML private AnchorPane root, toolbarPane;
	
	@FXML private Pane paneUp, paneRight, paneDown, paneLeft, paneUpLeft, paneUpRight, paneDownRight, paneDownLeft;
	
	@FXML private TableView<Web> table;

    @FXML private TableColumn<Web, String> columnWeb;
    
    @FXML private Button btnBorrar, btnNavegar, btnClose, btnMaximize, btnMinimize, btnWord, btnExcel, btnPowerPoint;
    
    @FXML private TextField tfWeb;
    
    @FXML private ImageView imgMaximize;
    
    private ObservableList<Web> webs = FXCollections.observableArrayList();
    
    private Image minimizeIcon = new Image(getClass().getResource("images/minimizeSize_icon.png").toExternalForm());
    private Image maximizeIcon = new Image(getClass().getResource("images/maximizeSize_icon.png").toExternalForm());
    
    private FXMove fxmove;
    private FXResize fxresize;
    private FXToolbar fxtoolbar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		columnWeb.setCellValueFactory(new PropertyValueFactory<Web, String>("nombre"));
        cargarWebs();
        
        fxmove = new FXMove(root);
    	
    	fxresize = new FXResize(root, paneUp, paneRight, paneDown, paneLeft, paneUpLeft, paneUpRight, paneDownRight, paneDownLeft);
    	
    	fxtoolbar = new FXToolbar(root, minimizeIcon, maximizeIcon);
    	
		FadeTransition ft = new FadeTransition();
		ft.setDuration(Duration.millis(750));
		ft.setNode(root);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.play();
	}
	
	@FXML void word(ActionEvent e) {
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("rundll32 url.dll,FileProtocolHandler WINWORD");
		}
		catch (IOException e1) {
			System.out.println(e1);
		}
    }
	
	@FXML void excel(ActionEvent e) {
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("rundll32 url.dll,FileProtocolHandler EXCEL");
		}
		catch (IOException e1) {
			System.out.println(e1);
		}
    }
	
	@FXML void powerpoint(ActionEvent e) {
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("rundll32 url.dll,FileProtocolHandler POWERPNT");
		}
		catch (IOException e1) {
			System.out.println(e1);
		}
    }
	
	private void cargarWebs() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("webs.txt"));
			
			String linea = null;
			while ((linea = br.readLine()) != null) {
				webs.add(new Web(linea));  // Añadimos la direccion web al array de la tabla
			}
			table.setItems(getWebs());  // Cargamos las webs en la tabla
		}
		catch (FileNotFoundException e) {
			System.out.println(e);
		}
		catch (IOException e) {
			System.out.println(e);
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException e) {
					System.out.println("Error IO: " + e.getMessage());
				}
			}
		}
	}
	
	@FXML private void borrarWeb(ActionEvent e) {
		Web w = table.getSelectionModel().getSelectedItem();
		table.getItems().remove(w);  // Eliminamos la web seleccionada del array ObservableList y de la tabla
		
		File f = new File("webs.txt");
		
	    try {
	    	// Sobreescribimos el archivo para vaciarlo
	    	FileWriter fw = new FileWriter(f, false);
			
			for (Web web : webs) {
				fw.write(web.getNombre()+"\n");  // Escribimos las webs restantes en el archivo sobreescribiendolo
			}
			fw.close();
		}
	    catch (IOException e1) {
			System.out.println(e1);
		}
    }
	
	private void guardarWeb(Web web) {
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter("webs.txt", true));
			
			bw.write(web.getNombre());
			bw.newLine();
		}
		catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo");
		}
		catch (IOException e) {
			System.out.println("Error IO: " + e.getMessage());
		}
		finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					System.out.println("Error IO: " + e.getMessage());
				}
			}
		}
	}

	@FXML private void navegar(ActionEvent e) {
		// Subproceso
		Runtime r = Runtime.getRuntime();
		Web w = new Web(tfWeb.getText());
		
		try {
			if (!w.getNombre().equals("")) {  // Se comprueba que se ha introducido una url
				r.exec("rundll32 url.dll,FileProtocolHandler http://" + w.getNombre());
				
				int coincidencia=0;
				for (int i=0; i<webs.size(); i++) {
					if (w.getNombre().equals(webs.get(i).getNombre())) {coincidencia++;}  // Comprobamos que la web introducida no este guardada
				}
				
				if (coincidencia == 0) {
					webs.add(w);  // Añadimos la web al array
					guardarWeb(w);  // Guardamos la web en el archivo de texo
				}
			}
		}
		catch (IOException e1) {
			System.out.println(e1);
		}
		catch (NullPointerException e1) {
			System.out.println("No se ha seleccionado nada");
		}
		catch (RuntimeException e1) {
			System.out.println("Error runtime");
		}
    }
	
	@FXML private void seleccionarWeb(MouseEvent e) {
		Web w = table.getSelectionModel().getSelectedItem();
		tfWeb.setText(w.getNombre());
    }
	
	@FXML private void cerrarVentana(ActionEvent e) {
		fxtoolbar.closeWindow();
    }
	
	@FXML private void maximizarVentana(ActionEvent e) {
		fxtoolbar.maximizeWindow(fxresize, imgMaximize);
    }
	
	@FXML private void minimizarVentana(ActionEvent e) {
		fxtoolbar.iconifyWindow();
    }
	
	@FXML private void movePressed(MouseEvent e) {
		fxmove.movePressed(e);
    }
	
	@FXML private void moveDragged(MouseEvent e) {
		fxmove.moveDragged(e);
	}
	
	@FXML private void moveReleased(MouseEvent e) {
		if (fxmove.isDraggedToUp(e)) {
			fxmove.dragToUp(e);
		}
		else {
			fxmove.dragToCenter(e);
		}
	}
	
	@FXML private void resizeVerticalPressed(MouseEvent e) {
        fxresize.resizeVerticalPressed(e);
    }
	
    @FXML private void resizeUpDragged(MouseEvent e) {
        fxresize.resizeUpDragged(e);
    }
    
    @FXML private void resizeDownDragged(MouseEvent e) {
        fxresize.resizeDownDragged(e);
    }
    
    @FXML private void resizeHorizontalPressed(MouseEvent e) {
        fxresize.resizeHorizontalPressed(e);
    }
    
    @FXML private void resizeLeftDragged(MouseEvent e) {
        fxresize.resizeLeftDragged(e);
    }
    
    @FXML private void resizeRightDragged(MouseEvent e) {
        fxresize.resizeRightDragged(e);
    }
    
    @FXML private void resizeCornerPressed(MouseEvent e) {
    	fxresize.resizeCornerPressed(e);
    }
    
    @FXML private void resizeUpLeftDragged(MouseEvent e) {
    	fxresize.resizeUpLeftDragged(e);
    }
    
    @FXML private void resizeUpRightDragged(MouseEvent e) {
    	fxresize.resizeUpRightDragged(e);
    }
    
    @FXML private void resizeDownRightDragged(MouseEvent e) {
    	fxresize.resizeDownRightDragged(e);
    }
    
    @FXML private void resizeDownLeftDragged(MouseEvent e) {
    	fxresize.resizeDownLeftDragged(e);
    }
    
	private ObservableList<Web> getWebs() {
		return webs;
	}
	
}
