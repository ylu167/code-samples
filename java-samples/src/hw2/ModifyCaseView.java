package hw2;

import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;

public class ModifyCaseView extends CaseView {
    ModifyCaseView(String header) {
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

        // Set updateButton text to "Modify Case"
        updateButton.setText("Modify Case");

        // Build the scene
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene);

        return stage;
    }
}
