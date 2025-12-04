package database.tests;

import database.pages.CheckboxesPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

public class CheckboxesTest {

    private WebDriver driver;
    private CheckboxesPage checkboxesPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        checkboxesPage = new CheckboxesPage(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCheckboxesSelection() {
        checkboxesPage.open();

        assertFalse(checkboxesPage.isCheckbox1Selected(),
                "Checkbox 1 deve começar desmarcada");
        assertTrue(checkboxesPage.isCheckbox2Selected(),
                "Checkbox 2 deve começar marcada");

        checkboxesPage.selectCheckbox1();
        checkboxesPage.unselectCheckbox2();

        assertTrue(checkboxesPage.isCheckbox1Selected(),
                "Checkbox 1 deve ficar marcada");
        assertFalse(checkboxesPage.isCheckbox2Selected(),
                "Checkbox 2 deve ficar desmarcada");
    }
}
