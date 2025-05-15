import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Runner extends Enemies {

    public Runner() {
        super();
    }

    public Runner(int x, int y, int w, int h, int speed) {
        super(x, y, w, h, speed,15, 15, 1, new Image("file:PlaceHolderBlue.png"));
    }

}
