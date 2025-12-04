package database.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class FileUploadPage {

    private final SelenideElement uploadInput = $("#file-upload");
    private final SelenideElement uploadButton = $("#file-submit");
    private final SelenideElement uploadedFiles = $("#uploaded-files");

    public void openPage() {
        open("https://the-internet.herokuapp.com/upload");
    }

    // Usa uploadFromClasspath -> requer que o ficheiro esteja em src/test/resources/files/
    public void uploadFileFromClasspath(String classpathPath) {
        uploadInput.uploadFromClasspath(classpathPath);
    }

    public void submitUpload() {
        uploadButton.click();
    }

    public String getUploadedFileName() {
        return uploadedFiles.text();
    }
}
