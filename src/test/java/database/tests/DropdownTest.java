package database.tests;

import database.pages.DropdownPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

public class DropdownTest {

    private WebDriver driver;
    private DropdownPage dropdownPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        dropdownPage = new DropdownPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testDropdownSelection() {

        // 1. Abrir página
        dropdownPage.open();

        // 2. Verificar seleção inicial (normalmente "Please select an option")
        assertEquals("Please select an option", dropdownPage.getSelectedOptionText(),
                "Estado inicial do dropdown não é o esperado");

        // 3. Selecionar Option 1 por VALUE
        dropdownPage.selectOptionByValue("1");
        assertTrue(dropdownPage.isOptionSelected("Option 1"),
                "Option 1 devia estar selecionada");

        // 4. Selecionar Option 2 por TEXTO VISÍVEL
        dropdownPage.selectOptionByVisibleText("Option 2");
        assertTrue(dropdownPage.isOptionSelected("Option 2"),
                "Option 2 devia estar selecionada");
    }
}
