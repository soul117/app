package data;
import android.graphics.Canvas;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PlayerInfo {
    //initializing players info
    //Player`s nickmane - Player
    private static String nickname = "Player";
    //Player`s score - o
    private static int score = 0;
    //creating new array list of records for Player
    private static ArrayList<Record> records = new ArrayList<>();

    //Player activity system information
    //click coordinates x and y axis
    public static float x;
    public static float y;
    public static int motionEvent;
    //Check is screen clicked
    public static boolean isClicked = false;
    //Clicks count
    public static int clickCount = 0;

    //nickname getter
    public static String getNickname() {
        return nickname;
    }
    //nickname setter
    public static void setNickname(String nickname) {
        PlayerInfo.nickname = nickname;
    }
    //score getter
    public static int getScore() {
        return score;
    }
    //resetting player`s score to 0
    public static void resetScore() {
        PlayerInfo.score = 0;
    }
    //new player`s score
    public static void addScore(int score) {
        if (score >= 0) {
            PlayerInfo.score += score;
        }
    }
    //players`s subScore
    public static void subScore(int score) {
        //if score >=0 and PlayerInfo.score>=0
        if (score >= 0 && PlayerInfo.score >= 0) {
            //and their difference >=0
            if (PlayerInfo.score - score >= 0) {
                //PlayerInfo.score = PlayerInfo.score - score
                PlayerInfo.score -= score;
            } else {
                //set score 0
                PlayerInfo.score = 0;
            }
        }
    }
    //set player score
    public static void setScore(int score) {
        //if PlayerInfo.score - score >= 0
        if (PlayerInfo.score - score >= 0) {
            //assign PlayerInfo.score score value
            PlayerInfo.score = score;
        }
    }
    //player`s ArrayList<Record> getter
    public static ArrayList<Record> getRecords() {
        return records;
    }
    //player`s ArrayList<Record> setter
    public static void setRecords(ArrayList<Record> records) {
        //assign PlayerInfo.score records value
        PlayerInfo.records = records;
    }
    //add new player`s record
    public static void addRecord(Record record) {
        //if there are no record
        if (record != null) {
            //add new record to playerInfo
            PlayerInfo.records.add(record);
        }
    }
    //resetting player`s actions (clicks on screen)
    public static void resetUserActions() {
        PlayerInfo.x = -1;
        PlayerInfo.y = -1;
        PlayerInfo.isClicked = false;
        PlayerInfo.clickCount = 0;
    }
}
