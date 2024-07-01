package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerCreateFormController implements Initializable {

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

	private Seller seller;

	private SellerService sellerService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
		if (seller == null) {
			throw new IllegalStateException("Seller was null!");
		}
		if (sellerService == null) {
			throw new IllegalStateException("Service was null!");
		}
		try {
			seller = getFormData();
			sellerService.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());

		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	private Seller getFormData() {
		ValidationException exception = new ValidationException("Validation Error");
		Seller seller = new Seller();

		seller.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Name can't be empty");
		}
		seller.setName(txtName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return seller;
	}

	public void updateFormData() {
		if (seller == null) {
			throw new IllegalStateException("Seller was null!");
		}
		txtId.setText(String.valueOf(seller.getId()));
		txtName.setText(seller.getName());
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
}
