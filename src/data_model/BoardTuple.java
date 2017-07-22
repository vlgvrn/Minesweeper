package data_model;

public class BoardTuple extends Tuple implements IModelElement{
    public final static int STATE_OPEN = 1;
    public final static int STATE_UNKNOWN = 0; //closed cell
    public final static int STATE_BOMBED = -1;

    private int bombsInNeighbourhood = 0;
    private boolean isBombed = false;
    private int state = STATE_UNKNOWN;

    public BoardTuple(int a, int b) {
        super(a, b);

    }

    public void setBombsInNeighbourhood(int a){

        bombsInNeighbourhood = a;
    }

    public int getBombsInNeighbourhood(){

        return bombsInNeighbourhood;
    }

    public void setBombed(boolean a){

        isBombed = a;
    }

    public boolean isBombed(){

        return isBombed;
    }

    @Override
    public void setState(int a){

        state = a;
    }

    @Override
    public int getState(){

        return state;
    }
}
