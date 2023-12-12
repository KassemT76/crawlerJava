import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[]args) throws IOException {
        Searcher c = new Searcher();
        c.crawl("https://people.scs.carleton.ca/~davidmckenney/tinyfruits/N-0.html");
        BufferedReader aFile;
        aFile = new BufferedReader(new FileReader("test-resources" + File.separator + "FruitsTinySearchTester-expected-search-results" +".txt"));
        int count = 0;
        int pass_count = 0;
        int fail_count = 0;

        while (true){
            boolean passed = true;
            String expected_output = "";
            String actual_output = "";

            String query = aFile.readLine();
            if(query == null) break;
            boolean boost = Boolean.parseBoolean(aFile.readLine());
            int x = Integer.parseInt(aFile.readLine());
            System.out.println("Test #"+count);
            List<SearchResult> result = c.search(query, boost, x);
            System.out.println("Searching for" +x+ " results for \""+query+"\" boost="+boost);
            for(int i = 0; i < result.size(); i++){
                String expected_title = aFile.readLine();
                double expected_score = Double.parseDouble(aFile.readLine());

                expected_output += i + ". " + expected_title + " " + expected_score + "\n";

                String actual_title = result.get(i).getTitle();
                double actual_score = result.get(i).getScore();

                actual_output += i + ". " + actual_title + " " + actual_score + "\n";

                if(Objects.equals(actual_title, expected_title) && compareDouble(actual_score, expected_score) && passed) {
                    passed = true;
                }
                else {
                    passed = false;
                }
            }
            if(passed){
                System.out.println("Passed");
                pass_count++;
            }
            else {
                System.out.println("FAILED!");
                System.out.println("Expected Result");
                System.out.println(expected_output);
                System.out.println("Actual Result");
                System.out.println(actual_output);
                fail_count++;
            }
            count++;
        }
        System.out.printf("Passed %d/%d tests \n", pass_count, count);
        System.out.printf("Failed %d/%d tests \n", fail_count, count);
    }
    private static boolean compareDouble(double x, double y){
        double difference = x-y;
        if(difference < 0.001) return true;

        return false;
    }
}