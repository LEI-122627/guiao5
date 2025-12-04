package database.pages; // muda para o teu package

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BasicAuthPage {

    private WebDriver driver;

    // URL base sem credenciais
    private final String BASE_URL = "https://the-internet.herokuapp.com/basic_auth";

    // URL com credenciais válidas (admin/admin)
    private final String AUTH_URL =
            "https://admin:admin@the-internet.herokuapp.com/basic_auth";

    // Locator para a mensagem de sucesso
    // Normalmente é um <p> com o texto "Congratulations! You must have the proper credentials."
    private By successMessageLocator = By.cssSelector("#content > div > p");

    public BasicAuthPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Abre a página sem credenciais (vai aparecer o pop-up do browser).
     * Normalmente não conseguimos automatizar esse pop-up com Selenium puro.
     */
    public void openWithoutAuth() {
        driver.get(BASE_URL);
    }

    /**
     * Abre a página já com credenciais na URL (sem pop-up do browser).
     */
    public void openWithValidCredentials() {
        driver.get(AUTH_URL);
    }

    public String getSuccessMessageText() {
        WebElement msg = driver.findElement(successMessageLocator);
        return msg.getText();
    }

    public boolean isSuccessMessageVisible() {
        return driver.findElement(successMessageLocator).isDisplayed();
    }
}