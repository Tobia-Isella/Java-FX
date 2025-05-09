import javafx.scene.control.Button;

public class UpgradeButton {
    public Button button;
    private String text;
    private int cost;

    public UpgradeButton(String text, int cost) {
        this.text = text;
        this.cost = cost;
        button = new Button(text + " ($" + cost + ")");
        setupButtonStyle(button);
        
    }

    private void setupButtonStyle(Button button) {
        button.setStyle("""
            -fx-font-size: 24px;
            -fx-background-color: linear-gradient(to bottom right, #2196F3, #1976D2);
            -fx-text-fill: white;
            -fx-background-radius: 30px;
            -fx-border-radius: 30px;
            -fx-padding: 10 20 10 20;
            -fx-cursor: hand;
        """);

        button.setOnMouseEntered(e -> button.setStyle("""
            -fx-font-size: 24px;
            -fx-background-color: linear-gradient(to bottom right, #1976D2, #0D47A1);
            -fx-text-fill: white;
            -fx-background-radius: 30px;
            -fx-border-radius: 30px;
            -fx-padding: 10 20 10 20;
            -fx-cursor: hand;
        """));

        button.setOnMouseExited(e -> button.setStyle("""
            -fx-font-size: 24px;
            -fx-background-color: linear-gradient(to bottom right, #2196F3, #1976D2);
            -fx-text-fill: white;
            -fx-background-radius: 30px;
            -fx-border-radius: 30px;
            -fx-padding: 10 20 10 20;
            -fx-cursor: hand;
        """));
    }

    public void updateButtonText() {
        button.setText(text + " ($" + cost + ")");
    }

    public int getcost() { return (int) cost; }

    public void setCost(int cost) { this.cost = cost; }
}
