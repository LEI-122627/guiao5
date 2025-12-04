package iscteiul.ista.guiao5;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainPageTest {

    private final MainPage mainPage = new MainPage();

    @BeforeAll
    public static void globalSetup() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1280x800";
        Configuration.timeout = 10000; //em ms
        Configuration.pageLoadTimeout = 40000; //em ms

        SelenideLogger.addListener("allure", new AllureSelenide());

        // Opcional:
        // Configuration.holdBrowserOpen = true;
    }

    @BeforeEach
    public void loadMainPage() {
        open("https://www.jetbrains.com/");
        CookieHelper.closeCookiesIfVisible();
    }

    /**
     * ---------- TESTE 1: Pesquisa ----------
     */
    @Test
    public void search() {
        // Acessa a barra de busca
        mainPage.searchButton
                .shouldBe(visible)
                .click();

        // Preenche o termo de pesquisa
        final String term = "Selenium";
        SelenideElement input = SearchHelper.locateSearchInput()
                .shouldBe(visible);
        input.setValue(term)
                .shouldHave(attribute("value", term));

        // Dispara a busca avançada
        $("button[data-test='full-search-button']")
                .shouldBe(visible)
                .click();

        // Valida que a navegação ocorreu conforme esperado
        String currentUrl = WebDriverRunner.url();
        assertTrue(
                currentUrl.contains("s=full"),
                String.format("Esperava que a URL contivesse 's=full', mas foi: %s", currentUrl)
        );
    }


    /**
     * ---------- TESTE 2: Menu Tools ----------
     */
    @Test
    public void toolsMenu() {
        // Abre o menu Tools
        mainPage.toolsMenu
                .shouldBe(visible)
                .click();

        // Obtém o primeiro submenu visível
        SelenideElement submenu = $$("div[data-test='main-submenu']")
                .filterBy(visible)
                .first();

        // Verifica se o submenu está visível
        submenu.shouldBe(visible);
    }


    /**
     * ---------- TESTE 3: Navegar para All Tools ----------
     */
    @Test
    public void navigationToAllTools() {
        // Abre o menu de ferramentas do desenvolvedor
        mainPage.seeDeveloperToolsButton
                .shouldBe(visible)
                .click();

        // Seleciona o link para "Find your tool"
        SelenideElement findYourToolLink = $$("a[data-test='suggestion-link']")
                .filterBy(attribute("aria-label", "Find your tool"))
                .first();

        findYourToolLink
                .shouldBe(visible)
                .click();

        // Verifica se a página de produtos abriu
        SelenideElement productsPage = $("#products-page");
        productsPage.shouldBe(visible);

        // Valida o título da página
        String expectedTitle = "All Developer Tools and Products by JetBrains";
        assertEquals(expectedTitle, title());
    }



    /**
     * ---------- HELPERS ----------
     */

    /** Helper para o banner de cookies */
    static class CookieHelper {
        static void closeCookiesIfVisible() {
            SelenideElement container = $(".ch2-container");

            // Espera até 1 segundo pelo cookie aparecer
            // Tenta esperar 1s sem quebrar o teste
            try {
                container.should(appear, Duration.ofSeconds(1));
            } catch (Exception ignored) {
                return; // Não apareceu → segue o teste
            }

            if (container.exists() && container.is(visible)) {
                container.$("button").click();
            }
        }
    }

    /** Helper para localizar o campo de pesquisa */
    static class SearchHelper {

        private static final String[] POSSIBLE_SELECTORS = {
                "[data-test='search-input']",
                "[data-test='site-header-search-input']",
                "#header-search",
                "input[type='search']",
                "input[placeholder*='Search']",
                "input[placeholder*='search']"
        };

        static SelenideElement locateSearchInput() {
            for (String selector : POSSIBLE_SELECTORS) {
                SelenideElement el = $(selector);
                if (el.exists()) return el;
            }

            throw new IllegalStateException(
                    "O campo de pesquisa não foi encontrado. Inspeciona a página e ajusta os seletores."
            );
        }
    }
}
