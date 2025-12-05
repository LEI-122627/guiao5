package test4_form;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

// page_url = https://vaadin-form-example.demo.vaadin.com
public class FormPage {

    // ---------- ELEMENTOS DA PÁGINA ----------

    // Primeiro campo de texto (First Name)
    public SelenideElement inputFirstName = $("html > body > vaadin-vertical-layout > vaadin-form-layout > vaadin-text-field:nth-of-type(1)");

    // Segundo campo de texto (Last Name)
    public SelenideElement inputLastName = $("html > body > vaadin-vertical-layout > vaadin-form-layout > vaadin-text-field:nth-of-type(2)");

    // Terceiro campo de texto (Username)
    public SelenideElement inputUsername = $("html > body > vaadin-vertical-layout > vaadin-form-layout > vaadin-text-field:nth-of-type(3)");

    // Campo de Password
    public SelenideElement inputPassword = $("html > body > vaadin-vertical-layout > vaadin-form-layout > vaadin-password-field:nth-of-type(1)");

    // Campo de Confirmar Password
    public SelenideElement inputConfirmPassword = $("html > body > vaadin-vertical-layout > vaadin-form-layout > vaadin-password-field:nth-of-type(2)");

    // Checkbox para permitir marketing
    public SelenideElement checkboxAllowMarketing = $("vaadin-checkbox[aria-checked='false']");

    // Campo Email (apenas aparece quando o checkbox é clicado)
    public SelenideElement inputEmail = $("vaadin-email-field[tabindex='0']");

    // Botão "Join the community"
    public SelenideElement buttonJoin = $("vaadin-button[colspan='2']");


    // ---------- MÉTODOS ----------

    // Abrir a página
    public void open() {
        Selenide.open("https://vaadin-form-example.demo.vaadin.com");
    }


    // Preencher apenas os campos obrigatórios
    public void fillRequiredFields(String firstName, String lastName, String user, String password) {

        // Script JS para definir valor do input e disparar evento "change"
        String script = "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'))";

        Selenide.executeJavaScript(script, inputFirstName, firstName);
        Selenide.executeJavaScript(script, inputLastName, lastName);
        Selenide.executeJavaScript(script, inputUsername, user);
        Selenide.executeJavaScript(script, inputPassword, password);
        Selenide.executeJavaScript(script, inputConfirmPassword, password);
    }


    // Preencher todos os campos, incluindo marketing e email
    public void fillRequiredFields(String firstName, String lastName, String user, String password,
                                   boolean allowMarketing, String email) {

        String script = "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'))";

        Selenide.executeJavaScript(script, inputFirstName, firstName);
        Selenide.executeJavaScript(script, inputLastName, lastName);
        Selenide.executeJavaScript(script, inputUsername, user);
        Selenide.executeJavaScript(script, inputPassword, password);
        Selenide.executeJavaScript(script, inputConfirmPassword, password);

        // Se permitir marketing, o checkbox é clicado e o email é preenchido
        if (allowMarketing) {
            checkboxAllowMarketing.click();

            // Espera até o campo email aparecer
            inputEmail.shouldBe(visible);

            Selenide.executeJavaScript(script, inputEmail, email);
        }
    }


    // Obter texto da notificação de sucesso (elemento dentro do shadow DOM)
    public SelenideElement getNotificationText() {

        // Obtém o cartão de notificação
        SelenideElement notificationCard = $("vaadin-notification-card[theme='success']");

        // Acede ao shadow root e devolve o elemento do conteúdo
        return $(notificationCard.getWrappedElement()
                .getShadowRoot()
                .findElement(By.cssSelector("div[part='overlay'] div[part='content']")));
    }
}