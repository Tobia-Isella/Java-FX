import java.util.List;
import java.util.Queue;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Boss extends Enemies {

    private long lastBombTime = 0;
    private static final long BOMB_COOLDOWN = 2000; // 2 seconds cooldown
    private static final double SHOOT_RADIUS = 1000; // Only shoot bombs if closer than 300 pixels
    private Queue<BombSpawnTask> bombSpawnQueue;


    public Boss() {
        super();
    }

    public Boss(int x, int y, int w, int h, int speed, Queue<BombSpawnTask> bombSpawnQueue) {
        super(x, y, w, h, speed,500, 500, 100, new Image("file:Boss.png"));
        this.bombSpawnQueue = bombSpawnQueue;
    }

    public void tryFireBombs(Player player, List<Bomb> bombs) {
        long currentTime = System.currentTimeMillis();

        double dx = player.getX() - this.getX();
        double dy = player.getY() - this.getY();
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist <= SHOOT_RADIUS && currentTime - lastBombTime >= BOMB_COOLDOWN) {
            fireBombs(6, 1000, 5, player); // Example params, you can adjust
            lastBombTime = currentTime;
        }
    }

public void fireBombs(int count, double spreadRadius, double speed, Player player) {
    double playerX = player.getX();
    double playerY = player.getY();

    for (int i = 0; i < count; i++) {
        double angle = Math.random() * 2 * Math.PI;
        double distance = Math.random() * spreadRadius;

        double targetX = playerX + Math.cos(angle) * distance;
        double targetY = playerY + Math.sin(angle) * distance;

        double dx = targetX - (this.getX() + this.getWidth() / 2.0);
        double dy = targetY - (this.getY() + this.getHeight() / 2.0);
        double mag = Math.sqrt(dx * dx + dy * dy);

        double vx = (dx / mag) * speed;
        double vy = (dy / mag) * speed;

        double spawnX = this.getX() + this.getWidth() / 2.0;
        double spawnY = this.getY() + this.getHeight() / 2.0;

        bombSpawnQueue.add(new BombSpawnTask(spawnX, spawnY, vx, vy, 300, 1000));
    }
}



}
