package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartamentoService;
import model.services.VendedorService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/ListaVendedor.fxml", (ListaVendedorController controle) -> {
			controle.setVendedorService(new VendedorService());
			controle.updateTableView();
		});
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/ListaDepartamento.fxml", (ListaDepartamentoController controle) -> {
			controle.setDepartamentoService(new DepartamentoService());
			controle.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	@Override
	public synchronized void initialize(URL url, ResourceBundle rb) {
	}

	private <T> void loadView(String absoluteView, Consumer<T> acaoDeInicializacao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			T controle = loader.getController();
			acaoDeInicializacao.accept(controle);
		} catch (IOException e) {
			Alerts.showAlert("IO Exeption", "Error loading View", e.getMessage(), AlertType.ERROR);
		}
	}

}
