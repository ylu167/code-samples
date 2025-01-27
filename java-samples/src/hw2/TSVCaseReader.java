package hw2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSVCaseReader extends CaseReader {

    TSVCaseReader(String filename) {
        super(filename);
    }

    @Override
    List<Case> readCases() {
        List<Case> caseList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String caseData;

            while ((caseData = reader.readLine()) != null) {
                if (caseData.trim().isEmpty()) {
                    continue;
                }
                String[] caseString = caseData.split("\t");

                Case c = new Case(
                        caseString.length > 0 ? caseString[0] : null,
                        caseString.length > 1 ? caseString[1] : null,
                        caseString.length > 2 ? caseString[2] : null,
                        caseString.length > 3 ? caseString[3] : null,
                        caseString.length > 4 ? caseString[4] : null,
                        caseString.length > 5 ? caseString[5] : null,
                        caseString.length > 6 ? caseString[6] : null
                );
                caseList.add(c);
            }
            reader.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return caseList;
    }
}
