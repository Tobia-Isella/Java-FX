import javafx.scene.image.Image;

public class Missile extends Weapons {
    private double preciseX, preciseY;
    private double dx, dy;

    public Missile(int x, int y, int w, int h, int dmg) {
        super(dmg, x, y, w, h, 0, 0, new Image("file:PlaceHolderMissile.png"));
        this.preciseX = x;
        this.preciseY = y;
    }

    public void fireTowards(double targetX, double targetY) {
        double deltaX = targetX - preciseX;
        double deltaY = targetY - preciseY;
        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (magnitude == 0) return;

        dx = (deltaX / magnitude) * 10;
        dy = (deltaY / magnitude) * 10;
    }

    public void update() {
        preciseX += dx;
        preciseY += dy;
        setX((int) preciseX);
        setY((int) preciseY);
    }
}
