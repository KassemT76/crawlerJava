public class Fruits5AllTester {
    public static void main(String[] args) throws Exception {
        ProjectTester tester = new ProjectTesterImp(); //Instantiate your own ProjectTester instance here
        long first = System.currentTimeMillis();
        tester.initialize();
        tester.crawl("https://people.scs.carleton.ca/~davidmckenney/fruits5/N-0.html");
        long last = System.currentTimeMillis();

        System.out.println(last-first);

        first = System.currentTimeMillis();
        Fruits5OutgoingLinksTester.runTest(tester);
        Fruits5IncomingLinksTester.runTest(tester);
        Fruits5PageRanksTester.runTest(tester);
        Fruits5IDFTester.runTest(tester);
        Fruits5TFTester.runTest(tester);
        Fruits5TFIDFTester.runTest(tester);
        Fruits5SearchTester.runTest(tester);
        last = System.currentTimeMillis();
        System.out.println("Finished running all tests.");
        System.out.println(last-first);
    }
}
