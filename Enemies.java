import java.util.ArrayList;

import javafx.scene.image.Image;

public class Enemies extends Player {

    private int value;

    public Enemies() {
        super();
    }

    public Enemies(int x, int y, int w, int h, int speed, int health, int maxHealth, int value, Image pic) {
        super(x, y, speed, health, maxHealth, value, w, h, pic );
    }

    public void moveTowardsPlayer(int playerX, int playerY, ArrayList<Enemies> allEnemies) {
        int dx = playerX - getX();
        int dy = playerY - getY();
    
        double steerX = dx;
        double steerY = dy;
    
        double separationX = 0;
        double separationY = 0;
        int separationRadius = 100;
    
        for (Enemies other : allEnemies) {
            if (other == this) continue;
    
            double distance = Math.hypot(other.getX() - getX(), other.getY() - getY());
            if (distance < separationRadius && distance > 0) {
                double pushStrength = Math.min(1.0, 1.0 / distance);
                separationX -= (other.getX() - getX()) * pushStrength;
                separationY -= (other.getY() - getY()) * pushStrength;

                
            }
        }
    
        steerX += separationX * 100;
        steerY += separationY * 100;
    
        double magnitude = Math.sqrt(steerX * steerX + steerY * steerY);
        if (magnitude < 1e-3) return; // was <1, now much smaller to prevent jitter
    
        double normalizedDx = steerX / magnitude;
        double normalizedDy = steerY / magnitude;
    
        setX((int) (getX() + normalizedDx * getSpeed()));
        setY((int) (getY() + normalizedDy * getSpeed()));
    }
    

    // Class field to store previous direction for smoothing (add to Enemies class)
private double lastDirX = 0;
private double lastDirY = 0;

public void HoverPlayer(int playerX, int playerY, ArrayList<Enemies> allEnemies) {
    double dx = playerX - getX();
    double dy = playerY - getY();
    double distanceToPlayer = Math.hypot(dx, dy);

    double orbitRadius = 600;
    double steerX;
    double steerY;

    if (distanceToPlayer > orbitRadius) {
        // Move toward the player
        steerX = dx;
        steerY = dy;
    } else {
        // Orbit: perpendicular movement
        steerX = -dy;
        steerY = dx;
    }

    // Separation force
    double separationX = 0;
    double separationY = 0;
    int separationRadius = 100;

    for (Enemies other : allEnemies) {
        if (other == this) continue;

        double ox = other.getX() - getX();
        double oy = other.getY() - getY();
        double dist = Math.hypot(ox, oy);

        if (dist < separationRadius && dist > 0) {
            double pushStrength = Math.min(1.0, 1.0 / dist);
            separationX -= ox * pushStrength;
            separationY -= oy * pushStrength;
        }
    }

    // Blend separation (less aggressive)
    steerX += separationX * 50;  // was 100
    steerY += separationY * 50;

    // Normalize
    double magnitude = Math.hypot(steerX, steerY);
    if (magnitude < 1e-3) return;

    steerX /= magnitude;
    steerY /= magnitude;

    // ✅ Smooth the direction: apply inertia (lerp between lastDir and new dir)
    double smoothing = 0.2; // higher = snappier, lower = smoother
    lastDirX = lastDirX * (1 - smoothing) + steerX * smoothing;
    lastDirY = lastDirY * (1 - smoothing) + steerY * smoothing;

    double finalMag = Math.hypot(lastDirX, lastDirY);
    if (finalMag < 1e-3) return;

    double finalX = lastDirX / finalMag;
    double finalY = lastDirY / finalMag;

    setX((int) (getX() + finalX * getSpeed()));
    setY((int) (getY() + finalY * getSpeed()));
}


   

    



    @Override
    public String toString() {
        return "Enemy";
    }
}


