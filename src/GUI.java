import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

public class GUI extends Application {

    private TextField searchField;
    private Button searchButton;
    private CheckBox pageRankBoost;
    private ListView<String> searchResults;
    private Controller controller;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        initUIComponents(root);

        Scene scene = new Scene(root, 600, 325);
        primaryStage.setScene(scene);
        primaryStage.setTitle("1406Z Course Project");
        primaryStage.show();

        controller = new Controller(this);
    }

    private void initUIComponents(Pane root) {
        searchField = new TextField();
        searchField.setPromptText("Enter query");
        searchField.setLayoutX(10);
        searchField.setLayoutY(10);
        searchField.setPrefSize(450, 30);

        searchButton = new Button("Search");
        searchButton.setLayoutX(470);
        searchButton.setLayoutY(10);
        searchButton.setPrefSize(120, 30);

        pageRankBoost = new CheckBox("Use PageRank boost");
        pageRankBoost.setLayoutX(10);
        pageRankBoost.setLayoutY(50);

        searchResults = new ListView<>();
        searchResults.setLayoutX(10);
        searchResults.setLayoutY(78);
        searchResults.setPrefSize(580, 237);
        searchResults.setStyle("-fx-background-radius: 7; -fx-padding: 3;");

        root.getChildren().addAll(searchField, searchButton, pageRankBoost, searchResults);
    }

    public String getQuery() {
        return searchField.getText();
    }

    public boolean isPageRankBoosted() {
        return pageRankBoost.isSelected();
    }

    public void setSearchResults(List<SearchResult> results) {
        searchResults.getItems().clear();
        for (SearchResult searchResult : results) {
            searchResults.getItems().add(String.format("Title: %s Score: %.3f ", searchResult.getTitle(), searchResult.getScore()));
        }
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
