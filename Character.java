import javafx.scene.image.Image;

public class Character extends Player {

    public Character() {
        super();
    }

    public Character(int x, int y, int w, int h) {
        super(x, y, 0, 0, 1, w, h, 3, 3, new Image("file:PlaceHolder.png"));
    }

    @Override
    public String toString() {
        return "Character";
    }
}
