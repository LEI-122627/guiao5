package database.tests; // altera para o teu package

import database.pages.InputsPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

public class InputsTest {

    private WebDriver driver;
    private InputsPage inputsPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        inputsPage = new InputsPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSetNumericValue() {
        // 1. Abrir a página
        inputsPage.open();

        // 2. Definir um valor numérico
        inputsPage.setValue("12345");

        // 3. Verificar que o valor foi colocado corretamente
        assertEquals("12345", inputsPage.getValue(),
                "O input devia conter o valor numérico que foi escrito.");
    }

    @Test
    public void testIncrementAndDecrementWithArrows() {
        // 1. Abrir a página
        inputsPage.open();

        // 2. Começar com 0
        inputsPage.setValue("0");

        // 3. Incrementar 3 vezes com ARROW_UP
        inputsPage.incrementWithArrowUp(3);
        String afterIncrement = inputsPage.getValue();
        // Dependendo do browser, o valor pode ser "3" (string) – validamos isso
        assertEquals("3", afterIncrement,
                "Após 3 incrementos, o valor devia ser 3.");

        // 4. Decrementar 2 vezes com ARROW_DOWN
        inputsPage.decrementWithArrowDown(2);
        String afterDecrement = inputsPage.getValue();
        assertEquals("1", afterDecrement,
                "Após 2 decrementos, o valor devia ser 1.");
    }
}