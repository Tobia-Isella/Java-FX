import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.animation.RotateTransition;
import javafx.util.Duration;





public class Game extends Canvas {
    private final double width, height;

    private double mouseX, mouseY;
    private GraphicsContext gc;

    private Character player;
    private ArrayList<Runner> runners = new ArrayList<>();
    private ArrayList<Shooter> shooters = new ArrayList<>();
    private ArrayList<Missile> missiles = new ArrayList<>();
    private ArrayList<EnemyMissile> enemyMissiles = new ArrayList<>();

    private Button startButton;
    private Button continueButton;
    private Button optionsButton;
    private Button mainMenuButton;
    private Button dropdownButton;

    private UpgradeButton healthButton = new UpgradeButton("HEALTH +1", 100);
    private UpgradeButton speedButton = new UpgradeButton("SPEED +1", 150);

    private Button exitButton;  
    private VBox dropdownOptions;
    private boolean dropdownVisible = false;
    private Label arrowLabel;



    private int cameraX, cameraY;

    private final int WORLD_WIDTH = 10000;
    private final int WORLD_HEIGHT = 10000;

    private int TOTAL_RUNNERS;
    private int TOTAL_SHOOTERS;


    private int fps = 0; 
    private int frames = 0;
    private long lastFpsTime = System.currentTimeMillis();

    private int currentWave = 0;
    private boolean waveInProgress = false;
    private final int MAX_WAVES = 10;

    private int score = 0;


    String playerUUID = AccountManager.getOrCreateUUID();
    int coins = AccountManager.getCoinsFromServer(playerUUID); // get existing or 0

    

    private enum GameState {
        START_SCREEN,
        GAME_SCREEN,
        END_SCREEN,
        GAME_PAUSED
    }

    private GameState currentState = GameState.START_SCREEN;

    


