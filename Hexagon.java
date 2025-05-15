import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Hexagon extends Enemies {

    private long lastShotTime = 0;
    private long fireCooldown = 2000; // milliseconds

    public Hexagon() {
        super();
    }

    public Hexagon(int x, int y, int w, int h, int speed) {
        super(x, y, w, h, speed,5, 5, 3, null); // no image
    }
    @Override
    public void drawActor(GraphicsContext gc, int cameraX, int cameraY) {
        double screenX = getX() - cameraX;
        double screenY = getY() - cameraY;
        double width = getWidth();
        double height = getHeight();

        // Hexagon dimensions
        double centerX = screenX + width / 2;
        double centerY = screenY + height / 2;
        double radius = Math.min(width, height) / 2;

        double[] xPoints = new double[6];
        double[] yPoints = new double[6];

        for (int i = 0; i < 6; i++) {
            xPoints[i] = centerX + radius * Math.cos(Math.toRadians(60 * i - 30));
            yPoints[i] = centerY + radius * Math.sin(Math.toRadians(60 * i - 30));
        }

        gc.setFill(Color.HOTPINK);
        gc.fillPolygon(xPoints, yPoints, 6);

        // Health bar
        double barWidth = width;
        double barHeight = 10;
        double healthPercent = (double) getHealth() / getMaxHealth();
        double currentBarWidth = barWidth * healthPercent;

        double barX = screenX;
        double barY = screenY - 15; // 15 pixels above top Y

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);

        Color barColor = Color.LIME;
        if (healthPercent < 0.5) barColor = Color.ORANGE;
        if (healthPercent < 0.25) barColor = Color.RED;

        gc.setFill(barColor);
        gc.fillRect(barX, barY, currentBarWidth, barHeight);
    }

    
}
