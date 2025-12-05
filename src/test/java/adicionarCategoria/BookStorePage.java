package adicionarCategoria;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

/**
 * Página única que encapsula navegação principal do Bookstore demo
 * (login, ir para Admin, adicionar categoria).
 */
public class BookStorePage {

    private final Duration TIMEOUT = Duration.ofSeconds(10);

    public BookStorePage openHome() {
        open("https://vaadin-bookstore-example.demo.vaadin.com/");
        $("body").shouldBe(visible, TIMEOUT);
        return this;
    }

    @Step("Login com {username}:{password}")
    public BookStorePage loginAs(String username, String password) {
        SelenideElement userInput = findFirstVisible(
                "input[name='username']",
                "input[id*='user']",
                "input[placeholder*='User']",
                "input[type='text']"
        );

        SelenideElement passInput = findFirstVisible(
                "input[name='password']",
                "input[id*='pass']",
                "input[placeholder*='Password']",
                "input[type='password']"
        );

        if (userInput == null || passInput == null) {
            ElementsCollection allInputs = $$("input").filter(visible);
            if (allInputs.size() >= 2) {
                userInput = allInputs.get(0);
                passInput = allInputs.get(1);
            }
        }

        if (userInput == null || passInput == null) {
            throw new IllegalStateException("Não foi possível localizar os campos de login.");
        }

        userInput.shouldBe(visible, TIMEOUT).clear();
        userInput.setValue(username);

        passInput.shouldBe(visible, TIMEOUT).clear();
        passInput.setValue(password);

        SelenideElement submit = findFirstVisible(
                "button[type='submit']",
                "button:has-text('Login')",
                "button:has-text('Sign in')",
                "button:has-text('Log in')"
        );

        if (submit != null) {
            submit.shouldBe(visible, TIMEOUT).click();
        } else {
            passInput.pressEnter();
        }

        // espera um indicador de sessão (Logout, Admin, Hello)
        SelenideElement indicator = findFirstVisible(
                "a:has-text('Logout')",
                "button:has-text('Logout')",
                "//*[normalize-space()='Logout']",
                "//*[contains(normalize-space(.),'Hello')]",
                "//nav"
        );

        if (indicator != null) indicator.shouldBe(visible, TIMEOUT);
        else sleep(800);

        return this;
    }

    @Step("Clicar no menu lateral Admin")
    public BookStorePage clickAdminNav() {
        SelenideElement admin = $x(
                "//*[normalize-space(text())='Admin' and (self::a or self::div or self::span or self::button or self::vaadin-tab or self::vaadin-item)]"
        );
        admin.shouldBe(visible, TIMEOUT).scrollIntoView("{block: 'center'}").click();
        return this;
    }

    @Step("Assegurar vista Admin visível")
    public BookStorePage ensureAdminView() {
        // espera o título Hello Admin ou Edit Categories e o botão Add New Category aparecerem
        $x("//h2[normalize-space()='Hello Admin' or contains(normalize-space(.),'Edit Categories')]")
                .shouldBe(visible, TIMEOUT);

        // dá um pouco mais de tempo para o painel lateral e o botão renderizarem
        // procura de forma tolerante (vaadin-button, button, a, span, div)
        SelenideElement maybeAdd = robustFindButtonByText("Add New Category");
        if (maybeAdd == null) {
            // tenta esperar um pouco e procurar de novo
            sleep(500);
            maybeAdd = robustFindButtonByText("Add New Category");
        }
        if (maybeAdd == null) {
            // não falha ainda — o teste seguirá e o addCategoryDirect vai lançar erro com mensagem mais clara
            return this;
        }
        maybeAdd.shouldBe(visible, TIMEOUT);
        return this;
    }

    /**
     * Procura de forma tolerante por um botão/elemento que contenha exatamente o texto pedido.
     * Varre vários tipos de elementos e usa Condition.text para corresponder ao texto visível.
     */
    private SelenideElement robustFindButtonByText(String text) {
        // pesquisa direta por tipos comuns
        ElementsCollection candidates = $$("vaadin-button, button, a, span, div").filter(visible);
        SelenideElement found = candidates.findBy(text(text));
        if (found != null && found.exists()) return found;

        // fallback: procura em qualquer elemento visível que contenha o texto exacto
        ElementsCollection any = $$("*").filter(visible);
        found = any.findBy(text(text));
        if (found != null && found.exists()) return found;

        return null;
    }

    /**
     * Versão mínima: clica em "Add New Category", escreve o nome e envia ENTER.
     * Não faz validações nem procura botões de confirmação — só força o input e Enter.
     */
    @Step("Adicionar nova categoria simples: {categoryName}")
    public BookStorePage addCategoryDirect(String categoryName) {
        // clicar no botão "Add New Category" (procura robusta mas sem checks)
        SelenideElement addBtn = robustFindButtonByText("Add New Category");
        if (addBtn == null) {
            // ainda assim lança erro para sabermos que o botão não existe
            throw new IllegalStateException("Botão 'Add New Category' não encontrado.");
        }
        addBtn.scrollIntoView("{block: 'center'}").click();

        // pequena pausa para Vaadin renderizar o slot/foco
        sleep(200);

        // tenta primeiro escrever num input directo se existir
        SelenideElement nameInput = findFirstVisible(
                "vaadin-text-field input",
                "input[placeholder*='Name']",
                "input[placeholder*='Category']",
                "//vaadin-dialog//input",
                "input"
        );

        if (nameInput != null) {
            // escreve e press Enter
            nameInput.clear();
            nameInput.setValue(categoryName);
            nameInput.pressEnter();
            return this;
        }

        // caso não haja um input "normal", escreve no element activo e press Enter
        WebDriver driver = WebDriverRunner.getWebDriver();
        WebElement focused = driver.switchTo().activeElement();

        actions()
                .click(focused)
                .sendKeys(categoryName)
                .sendKeys(Keys.ENTER)
                .perform();

        return this;
    }


    public boolean categoryExists(String name) {
        SelenideElement e = $x("//*[normalize-space()='" + name + "']");
        return e.exists() && e.is(visible);
    }

    // ---------------- helpers ----------------
    private SelenideElement findFirstVisible(String... selectors) {
        for (String sel : selectors) {
            try {
                SelenideElement el = $(sel);
                if (el.exists() && el.is(visible)) return el;
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
