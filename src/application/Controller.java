package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import data_model.BoardTuple;
import data_model.ControllerModel;
import data_model.Tuple;
import solver_package.Solver;

public class Controller implements IController, IUpdatedController {

    private ControllerModel boardModel = null;
    private int height;
    private int width;
    private int numberOfBombs;
    private boolean isFirstMove = true;
    private IView view;
    private Solver solver;
    private HashMap<Tuple, Integer> cellsForSolver;
    private HashMap<Tuple, Double> currentSolution;

    public Controller(IView view){

        this.view = view;
        cellsForSolver = new HashMap<Tuple, Integer>();
    }

    public Controller(IView view, int height, int width, int numberOfBombs) throws IOException{

        this.view = view;
        setParameters(height, width, numberOfBombs);
    }

    public void setParameters(int height, int width, int numberOfBombs) throws IOException{

        this.width = width;
        this.height = height;
        this.numberOfBombs = numberOfBombs;
        boardModel = new ControllerModel(height, width, numberOfBombs);
        isFirstMove = true;
        solver = new Solver(height, width, numberOfBombs);
        cellsForSolver = new HashMap<Tuple, Integer>();
    }

    public void processMove(int i, int j){  //making move to cell (i, j)

        view.resetButtonColors();
        if(isFirstMove){

            isFirstMove = false;
            boardModel.createBoard(i, j);
            view.markBombs(boardModel.setOfBombs);
            HashSet<BoardTuple> cellsToOpen = processEmptyCells(i, j);
            for(BoardTuple cell:cellsToOpen){

                cellsForSolver.put(new Tuple(cell.getX(), cell.getY()), cell.getBombsInNeighbourhood());
                view.openCell(cell.getX(), cell.getY(), cell.getBombsInNeighbourhood());
            }
            currentSolution = solver.processOpenCells(cellsForSolver);
            Iterator iterator = currentSolution.entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry<Tuple, Double> entry = (Map.Entry<Tuple, Double>) iterator.next();

                Tuple coord = entry.getKey();

                Double probability = entry.getValue();

                //view.setToolTip(coord.getX(), coord.getY(), probability);
                view.setButtonColor(coord.getX(), coord.getY(), probability);
            }
        }
        else{

            if(boardModel.board[i][j].isBombed())
            {
                boardModel.setOfBombs.remove(boardModel.board[i][j]);
                view.failGame(i,  j, boardModel.wronglyMarked, boardModel.setOfBombs);
                boardModel = null;
                isFirstMove = true;
            }

            else{

                HashSet<BoardTuple> cellsToOpen = processEmptyCells(i, j);
                for(BoardTuple cell:cellsToOpen){

                    cellsForSolver.put(new Tuple(cell.getX(), cell.getY()), cell.getBombsInNeighbourhood());
                    view.openCell(cell.getX(), cell.getY(), cell.getBombsInNeighbourhood());
                }
                boardModel.notBombedCellsLeftToOpen.removeAll(cellsToOpen);
                if(boardModel.notBombedCellsLeftToOpen.isEmpty()){

                    view.userWin();
                    boardModel = null;
                    isFirstMove = true;
                }
                else
                {
                    currentSolution = solver.processOpenCells(cellsForSolver);
                    Iterator iterator = currentSolution.entrySet().iterator();

                    while (iterator.hasNext()) {

                        Map.Entry<Tuple, Double> entry = (Map.Entry<Tuple, Double>) iterator.next();

                        Tuple coord = entry.getKey();

                        Double probability = entry.getValue();

                        //view.setToolTip(coord.getX(), coord.getY(), probability);
                        view.setButtonColor(coord.getX(), coord.getY(), probability);
                    }
                }
            }

        }
    }

    public HashSet<BoardTuple> processEmptyCells(int i, int j){

        HashSet<BoardTuple> cellsToOpen = new HashSet<BoardTuple>();
        cellsToOpen.add(boardModel.board[i][j]);
        boardModel.board[i][j].setState(BoardTuple.STATE_OPEN);
        LinkedList<BoardTuple> queue = new LinkedList<BoardTuple>();

        if(boardModel.board[i][j].getBombsInNeighbourhood() == 0)
            queue.add(boardModel.board[i][j]);

        while(!queue.isEmpty()){

            BoardTuple t = queue.pop();
            HashSet<BoardTuple> nbhd = boardModel.getNeighboursWithState(t.getX(), t.getY(), BoardTuple.STATE_UNKNOWN);
            for(BoardTuple cell: nbhd){
                cellsToOpen.add(cell);
                if(cell.getBombsInNeighbourhood() == 0){

                    cell.setState(BoardTuple.STATE_OPEN);
                    queue.add(cell);
                }
            }
        }

        return cellsToOpen;
    }

    public void processMarker(int i, int j){

        if(boardModel.board[i][j].getState() == BoardTuple.STATE_BOMBED){

            view.unMarkCell(i, j);
            boardModel.board[i][j].setState(BoardTuple.STATE_UNKNOWN);
            if(boardModel.board[i][j].isBombed()){

                boardModel.setOfBombs.add(boardModel.board[i][j]);
            }
            else{

                boardModel.wronglyMarked.remove(boardModel.board[i][j]);
            }

        }
        else{
            if(boardModel.board[i][j].getState() == BoardTuple.STATE_UNKNOWN){

                view.markCell(i, j);
                boardModel.board[i][j].setState(BoardTuple.STATE_BOMBED);
                if(boardModel.board[i][j].isBombed()){

                    boardModel.setOfBombs.remove(boardModel.board[i][j]);
                }
                else{

                    boardModel.wronglyMarked.add(boardModel.board[i][j]);
                }
            }
        }

    }

    public void createBoard(){

        view.createBoard(height, width);
    }

    @Override
    public void OnUpdateItemLeftClick(int i, int j) {

        processMove(i, j);
    }

    public void OnUpdateItemRightClick(int i, int j) {

        processMarker(i, j);
    }


}

