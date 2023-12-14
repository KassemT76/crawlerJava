import javafx.application.Platform;

import java.util.List;

public class Controller {
    private GUI view;
    private ProjectTesterImp c;
    private List<SearchResult> searchResults; // This will be your actual search results.
    private boolean crawled;

    public Controller (GUI view) {
        this.view = view;
        this.c = new ProjectTesterImp();
        crawled = false;
        view.getSearchButton().setOnAction(event -> performSearch());
        view.getCrawlButton().setOnAction(event -> performCrawl());
    }

    private void performSearch () {
        view.clearSearchResults();
        String query = view.getQuery();
        boolean isBoosted = view.isPageRankBoosted();

        try {
            List<SearchResult> results = performanceSearch(query, isBoosted);
            view.setSearchResults(results);

            if (!crawled){
                view.setCrawlLabelText("Getting data from seed: " + c.getSeedURL());
            }
        } catch (Exception e){
            view.setCrawlLabelText("Please crawl a valid webpage! The seed URL given, "+c.getSeedURL()+" ,is invalid.");
        }


    }

    private void performCrawl () {
        String seed = view.getSeed();
        
        // Better method of displaying the "Crawling..." text.
        try {
            c.initialize();
            view.setCrawlLabelText("Crawling...");

            new Thread(() -> {
                try {
                    c.crawl(seed);

                    Platform.runLater(() -> {
                        view.setCrawlLabelText("Crawl complete!");
                        crawled = true;
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        view.setCrawlLabelText("Crawl could not be completed, try again.");
                    });
                }
            }).start();

        } catch (Exception e) {
            view.setCrawlLabelText("An unexpected error occurred.");
        }
    }

    private List<SearchResult> performanceSearch(String query, boolean isBoosted) {
        return c.search(query, isBoosted, 10);
    }
}
