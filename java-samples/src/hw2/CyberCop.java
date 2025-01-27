package hw2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.util.List;
import javafx.collections.FXCollections;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		//write your code here
		// Set up event handlers
		ccView.openFileMenuItem.setOnAction(new OpenMenuItemHandler());
		ccView.closeFileMenuItem.setOnAction(new CloseMenuItemHandler());
		ccView.exitMenuItem.setOnAction(new ExitMenuItemHandler());
		ccView.searchButton.setOnAction(new SearchButtonHandler());
		ccView.clearButton.setOnAction(new ClearButtonHandler());
		ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());

		// Disable properties
		ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
		ccView.searchButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.clearButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());

		ccView.titleTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseTypeTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.yearComboBox.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseNumberTextField.disableProperty().bind(ccView.isFileOpen.not());

		// Set up selection listener
		ccView.caseTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						currentCase = newSelection;
						ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

						// Update the fields
						ccView.titleTextField.setText(currentCase.getCaseTitle());
						ccView.caseTypeTextField.setText(currentCase.getCaseType());
						ccView.caseNumberTextField.setText(currentCase.getCaseNumber());

						// Extract year from caseDate and set in yearComboBox
						String caseDate = currentCase.getCaseDate();
						String caseYear = "";
						if (caseDate != null && !caseDate.isEmpty()) {
							if (caseDate.contains("-")) {
								caseYear = caseDate.split("-")[0];
							} else if (caseDate.contains("/")) {
								caseYear = caseDate.split("/")[2];
							} else {
								caseYear = caseDate;
							}
						}
						ccView.yearComboBox.setValue(caseYear);

						// Display the web page
						if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) { // if no link in data
							URL url = getClass().getClassLoader().getResource(DEFAULT_HTML); // default html
							if (url != null)
								ccView.webEngine.load(url.toExternalForm());
						} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")) { // if external link
							ccView.webEngine.load(currentCase.getCaseLink());
						} else {
							URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim()); // local link
							if (url != null)
								ccView.webEngine.load(url.toExternalForm());
						}
					}
				});

		ccView.isFileOpen.set(false);
	}

	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Open file chooser dialog
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			File selectedFile = fileChooser.showOpenDialog(stage);

			if (selectedFile != null) {
				String filepath = selectedFile.getAbsolutePath();
				String filename = selectedFile.getName(); // Extract file name without path

				ccModel.readCases(filepath);
				ccModel.buildYearMapAndList();

				ccView.caseTableView.setItems(ccModel.caseList);
				ccView.yearComboBox.setItems(ccModel.yearList);

				// Select first record in caseTableView
				if (!ccModel.caseList.isEmpty()) {
					ccView.caseTableView.getSelectionModel().selectFirst();
					currentCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
					ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

					// Update the fields
					ccView.titleTextField.setText(currentCase.getCaseTitle());
					ccView.caseTypeTextField.setText(currentCase.getCaseType());
					ccView.caseNumberTextField.setText(currentCase.getCaseNumber());

					// Extract year from caseDate and set in yearComboBox
					String caseDate = currentCase.getCaseDate();
					String caseYear = "";
					if (caseDate != null && !caseDate.isEmpty()) {
						if (caseDate.contains("-")) {
							caseYear = caseDate.split("-")[0];
						} else if (caseDate.contains("/")) {
							caseYear = caseDate.split("/")[2];
						} else {
							caseYear = caseDate;
						}
					}
					ccView.yearComboBox.setValue(caseYear);

					// Display the web page
					if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) { // if no link in data
						URL url = getClass().getClassLoader().getResource(DEFAULT_HTML); // default html
						if (url != null)
							ccView.webEngine.load(url.toExternalForm());
					} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")) { // if external link
						ccView.webEngine.load(currentCase.getCaseLink());
					} else {
						URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim()); // local link
						if (url != null)
							ccView.webEngine.load(url.toExternalForm());
					}
				}

				// Update stage title with only the file name
				stage.setTitle(APP_TITLE + " - " + filename);

				// Update messageLabel with the number of cases
				ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");

				// Set isFileOpen to true
				ccView.isFileOpen.set(true);
			}
		}
	}

	class CloseMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// Clear GUI elements
			ccView.caseTableView.getItems().clear();
			ccView.yearComboBox.getItems().clear();
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();
			ccView.yearComboBox.setValue(null);
			ccView.messageLabel.setText("");
			ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());

			// Set isFileOpen to false
			ccView.isFileOpen.set(false);

			// Clear the caseList, caseMap, etc.
			ccModel.caseList.clear();
			ccModel.caseMap.clear();
			ccModel.yearList.clear();
			ccModel.yearMap.clear();

			// Reset stage title
			stage.setTitle(APP_TITLE);

			currentCase = null;
		}
	}

	class ExitMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			stage.close();
		}
	}

	class SearchButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			String title = ccView.titleTextField.getText().trim();
			String caseType = ccView.caseTypeTextField.getText().trim();
			String year = ccView.yearComboBox.getValue();
			String caseNumber = ccView.caseNumberTextField.getText().trim();

			List<Case> searchResults = ccModel.searchCases(title, caseType, year, caseNumber);

			ccView.caseTableView.setItems(FXCollections.observableArrayList(searchResults));

			ccView.messageLabel.setText(searchResults.size() + " cases.");

			// If there are results, select the first one
			if (!searchResults.isEmpty()) {
				ccView.caseTableView.getSelectionModel().selectFirst();
				currentCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
				ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

				// Update the fields
				ccView.titleTextField.setText(currentCase.getCaseTitle());
				ccView.caseTypeTextField.setText(currentCase.getCaseType());
				ccView.caseNumberTextField.setText(currentCase.getCaseNumber());

				// Extract year from caseDate and set in yearComboBox
				String caseDate = currentCase.getCaseDate();
				String caseYear = "";
				if (caseDate != null && !caseDate.isEmpty()) {
					if (caseDate.contains("-")) {
						caseYear = caseDate.split("-")[0];
					} else if (caseDate.contains("/")) {
						caseYear = caseDate.split("/")[2];
					} else {
						caseYear = caseDate;
					}
				}
				ccView.yearComboBox.setValue(caseYear);

				// Display the web page
				if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) { // if no link in data
					URL url = getClass().getClassLoader().getResource(DEFAULT_HTML); // default html
					if (url != null)
						ccView.webEngine.load(url.toExternalForm());
				} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")) { // if external link
					ccView.webEngine.load(currentCase.getCaseLink());
				} else {
					URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim()); // local link
					if (url != null)
						ccView.webEngine.load(url.toExternalForm());
				}
			} else {
				currentCase = null;
				ccView.caseNotesTextArea.clear();
				ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());

				// Clear the fields
				ccView.titleTextField.clear();
				ccView.caseTypeTextField.clear();
				ccView.caseNumberTextField.clear();
				ccView.yearComboBox.setValue(null);
			}
		}
	}

	class ClearButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.yearComboBox.setValue(null);
		}
	}

	class CaseMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			if (arg0.getSource() == ccView.addCaseMenuItem) {
				caseView = new AddCaseView("Add Case");
				caseView.buildView();
				caseView.updateButton.setOnAction(new AddButtonHandler());
			} else if (arg0.getSource() == ccView.modifyCaseMenuItem) {
				if (currentCase == null) {
					return;
				}
				caseView = new ModifyCaseView("Modify Case");
				caseView.buildView();
				caseView.updateButton.setOnAction(new ModifyButtonHandler());
			} else if (arg0.getSource() == ccView.deleteCaseMenuItem) {
				if (currentCase == null) {
					return;
				}
				caseView = new DeleteCaseView("Delete Case");
				caseView.buildView();
				caseView.updateButton.setOnAction(new DeleteButtonHandler());
			}

			// Set up clearButton and closeButton
			caseView.clearButton.setOnAction(e -> {
				caseView.titleTextField.clear();
				caseView.caseTypeTextField.clear();
				caseView.caseNumberTextField.clear();
				caseView.categoryTextField.clear();
				caseView.caseLinkTextField.clear();
				caseView.caseNotesTextArea.clear();
				caseView.caseDatePicker.setValue(LocalDate.now());
			});

			caseView.closeButton.setOnAction(e -> {
				caseView.stage.close();
			});

			// Show the caseView stage
			caseView.stage.show();
		}
	}

	class AddButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			// Get data from caseView controls
			String date = caseView.caseDatePicker.getValue()
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String title = caseView.titleTextField.getText().trim();
			String caseType = caseView.caseTypeTextField.getText().trim();
			String caseNumber = caseView.caseNumberTextField.getText().trim();
			String category = caseView.categoryTextField.getText().trim();
			String link = caseView.caseLinkTextField.getText().trim();
			String notes = caseView.caseNotesTextArea.getText().trim();

			// Create new case
			Case newCase = new Case(date, title, caseType, caseNumber, link, category, notes);

			// Add to caseList and caseMap
			ccModel.caseList.add(newCase);
			ccModel.caseMap.put(caseNumber, newCase);

			// Rebuild yearMap and yearList
			ccModel.buildYearMapAndList();
			ccView.yearComboBox.setItems(ccModel.yearList);

			// Update the caseTableView
			ccView.caseTableView.refresh();

			// Update messageLabel
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
		}
	}

	class ModifyButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			if (currentCase != null) {
				// Update currentCase with data from caseView controls
				currentCase.setCaseDate(caseView.caseDatePicker.getValue()
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				currentCase.setCaseTitle(caseView.titleTextField.getText().trim());
				currentCase.setCaseType(caseView.caseTypeTextField.getText().trim());
				currentCase.setCaseNumber(caseView.caseNumberTextField.getText().trim());
				currentCase.setCaseCategory(caseView.categoryTextField.getText().trim());
				currentCase.setCaseLink(caseView.caseLinkTextField.getText().trim());
				currentCase.setCaseNotes(caseView.caseNotesTextArea.getText().trim());

				// Rebuild yearMap and yearList
				ccModel.buildYearMapAndList();
				ccView.yearComboBox.setItems(ccModel.yearList);

				// Update the caseTableView
				ccView.caseTableView.refresh();

				// Update messageLabel
				ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
			}
		}
	}

	class DeleteButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			if (currentCase != null) {
				ccModel.caseMap.remove(currentCase.getCaseNumber());
				ccModel.caseList.remove(currentCase);

				// Rebuild yearMap and yearList
				ccModel.buildYearMapAndList();
				ccView.yearComboBox.setItems(ccModel.yearList);

				// Update the caseTableView
				ccView.caseTableView.setItems(ccModel.caseList);

				// Update messageLabel
				ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");

				// Clear currentCase
				currentCase = null;
				ccView.caseNotesTextArea.clear();
				ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());

				// Clear the fields
				ccView.titleTextField.clear();
				ccView.caseTypeTextField.clear();
				ccView.caseNumberTextField.clear();
				ccView.yearComboBox.setValue(null);
			}
		}
	}
}
