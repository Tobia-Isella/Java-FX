class BombSpawnTask {
    double startX, startY, vx, vy, minDist, maxDist;

    public BombSpawnTask(double startX, double startY, double vx, double vy, double minDist, double maxDist) {
        this.startX = startX;
        this.startY = startY;
        this.vx = vx;
        this.vy = vy;
        this.minDist = minDist;
        this.maxDist = maxDist;
    }
}

