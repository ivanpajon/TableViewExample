package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.stage.Stage;

public class MainController implements Initializable {
	
	@FXML private AnchorPane anchorPane, toolbarPane;
	
	@FXML private Pane up, right, down, left;
	
	@FXML private TableView<Web> table;

    @FXML private TableColumn<Web, String> columnWeb;
    
    @FXML private Button btnBorrar, btnNavegar, btnClose, btnMaximize, btnMinimize;
    
    @FXML private TextField tfWeb;
    
    @FXML private ImageView imgMaximize;
    
    private ObservableList<Web> webs = FXCollections.observableArrayList();
    
    private double initX,initY,initHeight,initWidth;
    
    private boolean maximized = false;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		columnWeb.setCellValueFactory(new PropertyValueFactory<Web, String>("nombre"));
        cargarWebs();
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
				} catch (IOException e) {
					System.out.println("Error IO: " + e.getMessage());
				}
			}
		}
	}
	
	@FXML void borrarWeb(ActionEvent event) {
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
	    catch (IOException e) {
			System.out.println(e);
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

	@FXML void navegar(ActionEvent e) {
		// Subproceso
		Runtime r = Runtime.getRuntime();
		Process p = null;
		Web w = new Web(tfWeb.getText());
		
		try {
			if (!w.getNombre().equals("")) {  // Se comprueba que se ha introducido una url
				p = r.exec("rundll32 url.dll,FileProtocolHandler http://" + w.getNombre());
				
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
	
	@FXML void seleccionarWeb(MouseEvent e) {
		Web w = table.getSelectionModel().getSelectedItem();
		tfWeb.setText(w.getNombre());
    }
	
	@FXML void cerrarVentana(ActionEvent e) {
		Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
		//System.exit(0);  // Se fuerza a la aplicacion a cerrarse, usar como ultimo recurso
    }
	
	// TODO: Use relative path string in setImage() instead of getClass().getResource()
	@FXML void maximizarVentana(ActionEvent e) throws IOException {
		Stage stage = (Stage) btnMaximize.getScene().getWindow();
		
        if (!maximized) {  // Si no esta maximizada, se maximiza
            stage.setMaximized(true);  // Se maximiza la ventana
            imgMaximize.setImage(new Image(getClass().getResource("images/minimizeSize_icon.png").toExternalForm()));  // Se cambia el icono de maximizar por el de minimizar
            maximized = true;
        }
        else {  // Si esta maximizada, se minimiza
            stage.setMaximized(false);  // Se minimiza la ventana
            imgMaximize.setImage(new Image(getClass().getResource("images/maximizeSize_icon.png").toExternalForm()));  // Se cambia el icono de minimizar por el de maximizar
            maximized = false;
        }
    }
	
	@FXML void minimizarVentana(ActionEvent e) {
		Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }
	
	@FXML void movePressed(MouseEvent e) {
        initX= e.getSceneX();
        initY = e.getSceneY();
    }
	
	@FXML void moveDragged(MouseEvent e) {
		Stage stage = (Stage)toolbarPane.getScene().getWindow();
        stage.setX(e.getScreenX()-initX);
        stage.setY(e.getScreenY()-initY);
	}
	
	@FXML void resizeVerticalPressed(MouseEvent evt){
        Stage stage = (Stage)up.getScene().getWindow();  // Se obtiene el stage de la ventana actual
        initY = stage.getY();  // Se obtiene el punto del borde superior de la ventana con respecto a la pantalla
        initHeight = stage.getHeight();  // Se obtiene el height de la ventana
    }
    
    /* 
     * Se hace e.getScreenY()-initY para calcular la diferencia entre el punto inicial al actual
     * Se le resta initHeight a (e.getScreenY()-initY) para obtener el nuevo height de la ventana
     */
    @FXML void resizeUpDragged(MouseEvent e){
        Stage stage = (Stage)up.getScene().getWindow();
        stage.setHeight(initHeight-(e.getScreenY()-initY));  // Se le asigna el nuevo height a la ventana
        stage.setY(e.getScreenY());  // Se mueve la ventana junto con el mouse a la vez que se le asigna el nuevo height
    }
    
    //  Se hace e.getScreenY()-initY para calcular el height a partir de la posicion actual del mouse
    @FXML void resizeDownDragged(MouseEvent e){
        Stage stage = (Stage)down.getScene().getWindow();
        stage.setHeight(e.getScreenY()-initY);  // Se establece el height agrandando la ventana hacia abajo, por eso no necesita setY()
    }
    
    
    
	private ObservableList<Web> getWebs() {
		return webs;
	}
	
}
