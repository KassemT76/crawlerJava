import java.util.List;

public class Controller {

    private GUI view;
    private crawlMain c;
    private List<SearchResult> searchResults; // This will be your actual search results.

    public Controller(GUI view) {
        this.view = view;
        this.c = new crawlMain();
        c.crawl("https://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html");
        view.getSearchButton().setOnAction(event -> performSearch());
    }

    private void performSearch() {
        String query = view.getQuery();
        boolean isBoosted = view.isPageRankBoosted();
        List<SearchResult> results = performanceSearch(query, isBoosted, 10);
        view.setSearchResults(results);
    }

    private List<SearchResult> performanceSearch(String query, boolean isBoosted, int x) {
        return c.search(query, isBoosted, x);
    }
}
