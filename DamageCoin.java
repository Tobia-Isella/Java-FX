import javafx.scene.image.Image;

public class DamageCoin extends Consumables {

    public DamageCoin() {
        super();
    }

    public DamageCoin(int x, int y, int w, int h) {
        super(x, y, w, h, new Image("file:X2.png"));
    }

}
