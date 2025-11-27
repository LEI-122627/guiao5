package iscteiul.ista.guiao5;

import com.codeborne.selenide.Configuration;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainPageTest {

    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1280x800";

        Configuration.timeout = 10000; // waits de 10s
        Configuration.pageLoadTimeout = 40000;

        SelenideLogger.addListener("allure", new AllureSelenide());

        // Opcional:
        // Configuration.holdBrowserOpen = true;
    }

    @BeforeEach
    public void setUp() {
        open("https://www.jetbrains.com/");
        closeCookieBannerReliably();
    }

    private void closeCookieBannerReliably() {
        SelenideElement container = $(".ch2-container");

        try {
            container.should(appear, Duration.ofSeconds(5));
        } catch (AssertionError ignored) {
            return;
        }

        SelenideElement button = container.$("button");
        button.shouldBe(visible, Duration.ofSeconds(5)).click();
    }

    private SelenideElement findSearchInput() {
        String[] selectors = {
                "[data-test='search-input']",
                "[data-test='site-header-search-input']",
                "#header-search",
                "input[type='search']",
                "input[placeholder*='Search']",
                "input[placeholder*='search']"
        };

        for (String s : selectors) {
            SelenideElement el = $(s);
            if (el.isDisplayed()) return el;
        }

        throw new IllegalStateException(
                "Nenhum input de pesquisa encontrado! Verifica o HTML atual."
        );
    }

    @Test
    public void search() {
        mainPage.searchButton.shouldBe(visible).click();

        SelenideElement searchInput = findSearchInput().shouldBe(visible);

        String term = "Selenium";
        searchInput.setValue(term);
        searchInput.shouldHave(attribute("value", term));

        $("button[data-test='full-search-button']")
                .shouldBe(visible)
                .click();

        String url = WebDriverRunner.url();
        assertTrue(
                url.contains("s=full"),
                "URL não é de pesquisa completa: " + url
        );
    }

    @Test
    public void toolsMenu() {
        mainPage.toolsMenu.shouldBe(visible).click();

        $$("div[data-test='main-submenu']")
                .findBy(visible)
                .shouldBe(visible);
    }

    @Test
    public void navigationToAllTools() {
        mainPage.seeDeveloperToolsButton.shouldBe(visible).click();

        $$("a[data-test='suggestion-link']")
                .findBy(attribute("aria-label", "Find your tool"))
                .shouldBe(visible)
                .click();

        $("#products-page").shouldBe(visible);

        assertEquals(
                "All Developer Tools and Products by JetBrains",
                title()
        );
    }
}
