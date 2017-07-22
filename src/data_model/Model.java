package data_model;

import java.util.HashSet;
import java.math.*;

public class Model<T extends IModelElement> {

    public int numberOfBombs;
    public int height;
    public int width;
    public T[][] board;

    public HashSet<T> getNeighbours(int i, int j){

        HashSet<T> result = new HashSet<T>();

        for(int l = Math.max(i - 1, 0); l < Math.min(i+2, height); l++)
            for(int k = Math.max(j - 1, 0); k < Math.min(j + 2, width); k++)
                result.add(board[l][k]);

        result.remove(board[i][j]);

        return result;
    }

    public HashSet<T> getNeighboursWithState(int i, int j, int state){

        HashSet<T> result = new HashSet<T>();
        HashSet<T> neighbours = getNeighbours(i, j);
        for(T t: neighbours)
            if(t.getState() == state)
                result.add(t);
        return result;
    }
}
