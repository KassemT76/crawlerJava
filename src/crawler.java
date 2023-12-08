import java.awt.geom.Arc2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class crawler {
    private String webString;
    private String baseurl;
    private String title;
    private ArrayList<String> links;
    private HashMap<String, Double> tf;
    public HashMap<String, ArrayList<String>> idf_counter;



    private ArrayList<String> outlinks;


    crawler(){
        title = "";
        baseurl = "";

        links = new ArrayList<String>();
        outlinks = new ArrayList<String>();

        tf  = new HashMap<String, Double>();
        idf_counter = new HashMap<String, ArrayList<String>>();
    }

    public void fetchString(String url){
        try {
            webString = WebRequester.readURL(url);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        int index = 0;
        for(int i = url.length()-1; i > 0; i--){
            if(url.charAt(i) == '/'){
                index = i;
                break;
            }
        }
        baseurl = url.substring(0,index);
    }
    public String getWebString(){return webString;}

    public int scrape_url(String scraping_link){
        ArrayList<String> words = new ArrayList<String>();
        HashMap<String, Integer> wordCount  = new HashMap<String, Integer>();

        tf.clear();
        outlinks.clear();
        title = "";

        fetchString(scraping_link);

        if(webString == null){return 1;}

        int i = 0;

        while(i < webString.length()-1){
            //Tag Detection
            if(webString.charAt(i) == '<'){
                //Title Detection
                if(webString.substring(i + 1, i + 6).equals("title")){
                    while(webString.charAt(i) != '>'){
                        i++;
                    }
                    i++;
                    while (!(webString.charAt(i) == '<' || webString.charAt(i) == '/')){
                        title += webString.charAt(i);
                        i++;
                    }
                }
                //P Detection
                if(webString.charAt(i+1)=='p'){
                    String word = "";
                    while(webString.charAt(i) != '>'){
                        i++;
                    }
                    i++;
                    while (!(webString.charAt(i) == '<' || webString.charAt(i) == '/')){
                        if(!word.isEmpty() && (webString.charAt(i) == ' ' || webString.substring(i, i + 1).equals("\n"))){
                            words.add(word);
                            word = "";
                        }
                        else if(!(webString.substring(i, i + 1).equals("\n"))){
                            word += webString.charAt(i);
                        }
                        i++;
                    }
                    for(String currWord : words){
                        if(wordCount.containsKey(currWord)){
                            wordCount.put(currWord,wordCount.get(currWord)+1);
                        }
                        else{
                            wordCount.put(currWord, 1);
                        }
                        if(idf_counter.containsKey(currWord)){
                                if(!idf_counter.get(currWord).contains(scraping_link)) {
                                    idf_counter.get(currWord).add(scraping_link);
                                }
                        }
                        else {
                            ArrayList<String> tempList = new ArrayList<String>();
                            tempList.add(scraping_link);
                            idf_counter.put(currWord, tempList);
                        }
                    }
                    int size = words.size();
                    for(String wordKey : wordCount.keySet()){
                        tf.put(wordKey, (double) (wordCount.get(wordKey))/words.size());
                    }
                }
                if(webString.charAt(i+1)=='a'){
                    String link = "";

                    i+=3;

                    while (!(webString.charAt(i) == '<' || webString.charAt(i) == '/')){
                        if (webString.substring(i - 6, i).equals("href=\"")){
                            while (webString.charAt(i) != '\"'){
                                link += webString.charAt(i);
                                i++;
                            }
                        }
                        i++;
                    }
                    link = baseurl+link.substring(1);

                    if(!links.contains(link)){
                        links.add(link);
                    }
                    if(!outlinks.contains(link)){
                        outlinks.add(link);
                    }
                }
            }
            i++;
        }
//        System.out.println(title);
//        System.out.println(words);
//        System.out.println(links);
//        System.out.println(outlinks);
//        System.out.println(tf);
//        System.out.println(idf_counter);

        return 1;
    }

    public void crawl(String seed) throws IOException {
        idf_counter.clear();
        links.clear();
        tf.clear();
        idf_counter.clear();
        outlinks.clear();

        osutil os = new osutil("resources");

        os.createFolder("idf");
        os.createFolder("tf");
        os.createFolder("pagerank");
        os.createFolder("incoming");
        os.createFolder("outgoing");

        os.createFile("index");
        os.createFile("title");
        os.createFile("links");
        os.createFile("baseurl");

        links.add(seed);
        for(int i = 0; i < links.size(); i++){
            String cur_link = links.get(i);

            scrape_url(cur_link);

            os.appendFile("title", title);
            os.appendFile("index", String.valueOf(i));
            os.appendFile("links", cur_link);

            os.createFileWithList("outgoing"+File.separator+String.valueOf(i), outlinks);
            os.createFileWithHash("tf"+File.separator+i, tf);
        }
        os.appendFile("baseurl",baseurl);
        //Incoming links
        HashMap<String, Integer> linkMap = new HashMap<String, Integer>();

        for(int i = 0; i < links.size();i++){
            linkMap.put(links.get(i), i);
        }

        for(int i = 0; i < links.size(); i++){
            String link = links.get(i);

           ArrayList<String> outlink = os.readFile("outgoing"+File.separator+i);

           for(String j : outlink){
                os.appendFile("incoming"+File.separator+linkMap.get(j), link);
           }
        }
        //IDF
        HashMap<String, Double> idf = new HashMap<String, Double>();

        for (String key: idf_counter.keySet()){
            idf.put(key, (Math.log((double) links.size() /(1+ idf_counter.get(key).size()))/Math.log(2)));
        }
        os.createFileWithHash("idf"+File.separator, idf);
        //Pagerank
        Double[][] matrix = new Double[links.size()][links.size()];
        double alpha = 0.1;

        for(int i = 0; i < links.size(); i++){
            ArrayList<String> inlinks = os.readFile("incoming"+File.separator+linkMap.get(links.get(i)));
            for(int j = 0; j < links.size(); j++){
                if(inlinks.contains(links.get(j))){
                    matrix[i][j] = 1.0;
                    matrix[i][j] /= inlinks.size();
                    matrix[i][j] = (1-alpha)*matrix[i][j];
                    matrix[i][j] += alpha/links.size();
                }
                else {
                    matrix[i][j]= 0.0;
                    matrix[i][j] += alpha/links.size();
                }
            }
        }
        Double[][] t = new Double[1][links.size()];
        double distance = 1f;
        for (int i = 0; i < links.size(); i++){
            t[0][i] = 1.0/links.size();
        }

        while (distance > 0.0001){
            Double[][] old_t = t;
            t = mult_matrix(t, matrix);
            distance = euclidean_dist(t, old_t);
        }
        for (int i = 0; i < t[0].length; i++) {
            os.appendFile("pagerank"+File.separator+i, String.valueOf(t[0][i]));
        }

    }
    private Double[][] mult_matrix(Double[][] a, Double[][] b){
        Double[][] result = new Double[a.length][a[0].length];

        for (int i = 0; i < a.length; i++){
            for (int j = 0; j < b[0].length; j++){
                result[i][j] = 0.0;
                for(int k = 0; k < a[0].length; k++){
                    result[i][j] += a[i][k]*b[k][j];
                }
            }
        }
        return result;
    }
    private double euclidean_dist(Double[][] a, Double[][] b){
        double result = 0f;

        for (int i = 0; i < a[0].length; i++) {
            result += (a[0][i]-b[0][i])*(a[0][i]-b[0][i]); //basically just (a+b)^2 but im too lazy to check the exp function in java
        }
        return Math.sqrt(result);
    }
}

