import java.io.File;
import java.io.IOException;
import java.util.*;

public class crawlMain implements ProjectTester {
    private osutil os;
    private ArrayList<String> links;
    private ArrayList<String> indexes;
    private ArrayList<String> titles;
    private HashMap<String, String> linkMap;
    /*
    This method must delete any existing data that has been stored from any previous crawl.
    This method should also perform any other initialization needed by your system.
    This method will be always called before executing the crawl for a new dataset
     */
    public void initialize(){
        os = new osutil("resources");
        os.deleteFolder(new File("resources"));
    }

    /*
    This method performs a crawl starting at the given seed URL.
    It should visit each page it can find once.
    It should not stop until it has visited all reachable pages.
    All data required for later search queries should be saved in files once this completes.
     */
    public void crawl(String seedURL){
        initialize();

        crawler c = new crawler();
        try {
            c.crawl(seedURL);
        }
        catch (IOException e){
            System.out.println("CRITICAL FAILURE: could not finish crawl");
        }
        links = os.readFile("links");
        indexes = os.readFile("index");
        titles= os.readFile("title");

        linkMap = new HashMap<String, String>();
        for (int i = 0; i < links.size(); i++) {
            linkMap.put(links.get(i), indexes.get(i));
        }
    }

    /*
    Returns a list of the outgoing links of the page with the given URL.
    That is, the URLs that the page with the given URL links to.
    If no page with the given URL exists, returns null.
     */
    public List<String> getOutgoingLinks(String url){
        if(linkMap.containsKey(url)){
            return os.readFile("outgoing"+ File.separator +linkMap.get(url));
        }
        else {
            return null;
        }
    }

    /*
    Returns a list of the incoming links for the page with the given URL.
    That is, the URLs that link to the page with the given URL
    If no page with the given URL exists, returns null.
     */
    public List<String> getIncomingLinks(String url){
        if(linkMap.containsKey(url)){
            return os.readFile("incoming"+ File.separator +linkMap.get(url));
        }
        else {
            return null;
        }
    }

    /*
    Returns the PageRank value for the page with the given URL.
    If no page with the given URL exists, returns -1.
     */
    public double getPageRank(String url){
        try {
            if(linkMap.containsKey(url)){
                return Float.parseFloat(os.readFile("pagerank"+ File.separator + linkMap.get(url)).get(0));
            }
            else {
                return 0;
            }
        }
        catch (NullPointerException e){
            return 0;
        }
    }

    /*
    Returns the IDF value for the given word.
    A word that did not show up during the crawl should have an IDF of 0.
     */
    public double getIDF(String word){
        try {
            return Float.parseFloat(os.readFile("idf"+File.separator+word).get(0));
        }
        catch (NullPointerException e){
            return 0;
        }
    }

    /*
    Returns the term frequency of the given word within the page with the given URL.
    If the word did not appear on the given page, the TF should be 0.
     */
    public double getTF(String url, String word){
        try {
            if(linkMap.containsKey(url)){
                return Float.parseFloat(os.readFile("tf"+ File.separator +linkMap.get(url)+word).get(0));
            }
            else {
                return 0;
            }
        }
        catch (NullPointerException e){
            return 0;
        }
    }

    /*
    Returns the TF-IDF value of the given word within the page with the given URL.
     */
    public double getTFIDF(String url, String word){
        return (float) (Math.log(1f+getTF(url,word))/Math.log(2))*getIDF(word);
    }

    /*
    Performs a search using the given query.
    If boost is true, the search score for a page should be boosted by the page's PageRank value.
    If boost is false, the search score for a page will be only based on cosine similarity.
    This method must return a list of objects that implement the SearchResult interface.
    The list should return the top X search results for the given query/boost values.
    Results should be sorted from highest score to lowest.
    If two search results have the same score when rounded to 3 decimal places,
    the scores for those two results should be considered identical and their
    lexicographical ordering (this is what Java's String compareTo() method uses)
    should be used to determine which goes before the other.
    A copy of this interface is included on the project's BrightSpace page.
     */
    public List<SearchResult> search(String query, boolean boost, int X) {
         ArrayList<SearchResult> top = new ArrayList<SearchResult>();

        String[] phrase_arr = query.split(" ");

        HashMap<String, Integer> words = new HashMap<String, Integer>();

        for (String s : phrase_arr) {
            if (words.containsKey(s)) {
                words.put(s, words.get(s) + 1);
            } else {
                words.put(s, 1);
            }
        }

        ArrayList<Double> qvector = new ArrayList<Double>();
        for (String key : words.keySet()) {
            double value = (float) (Math.log(1f + (float) words.get(key) / phrase_arr.length) * getIDF(key));
            qvector.add(value);
        }

        Double[][] vectors = new Double[links.size()][qvector.size()];
        int index = 0;
        for (String key : words.keySet()) {
            for (int j = 0; j < links.size(); j++) {
                vectors[j][index] = getTFIDF(links.get(j), key);
            }
            index++;
        }


        Double[] cosine = new Double[vectors.length];

        for (int i = 0; i < vectors.length; i++) {
            double numerator = 0;
            double leftdenom = 0;
            double rightdenom = 0;
            for (int j = 0; j < qvector.size(); j++) {
                numerator += vectors[i][j] * qvector.get(j);
                leftdenom += (qvector.get(j)) * (qvector.get(j));
                rightdenom += (vectors[i][j]) * (vectors[i][j]);
            }
            if (numerator == 0) cosine[i] = 0.0;
            else cosine[i] = ((numerator) / (Math.sqrt(leftdenom) * Math.sqrt(rightdenom)));
        }
        if (boost) {
            for (int i = 0; i < cosine.length; i++) {
                cosine[i] *= getPageRank(links.get(i));
                cosine[i] = Math.round(cosine[i] * 1000.0) / 1000.0;
            }
        }
        else {
            for (int i = 0; i < cosine.length; i++) {
                cosine[i] = Math.round(cosine[i] * 1000.0) / 1000.0;
            }
        }

        int[] already_picked = new int[X];
        Arrays.fill(already_picked, -1);
        for (int i = 0; i < X; i++) {
            double high = -2f;
            int high_index = -2;

            for (int j = 0; j < cosine.length; j++) {
                if (cosine[j] > high && !searchArray(already_picked, j)) {
                    high = cosine[j];
                    high_index = j;
                }
            }

            already_picked[i] = high_index;
            SearchResult entry = new SearchResult(titles.get(high_index), cosine[high_index], links.get(high_index));
            top.add(entry);
        }
        Comparator<SearchResult> customComparator = Comparator
                .<SearchResult, Double>comparing(SearchResult::getScore)
                .reversed()
                .thenComparing(SearchResult::getTitle);

        // Sort the list based on score and title
        Collections.sort(top, customComparator);

        return top;
    }
    private boolean searchArray(int[] arr, int x){
        if(arr != null){
            for(int i : arr){
                if(i == x){
                    return true;
                }
            }
        }
        return false;
    }
}
