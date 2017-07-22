package data_model;

public class SolverModel extends Model<Cell> implements IModelSource{

    public SolverModel(int height, int width, int numberOfBombs){

        this.height = height;
        this.width = width;
        this.numberOfBombs = numberOfBombs;
        board = new Cell[height][width];
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                board[i][j] = new Cell(i, j);
    }
}
