package test3_database;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class MovieInformation {

    public static final String URL = "https://vaadin-database-example.demo.vaadin.com";

    // grelha principal
    private final SelenideElement movieGrid = $("vaadin-grid");

    public void openPage() {
        open(URL);
        movieGrid.shouldBe(visible);
    }

    /**
     * Tenta aplicar filtros, se existirem campos de texto.
     * Se a página não tiver filtros, não lança erro.
     */
    public void filterMovies(String title, String year, String director) {
        movieGrid.shouldBe(visible);

        // todos os campos de texto que possam servir de filtro
        ElementsCollection filters = $$("vaadin-text-field, input");

        if (filters.isEmpty()) {
            // não há campos de filtro na UI → não faz nada, mas também não falha
            return;
        }

        if (title != null && !filters.isEmpty()) {
            filters.get(0).setValue(title);
        }
        if (year != null && filters.size() > 1) {
            filters.get(1).setValue(year);
        }
        if (director != null && filters.size() > 2) {
            filters.get(2).setValue(director);
        }
    }

    /** Limpa todos os campos de filtro que encontrar (se existirem) */
    public void clearFilters() {
        ElementsCollection filters = $$("vaadin-text-field, input");
        for (SelenideElement f : filters) {
            f.setValue("");
        }
    }

    /** Textos de todas as células visíveis na grelha */
    public List<String> getVisibleCellsText() {
        movieGrid.shouldBe(visible);
        return $$("vaadin-grid vaadin-grid-cell-content").texts();
    }

    /** Para simplificar, devolve os textos da grelha (inclui títulos) */
    public List<String> getVisibleMovieTitles() {
        movieGrid.shouldBe(visible);
        return $$("vaadin-grid vaadin-grid-cell-content").texts();
    }

    /** Seleciona uma linha pelo título do filme */
    public void selectMovieByTitle(String title) {
        movieGrid.shouldBe(visible);
        $$("vaadin-grid vaadin-grid-cell-content")
                .findBy(text(title))
                .click();
    }

    /** Clica no "Click to IMBD site" da linha do filme dado */
    public void clickImbdLink(String movieTitle) {
        movieGrid.shouldBe(visible);

        // célula que contém o título
        SelenideElement titleCell = $$("vaadin-grid-cell-content")
                .findBy(text(movieTitle))
                .shouldBe(visible);

        // linha (no Vaadin costuma ser parent().parent())
        SelenideElement row = titleCell.parent().parent();

        // link IMBD
        SelenideElement link = row.$("a");
        link.shouldBe(visible).click();
    }
}
