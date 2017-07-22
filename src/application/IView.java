package application;

import java.util.HashSet;

import data_model.BoardTuple;

public interface IView {

    void openCell(int i, int j, int quantity);  //opens a cell at position (i, j) and assigns text - the number of bombed neighbours
    void failGame(int i, int j, HashSet<BoardTuple> wronglyMarked, HashSet<BoardTuple> SetOfBombs);
    void unMarkCell(int i, int j);
    void markCell(int i, int j);
    void createBoard(int height, int width);
    void userWin();
    void registerIControllerInstance(IUpdatedController controller);
    void setToolTip(int i, int j, double toolTip);
    public void resetButtonColors();
    public void setButtonColor(int i, int j, double probability);
    public void markBombs(HashSet<BoardTuple> bombsSet);
}
