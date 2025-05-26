import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Consumables {
    private double x, y;
    private int width, height;
    private Image pic;

    public Consumables() {}

    public Consumables(int x, int y, int width, int height, Image pic) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.pic = pic;
 
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
    }

    public boolean Collision(Player b) {
		return( getX()+getWidth()>=b.getX() && getX()<=b.getX()+b.getWidth() && getY()+getHeight()>=b.getY() && getY()<=b.getY()+b.getHeight());

	}


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getPic() {
        return pic;
    }

    public void setPic(Image pic) {
        this.pic = pic;
    }
}