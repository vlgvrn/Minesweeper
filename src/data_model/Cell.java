package data_model;

public class Cell extends Tuple implements IModelElement{

    public final static int STATE_SAFE = 0;
    public final static int STATE_BOMBED = 1;
    public final static int STATE_CLOSED = -1;
    public final static int STATE_OPEN = 2;
    public final static int STATE_ELIMINATED = -2;
    private int state = STATE_CLOSED;

    int NumberAdjacentBombs;
    int currBombs;

    public Cell(int a, int b) {
        super(a, b);

    }

    public int getNumberAdjacentBombs(){

        return NumberAdjacentBombs;
    }

    public void setNumberAdjacentBombs(int NumberAdjacentBombs){

        this.NumberAdjacentBombs = NumberAdjacentBombs;
    }

    public int getCurrBombs(){

        return currBombs;
    }

    public void setCurrBombs(int currBombs){

        this.currBombs = currBombs;
    }

    @Override
    public void setState(int x){

        state = x;
    }

    @Override
    public int getState(){

        return state;
    }

}