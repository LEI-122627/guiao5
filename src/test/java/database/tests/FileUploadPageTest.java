package database.tests;

import database.pages.FileUploadPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUploadPageTest {

    @Test
    public void uploadFileSuccessfully() {
        FileUploadPage page = new FileUploadPage();
        page.openPage();

        String fileName = "example.txt";
        // coloca o ficheiro em src/test/resources/files/example.txt
        page.uploadFileFromClasspath("files/" + fileName);

        page.submitUpload();

        assertEquals(fileName, page.getUploadedFileName());
    }
}
