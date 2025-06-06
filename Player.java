import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {

    private double x, y, dx, dy;
    private int width, height, health, speed, value, maxHealth, dmg;
    private Image pic;
    private int lastDx = 0;
    private int lastDy = 0;
    private long lastDashTime = 0;
    private final long dashCooldown = 1000; // in milliseconds

   
    private boolean isDashing = false;  // Check if the player is dashing
    private double dashEffectAlpha = 1.0;  // The transparency of the dash effect (1.0 = opaque, 0.0 = transparent)




    public Player() {}

    public Player(int x, int y, int dx, int dy, int speed, int width, int height, int health, int maxHealth, Image pic) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.width = width;
        this.height = height;
        this.pic = pic;
        this.health =health;
        this.speed =speed;
        this.maxHealth = maxHealth;
    }

    public Player(int x, int y, int speed, int health, int maxHealth, int value, int width, int height, Image pic) {
        this.x = x;
        this.y = y;
        this.speed =speed;
        this.width = width;
        this.height = height;
        this.pic = pic;
        this.value =value;
        this.health =health;
        this.maxHealth =maxHealth;
       
    }


   
    public void drawActor(GraphicsContext gc, int cameraX, int cameraY) {
        double screenX = x - cameraX;
        double screenY = y - cameraY;

        // Draw image or rectangle
        if (pic != null) {
            gc.drawImage(pic, screenX, screenY, width, height);
        } else {
            gc.setFill(javafx.scene.paint.Color.BLUE);
            gc.fillRect(screenX, screenY, width, height);
        }

        // --- Health Bar Drawing ---
        double barWidth = width;
        double barHeight = 8;
        double healthPercent = (double) health / maxHealth;
        double currentBarWidth = barWidth * healthPercent;

        // Bar position (above the actor)
        double barX = screenX;
        double barY = screenY - 15; // 7 pixels above the top of the actor

        // Background (gray)
        gc.setFill(javafx.scene.paint.Color.DARKGRAY);
        gc.fillRect(barX, barY, barWidth, barHeight);

        // Foreground (green/yellow/red based on health)
        javafx.scene.paint.Color barColor = javafx.scene.paint.Color.LIME;
        if (healthPercent < 0.5) barColor = javafx.scene.paint.Color.ORANGE;
        if (healthPercent < 0.25) barColor = javafx.scene.paint.Color.RED;

        gc.setFill(barColor);
        gc.fillRect(barX, barY, currentBarWidth, barHeight);
    }
    
        
    public void move(int worldWidth, int worldHeight) {
        x += dx;
        y += dy;

        // Clamp to world bounds
        if (x < 0) x = 0;
        if (x + width > worldWidth) x = worldWidth - width;
        if (y < 0) y = 0;
        if (y + height > worldHeight) y = worldHeight - height;
    }

    public void dash() {
        int dashDistance = 255;  // Distance to move during the dash
    
        // Update player's position based on last direction of movement
        if (lastDx != 0) {
            x += dashDistance * Integer.signum(lastDx);
        } else if (lastDy != 0) {
            y += dashDistance * Integer.signum(lastDy);
        }
    
        // Set dash effect active and store the time of the dash
        isDashing = true;
        lastDashTime = System.currentTimeMillis();
    
    }
    
    

    //Collison detection

    public boolean Collision(Player b) {
		return( getX()+getWidth()>=b.getX() && getX()<=b.getX()+b.getWidth() && getY()+getHeight()>=b.getY() && getY()<=b.getY()+b.getHeight());

	}

    // Getters and setters
    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getHealth() { return health; }
    public int getSpeed() { return speed; }
    public double getDX() { return dx; }
    public double getDY() { return dy; }
    public int getValue() { return (int) value; }
    public int getMaxHealth() { return (int) maxHealth; }
    public Image getPic() { return (Image) pic; }
    

    public int getLastDx() {
        return lastDx;
    }
    
    public int getLastDy() {
        return lastDy;
    }
    

    public void setDx(int dx) {
        this.dx = dx;
        if (dx != 0) {
            lastDx = dx;
            lastDy = 0;
        }
    }
    
    public void setDy(int dy) {
        this.dy = dy;
        if (dy != 0) {
            lastDy = dy;
            lastDx = 0;
        }
    }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setHealth(int health) { this.health = health; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setImage(Image pic) { this.pic = pic; }
    public void setMaxHealth(int maxHealth ) { this.maxHealth = maxHealth; }

    public long getLastDashTime() {
        return lastDashTime;
    }
    
    public void setLastDashTime(long time) {
        lastDashTime = time;
    }
    
    public long getDashCooldown() {
        return dashCooldown;
    }
    
}
