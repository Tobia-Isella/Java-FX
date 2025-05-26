
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PopEffect {
    private int x, y;
    private int currentFrame = 0;
    
    private long frameTime = 100_000_000; // 100ms
    private long lastFrameTime = System.nanoTime();
    private boolean finished = false;
    private int startY = 312;  // vertical offset of frames in sprite sheet
    private Image spriteSheet;

    public PopEffect(int x, int y, Image spriteSheet) {
        this.x = x;
        this.y = y;
        this.spriteSheet = spriteSheet;

        System.out.println("PopEffect triggered at " + x + "," + y);

    }

    public void update() {
        if (System.nanoTime() - lastFrameTime > frameTime) {
            currentFrame++;
            lastFrameTime = System.nanoTime();
            if (currentFrame >= 3) finished = true;
        }
    }

    public void draw(GraphicsContext gc, int cameraX, int cameraY) {
        if (finished) return;
        gc.drawImage(
            spriteSheet,
            currentFrame * 341, startY, 341, 341, // source rectangle with vertical offset
            x - cameraX, y - cameraY, 100, 100 // destination (scaled down if needed)
            
        );
    }

    public boolean isFinished() {
        return finished;
    }
}

