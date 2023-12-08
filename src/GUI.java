import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

public class GUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        TextField searchField = new TextField();
        searchField.setPromptText("Enter query");
        searchField.setLayoutX(10);
        searchField.setLayoutY(10);
        searchField.setPrefSize(450, 30);

        Button searchButton = new Button("Search");
        searchButton.setLayoutX(470);
        searchButton.setLayoutY(10);
        searchButton.setPrefSize(120, 30);

        CheckBox pageRankBoost = new CheckBox("Use PageRank boost");
        pageRankBoost.setLayoutX(10);
        pageRankBoost.setLayoutY(50);

        ListView<String> searchResults = new ListView<>();
        searchResults.setLayoutX(10);
        searchResults.setLayoutY(78);
        searchResults.setPrefSize(580, 237);
        searchResults.setStyle("-fx-background-radius: 7; -fx-padding: 3;");

        root.getChildren().addAll(searchField, searchButton, pageRankBoost, searchResults);

        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            boolean isBoosted = pageRankBoost.isSelected();
            List<String> results = performanceSearch(query, isBoosted);
            searchResults.getItems().setAll(results);
        });

        Scene scene = new Scene(root, 600, 325);
        primaryStage.setScene(scene);
        primaryStage.setTitle("1406Z Course Project");
        primaryStage.show();
    }

    // Add actual results
    private List<String> performanceSearch(String query, boolean isBoosted) {
        return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    }

    public static void main (String[] args) {
        launch(args);
    }
}
