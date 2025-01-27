package hw2;

import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;

public class AddCaseView extends CaseView {
    AddCaseView(String header) {
        super(header);
    }

    public Stage buildView() {
        // Initialize date picker to current date
        caseDatePicker.setValue(LocalDate.now());

        // Set updateButton text to "Add Case"
        updateButton.setText("Add Case");

        // Build the scene
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene);

        return stage;
    }
}
