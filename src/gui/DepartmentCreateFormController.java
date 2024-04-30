package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentCreateFormController implements Initializable {

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private Department department;

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		defineInputsLimits();
	}

	private void defineInputsLimits() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	@FXML
	public void onBtSaveAction() {

	}

	@FXML
	public void onBtCancelAction() {

	}
	
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public void updateFormData() {
		if(department == null) {
			throw new IllegalStateException("Department was null!");
		}
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());
	}
}
