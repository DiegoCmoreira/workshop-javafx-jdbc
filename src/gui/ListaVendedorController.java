package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.services.DepartamentoService;
import model.services.VendedorService;

public class ListaVendedorController implements Initializable, DataChangeListener {

	private VendedorService service;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML
	private TableColumn<Vendedor, String> tableColumnNome;
	
	@FXML
	private TableColumn<Vendedor, String> tableColumnEmail;

	@FXML
	private TableColumn<Vendedor, Date> tableColumnDataNascimento;
	
	@FXML
	private TableColumn<Vendedor, Double> tableColumnSalarioBase;
	
	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnEDIT;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnREMOVE;

	@FXML
	private Button btnNovo;

	public void setVendedorService(VendedorService service) {
		this.service = service;
	}

	private ObservableList<Vendedor> obsList;

	@FXML
	public void onBtnNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.palcoAtual(evento);
		Vendedor obj = new Vendedor();
		createDialogForm(obj, "/gui/FormaVendedor.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InitializeNodes();
	}

	private void InitializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatoTabelaColumnData(tableColumnDataNascimento, "dd/MM//yyyy");
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatoTabelaColumnDouble(tableColumnSalarioBase, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service está nulo");
		}

		List<Vendedor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVendedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Vendedor obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			FormaVendedorController controle = loader.getController();
			controle.setVendedor(obj);
			controle.setServices(new VendedorService(), new DepartamentoService());
			controle.loadObjetoAssociado();
			controle.updateFormData();
			controle.inscreverDataChangeListener(this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite data Vendedor");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.getStackTrace();
			Alerts.showAlert("IO Exception", "Error load view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/FormaVendedor.fxml", Utils.palcoAtual(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Vendedor obj) {
		Optional<ButtonType> resultado = Alerts.showConfirmation("Confirme", "Tem certeza que quer deletar?");
		
		if(resultado.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service está nulo");
			}
			try {
				service.remover(obj);
				updateTableView();
				
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro removendo obj", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
