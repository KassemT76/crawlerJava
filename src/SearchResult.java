public class SearchResult {
    private String title;
    private double score;
    private String url;

    SearchResult(String title, double score){
        this.title = title;
        this.score = score;
    }
    SearchResult(String title, double score, String url){
        this.title = title;
        this.score = score;
        this.url = url;
    }
    /*
    Returns the title of the page this search result is for.
     */
    public String getTitle(){
        return title;
    }
    /*
    Returns the search score for the page this search result is for.
     */
    public double getScore(){
        return score;
    }
    public String getUrl(){
        return url;
    }
    public String toString(){
        return String.format("Score: %.4f, Title: %s, URL: %s", score, title, url);
    }
}
