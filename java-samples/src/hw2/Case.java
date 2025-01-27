package hw2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{
    private StringProperty caseDate;
    private StringProperty caseTitle;
    private StringProperty caseType;
    private StringProperty caseNumber;
    private StringProperty caseLink;
    private StringProperty caseCategory;
    private StringProperty caseNotes;

    Case(String caseDate, String caseTitle, String caseType, String caseNumber, String caseLink, String caseCategory, String caseNotes) {
        this.caseDate = new SimpleStringProperty(caseDate);
        this.caseTitle = new SimpleStringProperty(caseTitle);
        this.caseType = new SimpleStringProperty(caseType);
        this.caseNumber = new SimpleStringProperty(caseNumber);
        this.caseLink = new SimpleStringProperty(caseLink);
        this.caseCategory = new SimpleStringProperty(caseCategory);
        this.caseNotes = new SimpleStringProperty(caseNotes);
    }

    // Property getters
    public StringProperty caseDateProperty() { return caseDate; }
    public StringProperty caseTitleProperty() { return caseTitle; }
    public StringProperty caseTypeProperty() { return caseType; }
    public StringProperty caseNumberProperty() { return caseNumber; }
    public StringProperty caseLinkProperty() { return caseLink; }
    public StringProperty caseCategoryProperty() { return caseCategory; }
    public StringProperty caseNotesProperty() { return caseNotes; }

    // Standard getters and setters
    public String getCaseDate() { return caseDate.get(); }
    public void setCaseDate(String caseDate) { this.caseDate.set(caseDate); }

    public String getCaseTitle() { return caseTitle.get(); }
    public void setCaseTitle(String caseTitle) { this.caseTitle.set(caseTitle); }

    public String getCaseType() { return caseType.get(); }
    public void setCaseType(String caseType) { this.caseType.set(caseType); }

    public String getCaseNumber() { return caseNumber.get(); }
    public void setCaseNumber(String caseNumber) { this.caseNumber.set(caseNumber); }

    public String getCaseLink() { return caseLink.get(); }
    public void setCaseLink(String caseLink) { this.caseLink.set(caseLink); }

    public String getCaseCategory() { return caseCategory.get(); }
    public void setCaseCategory(String caseCategory) { this.caseCategory.set(caseCategory); }

    public String getCaseNotes() { return caseNotes.get(); }
    public void setCaseNotes(String caseNotes) { this.caseNotes.set(caseNotes); }

    @Override
    public int compareTo(Case other) {
        return this.getCaseDate().compareTo(other.getCaseDate());
    }

    @Override
    public String toString() {
        return this.getCaseNumber();
    }
}
