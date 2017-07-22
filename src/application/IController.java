package application;

import java.io.IOException;

public interface IController {

    void processMove(int i, int j);
    void processMarker(int i, int j);
    void setParameters(int height, int width, int numberOfBombs) throws IOException;
    void createBoard();
}
