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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

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

	private DepartmentService departmentService;

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		defineInputsLimits();
	}

	private void defineInputsLimits() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (department == null) {
			throw new IllegalStateException("Department was null!");
		}
		if (departmentService == null) {
			throw new IllegalStateException("Department service was null!");
		}
		try {
			department = getFormData();
			departmentService.saveOrUpdate(department);
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	private Department getFormData() {
		Department department = new Department();

		department.setId(Utils.tryParseToInt(txtId.getText()));
		department.setName(txtName.getText());

		return department;
	}

	public void updateFormData() {
		if (department == null) {
			throw new IllegalStateException("Department was null!");
		}
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());
	}
}
