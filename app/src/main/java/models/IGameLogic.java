package models;
import android.content.Context;

import java.util.ArrayList;
//Here is a GameLogic interface
//which contain screateNewBallRandomly(), whereToMove() and isClickOnBascket() methods
public interface IGameLogic {
    void createNewBallRandomly(Context context, Ball ball);
    Bascket whereToMove();
    boolean isClickOnBascket();
    //ArrayList<AGameObject> getGameObjectsByType(String type);
}
