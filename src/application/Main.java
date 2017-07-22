package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    RadioButton[] rbGameModes = new RadioButton[4];
    Label[] boardHeader = new Label[3];
    Label[][] gameModeParams = new Label[3][3];
    Button btnNewGame;
    Button[][] gridButtons;
    GridPane gameModesPane, grid;
    TextField[] userInputs = new TextField[3];
    ToggleGroup gameModes;
    VBox border;
    HBox top;
    IController controller;
    IView view;

    private void initControlComponents(){

        view = new View(grid);
        controller = new Controller(view);
        view.registerIControllerInstance((IUpdatedController) controller);
    }

    private void initLayout()
    {
        border = new VBox();
        top = new HBox();
        gameModesPane = new GridPane();
        gameModesPane.setVgap(5);
        gameModesPane.setHgap(20);
        gameModesPane.setPadding(new Insets(10,10,10,10));
        top.getChildren().add(gameModesPane);
        border.getChildren().add(top);
        border.setPadding(new Insets(10, 10, 10, 10));
        grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPadding(new Insets(0, 1, 0, 1));
        grid.setAlignment(Pos.TOP_LEFT);
        border.getChildren().add(grid);
    }

    private void initBoardHeader()
    {
        for(int i=0;i<3;i++){
            boardHeader[i] = new Label();
            boardHeader[i].setText(StrConst.boardHeaderParameters[i]);
            gameModesPane.add(boardHeader[i], i+1, 0);
        }
    }

    private void initRadioButtons()
    {
        gameModes = new ToggleGroup();
        for(int i=0; i<4; i++){
            rbGameModes[i] = new RadioButton();
            rbGameModes[i].setToggleGroup(gameModes);
            rbGameModes[i].setText(StrConst.gameModeNames[i]);
            gameModesPane.add(rbGameModes[i], 0, i+1);
        }
        rbGameModes[0].setSelected(true);
        gameModes.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (gameModes.getSelectedToggle().equals(rbGameModes[3])) {
                    for (int i=0; i<3; i++){
                        userInputs[i].setText(Integer.toString(StrConst.gameModeParameters[0][i]));
                        userInputs[i].setEditable(true);
                    }
                }
                else{
                    for (int i=0; i<3; i++){
                        userInputs[i].setText("");
                        userInputs[i].setEditable(false);
                    }
                }
            }
        });
    }

    private void initGameModeParams()
    {
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++){
                gameModeParams[i][j] = new Label();
                gameModeParams[i][j].setText(Integer.toString(StrConst.gameModeParameters[i][j]));
                gameModesPane.add(gameModeParams[i][j], j+1, i+1);
            }
    }

    private void initTextFields()
    {
        for (int i=0; i<3; i++){
            userInputs[i] = new TextField();
            userInputs[i].setEditable(false);
            userInputs[i].setPrefWidth(40);
            gameModesPane.add(userInputs[i], i+1, 4);
        }

    }

    private void initStartButton()
    {
        btnNewGame = new Button();
        btnNewGame.setStyle("-fx-focus-color: transparent;");
        btnNewGame.setText(StrConst.gameStart);
        btnNewGame.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e){
                if (MouseButton.PRIMARY.equals(e.getButton())){

                    for(int i = 0; i < 3; i++){
                        if(rbGameModes[i].isSelected()){

                            try {
                                controller.setParameters(StrConst.gameModeParameters[i][0],
                                        StrConst.gameModeParameters[i][1], StrConst.gameModeParameters[i][2]);
                            } catch (IOException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        }
                    }

                    if (rbGameModes[3].isSelected()){

                        try {
                            controller.setParameters(Integer.parseInt(userInputs[0].getCharacters().toString()),
                                    Integer.parseInt(userInputs[1].getCharacters().toString()),
                                    Integer.parseInt(userInputs[2].getCharacters().toString()));
                        } catch (NumberFormatException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }

                    controller.createBoard();
                }
            }
        });
        gameModesPane.add(btnNewGame, 0, 5);
    }


    @Override
    public void start(Stage primaryStage) {

        initLayout();
        initControlComponents();
        initBoardHeader();
        initRadioButtons();
        initGameModeParams();
        initTextFields();
        initStartButton();

        Scene scene = new Scene(border, 300, 250);

        primaryStage.setTitle("Minesweeper with Solver");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }



}
