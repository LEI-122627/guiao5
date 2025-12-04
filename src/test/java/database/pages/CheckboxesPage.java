package database.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckboxesPage {

    private WebDriver driver;

    // URL da página de checkboxes
    private final String URL = "https://the-internet.herokuapp.com/checkboxes";

    // Localizadores
    private By checkbox1Locator = By.xpath("//form[@id='checkboxes']/input[1]");
    private By checkbox2Locator = By.xpath("//form[@id='checkboxes']/input[2]");

    public CheckboxesPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(URL);
    }

    private WebElement getCheckbox1() {
        return driver.findElement(checkbox1Locator);
    }

    private WebElement getCheckbox2() {
        return driver.findElement(checkbox2Locator);
    }

    // --- Métodos de interação ---

    public void selectCheckbox1() {
        if (!getCheckbox1().isSelected()) {
            getCheckbox1().click();
        }
    }

    public void unselectCheckbox1() {
        if (getCheckbox1().isSelected()) {
            getCheckbox1().click();
        }
    }

    public void selectCheckbox2() {
        if (!getCheckbox2().isSelected()) {
            getCheckbox2().click();
        }
    }

    public void unselectCheckbox2() {
        if (getCheckbox2().isSelected()) {
            getCheckbox2().click();
        }
    }

    // --- Métodos de verificação ---

    public boolean isCheckbox1Selected() {
        return getCheckbox1().isSelected();
    }

    public boolean isCheckbox2Selected() {
        return getCheckbox2().isSelected();
    }
}

