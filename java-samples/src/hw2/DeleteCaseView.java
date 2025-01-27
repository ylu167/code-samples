package hw2;

import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;

public class DeleteCaseView extends CaseView {
    DeleteCaseView(String header) {
        super(header);
    }

    public Stage buildView() {
        // Set controls to display currentCase
        titleTextField.setText(CyberCop.currentCase.getCaseTitle());
        caseDatePicker.setValue(LocalDate.parse(CyberCop.currentCase.getCaseDate()));
        caseTypeTextField.setText(CyberCop.currentCase.getCaseType());
        caseNumberTextField.setText(CyberCop.currentCase.getCaseNumber());
        categoryTextField.setText(CyberCop.currentCase.getCaseCategory());
        caseLinkTextField.setText(CyberCop.currentCase.getCaseLink());
        caseNotesTextArea.setText(CyberCop.currentCase.getCaseNotes());

        // Disable editing
        titleTextField.setEditable(false);
        caseDatePicker.setDisable(true);
        caseTypeTextField.setEditable(false);
        caseNumberTextField.setEditable(false);
        categoryTextField.setEditable(false);
        caseLinkTextField.setEditable(false);
        caseNotesTextArea.setEditable(false);

        // Set updateButton text to "Delete Case"
        updateButton.setText("Delete Case");

        // Build the scene
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene);

        return stage;
    }
}
