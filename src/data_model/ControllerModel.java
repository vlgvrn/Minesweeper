package data_model;

import java.util.HashSet;
import java.util.Random;

public class ControllerModel extends Model<BoardTuple>{


    public HashSet<BoardTuple> setOfBombs = new HashSet<BoardTuple>();
    public HashSet<BoardTuple> wronglyMarked = new HashSet<BoardTuple>();
    public HashSet<BoardTuple> notBombedCellsLeftToOpen = new HashSet<BoardTuple>();

    public ControllerModel(int height, int width, int numberOfBombs){

        this.height = height;
        this.width = width;
        this.numberOfBombs = numberOfBombs;
        board = new BoardTuple[height][width];
        for(int i=0; i < height; i++)
            for(int j=0; j < width; j++){

                board[i][j] = new BoardTuple(i, j);
            }

    }

    public void createBoard(int i, int j){   //creates board with random bombs location,
        //with the safe first move on the cell (i,j)
        int c = 0;
        Random generator = new Random();
        while(c < numberOfBombs){

            int x = generator.nextInt(height);
            int y = generator.nextInt(width);
            if(((x != i) || (y != j)) && (!board[x][y].isBombed())){

                board[x][y].setBombed(true);
                setOfBombs.add(board[x][y]);
                c++;
            }
        }
        for(int u = 0; u < height; u++){
            for(int v = 0; v < width; v++){

                if(!board[u][v].isBombed()){
                    notBombedCellsLeftToOpen.add(board[u][v]);
                }
            }
        }

        for(int k = 0; k < height; k++)
            for(int l = 0; l < width; l++){

                board[k][l].setBombsInNeighbourhood(numberBombedNeighbours(k, l));
            }

    }

    public HashSet<BoardTuple> getBombedNeighbours(int i, int j){

        HashSet<BoardTuple> result = new HashSet<BoardTuple>();
        HashSet<BoardTuple> Neighbours = getNeighbours(i, j);
        for(BoardTuple t: Neighbours)
            if(t.isBombed())
                result.add(t);
        return result;
    }

    public int numberBombedNeighbours(int i, int j){

        return getBombedNeighbours(i, j).size();
    }
}
