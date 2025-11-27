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

    private final MainPage page = new MainPage();

    @BeforeAll
    public static void globalSetup() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1280x800";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 40_000;
        Configuration.headless = false;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openHomeAndDismissPopups() {
        open("https://www.jetbrains.com/");
        $("body").should(appear);
        dismissCookieBannerIfPresent();
        waitForTransientOverlays(); // torna isto resiliente (não lança)
    }

    // ---------------- helpers ----------------

    private void dismissCookieBannerIfPresent() {
        try {
            SelenideElement byDataTest = $("button[data-test='cookie-accept'], button[data-test='accept-cookies']");
            if (byDataTest.exists() && byDataTest.isDisplayed()) {
                byDataTest.click();
                sleep(400);
                System.out.println("[cookies] clicked data-test cookie accept");
                return;
            }

            SelenideElement byAria = $("button[aria-label*='accept'], button[aria-label*='Accept']");
            if (byAria.exists() && byAria.isDisplayed()) {
                byAria.click();
                sleep(400);
                System.out.println("[cookies] clicked aria-label accept");
                return;
            }
        } catch (Exception ignored) {}

        // xpath textual fallback
        String[] texts = {"Accept", "Accept all", "Aceitar", "Aceitar tudo", "Agree", "Consent"};
        for (String t : texts) {
            try {
                SelenideElement xpathBtn = $x("//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" + t.toLowerCase() + "')]");
                if (xpathBtn.exists() && xpathBtn.isDisplayed()) {
                    xpathBtn.click();
                    sleep(400);
                    System.out.println("[cookies] clicked xpath text: " + t);
                    return;
                }
            } catch (Exception ignored) {}
        }

        // fallback: remove programaticamente
        try {
            executeJavaScript("document.querySelectorAll('.cookie-banner, .cookie-consent, .ch2-container, .cookie-wrapper').forEach(e => e.remove());");
            sleep(300);
            System.out.println("[cookies] removed cookie elements by JS fallback");
        } catch (Exception ignored) {}
    }

    /**
     * Espera/fecha overlays transitórios de forma tolerante.
     * Não lança se o overlay persistir — tenta vários métodos para fechá-lo.
     */
    private void waitForTransientOverlays() {
        try {
            ElementsCollection overlays = $$(".ch2-container, .ch2-backdrop, .overlay, .modal, .cookie-banner");
            // se não houver nenhum visível, nada a fazer
            if (overlays.filter(visible).isEmpty()) {
                return;
            }

            // tenta fechar usando botões conhecidos dentro do overlay
            overlays.filter(visible).forEach(overlay -> {
                try {
                    // procura um botão "close" ou "x" dentro do overlay
                    SelenideElement closeBtn = overlay.$("button[aria-label='Close'], button[aria-label='close'], button.close, .close-button");
                    if (closeBtn.exists() && closeBtn.isDisplayed()) {
                        closeBtn.click();
                        System.out.println("[overlay] clicked close button inside overlay");
                        sleep(300);
                        return;
                    }

                    // procura botão com texto 'close'/'fechar' via XPath
                    SelenideElement txtClose = overlay.$x(".//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'close') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'fechar')]");
                    if (txtClose.exists() && txtClose.isDisplayed()) {
                        txtClose.click();
                        System.out.println("[overlay] clicked textual close button");
                        sleep(300);
                        return;
                    }

                    // procura accept/agree dentro do overlay (pode ser cookie banner)
                    SelenideElement accept = overlay.$x(".//button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'accept') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'aceitar')]");
                    if (accept.exists() && accept.isDisplayed()) {
                        accept.click();
                        System.out.println("[overlay] clicked accept inside overlay");
                        sleep(300);
                        return;
                    }

                } catch (Exception ex) {
                    // tenta próximo overlay
                }
            });

            // aguarda um pequeno período para as animações terminarem
            sleep(500);

            // se ainda houver overlays visíveis, remove via JS (último recurso)
            if (!$$(".ch2-container, .ch2-backdrop, .overlay, .modal, .cookie-banner").filter(visible).isEmpty()) {
                executeJavaScript("document.querySelectorAll('.ch2-container, .ch2-backdrop, .overlay, .modal, .cookie-banner').forEach(e => e.remove());");
                System.out.println("[overlay] removed overlays via JS fallback");
                sleep(300);
            }
        } catch (Exception e) {
            // Não propagar exceção — apenas regista e segue em frente
            System.out.println("[overlay] warning while trying to clear overlays: " + e.getMessage());
        }
    }

    /**
     * Tenta clicar no elemento com várias estratégias:
     * 1) garantia de overlays limpos -> click normal
     * 2) scroll + actions
     * 3) JS click (remoção de overlays antes)
     */
    private void clickRobust(SelenideElement element) {
        // garante primeiro que overlays foram tratados
        waitForTransientOverlays();

        try {
            element.shouldBe(visible, Duration.ofSeconds(5)).click();
            return;
        } catch (Exception e1) {
            // fallback
        }

        try {
            element.scrollIntoView("{block: 'center'}");
            actions().moveToElement(element.getWrappedElement()).click().perform();
            return;
        } catch (Exception e2) {
            // fallback
        }

        try {
            executeJavaScript("document.querySelectorAll('.ch2-container, .ch2-backdrop, .overlay, .modal, .cookie-banner').forEach(e => e.remove());");
            sleep(250);
            executeJavaScript("arguments[0].click();", element);
        } catch (Exception finalEx) {
            throw new RuntimeException("Não foi possível clicar no elemento após todos os fallbacks", finalEx);
        }
    }

    // ---------------- tests ----------------

    @Test
    public void search() {
        clickRobust(page.searchButton);

        SelenideElement input = locateSearchInput().shouldBe(visible, Duration.ofSeconds(5));
        String query = "Selenium";
        input.setValue(query);
        input.shouldHave(attribute("value", query));

        // enviar
        SelenideElement submit = $("button[data-test='full-search-button'], button[type='submit']");
        if (submit.exists() && submit.isDisplayed()) {
            clickRobust(submit);
        } else {
            input.pressEnter();
        }
    }

    @Test
    public void toolsMenu() {
        clickRobust(page.toolsMenu);

        // aguarda submenu visível (várias opções)
        $$(".main-submenu, div[data-test='main-submenu'], nav .sub-menu, .subnav")
                .findBy(visible)
                .shouldBe(visible, Duration.ofSeconds(8));
    }

    @Test
    public void navigationToAllTools() {
        clickRobust(page.seeDeveloperToolsButton);

        // encontra e clica no link "Find your tool" com heurística
        SelenideElement suggestion = $x("//a[contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'find your tool')]");


        suggestion.shouldBe(visible, Duration.ofSeconds(6)).click();

        // espera a página de produtos
        $("#products-page, #products, .products-list").shouldBe(visible, Duration.ofSeconds(8));

        String t = title();
        assertTrue(t.contains("Developer Tools") || t.contains("Products") || t.contains("Tools"),
                "Título inesperado: " + t);
    }

    // Reusa locateSearchInput do exemplo anterior (podes manter sua implementação)
    private SelenideElement locateSearchInput() {
        String[] selectors = {
                "[data-test='search-input']",
                "[data-test='site-header-search-input']",
                "input[type='search']",
                "input[name='q']",
                "input[placeholder*='Search']",
                "input[placeholder*='search']"
        };

        for (String sel : selectors) {
            SelenideElement el = $(sel);
            if (el.exists() && el.isDisplayed()) {
                return el;
            }
        }
        throw new IllegalStateException("Input de pesquisa não encontrado — confirma o DOM atual do site.");
    }
}
