package test4_form;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import test4_form.FormPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class FormTest {

    // Valores de teste usados para preencher o formulário
    private static final String TEST_NAME = "CC";
    private static final String TEST_LAST_NAME = "seila";
    private static final String TEST_USER = "testetest";
    private static final String TEST_PASSWORD = "Pass1234";
    private static final String EXPECTED_NOTIFICATION_TEXT = "Data saved, welcome testetest";
    private static final String TEST_EMAIL = "eu@exemplo.com";

    // Instância da página (Page Object)
    private FormPage page = new FormPage();

    @BeforeAll
    public static void setUp() {
        // Define o tamanho da janela do browser
        Configuration.browserSize = "1280x800";

        // Define timeout global para encontrar elementos (em milissegundos)
        Configuration.timeout = 10000;
    }

    @BeforeEach
    public void openPage(){
        // Abre a página antes de cada teste
        page.open();
    }

    @Test
    public void testSuccessfulJoinCommunityWithEmail() {

        // Preenche todos os campos obrigatórios incluindo email e checkbox
        page.fillRequiredFields(TEST_NAME, TEST_LAST_NAME, TEST_USER, TEST_PASSWORD, true, TEST_EMAIL);

        // Pausa curta para estabilizar a página (não ideal mas funciona para demonstração)
        sleep(500);

        // Clica no botão "Join the community"
        page.buttonJoin.click();

        // Outra pequena pausa para aguardar a notificação aparecer
        sleep(1000);

        // Valida que a notificação está visível e contém exatamente o texto esperado
        page.getNotificationText()
                .shouldBe(visible)
                .shouldHave(exactText(EXPECTED_NOTIFICATION_TEXT));
    }

}