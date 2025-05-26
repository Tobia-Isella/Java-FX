import javafx.scene.image.Image;

public class ShieldCoin extends Consumables {

    private static final Image SHARED_IMAGE = new Image("file:ShieldCoin.png");

    public ShieldCoin() {
        super();
    }

    public ShieldCoin(int x, int y, int w, int h) {
        super(x, y, w, h, SHARED_IMAGE);
    }
}
