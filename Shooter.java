import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Shooter extends Enemies {

    private long lastShotTime = 0;
    private long fireCooldown = 2000; // milliseconds

    public Shooter() {
        super();
    }

    public Shooter(int x, int y, int w, int h, int speed) {
        super(x, y, w, h, speed,10, 10, 2, null); // no image
    }

    @Override
    public void drawActor(GraphicsContext gc, int cameraX, int cameraY) {
        double screenX = getX() - cameraX;
        double screenY = getY() - cameraY;

        gc.setFill(Color.PURPLE);
        gc.fillPolygon(
            new double[] { screenX + getWidth()/2, screenX, screenX + getWidth() },
            new double[] { screenY, screenY + getHeight(), screenY + getHeight() },
            3
        );

        double barWidth = getWidth();
        double barHeight = 10;
        double healthPercent = (double) getHealth() / getMaxHealth();
        double currentBarWidth = barWidth * healthPercent;
    
        double barX = screenX;
        double barY = screenY - 15; // 7 pixels above the top point of the triangle
    
        // Background bar (gray)
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);
    
        // Health bar color (green/orange/red)
        Color barColor = Color.LIME;
        if (healthPercent < 0.5) barColor = Color.ORANGE;
        if (healthPercent < 0.25) barColor = Color.RED;
    
        // Current health foreground bar
        gc.setFill(barColor);
        gc.fillRect(barX, barY, currentBarWidth, barHeight);

        
    }

    
    public void maybeFireMissile(List<EnemyMissile> missileList, int playerX, int playerY) {
        long now = System.currentTimeMillis();
    
        // Calculate distance to player
        double dx = playerX - (getX() + getWidth() / 2);
        double dy = playerY - (getY() + getHeight() / 2);
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        double firingRadius = 800; // Change this value to set the range
    
        // Only fire if player is within radius and cooldown has passed
        if (distance <= firingRadius && (now - lastShotTime >= fireCooldown)) {
            EnemyMissile missile = new EnemyMissile(getX() + getWidth() / 2, getY() + getHeight() / 2, 20, 20, 7);
            missile.fireTowards(playerX, playerY);
            missileList.add(missile);
            lastShotTime = now;
        }
    }

    
}
