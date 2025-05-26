import javafx.scene.image.Image;

public class DamageCoin extends Consumables {

    private static final Image SHARED_IMAGE = new Image("file:X2.png");

    public DamageCoin() {
        super();
    }

    public DamageCoin(int x, int y, int w, int h) {
        super(x, y, w, h, SHARED_IMAGE);
    }
}
