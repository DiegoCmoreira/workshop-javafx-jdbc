package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.services.DepartamentoService;

public class FormaDepartamentoController implements Initializable{

	private Departamento entidade;
	
	private DepartamentoService service;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnCancelar;
	
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}
	
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}
	
	@FXML
	private void onBtnSalvarAction(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entidade = getFormData();
			service.saveOrUpdate(entidade);
			Utils.palcoAtual(evento).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Departamento getFormData() {
		Departamento obj = new Departamento();

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utils.palcoAtual(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() {
		if(entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtName.setText(entidade.getName());
	}

}
