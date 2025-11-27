package database.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DropdownPage {

    private WebDriver driver;

    private final String URL = "https://the-internet.herokuapp.com/dropdown";

    // Localizador do elemento <select>
    private By dropdownLocator = By.id("dropdown");

    public DropdownPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(URL);
    }

    private Select getDropdown() {
        WebElement dropdownElement = driver.findElement(dropdownLocator);
        return new Select(dropdownElement);
    }

    // --- Métodos de interação ---

    public void selectOptionByValue(String value) {
        getDropdown().selectByValue(value);  // valores: "1" ou "2"
    }

    public void selectOptionByVisibleText(String text) {
        getDropdown().selectByVisibleText(text);
    }

    // --- Métodos de verificação ---

    public String getSelectedOptionText() {
        return getDropdown().getFirstSelectedOption().getText();
    }

    public boolean isOptionSelected(String expectedText) {
        return getSelectedOptionText().equals(expectedText);
    }
}
