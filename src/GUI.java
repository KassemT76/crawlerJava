import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

public class GUI extends Application {

    private TextField searchField;
    private TextField seedField;
    private Button searchButton;
    private Button crawlButton;
    private Label crawlLabel;
    private CheckBox pageRankBoost;
    private ListView<String> searchResults;
    private Controller controller;

    @Override
    public void start (Stage primaryStage) {
        Pane root = new Pane();

        initUIComponents(root);

        Scene scene = new Scene(root, 600, 375);
        primaryStage.setScene(scene);
        primaryStage.setTitle("1406Z Course Project");
        primaryStage.show();

        controller = new Controller(this);
    }

    private void initUIComponents (Pane root) {
        searchField = new TextField();
        searchField.setPromptText("Enter query");
        searchField.setLayoutX(10);
        searchField.setLayoutY(60);
        searchField.setPrefSize(450, 30);

        seedField = new TextField();
        seedField.setPromptText("Enter seed");
        seedField.setLayoutX(10);
        seedField.setLayoutY(20);
        seedField.setPrefSize(450, 30);

        searchButton = new Button("Search");
        searchButton.setLayoutX(470);
        searchButton.setLayoutY(60);
        searchButton.setPrefSize(120, 30);

        crawlButton = new Button("Crawl");
        crawlButton.setLayoutX(470);
        crawlButton.setLayoutY(20);
        crawlButton.setPrefSize(120, 30);

        crawlLabel = new Label("Enter seed URL and click Crawl to start.");
        crawlLabel.setLayoutX(10);
        crawlLabel.setLayoutY(0);

        pageRankBoost = new CheckBox("Use PageRank boost");
        pageRankBoost.setLayoutX(10);
        pageRankBoost.setLayoutY(100);

        searchResults = new ListView<>();
        searchResults.setLayoutX(10);
        searchResults.setLayoutY(128);
        searchResults.setPrefSize(580, 237);
        searchResults.setStyle("-fx-background-radius: 7; -fx-padding: 3;");

        root.getChildren().addAll(searchField, seedField, searchButton, crawlButton, crawlLabel, pageRankBoost, searchResults);
    }

    public String getQuery () {
        return searchField.getText();
    }

    public String getSeed () {
        return seedField.getText();
    }

    public boolean isPageRankBoosted () {
        return pageRankBoost.isSelected();
    }

    public void setSearchResults (List<SearchResult> results) {
        searchResults.getItems().clear();

        for (SearchResult searchResult : results) {
            searchResults.getItems().add(String.format("Title: %s Score: %.3f ", searchResult.getTitle(), searchResult.getScore()));
        }
    }

    public Button getSearchButton () {
        return searchButton;
    }

    public Button getCrawlButton() {
        return crawlButton;
    }

    public void setCrawlLabelText(String text) {
        crawlLabel.setText(text);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
