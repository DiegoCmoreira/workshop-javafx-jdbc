package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Vendedor;
import model.excecao.ValidacaoExcecao;
import model.services.VendedorService;

public class FormaVendedorController implements Initializable{

	private Vendedor entidade;
	
	private VendedorService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker txtDtNasci;
	
	@FXML
	private TextField txtSalarioBase;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorDtNasci;
	
	@FXML
	private Label labelErrorSalarioBase;
	
	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnCancelar;
	
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}
	
	public void setVendedorService(VendedorService service) {
		this.service = service;
	}
	
	public void inscreverDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
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
			notificaDataChangeListener();
			Utils.palcoAtual(evento).close();
		}
		catch (ValidacaoExcecao e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notificaDataChangeListener() {
		for(DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Vendedor getFormData() {
		Vendedor obj = new Vendedor();

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());

		return obj;
	}

	@FXML
	public void onBtnCancelarAction(ActionEvent evento) {
		Utils.palcoAtual(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldDouble(txtSalarioBase); 	
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatoDatePicker(txtDtNasci, "dd/MM/yyyy");
	}
	
	public void updateFormData() {
		if(entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtName.setText(entidade.getName());
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", entidade.getBaseSalary()));
		if(entidade.getBirthDate() != null) {
			txtDtNasci.setValue(LocalDate.ofInstant(entidade.getBirthDate().toInstant(), ZoneId.systemDefault()));			
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}