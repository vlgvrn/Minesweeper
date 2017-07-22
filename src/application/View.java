package application;

import java.util.HashSet;
import java.util.LinkedList;

import com.sun.prism.paint.Color;

import data_model.BoardTuple;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.geometry.Insets;

public class View implements IView{

    GridPane grid;
    Button[][] gridButtons;
    IUpdatedController controllerInstance;

    public View(GridPane grid){

        this.grid = grid;
    }

    public void registerIControllerInstance(IUpdatedController controller){

        controllerInstance = controller;
    }

    private void disposePreviousBoard(){

        if (gridButtons != null){

            int rows = gridButtons.length;
            int cols = gridButtons[0].length;

            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++){

                    grid.getChildren().remove(gridButtons[i][j]);
                }
        }
    }

    @Override
    public void userWin(){

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Victory!");
        String s ="Congratulations! You won!";
        alert.setContentText(s);
        alert.show();
        disableBoard();

    }

    @Override
    public void openCell(int i, int j, int quantity){

        gridButtons[i][j].setDisable(true);
        gridButtons[i][j].setText(Integer.toString(quantity));

    }

    @Override
    public void markBombs(HashSet<BoardTuple> bombsSet){

        for (BoardTuple cell : bombsSet){

            gridButtons[cell.getX()][cell.getY()].setTextFill(Paint.valueOf("black"));
            gridButtons[cell.getX()][cell.getY()].setText("B");
        }
    }

    @Override
    public void failGame(int i, int j, HashSet<BoardTuple> wronglyMarked, HashSet<BoardTuple> SetOfBombs){

        gridButtons[i][j].setStyle("-fx-base : #ff0000");
        gridButtons[i][j].setTextFill(Paint.valueOf("black"));
        gridButtons[i][j].setText("B");
        gridButtons[i][j].setDisable(true);

        for(BoardTuple elem : wronglyMarked){

            int k = elem.getX();
            int l = elem.getY();
            gridButtons[k][l].setTextFill(Paint.valueOf("red"));
            gridButtons[k][l].setText("X");
            gridButtons[i][j].setDisable(true);
        }

        for(BoardTuple elem : SetOfBombs){

            int k = elem.getX();
            int l = elem.getY();
            gridButtons[k][l].setTextFill(Paint.valueOf("black"));
            gridButtons[k][l].setText("B");
            gridButtons[i][j].setDisable(true);
        }

        disableBoard();
    }

    @Override
    public void markCell(int i, int j) {

        gridButtons[i][j].setText("?");
    }

    @Override
    public void unMarkCell(int i, int j) {

        gridButtons[i][j].setText("");
    }

    private void disableBoard(){

        for (int i = 0; i < gridButtons.length; i++)
            for (int j = 0; j < gridButtons[0].length; j++)
                gridButtons[i][j].setOnMouseClicked(null);
    }

    @Override
    public void createBoard(int height, int width) {

        disposePreviousBoard();
        gridButtons = new Button[height][width];
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
            {
                gridButtons[i][j] = new Button();
                final int finI = i;
                final int finJ = j;
                gridButtons[i][j].setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent e){
                        if (MouseButton.PRIMARY.equals(e.getButton()))
                            controllerInstance.OnUpdateItemLeftClick(finI, finJ);
                        if (MouseButton.SECONDARY.equals(e.getButton()))
                            controllerInstance.OnUpdateItemRightClick(finI, finJ);
                    }
                });
                gridButtons[i][j].setMinHeight(25);
                gridButtons[i][j].setMaxHeight(25);
                gridButtons[i][j].setMinWidth(25);
                gridButtons[i][j].setMaxWidth(25);
                gridButtons[i][j].setStyle("-fx-focus-color: transparent;");
                gridButtons[i][j].setFocusTraversable(false);
                Tooltip t = new Tooltip();
                t.setText(Integer.toString(i) + ", " + Integer.toString(j));
                gridButtons[i][j].setTooltip(t);
                grid.add(gridButtons[i][j], j, i);

            }

    }

    public void setToolTip(int i, int j, double toolTip){

        Tooltip t = new Tooltip();
        t.setText(Double.toString(toolTip));
        gridButtons[i][j].setTooltip(t);
    }

    public void setButtonColor(int i, int j, double probability){

        if (probability == 1.0)
            gridButtons[i][j].setStyle("-fx-background-color: #ff0000;");
        if (probability == 0.0)
            gridButtons[i][j].setStyle("-fx-background-color: #0000ff;");
        //gridButtons[i][j].setBackground(new Background(new BackgroundFill(Color.RED, null, null)));;
    }

    public void resetButtonColors(){

        for (int i = 0; i < gridButtons.length; i++)
            for (int j = 0; j < gridButtons[0].length; j++)
                gridButtons[i][j].setStyle("-fx-background-color: #C0C0C0;");
    }

}
