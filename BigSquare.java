import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BigSquare extends Enemies {

    public BigSquare() {
        super();
    }

    public BigSquare(int x, int y, int w, int h, int speed) {
        super(x, y, w, h, speed, 100, 100, 5, new Image("file:BigSquare.png"));
    }

}

