package hw2;

public class CaseReaderFactory {

    public static CaseReader createReader(String filename) {
        if (filename.endsWith(".csv")) {
            return new CSVCaseReader(filename);
        } else if (filename.endsWith(".tsv")) {
            return new TSVCaseReader(filename);
        } else {
            throw new IllegalArgumentException(filename);
        }
    }
}
