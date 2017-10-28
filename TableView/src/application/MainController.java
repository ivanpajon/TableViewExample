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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable {
	
	@FXML private AnchorPane anchorPane;
	
	@FXML private TableView<Web> table;

    @FXML private TableColumn<Web, String> columnWeb;
    
    @FXML private Button btnBorrar;
    
    @FXML private Button btnNavegar;
    
    @FXML private TextField tfWeb;
    
    @FXML private Button btnClose;
    
    private ObservableList<Web> webs = FXCollections.observableArrayList();

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

	@FXML void navegar(ActionEvent event) {
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
		catch (IOException e) {
			System.out.println(e);
		}
		catch (NullPointerException e) {
			System.out.println("No se ha seleccionado nada");
		}
		catch (RuntimeException e) {
			System.out.println("Error runtime");
		}
    }
	
	@FXML
    void seleccionarWeb(MouseEvent event) {
		if (!tfWeb.getText().equals("")) {
			Web w = table.getSelectionModel().getSelectedItem();
			tfWeb.setText(w.getNombre());
		}
    }
	
	@FXML
    void cerrarVentana(ActionEvent event) {
		System.exit(0);
    }
	
	@FXML
    void handle(MouseEvent event) {
		//main.primaryStage.setX(event.getScreenX());
        //main.primaryStage.setY(event.getScreenY());
    }

	private ObservableList<Web> getWebs() {
		return webs;
	}
	
}
