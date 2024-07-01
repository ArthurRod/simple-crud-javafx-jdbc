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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerViewController implements Initializable, DataChangeListener {

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private SellerService sellerService;

	private ObservableList<Seller> obsList;

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
		resizeTableViewWindow();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

	}

	private void resizeTableViewWindow() {
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller seller = new Seller();
		createDialogForm(seller, "/gui/SellerCreateForm.fxml", parentStage);
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	public void updateTableView() {
		if (sellerService == null) {
			throw new IllegalStateException("Service was null!");
		}

		List<Seller> list = sellerService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller seller, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerCreateFormController controller = loader.getController();
			controller.setSeller(seller);
			controller.setSellerService(new SellerService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Edit");
			private final HBox hbox = new HBox(button);

			@Override
			protected void updateItem(Seller seller, boolean empty) {
				super.updateItem(seller, empty);

				if (seller == null) {
					setGraphic(null);
					return;
				}

				button.setOnAction(event -> createDialogForm(seller, "/gui/SellerCreateForm.fxml",
						Utils.currentStage(event)));
				HBox.setMargin(button, new Insets(0, 0, 0, 3));

				setGraphic(hbox);
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Remove");
			private final HBox hbox = new HBox(button);

			@Override
			protected void updateItem(Seller seller, boolean empty) {
				super.updateItem(seller, empty);

				if (seller == null) {
					setGraphic(null);
					return;
				}

				button.setOnAction(event -> removeEntity(seller));
				HBox.setMargin(button, new Insets(0, 0, 0, 3));

				setGraphic(hbox);
			}
		});
	}

	private void removeEntity(Seller seller) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (sellerService == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				sellerService.remove(seller);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
