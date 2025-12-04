package database.pages; // altera para o teu package

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InputsPage {

    private WebDriver driver;

    private final String URL = "https://the-internet.herokuapp.com/inputs";

    // Na página existe apenas um <input>, por isso podemos usar este locator simples
    private By numberInputLocator = By.tagName("input");

    public InputsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(URL);
    }

    private WebElement getNumberInput() {
        return driver.findElement(numberInputLocator);
    }

    // --- Métodos de interação ---

    public void setValue(String value) {
        WebElement input = getNumberInput();
        input.clear();
        input.sendKeys(value);
    }

    public String getValue() {
        return getNumberInput().getAttribute("value");
    }

    public void clear() {
        getNumberInput().clear();
    }

    public void incrementWithArrowUp(int times) {
        WebElement input = getNumberInput();
        for (int i = 0; i < times; i++) {
            input.sendKeys(Keys.ARROW_UP);
        }
    }

    public void decrementWithArrowDown(int times) {
        WebElement input = getNumberInput();
        for (int i = 0; i < times; i++) {
            input.sendKeys(Keys.ARROW_DOWN);
        }
    }
}