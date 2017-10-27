package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
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

public class MainController implements Initializable {
	
	@FXML private TableView<Web> table;

    @FXML private TableColumn<Web, String> columnWeb;
    
    @FXML private Button btnBorrar;
    
    @FXML private Button btnNavegar;
    
    @FXML private TextField tfWeb;
    
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
				webs.add(new Web(linea));  // Añadimos la direccion web al array
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
		table.getItems().remove(w);
    }
	
	@FXML void navegar(ActionEvent event) {
		// Subproceso
		Runtime r = Runtime.getRuntime();
		Process p = null;
		Web w = new Web(tfWeb.getText());
		
		try {
			p = r.exec("rundll32 url.dll,FileProtocolHandler http://" + w.getNombre());
			
			int coincidencia=0;
			for (int i=0; i<webs.size(); i++) {
				if (w.getNombre().equals(webs.get(i).getNombre())) {coincidencia++;}  // Comprobamos que la web introducida no este guardada
			}
			
			if (coincidencia == 0) {
				webs.add(w);  // Añadimos la web al array
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
    }
	
	@FXML
    void seleccionarWeb(MouseEvent event) {
		Web w = table.getSelectionModel().getSelectedItem();
		tfWeb.setText(w.getNombre());
    }

	private ObservableList<Web> getWebs() {
		return webs;
	}
	
}
