import javafx.application.Platform;

import java.util.List;
import java.util.Objects;

public class Controller {
    private GUI view;
    private Searcher c;
    private List<SearchResult> searchResults;

    public Controller (GUI view) {
        this.view = view;
        this.c = new Searcher();
        view.getSearchButton().setOnAction(event -> performSearch());
        view.getCrawlButton().setOnAction(event -> performCrawl());
    }

    private void performSearch () {
        String query = view.getQuery();
        boolean isBoosted = view.isPageRankBoosted();
        List<SearchResult> results = performanceSearch(query, isBoosted);
        view.setSearchResults(results);
    }

    private void performCrawl () {
        String seed = view.getSeed();

        if (seed.isEmpty()) {
            seed = "https://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html";
            c.crawl(seed);
            view.setCrawlLabelText("Crawl complete using default seed (https://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html).");
        }
        else {
            try {
                c.crawl(seed);
                view.setCrawlLabelText("Crawl complete!");
            } catch (Exception e) {
                view.setCrawlLabelText("Crawl could not be completed, try again.");
            }
        }
    }

    private List<SearchResult> performanceSearch(String query, boolean isBoosted) {
        return c.search(query, isBoosted, 10);
    }
}