    public Game(double width, double height) {
        super(width, height);
        this.width = width;
        this.height = height;

        setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });

        setOnMouseClicked(e -> handleMouseClick(e));

        AccountManager.registerUUIDWithServer(coins);
       
        gc = getGraphicsContext2D();

        player = new Character(5000, 5000, 100, 100);
        
        long seed = 987654L;

        for (int i = 0; i < 1000; i++) {
            double x = Math.random() * WORLD_WIDTH;
            double y = Math.random() * WORLD_HEIGHT;
        
            double value = OpenSimplex2S.noise2(seed, x * 0.001, y * 0.001);
            if (value > 0.4) {
                runners.add(new Runner((int)x, (int)y, 50, 50, 5));
            }
        
            if (runners.size() >= 4 ) break;
        }

        System.out.println("Player UUID: " + playerUUID);

        for (int i = 0; i < 1000; i++) {
            double x = Math.random() * WORLD_WIDTH;
            double y = Math.random() * WORLD_HEIGHT;
        
            double value = OpenSimplex2S.noise2(seed, x * 0.001, y * 0.001);
            if (value > 0.4) {
                shooters.add(new Shooter((int)x, (int)y, 50, 50, 5));
            }
        
            if (shooters.size() >= 0 ) break;
        }

        TOTAL_RUNNERS = runners.size();
        TOTAL_SHOOTERS = shooters.size();
    
        setFocusTraversable(true);
    }
    public void start() {
        AnimationTimer timer = new AnimationTimer() {
            long lastTime = System.nanoTime();
    
            @Override
            public void handle(long now) {
                double delta = (now - lastTime) / 1e9;
                lastTime = now;
    
                switch (currentState){
                    case START_SCREEN:

                        drawStartScreen();
                        mainMenuButton.setVisible(false);
                        dropdownButton.setVisible(false);
                        dropdownOptions.setVisible(false);
                        exitButton.setVisible(true);
                        
                        

                        break;
                    
                    case GAME_SCREEN:
                 
                        update();
                        collisionEvent();
                        drawGameplay();
                        mainMenuButton.setVisible(false);
                        exitButton.setVisible(false);

                        dropdownButton.setVisible(true);
                        

                        break;

                    case END_SCREEN:
                        
                        break;
                    
                    case GAME_PAUSED:

                        drawGameplay();
                        
                        gc.setFill(Color.rgb(0, 0, 0, 0.5)); // translucent overlay
                        gc.fillRect(0, 0, width, height);

                        mainMenuButton.setVisible(true);
                        exitButton.setVisible(false);



                        break;

                }
       
                frames++;
                if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                    fps = frames;
                    frames = 0;
                    lastFpsTime = System.currentTimeMillis();
                }
            }
        };
        timer.start();
    }    

    public void update() {

        if (player.getHealth()<=0){
            currentState = GameState.END_SCREEN;
        }

        //Update Enemies 
        for (Runner runner : runners) {
            runner.moveTowardsPlayer(player.getX(), player.getY(), new ArrayList<>(runners));
        }
        for (Shooter shooter : shooters) {
            shooter.HoverPlayer(player.getX(), player.getY(), new ArrayList<>(shooters));
            shooter.maybeFireMissile(enemyMissiles, player.getX(), player.getY());
        }
        
        //Update Player
        player.move(WORLD_WIDTH, WORLD_HEIGHT);

        cameraX = player.getX() - (int) width / 2 + player.getWidth() / 2;
        cameraY = player.getY() - (int) height / 2 + player.getHeight() / 2;

        cameraX = Math.max(0, Math.min(cameraX, WORLD_WIDTH - (int) width));
        cameraY = Math.max(0, Math.min(cameraY, WORLD_HEIGHT - (int) height));

        //Update Missiles
        Iterator<Missile> iter = missiles.iterator();
        while (iter.hasNext()) {
            Missile m = iter.next();
            m.update();

            // You can remove the missile if it's out of bounds
            if (m.getX() < 0 || m.getX() > WORLD_WIDTH || m.getY() < 0 || m.getY() > WORLD_HEIGHT) {
                iter.remove();
            }
        }

        Iterator<EnemyMissile> enemyIter = enemyMissiles.iterator();
        while (enemyIter.hasNext()) {
            EnemyMissile m = enemyIter.next();
            m.update();
            if (m.getX() < 0 || m.getX() > WORLD_WIDTH || m.getY() < 0 || m.getY() > WORLD_HEIGHT) {
                enemyIter.remove();
        }
        }
        //update wave 
        if (!waveInProgress && runners.isEmpty() && shooters.isEmpty()) {
            if (currentWave < MAX_WAVES) {
                currentWave++;
                startWave(currentWave);
                waveInProgress = true;
            }
        }



    }

    public void drawStartScreen(){
        gc.clearRect(0, 0, width, height);

         // Background

        cameraX = player.getX() - (int) width / 2 + player.getWidth() / 2;
        cameraY = player.getY() - (int) height / 2 + player.getHeight() / 2;

        cameraX = Math.max(0, Math.min(cameraX, WORLD_WIDTH - (int) width));
        cameraY = Math.max(0, Math.min(cameraY, WORLD_HEIGHT - (int) height));

         gc.setFill(Color.LIGHTGRAY);
         gc.fillRect(-cameraX, -cameraY, WORLD_WIDTH, WORLD_HEIGHT);
 
         // Grid
         gc.setStroke(Color.DARKGRAY);
         for (int i = 0; i < WORLD_WIDTH; i += 100) {
             gc.strokeLine(i - cameraX, 0 - cameraY, i - cameraX, WORLD_HEIGHT - cameraY);
         }
         for (int i = 0; i < WORLD_HEIGHT; i += 100) {
             gc.strokeLine(0 - cameraX, i - cameraY, WORLD_WIDTH - cameraX, i - cameraY);
         }

         if (startButton != null) {
            startButton.setVisible(true);
        }

        Font originalFont = gc.getFont();
        Paint originalFill = gc.getFill();


        gc.setFill(Color.BLACK); // or any color you like
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 200)); // Use a sleek, bold font
        Text text = new Text("TOB.IO");
        text.setFont(gc.getFont());
        double textWidth = text.getLayoutBounds().getWidth();

        gc.fillText("TOB.IO", (width - textWidth) / 2, 290); // Adjust Y as needed

        gc.setFont(originalFont);
        gc.setFill(originalFill);



    }

    public void drawGameplay() {
        gc.clearRect(0, 0, width, height);

        // Background
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(-cameraX, -cameraY, WORLD_WIDTH, WORLD_HEIGHT);

        // Grid
        gc.setStroke(Color.DARKGRAY);
        for (int i = 0; i < WORLD_WIDTH; i += 100) {
            gc.strokeLine(i - cameraX, 0 - cameraY, i - cameraX, WORLD_HEIGHT - cameraY);
        }
        for (int i = 0; i < WORLD_HEIGHT; i += 100) {
            gc.strokeLine(0 - cameraX, i - cameraY, WORLD_WIDTH - cameraX, i - cameraY);
        }

        // Actors
        player.drawActor(gc, cameraX, cameraY);

        for (Runner runner : runners) {
            runner.drawActor(gc, cameraX, cameraY);
        }
        for (Shooter shooter : shooters) {
            shooter.drawActor(gc, cameraX, cameraY);
        }
        

        // Weapons
        for (Missile m : missiles) {
            if (m.getImage() != null) {
                gc.drawImage(m.getImage(), m.getX() - cameraX, m.getY() - cameraY, m.getW(), m.getH());
            }
            if (m.getImage() == null) {
                System.out.println("Missile image is not loaded!");
            }
            
        }    
        
        for (EnemyMissile m : enemyMissiles) {
            if (m.getImage() != null) {
                gc.drawImage(m.getImage(), m.getX() - cameraX, m.getY() - cameraY, m.getW(), m.getH());
            } else {
                System.out.println("Enemy missile image not loaded!");
            }
        }
        
        
        //UI
        gc.setFill(Color.BLACK);
        gc.fillText("Health " + player.getHealth(), 1860, 20);
        gc.fillText("MouseX " + mouseX, 1840, 40);
        gc.fillText("MouseY " + mouseY, 1840, 60);

        gc.setFill(Color.BLACK);
        gc.fillText("FPS: " + fps, 10, 20);
        
        gc.setFill(Color.BLACK);
        gc.fillText("SCORE " + score, 10, 40);

        gc.setFill(Color.BLACK);
        gc.fillText("COINS " + coins, 10, 60);
        

        gc.setFill(Color.BLACK);

        int enemiesLeft = runners.size() + shooters.size();
        int totalEnemies = TOTAL_RUNNERS + TOTAL_SHOOTERS;
        String enemyText = "Enemies: " + enemiesLeft + "/" + totalEnemies;

        double textWidth = getTextWidth(enemyText);
        double centerX = (width - textWidth) / 2;

        gc.fillText(enemyText, centerX, 40);
        gc.fillText("Wave: " + currentWave + "/" + MAX_WAVES, centerX, 20);


    }
    
    public void startWave(int wave) {
        int numRunners = 5 + wave * 2;   // Increase runners each wave
        int numShooters = 1 + wave / 2; // Increase shooters more slowly
    
        long seed = System.nanoTime();  // Use a different seed per wave
    
        int placedRunners = 0;
        int placedShooters = 0;
    
        for (int i = 0; i < 10000 && (placedRunners < numRunners || placedShooters < numShooters); i++) {
            double x = Math.random() * WORLD_WIDTH;
            double y = Math.random() * WORLD_HEIGHT;
    
            double value = OpenSimplex2S.noise2(seed, x * 0.001, y * 0.001);
            
            if (value > 0.4 && placedRunners < numRunners) {
                runners.add(new Runner((int) x, (int) y, 50, 50, 5));
                placedRunners++;
            } else if (value > 0.4 && placedShooters < numShooters) {
                shooters.add(new Shooter((int) x, (int) y, 50, 50, 5));
                placedShooters++;
            }
        }
    
        TOTAL_RUNNERS = runners.size();
        TOTAL_SHOOTERS = shooters.size();
    
        System.out.println("Wave " + wave + " started! Runners: " + TOTAL_RUNNERS + " | Shooters: " + TOTAL_SHOOTERS);
    }
    

    public void collisionEvent() {
        Random rand = new Random();
    
        // Handle collisions for runners
        for (Runner runner : runners) {
            if (runner.Collision(player)) {
                player.setHealth(player.getHealth() - 1);
    
                int newX = rand.nextInt(WORLD_WIDTH);
                int newY = rand.nextInt(WORLD_HEIGHT);
                runner.setX(newX);
                runner.setY(newY);
            }
        }
    
        // Handle collisions for shooters
        for (Shooter shooter : shooters) {
            if (shooter.Collision(player)) {
                player.setHealth(player.getHealth() - 1);
    
                int newX = rand.nextInt(WORLD_WIDTH);
                int newY = rand.nextInt(WORLD_HEIGHT);
                shooter.setX(newX);
                shooter.setY(newY);
            }
        }

        Iterator<Missile> missileIterator = missiles.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            boolean collided = false;
            Iterator<Runner> runnerIterator = runners.iterator();
            while (runnerIterator.hasNext()) {
                Runner runner = runnerIterator.next();
                if (missile.Collision(runner)) {
                    runnerIterator.remove();
                    collided = true;
                    score = score + runner.getValue();
                    break;
                }
            }   

            if (!collided) {
                Iterator<Shooter> shooterIterator = shooters.iterator();
                while (shooterIterator.hasNext()) {
                    Shooter shooter = shooterIterator.next();
                    if (missile.Collision(shooter)) {
                        shooterIterator.remove();
                        collided = true;
                        score = score + shooter.getValue();
                        break;
                    }
                }
            }

            if (collided) {
                missileIterator.remove();
            }
        }

        Iterator<EnemyMissile> enemyIter = enemyMissiles.iterator();
        while (enemyIter.hasNext()) {
            EnemyMissile m = enemyIter.next();
            if (m.Collision(player)) {
                player.setHealth(player.getHealth() - 5); // Or any damage value
                enemyIter.remove();
    }
}


        if (runners.isEmpty() && shooters.isEmpty()) {
            waveInProgress = false;
        }

    }

        public void setupDropdownButton(Pane root) {
        dropdownButton = new Button();
        setupSmallButton(dropdownButton, "UPGRADES");

        dropdownButton.setVisible(false);
    
        dropdownButton.setTranslateX(-820); // whatever X you want
        dropdownButton.setTranslateY(-400); // whatever Y you want
        
    
        Label textLabel = new Label("UPGRADES");
        arrowLabel = new Label("▼");
    
        textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30px;");
        arrowLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30px;");
    
        HBox buttonContent = new HBox(10, textLabel, arrowLabel);
        buttonContent.setAlignment(Pos.CENTER);
        dropdownButton.setGraphic(buttonContent);
    
        setupSmallButton(healthButton.button, "HEALTH +1");
        setupSmallButton(speedButton.button, "SPEED +1");
    
        dropdownOptions = new VBox(10, healthButton.button, speedButton.button);
        dropdownOptions.setVisible(false);
        dropdownOptions.setMouseTransparent(true);
        dropdownOptions.setPickOnBounds(false);
    
        // Bind dropdown position relative to buttonc
    
        Pane dropdownWrapper = new Pane(dropdownOptions);
        dropdownWrapper.setPickOnBounds(false);
    
        dropdownButton.setOnAction(e -> {
            dropdownVisible = !dropdownVisible;
            if (dropdownVisible) {

                Bounds bounds = dropdownButton.localToScene(dropdownButton.getBoundsInLocal());
                dropdownOptions.setLayoutX(bounds.getMinX());
                dropdownOptions.setLayoutY(bounds.getMaxY()+10);

                dropdownOptions.setVisible(true);
                dropdownOptions.setMouseTransparent(false);
    
                TranslateTransition slideIn = new TranslateTransition(Duration.millis(200), dropdownOptions);
                slideIn.setFromY(-20);
                slideIn.setToY(0);
                slideIn.play();
            } else {
                TranslateTransition slideOut = new TranslateTransition(Duration.millis(200), dropdownOptions);
                slideOut.setFromY(0);
                slideOut.setToY(-20);
                slideOut.setOnFinished(ev -> {
                    dropdownOptions.setVisible(false);
                    dropdownOptions.setMouseTransparent(true);
                });
                slideOut.play();
            }
            rotateArrow(dropdownVisible);
        });
    
        root.getChildren().addAll(dropdownButton, dropdownWrapper);
    }

    private void rotateArrow(boolean down) {
    RotateTransition rotate = new RotateTransition(Duration.millis(200), arrowLabel);
    if (down) {
        rotate.setFromAngle(0);
        rotate.setToAngle(180); // Point up
    } else {
        rotate.setFromAngle(180);
        rotate.setToAngle(0);   // Point down
    }
    rotate.play();
}


    private void setupSmallButton(Button button, String text) {
        button.setStyle("""
            -fx-font-size: 24px;
            -fx-background-color: linear-gradient(to bottom right, #2196F3, #1976D2);
            -fx-text-fill: white;
            -fx-background-radius: 30px;
            -fx-border-radius: 30px;
            -fx-padding: 10 20 10 20;
            -fx-cursor: hand;
        """);
    
        button.setOnMouseEntered(e -> button.setStyle("""
            -fx-font-size: 24px;
            -fx-background-color: linear-gradient(to bottom right, #1976D2, #0D47A1);
            -fx-text-fill: white;
            -fx-background-radius: 30px;
            -fx-border-radius: 30px;
            -fx-padding: 10 20 10 20;
            -fx-cursor: hand;
        """));
    
        button.setOnMouseExited(e -> button.setStyle("""
            -fx-font-size: 24px;
            -fx-background-color: linear-gradient(to bottom right, #2196F3, #1976D2);
            -fx-text-fill: white;
            -fx-background-radius: 30px;
            -fx-border-radius: 30px;
            -fx-padding: 10 20 10 20;
            -fx-cursor: hand;
        """));
    
        button.setOnAction(e -> {
            if (text.equals("SETTINGS")) {
                System.out.println("Settings clicked!");
                // Do something for settings
            } else if (text.equals("CREDITS")) {
                System.out.println("Credits clicked!");
                // Do something for credits
            }
        });
    }



    public void setupStartButton(Pane root) {
        
        // First Button: Start
        startButton = new Button("START");
        setupButton(startButton, "START", -100);
    
        // Second Button: Options (example)
        continueButton = new Button("CONTINUE");
        setupButton(continueButton, "CONTINUE", 0); // Adjust Y position for spacing
        
        // Third Button: Options 
        optionsButton = new Button("OPTIONS");
        setupButton(optionsButton, "OPTIONS", 100); // Adjust Y position for spacing

        // Third Button: Options 
        mainMenuButton = new Button("MAIN MENU");
        setupButton(mainMenuButton, "MAIN MENU", 0); // Adjust Y position for spacing

         // Third Button: Options 
        exitButton = new Button("EXIT TO DESKTOP");
        setupButton(exitButton, "EXIT TO DESKTOP", 200); // Adjust Y position for spacing
    
        // Add buttons to the root pane
        root.getChildren().add(startButton);
        root.getChildren().add(optionsButton);
        root.getChildren().add(continueButton);
        root.getChildren().add(mainMenuButton);
        root.getChildren().add(exitButton);
    }
    
    private void setupButton(Button button, String text, double translateYPosition) {
        button.setStyle("""
            -fx-font-size: 36px;
            -fx-background-color: linear-gradient(to bottom right, #4CAF50, #2E7D32);
            -fx-text-fill: white;
            -fx-background-radius: 40px;
            -fx-border-radius: 40px;
            -fx-padding: 20 40 20 40;
            -fx-cursor: hand;
        """);
    
        button.setOnMouseEntered(e -> button.setStyle("""
            -fx-font-size: 36px;
            -fx-background-color: linear-gradient(to bottom right, #388E3C, #1B5E20);
            -fx-text-fill: white;
            -fx-background-radius: 40px;
            -fx-border-radius: 40px;
            -fx-padding: 20 40 20 40;
            -fx-cursor: hand;
        """));
    
        button.setOnMouseExited(e -> button.setStyle("""
            -fx-font-size: 36px;
            -fx-background-color: linear-gradient(to bottom right, #4CAF50, #2E7D32);
            -fx-text-fill: white;
            -fx-background-radius: 40px;
            -fx-border-radius: 40px;
            -fx-padding: 20 40 20 40;
            -fx-cursor: hand;
        """));
    
        button.setLayoutX((width - 300) / 2); // center horizontally
        button.setTranslateY(translateYPosition); // Move it vertically (adjust this)
    
        // Set action for each button
        if (text.equals("START")) {
            button.setOnAction(e -> {
                currentState = GameState.GAME_SCREEN;
                startButton.setVisible(false);
                optionsButton.setVisible(false);
                continueButton.setVisible(false);
                mainMenuButton.setVisible(false);
            });
        } else if (text.equals("OPTIONS")) {
            button.setOnAction(e -> {
                currentState = GameState.GAME_SCREEN; // Change to another state for options
                optionsButton.setVisible(false);
                startButton.setVisible(false);
                continueButton.setVisible(false);
                mainMenuButton.setVisible(false);
            });
        } else if (text.equals("CONTINUE")) {
            button.setOnAction(e -> {
                currentState = GameState.GAME_SCREEN; // Change to another state for options
                continueButton.setVisible(false);
                startButton.setVisible(false);
                optionsButton.setVisible(false);
                mainMenuButton.setVisible(false);
            });
        } else if (text.equals("MAIN MENU")) {
            button.setOnAction(e -> {
                currentState = GameState.START_SCREEN; // Change to another state for options
                continueButton.setVisible(true);
                startButton.setVisible(true);
                optionsButton.setVisible(true);
                mainMenuButton.setVisible(false);
            });
        }  else if (text.equals("EXIT TO DESKTOP")) {
            button.setOnAction(e -> {
                javafx.application.Platform.exit(); // Cleanly shuts down the application
                System.exit(0); // Ensures JVM exit in case other threads are running
            });
        }
        
    }
    private double getTextWidth(String text) {
        Text t = new Text(text);
        t.setFont(gc.getFont());
        return t.getLayoutBounds().getWidth();
    }
    

    public void handleKeyPress(KeyEvent e) {
        switch (e.getCode()) {
            case W -> player.setDy(-10);
            case A -> player.setDx(-10);
            case S -> player.setDy(10);
            case D -> player.setDx(10);
            case ESCAPE -> {
                // Check if the current state is either GAME_SCREEN or GAME_PAUSED
                if (currentState == GameState.GAME_SCREEN) {
                    currentState = GameState.GAME_PAUSED;  // Pause the game
                } else if (currentState == GameState.GAME_PAUSED) {
                    currentState = GameState.GAME_SCREEN;  // Resume the game
                }
            }
            // handle other keys...
        }
    }

    public void handleKeyRelease(KeyEvent e) {
        switch (e.getCode()) {
            case W, S -> player.setDy(0);
            case A, D -> player.setDx(0);
        }
    }

    public void handleMouseClick(MouseEvent e) {
        // Convert screen coordinates to world coordinates
        int mouseWorldX = (int) e.getX() + cameraX;
        int mouseWorldY = (int) e.getY() + cameraY;
    
        Missile missile = new Missile(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 20, 20, 10);
        missile.fireTowards(mouseWorldX, mouseWorldY);
        missiles.add(missile);

        System.out.println("Mouse Clicked");


    }
    


}
