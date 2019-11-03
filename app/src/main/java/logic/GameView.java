package logic;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.game.R;
import java.util.ArrayList;
import java.util.Random;
import data.PlayerInfo;
import models.AGameObject;
import models.Ball;
import models.Bascket;
import models.IGameLogic;
import models.Label;
import settings.ApplicationSettings;
import settings.UserSettings;

//Class GameView extends SurfaceView methods and implements Runnable, IGameLogic interface
public class GameView extends SurfaceView implements Runnable, IGameLogic {

    //volatile means that "playing" vulnerable threads becomes visible to all other threads
    private static volatile boolean playing;
    //initializing gameThread
    private Thread gameThread = null;

    //adding all objects to this list
    private ArrayList<AGameObject> gameObjects;

    //These objects will be used for drawing
    //Paint define exactly how graphic primitives should be displayed on a bitmap (color, stroke, style etc.)
    private Paint paint;
    //Canvas works with pixels graphics
    private Canvas canvas;
    //SurfaceHolder uses to updating background image
    private SurfaceHolder surfaceHolder;
    //start time for game =0
    private long startTime = 0;

    //creating object which allows us to access to application-specific functions
    public GameView(Context context) {
        //reference to parent class
        super(context);

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();
        createObject();
        //to control the number of frames for smooth play (60 fps)
        startTime = System.nanoTime();
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static void setPlaying(boolean playing) {
        GameView.playing = playing;
    }

    @Override
    //update and draw object while we are playing. Also control frames
    public void run() {
        while (isPlaying()) {
            update();
            draw();
            control();
        }
    }

    private void createObject() {

        //create new array of elements
        gameObjects = new ArrayList<>();
        //creating label for our boxes
        gameObjects.add(new Label(this.getContext()));
        //setting positions for our boxes
        // Height and Width on the screen
        gameObjects.add(new Bascket(this.getContext(), new PointF(0, ApplicationSettings.getScreenHeight() - 350), 300, 300, R.drawable.box_green));
        gameObjects.add(new Bascket(this.getContext(), new PointF(ApplicationSettings.getScreenWidth() - 350, ApplicationSettings.getScreenHeight() - 350), 300, 300, R.drawable.box_red));
        //creating new ball object
        createNewBallRandomly(this.getContext(), null);
    }

    private void update() {

        //updating objects position
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update();
        }
    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            try {
                canvas = surfaceHolder.lockCanvas();

                //drawing a background color for canvas
                canvas.drawColor(Color.rgb(188, 234, 250));
                //drawing our game objects on canvas
                for (int i = 0; i < gameObjects.size(); i++) {
                    gameObjects.get(i).draw(canvas);
                }

            } finally {
                //Unlocking the canvas
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void control() {
        long now = System.nanoTime();
        // Interval to redraw game
        // (Change nanoseconds to milliseconds)
        long waitTime = (now - startTime) / 1000000;
        //17 millisec is 1 frame
        if (waitTime < 17 && waitTime >= 0) {
            waitTime = 17 - waitTime; // Millisecond.

            try {
                // Sleep.
                gameThread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        startTime = System.nanoTime();
    }
    //pause the game
    public void pause() {
        setPlaying(false);
        try {
            // join() method allows one thread to wait until another thread completes its execution
            gameThread.join();
            //interrupt thread (so pause)
        } catch (InterruptedException e) {
        }
    }
    //resume game
    public void resume() {
        setPlaying(true);
        gameThread = new Thread(this);
        //game thread starts (so game continues)
        gameThread.start();
    }

    @Override
    //dispatchTouchEvent is working with screen touches
    public boolean dispatchTouchEvent(MotionEvent event) {
        //getting X coordinates
        PlayerInfo.x = event.getX();
        //getting Y coordinates
        PlayerInfo.y = event.getY();
        //get actual user action on screen
        PlayerInfo.motionEvent = event.getAction();
        //Increase clicks counter by 1
        PlayerInfo.clickCount++;
        //was really clicked? - true
        PlayerInfo.isClicked = true;
        //if screen was touched
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //print "TOUCH DOWN!" in console
            System.out.println("TOUCH DOWN!");
        }
        //return current instance of the parent class dispatchTouchEvent
        return super.dispatchTouchEvent(event);
    }

    @Override
    //this class is creating new random falling ball
    public void createNewBallRandomly(Context context, Ball ball) {
        //create an object of a class
        Random rnd = new Random();
        //if variable ball not null
        if (ball != null) {
            //to prevent new ball
            gameObjects.remove(gameObjects.indexOf(ball));
        }

        int r = rnd.nextInt(20);
        //generating new random color ball
        //dependent of color
        if (r >= 8) {
            gameObjects.add(0, new Ball(this, UserSettings.getSpeed(), 75, Color.WHITE, R.drawable.ball_green));
        } else {
            gameObjects.add(0, new Ball(this, UserSettings.getSpeed(), 75, Color.WHITE, R.drawable.ball_red));
        }
    }

    @Override
    //finding boxes and move balls to them
    public Bascket whereToMove() {
        //looking through all game objects on screen
        //and cheeking their size
        for (int i = 0; i < gameObjects.size(); i++) {
            AGameObject item = gameObjects.get(i);
            //if size equals to "BASCKET"
            if (item.getType().equals("BASCKET"))
            {   //if founded object type is  "BASKET" then cast to Basket class
                Bascket bascket = (Bascket) item;
                //get coordinates of basket
                Rect bounds = item.getBounds();

                if (PlayerInfo.y >= bounds.top && PlayerInfo.x >= bounds.left && PlayerInfo.y <= bounds.bottom && PlayerInfo.x <= bounds.right) {
                    //
                    return bascket;
                }
            }
        }

        return null;
    }
    //cheeking if we really click on basket
    @Override
    public boolean isClickOnBascket() {
        //if method returns value other than null
        //that was really basked
        if (whereToMove() != null) {
            return true;
        }
        return false;
    }

  /*  @Override
    public ArrayList<AGameObject> getGameObjectsByType(String type) {
        ArrayList<AGameObject> basckets = new ArrayList<>();

        for (int i = 0; i < gameObjects.size(); i++) {
            AGameObject item = gameObjects.get(i);
            if (item.getType().equals(type)) {
                basckets.add(item);
            }
        }

        return basckets;
    }*/
}