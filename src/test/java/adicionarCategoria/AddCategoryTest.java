// src/test/java/adicionarCategoria/tests/AddCategoryTest.java
package adicionarCategoria;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddCategoryTest {

    private final BookStorePage page = new BookStorePage();

    @BeforeAll
    public void setupAll() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1280x800";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 40_000;
        Configuration.headless = false;
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    public void addCategoryFlow() {
        page.openHome();

        // login como admin/admin
        page.loginAs("admin", "admin");

        // vai ao admin e assegura vista
        page.clickAdminNav();
        page.ensureAdminView();

        String categoryName = "Testcategory#";

        // adiciona categoria directamente
        page.addCategoryDirect(categoryName);
    }
}
