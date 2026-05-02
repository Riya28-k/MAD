package com.example.snakegame;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    // 🔹 List to store snake body parts
    private final List<SnakePoints> SnakePointsList = new ArrayList<>();

    private SurfaceView surfaceView;
    private TextView scoreTV;

    // 🔹 Used to draw on screen
    private SurfaceHolder surfaceHolder;

    // 🔹 Current moving direction of snake
    private String movingPosition = "right";

    private int score = 0;

    // 🔹 Size of each snake block
    private static final int pointSize = 28;

    // 🔹 Initial snake length
    private static final int defaultTalePoints = 3;

    // 🔹 Snake color
    private static final int snakecolor = android.R.color.holo_orange_light;

    // 🔹 Speed of snake (lower value = faster)
    private static final int snakeMovingSpeed = 800;

    // 🔹 Food position
    private int positionX, PositionY;

    private Timer timer;

    private Canvas canvas = null;

    // 🔹 Paint object to draw snake
    private Paint pointcolor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Get UI components
        surfaceView = findViewById(R.id.surfaceView);
        scoreTV = findViewById(R.id.scoreTV);

        ImageButton topBtn = findViewById(R.id.btnUp);
        ImageButton leftBtn = findViewById(R.id.btnLeft);
        ImageButton rightBtn = findViewById(R.id.btnRight);
        ImageButton bottomBtn = findViewById(R.id.btnDown);

        // 🔹 Add callback for drawing surface
        surfaceView.getHolder().addCallback(this);

        // 🔹 Control buttons
        topBtn.setOnClickListener(v -> {
            if (!movingPosition.equals("bottom")) movingPosition = "top";
        });

        leftBtn.setOnClickListener(v -> {
            if (!movingPosition.equals("right")) movingPosition = "left";
        });

        rightBtn.setOnClickListener(v -> {
            if (!movingPosition.equals("left")) movingPosition = "right";
        });

        bottomBtn.setOnClickListener(v -> {
            if (!movingPosition.equals("top")) movingPosition = "bottom";
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.surfaceHolder = holder;

        // 🔹 Initialize game
        init();
    }

    @Override public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}
    @Override public void surfaceDestroyed(@NonNull SurfaceHolder holder) {}

    // 🔹 Initialize snake and game state
    private void init() {

        SnakePointsList.clear(); // clear old snake

        score = 0;
        scoreTV.setText("0");

        movingPosition = "right";

        // 🔹 Create initial snake
        int startX = pointSize * defaultTalePoints;

        for (int i = 0; i < defaultTalePoints; i++) {
            SnakePointsList.add(new SnakePoints(startX, pointSize));
            startX -= pointSize * 2;
        }

        addPoint();   // create food
        moveSnake();  // start movement
    }

    // 🔹 Generate random food position
    private void addPoint() {

        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        int randomX = new Random().nextInt(surfaceWidth / pointSize);
        int randomY = new Random().nextInt(surfaceHeight / pointSize);

        // 🔹 Ensure even positions for proper alignment
        if (randomX % 2 != 0) randomX++;
        if (randomY % 2 != 0) randomY++;

        positionX = (pointSize * randomX) + pointSize;
        PositionY = (pointSize * randomY) + pointSize;
    }

    // 🔹 Main game loop
    private void moveSnake() {

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                int headX = SnakePointsList.get(0).getPositionX();
                int headY = SnakePointsList.get(0).getPositionY();

                // 🔹 Check if food eaten
                if (headX == positionX && headY == PositionY) {
                    growSnake();
                    addPoint();
                }

                // 🔹 Move head
                switch (movingPosition) {
                    case "right": headX += pointSize * 2; break;
                    case "left": headX -= pointSize * 2; break;
                    case "top": headY -= pointSize * 2; break;
                    case "bottom": headY += pointSize * 2; break;
                }

                // 🔹 Move body (follow previous part)
                for (int i = SnakePointsList.size() - 1; i > 0; i--) {
                    SnakePointsList.get(i).setPositionX(SnakePointsList.get(i - 1).getPositionX());
                    SnakePointsList.get(i).setPositionY(SnakePointsList.get(i - 1).getPositionY());
                }

                // 🔹 Update head position
                SnakePointsList.get(0).setPositionX(headX);
                SnakePointsList.get(0).setPositionY(headY);

                // 🔹 Check game over
                if (checkGameOver(headX, headY)) {

                    timer.cancel();

                    runOnUiThread(() -> {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Game Over")
                                .setMessage("Score: " + score)
                                .setCancelable(false)
                                .setPositiveButton("Restart", (dialog, which) -> init())
                                .show();
                    });

                } else {

                    // 🔹 Draw everything
                    canvas = surfaceHolder.lockCanvas();

                    // 🔹 Clear screen
                    canvas.drawColor(Color.WHITE);

                    // 🔹 Draw food
                    canvas.drawCircle(positionX, PositionY, pointSize, createpointcolor());

                    // 🔹 Draw snake
                    for (SnakePoints point : SnakePointsList) {
                        canvas.drawCircle(
                                point.getPositionX(),
                                point.getPositionY(),
                                pointSize,
                                createpointcolor()
                        );
                    }

                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }, 1000 - snakeMovingSpeed, 1000 - snakeMovingSpeed);
    }

    // 🔹 Increase snake size
    private void growSnake() {

        SnakePoints last = SnakePointsList.get(SnakePointsList.size() - 1);

        // 🔹 Add new part at last position
        SnakePointsList.add(new SnakePoints(
                last.getPositionX(),
                last.getPositionY()
        ));

        score++;

        runOnUiThread(() -> scoreTV.setText(String.valueOf(score)));
    }

    // 🔹 Check collision (wall or self)
    private boolean checkGameOver(int headX, int headY) {

        // 🔹 Wall collision
        if (headX < 0 || headY < 0 ||
                headX >= surfaceView.getWidth() ||
                headY >= surfaceView.getHeight()) {
            return true;
        }

        // 🔹 Self collision
        for (int i = 1; i < SnakePointsList.size(); i++) {
            if (headX == SnakePointsList.get(i).getPositionX() &&
                    headY == SnakePointsList.get(i).getPositionY()) {
                return true;
            }
        }

        return false;
    }

    // 🔹 Create paint object
    private Paint createpointcolor() {

        if (pointcolor == null) {
            pointcolor = new Paint();
            pointcolor.setColor(getResources().getColor(snakecolor));
            pointcolor.setStyle(Paint.Style.FILL);
            pointcolor.setAntiAlias(true);
        }

        return pointcolor;
    }
}