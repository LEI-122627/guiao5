package database.tests;

import database.pages.DynamicContentPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DynamicContentTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void contentShouldChangeAfterRefresh() {
        DynamicContentPage page = new DynamicContentPage(driver);
        page.open();

        List<String> firstState = page.getRowsText();

        page.refresh();
        List<String> secondState = page.getRowsText();

        assertNotEquals(firstState, secondState,
                "O conteúdo dinâmico deveria ser diferente após o refresh");
    }
}
