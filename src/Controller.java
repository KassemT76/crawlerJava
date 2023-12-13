import javafx.application.Platform;

import java.util.List;

public class Controller {

    private GUI view;
    private Searcher c;
    private List<SearchResult> searchResults; // This will be your actual search results.
    private boolean crawled;

    public Controller (GUI view) {
        this.view = view;
        this.c = new Searcher();
        crawled = false;
        view.getSearchButton().setOnAction(event -> performSearch());
        view.getCrawlButton().setOnAction(event -> performCrawl());
    }

    private void performSearch () {
        String query = view.getQuery();
        boolean isBoosted = view.isPageRankBoosted();
        System.out.println(query);

        if(crawled){
            List<SearchResult> results = performanceSearch(query, isBoosted, 10);
            view.setSearchResults(results);

        }else {
            view.setCrawlLabelText("Please crawl a webpage before searching...");
        }
    }

    private void performCrawl () {
        String seed = view.getSeed();
        
        // Better method of displaying the "Crawling..." text.
        try {
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

    private List<SearchResult> performanceSearch(String query, boolean isBoosted, int x) {
        return c.search(query, isBoosted, x);
    }
}
