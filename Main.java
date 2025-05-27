import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        // Full screen resolution
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();

        // Game canvas
        Game gameCanvas = new Game(width, height);

        StackPane root = new StackPane();
        root.getChildren().add(gameCanvas);
        gameCanvas.setupStartButton(root);
        gameCanvas.setupDropdownButton(root);
        gameCanvas.setupSlider(root); // This should create volumeControls and add it to root


        Scene scene = new Scene(root, width, height);
        scene.setOnKeyPressed(gameCanvas::handleKeyPress);
        scene.setOnKeyReleased(gameCanvas::handleKeyRelease);

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);


        stage.setTitle("JavaFX Game");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        gameCanvas.start(); // Start the game loop
    }

    public static void main(String[] args) {
        launch();
    }
}
