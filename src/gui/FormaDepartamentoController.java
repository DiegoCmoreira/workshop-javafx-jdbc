package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;

public class FormaDepartamentoController implements Initializable{

	private Departamento entidade;
	
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
	
	@FXML
	private void onBtnSalvarAction() {
		System.out.println("onBtnSalvarAction");
	}
	
	@FXML
	private void onBtnCancelarAction() {
		System.out.println("onBtnCancelarAction");
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
