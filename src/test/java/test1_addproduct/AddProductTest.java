package test1_addproduct;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class AddProductTest {

    private AddProductPage page;

    @BeforeAll
    static void globalSetup() {
        Configuration.browser = "chrome";
        Configuration.timeout = 10000; // 10s
        // se quiseres ver o browser aberto no fim:
        // Configuration.holdBrowserOpen = true;
        // se quiseres em modo headless:
        // Configuration.headless = true;
    }

    @BeforeEach
    void setUp() {
        page = new AddProductPage();
    }

    @Test
    void login_criarNovoProduto() {
        page.openPage()
                .loginAsAdmin()
                .openAddProductForm()
                .fillNewProductForm(
                        "Livro de Teste - " + System.currentTimeMillis(),
                        "12.34",
                        "10",
                        "Available",   // availability desejada (ex: "Available", "Coming", "Discontinued")
                        "Romance"      // categoria desejada
                );
    }

    @AfterEach
    void tearDown() {
        // Se estiveres a debugar, comenta esta linha para o browser não fechar
        closeWebDriver();
    }
}
