// DynamicContentPage.java
package database.pages;
import org.openqa.selenium.*;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicContentPage {

    private WebDriver driver;
    private By rowsLocator = By.cssSelector("#content .row");

    public DynamicContentPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://the-internet.herokuapp.com/dynamic_content");
    }

    /** devolve o texto de todas as linhas de conteúdo */
    public List<String> getRowsText() {
        return driver.findElements(rowsLocator)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /** força a página a recarregar conteúdo dinâmico */
    public void refresh() {
        driver.navigate().refresh();
    }
}
