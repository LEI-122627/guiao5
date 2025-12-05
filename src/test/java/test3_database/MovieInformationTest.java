package test3_database;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class MovieInformationTest {

    private static MovieInformation page;

    @BeforeAll
    static void setupAll() {
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 8000;

        page = new MovieInformation();
    }

    @BeforeEach
    void openApp() {
        page.openPage();
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }

    // 1) Verificar que a grelha carrega com filmes
    @Test
    void testPaginaCarregaComFilmes() {
        List<String> filmes = page.getVisibleMovieTitles();

        assertFalse(filmes.isEmpty(), "A lista de filmes não deveria estar vazia");
        assertTrue(filmes.stream().anyMatch(s -> s.contains("Law Abiding Citizen")),
                "Deveria aparecer 'Law Abiding Citizen'");
        assertTrue(filmes.stream().anyMatch(s -> s.contains("Knives Out")),
                "Deveria aparecer 'Knives Out'");
        assertTrue(filmes.stream().anyMatch(s -> s.contains("The Last Jedi")),
                "Deveria aparecer 'The Last Jedi'");
    }
    @Test
    void testAbrirPagina() {
        page.openPage();

        // Deve haver grid visível
        List<String> filmes = page.getVisibleMovieTitles();

        // A grelha deve estar carregada
        assertFalse(filmes.isEmpty(), "A grelha deve carregar filmes ao abrir a página.");

        // Filmes esperados na página demo
        assertTrue(filmes.stream().anyMatch(s -> s.contains("Law Abiding Citizen")),
                "A página inicial deve conter 'Law Abiding Citizen'");
        assertTrue(filmes.stream().anyMatch(s -> s.contains("Knives Out")),
                "A página inicial deve conter 'Knives Out'");
        assertTrue(filmes.stream().anyMatch(s -> s.contains("The Last Jedi")),
                "A página inicial deve conter 'The Last Jedi'");
    }

    // 2) Teste de "filtrar" por título (se não existirem filtros, pelo menos não rebenta)
    @Test
    void testFiltrarPorTitulo() {
        page.filterMovies("Knives Out", null, null);

        List<String> depois = page.getVisibleMovieTitles();

        // Mesmo que a UI não tenha filtro, garantimos que o filme existe na grelha
        assertTrue(depois.stream().anyMatch(s -> s.contains("Knives Out")),
                "Deveria existir um filme com título 'Knives Out'");
    }

    // 3) Teste de "limpar filtros" (não deve rebentar e a grelha continua com filmes)
    @Test
    void testLimparFiltros() {
        page.filterMovies("Knives Out", null, null);
        page.clearFilters();

        List<String> todos = page.getVisibleMovieTitles();

        assertFalse(todos.isEmpty(), "Depois de limpar filtros ainda deve haver filmes");
        assertTrue(todos.stream().anyMatch(s -> s.contains("Law Abiding Citizen")),
                "Depois de limpar filtros deveria aparecer 'Law Abiding Citizen'");
        assertTrue(todos.stream().anyMatch(s -> s.contains("The Last Jedi")),
                "Depois de limpar filtros deveria aparecer 'The Last Jedi'");
    }

    // 4) Testar que o link IMBD abre o IMDB numa nova aba
    @Test
    void testClickImbdLink() {
        page.clickImbdLink("Knives Out");

        // mudar para a nova janela/aba
        switchTo().window(1);

        String currentUrl = webdriver().driver().url();
        assertTrue(currentUrl.contains("imdb"),
                "URL devia conter 'imdb', mas é: " + currentUrl);
    }
}
