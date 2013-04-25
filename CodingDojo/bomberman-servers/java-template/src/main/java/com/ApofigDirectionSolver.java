package com;

import com.utils.Board;
import com.utils.Point;

import java.util.Random;
import static com.utils.Point.*;

public class ApofigDirectionSolver {

    private static Direction direction;
    private Random dice = new Random();

    public ApofigDirectionSolver() {
    }

    public String get(Board board) {
        Point bomberman = board.getBomberman();

        Point bomb = null;
        if (board.isNear(bomberman.getX(), bomberman.getY(), Element.DESTROY_WALL) && !board.isAt(bomberman.getX(), bomberman.getY(), Element.BOMB_BOMBERMAN)) {
            bomb = new Point(bomberman);
        }

        direction = tryToMove(board, bomberman, bomb);
        return "" + ((bomb!=null)? Direction.ACT+",":"") + ((direction!=null)?direction:"");
    }

    private Direction tryToMove(Board board, Point pt, Point bomb) {
        int count = 0;
        int x = pt.getX();
        int y = pt.getY();
        Direction result = null;
        boolean again = false;
        do {
            int count1 = 0;
            do {
                result = Direction.valueOf(dice.nextInt(4));
            } while (count1++ < 10 && (result.inverted() == direction && board.countNear(pt.getX(), pt.getY(), Element.SPACE) > 1));

            x = result.changeX(pt.getX());
            y = result.changeY(pt.getY());

            boolean bombAtWay = bomb != null && bomb.equals(pt(x, y));
            boolean barrierAtWay = board.isBarrierAt(x, y);
            boolean meatChopperNearWay = board.isNear(x, y, Element.MEAT_CHOPPER);

            again = bombAtWay || barrierAtWay || meatChopperNearWay;
        } while (count++ < 20 && again);

        if (count < 20) {
            return result;
        }
        return Direction.ACT;
    }
}
