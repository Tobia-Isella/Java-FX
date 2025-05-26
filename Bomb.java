import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bomb extends Weapons {
    private static final Image SHARED_IMAGE = new Image("file:bomb.png"); // only loaded once

    private long spawnTime;
    private boolean exploded;
    private long explosionTime;
    private static final long LIFETIME = 3000; // ms until explosion
    private static final long POST_EXPLOSION_LIFETIME = 500; // shorter for better performance
    private boolean damagedPlayer = false;
    public static final double EXPLOSION_RADIUS = 100;

    private double traveledDistance;
    private double maxTravelDistance;
    private final double vx, vy;
    private final double speedPerFrame;

    private double x, y;
    private boolean stopped = false;

    public Bomb(double startX, double startY, double vx, double vy, double minTravelDistance, double maxTravelDistance) {
        super(20, (int) startX, (int) startY, 80, 80, 0, 0, SHARED_IMAGE);

        this.vx = vx;
        this.vy = vy;
        this.speedPerFrame = Math.sqrt(vx * vx + vy * vy); // only once!
        this.x = startX;
        this.y = startY;
        this.spawnTime = System.currentTimeMillis();
        this.exploded = false;

        this.maxTravelDistance = minTravelDistance + Math.random() * (maxTravelDistance - minTravelDistance);
    }

    public void update(long currentTime) {
        if (!exploded) {
            if (!stopped) {
                x += vx;
                y += vy;

                traveledDistance += speedPerFrame;

                if (traveledDistance >= maxTravelDistance) {
                    stopped = true;
                }

                setX((int) x);
                setY((int) y);
            }

            if (currentTime - spawnTime >= LIFETIME) {
                explode(currentTime);
            }
        }
    }

    private void explode(long currentTime) {
        exploded = true;
        explosionTime = currentTime;
        System.out.println("💥 Bomb exploded at " + currentTime);
    }

    public boolean hasExploded() {
        return exploded;
    }

    public boolean shouldBeRemoved(long currentTime) {
        if (exploded) {
            long elapsed = currentTime - explosionTime;
            System.out.println("⏱️ Bomb exploded, elapsed time: " + elapsed);
            return elapsed >= POST_EXPLOSION_LIFETIME;
    }
    return false;
    }

    public void render(GraphicsContext gc, int cameraX, int cameraY) {
        double screenX = getX() - cameraX;
        double screenY = getY() - cameraY;

        if (exploded) {
            // You can optionally use a pre-rendered explosion image here instead of fillOval
            gc.setFill(Color.rgb(255, 0, 0, 0.3));
            double explosionRadius = 50;
            gc.fillOval(screenX + getW() / 2 - explosionRadius,
                        screenY + getH() / 2 - explosionRadius,
                        explosionRadius * 2, explosionRadius * 2);
        } else {
            if (getImage() != null) {
                gc.drawImage(getImage(), screenX, screenY, getW(), getH());
            } else {
                gc.setFill(Color.BLUE);
                gc.fillRect(screenX, screenY, getW(), getH());
            }
        }
    }

    public boolean hasDamagedPlayer() {
        return damagedPlayer;
    }

    public void setDamagedPlayer(boolean b) {
        damagedPlayer = b;
    }
}
