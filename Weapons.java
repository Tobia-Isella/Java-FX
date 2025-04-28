import javafx.scene.image.Image;

public class Weapons {
    private int dmg, x, y, w, h, dx, dy;
    private Image pic;

    public Weapons() {
        dmg = 0;
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        dx = 0;
        dy = 0;
        pic = null;
    }

    public Weapons(int damage, int x1, int y1, int width, int height, int dxV, int dyV, Image p) {
        dmg = damage;
        x = x1;
        y = y1;
        w = width;
        h = height;
        dx = dxV;
        dy = dyV;
        pic = p;
    }

    public int getDMG() {
        return dmg;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public Image getImage() {
        return pic;
    }

    public void setImage(Image s) {
        pic = s;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
    }

    public void setDx(int c) {
        dx = c;
    }

    public void setDy(int a) {
        dy = a;
    }

    public void setX(int xfromgame) {
        x = xfromgame;
    }

    public void setY(int yfromgame) {
        y = yfromgame;
    }

    public boolean Collision(Player b) {
        return (getX() + getW() >= b.getX() && getX() <= b.getX() + b.getWidth()
                && getY() + getH() >= b.getY() && getY() <= b.getY() + b.getHeight());
    }

    public boolean Collision(Runner b) {
		return( getX()+getW()>=b.getX() && getX()<=b.getX()+b.getWidth() && getY()+getH()>=b.getY() && getY()<=b.getY()+b.getHeight());

	}

    public boolean Collision(Shooter b) {
		return( getX()+getW()>=b.getX() && getX()<=b.getX()+b.getWidth() && getY()+getH()>=b.getY() && getY()<=b.getY()+b.getHeight());

	}

}
