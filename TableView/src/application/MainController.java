package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.table.DefaultTableModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController implements Initializable {
	
	@FXML private TableView<Web> table;

    @FXML private TableColumn<Web, String> columnWeb;
    
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

	private ObservableList<Web> getWebs() {
		return webs;
	}
	
}
