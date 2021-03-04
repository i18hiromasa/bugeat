import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.layout.Priority;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;//printBoard()

public class Bugeat extends Application{ 
    Button button[][] = new Button[10][10];
    ImageView iv[][] = new ImageView[10][10];
    Image blackStone = new Image("img/black_1.jpg");
    Image blackCanPut = new Image("img/black_2.jpg");
    Image whiteStone = new Image("img/white_1.jpg");
    Image whiteCanPut = new Image("img/white_2.jpg");
    Image notStone = new Image("img/not.jpg");
    Image wall = new Image("img/wall.jpg");
    GridPane grid = new GridPane();
    BorderPane borderPane = new BorderPane();
    VBox right = new VBox();
    Reversi board = new Reversi();
    Reversi boardCopy = new Reversi();
    Reversi holdBoard = new Reversi();
    boolean flag = true;
    Label turn;
    Label numStone;
    TextArea score = new TextArea();
    String scoreText = "";
    String textCopy = "";
    String holdText = "";
    Stage end = new Stage();
    Stage infoPage = new Stage();
    Timeline callEnemy;
    boolean isEnemy;
    boolean first;
    ToggleGroup bow;
    RadioButton firstMove;
    RadioButton behind;
    public void start(Stage stage)throws Exception{//最初に呼ばれる関数,GUIを初期化する
        isEnemy = false;
        callEnemy = new Timeline(new KeyFrame(Duration.millis(500), e -> checkEnemyCall()));
        callEnemy.setCycleCount(Timeline.INDEFINITE);
        //右側
        numStone = new Label();
        numStone.setMinSize(30, 30);
        numStone.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        turn = new Label("turn:");
        //turn.setAlignment(Pos.CENTER);
        turn.setMinSize(40, 40);
        turn.setFont(new Font(30));
        turn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //right.setVgrow(turn, Priority.ALWAYS);
        first = true;
        bow = new ToggleGroup();
        firstMove = new RadioButton("先手");
        firstMove.setOnAction(e -> {first = true;startGame();});
        firstMove.setToggleGroup(bow);
        behind = new RadioButton("後手");
        behind.setOnAction(e -> {first = false;startGame();});
        behind.setToggleGroup(bow);
        HBox select = new HBox();
        select.getChildren().addAll(firstMove,behind);
        select.setHgrow(firstMove,Priority.ALWAYS);
        select.setHgrow(behind,Priority.ALWAYS);
        score.setEditable(false);
        score.setPrefColumnCount(5);
        score.setPrefRowCount(20);
        score.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button exitGame = new Button("exit");
        exitGame.setOnAction(event -> System.exit(0));
        exitGame.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button undo = new Button("undo");
        undo.setOnAction(event -> undoBoard());
        undo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button info = new Button("info");
        info.setOnAction(event -> infoPage.show());
        info.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(undo,info,exitGame);
        buttons.setHgrow(undo,Priority.ALWAYS);
        buttons.setHgrow(info,Priority.ALWAYS);
        buttons.setHgrow(exitGame,Priority.ALWAYS);
        right.getChildren().addAll(turn,numStone,select,score,buttons);
        right.setMinSize(200, 100);
        //right.setMaxSize(200, 100);
        right.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        right.setVgrow(score, Priority.ALWAYS);
        //左側
        Label topLabel[] = new Label[11]; 
        Label leftLabel[] = new Label[10];
        topLabel[0] = new Label();
        topLabel[0].setMinSize(20, 20);
        GridPane.setConstraints(topLabel[0], 0, 0);
        grid.getChildren().addAll(topLabel[0]);
        for(int i = 0; i < 10; i++){
            char a = (char)('a' + i);
            topLabel[i + 1] = new Label(String.valueOf(a));
            topLabel[i + 1].setAlignment(Pos.CENTER);
            topLabel[i + 1].setMinSize(52, 20);
            topLabel[i + 1].setMaxSize(52, 20);
            GridPane.setConstraints(topLabel[i + 1], i + 1, 0);
            //GridPane.setHgrow(topLabel[i + 1], Priority.ALWAYS);
            //GridPane.setVgrow(topLabel[i + 1], Priority.ALWAYS);
            grid.getChildren().addAll(topLabel[i + 1]);
            leftLabel[i] = new Label(String.valueOf(i + 1));
            leftLabel[i].setAlignment(Pos.CENTER);
            leftLabel[i].setMinSize(20, 52);
            leftLabel[i].setMaxSize(20, 52);
            GridPane.setConstraints(leftLabel[i], 0, i + 1);
            //GridPane.setHgrow(leftLabel[i], Priority.ALWAYS);
            //GridPane.setVgrow(leftLabel[i], Priority.ALWAYS);
            grid.getChildren().addAll(leftLabel[i]);
        }
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                iv[i][j] = new ImageView(notStone);
                iv[i][j].setFitWidth(50);
                iv[i][j].setFitHeight(50);

            }
        }
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                final int i0 = i;
                final int j0 = j;
                button[i][j] = new Button();
                button[i][j].setMinSize(50, 50);
                button[i][j].setMaxSize(50, 50);
                //button[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button[i][j].setOnAction(event -> playGame(i0, j0));
                button[i][j].setGraphic(iv[i][j]);
                GridPane.setConstraints(button[i][j], i + 1, j + 1);
                grid.getChildren().addAll(button[i][j]);
                button[i][j].setDisable(true);
            }
        }
        grid.setHgap(2);
        grid.setVgap(2);
        //grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        borderPane.setCenter(grid);
        borderPane.setRight(right);
        Scene scene = new Scene(borderPane);
        board.putCheck();
        boardGUIImage();
        stage.setScene(scene);
        stage.show();
        ////board.printBoard(); 
        setInfomation();
        //infoPage.show();
    }
    public void playGame(int x0, int y0){//自分の1ターンを行う関数
        if(isEnemy)return;
        int x = x0 + 1;
        int y = y0 + 1;
        if(board.getBoard(y, x) != Reversi.CAN_PUT_STONE){
            System.out.println("入力が正しくありません");
            return;
        }
        textCopy = scoreText;
        board.copy(boardCopy);
        setTextAlea(x0, y);
        board.putStone(y, x);
        board.updateBoard();
        //board.printBoard();
        board.putCheck();
        boardGUIImage();
        isEnemy = true;
        callEnemy.play();
    }
    public void checkEnemyCall(){//敵のターンかどうかを確認する関数
        if(isEnemy){
            callEnemy.stop();
            playEnemy();
            isEnemy = false;
        }
    }
    public void playEnemy(){//敵の1ターンを行う関数
        boolean pass;
        pass = board.putUCT();
        if((pass)){
            while(true){
                board.updateBoard();
                pass = board.putCheck();
                if(pass)break;
                else {
                    board.reverseIsTurn();
                    board.clearCanPut();
                    if(!(board.putCheck()))break;
                }
                board.putUCT();
            }  
        }
        else {
            board.reverseIsTurn();
            board.clearCanPut();
            pass = board.putCheck();
            if(pass){
                //printBoard();
            }
            else {
                int black = board.getNumBlackStone();
                int white = board.getNumWhiteStone();
                System.out.printf("黒の石の数は%d\n",black);
                System.out.printf("白の石の数は%d\n",white);
            }
        }
        //board.printBoard();
        setEnemyText();
        if(pass){
            boardGUIImage();
        }
        else {
            boardGUIImage();
            endWindow();
        }
    } 
    public void putButton(int x0, int y0){//盤面がボタンだった時に使用
        boolean play;
        int x = x0 + 1;
        int y = y0 + 1;
        //System.out.printf("x:%d y:%d\n",y,x);
        if(board.getBoard(y, x) != Reversi.CAN_PUT_STONE){
            System.out.println("入力が正しくありません");
            return;
        }
        textCopy = scoreText;
        board.copy(boardCopy);
        setTextAlea(x0, y);
        play = board.ReversiPlayUCT(y, x);
        //board.printBoard();
        //play = board.ReversiPlay(y, x);
        setEnemyText();
        if(play){
            boardGUI();
        }
        else {
            boardGUI();
            endWindow();
        }
    }
    private void boardGUI(){//盤面がボタンだった時に使用
        numStone.setText("●:" + board.getNumBlackStone() + "\n○:" + board.getNumWhiteStone());
        if(board.getIsTurn()){
            turn.setText("turn:●");
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    int color = board.getBoard(j + 1,i + 1);
                    button[i][j].setStyle(null);
                    if(color == Reversi.BLACK_STONE){
                        button[i][j].setStyle("-fx-base: #222222");
                    }
                    else if(color == Reversi.WHITE_STONE){
                        button[i][j].setStyle("-fx-base: #eeeeee");
                    }
                    else if(color == Reversi.NOT_STONE){
                        button[i][j].setStyle("-fx-base: #009100");
                    }
                    else if(color == Reversi.CAN_PUT_STONE){
                        button[i][j].setStyle("-fx-base: #66ff66");
                    }
                }
            }
        }
        else{
            turn.setText("turn:○");
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    int color = board.getBoard(j + 1,i + 1);
                    button[i][j].setStyle(null);
                    if(color == Reversi.BLACK_STONE){
                        button[i][j].setStyle("-fx-base: #222222");
                    }
                    else if(color == Reversi.WHITE_STONE){
                        button[i][j].setStyle("-fx-base: #eeeeee");
                    }
                    else if(color == Reversi.NOT_STONE){
                        button[i][j].setStyle("-fx-base: #009100");
                    }
                    else if(color == Reversi.CAN_PUT_STONE){
                        button[i][j].setStyle("-fx-base:#66ff66");
                    }
                }
            }
        }
        int x = board.getPutX();
        int y = board.getPutY();
        if(x != -1){
            button[y-1][x-1].setStyle(null);
            button[y-1][x-1].setStyle("-fx-base:#ffffff");
            board.setPutX(-1);
            board.setPutY(-1);
        }
    }
    private void boardGUIImage(){//盤面をGUIに出力する関数
        numStone.setText("●:" + board.getNumBlackStone() + "\n○:" + board.getNumWhiteStone());
        if(board.getIsTurn()){
            turn.setText("turn:●");
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    int color = board.getBoard(j + 1,i + 1);
                    if(color == Reversi.BLACK_STONE){
                        iv[i][j].setImage(blackStone);
                    }
                    else if(color == Reversi.WHITE_STONE){
                        iv[i][j].setImage(whiteStone);
                    }
                    else if(color == Reversi.NOT_STONE){
                        iv[i][j].setImage(notStone);
                    }
                    else if(color == Reversi.CAN_PUT_STONE){
                        iv[i][j].setImage(blackCanPut);
                    }
                    else if(color == Reversi.WALL){
                        iv[i][j].setImage(wall);
                    }
                }
            }
        }
        else{
            turn.setText("turn:○");
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 10; j++){
                    int color = board.getBoard(j + 1,i + 1);
                    if(color == Reversi.BLACK_STONE){
                        iv[i][j].setImage(blackStone);
                    }
                    else if(color == Reversi.WHITE_STONE){
                        iv[i][j].setImage(whiteStone);
                    }
                    else if(color == Reversi.NOT_STONE){
                        iv[i][j].setImage(notStone);
                    }
                    else if(color == Reversi.CAN_PUT_STONE){
                        iv[i][j].setImage(whiteCanPut);
                    }
                }
            }
        }
        int x = board.getPutX();
        int y = board.getPutY();
        if(x != -1){
            button[y-1][x-1].setStyle(null);
            button[y-1][x-1].setStyle("-fx-base:#ffffff");
            board.setPutX(-1);
            board.setPutY(-1);
        }
    }
    
    private void setTextAlea(int x, int y){//現在までの手をGUIに出力
        scoreText = score.getText();
        scoreText += String.valueOf(board.getTurn());
        if(board.getIsTurn())scoreText += ". ●:";
        else scoreText += ". ○:";
        scoreText += String.valueOf((char)('a' + x)) + String.valueOf(y) + "\n";
        score.setText(scoreText);
    }
    private void setEnemyText(){//敵が置いたところをGUIに出力する関数
        scoreText += board.getEnemyPut();
        board.setEnemyPut("");
        score.setText(scoreText);
    }
    private void setReGame(){//ゲームをリセットする関数
        end.close();
        board.reGame();
        boardCopy.reGame();
        holdBoard.reGame();
        scoreText = "";
        textCopy = "";
        holdText = "";
        score.setText(scoreText);
        board.putCheck();
        boardGUIImage();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                button[i][j].setDisable(true);
            }
        }
        firstMove.setDisable(false);
        firstMove.setSelected(false);
        behind.setDisable(false);
        behind.setSelected(false);
        //board.printBoard(); 
    }
    private void endWindow(){//ゲームが終わった時に出力される画面を作って表示する関数
        end.setWidth(250);
        end.setHeight(250);
        HBox buttons = new HBox();
        VBox root = new VBox();
        Label text =  new Label();
        text.setFont(new Font(40));
        text.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        int black = board.getNumBlackStone();
        int white = board.getNumWhiteStone();
        String t = "";
        if(black > white){
            t +="Black WIN!\n";
        }
        else if(white > black) {
            t +="White WIN!\n";
        }
        else {
            t += "Draw!\n";
        }
        t += "●:" + String.valueOf(black);
        t += "\n○:" + String.valueOf(white);
        text.setText(t);
        text.setAlignment(Pos.CENTER);
        Button reGame = new Button("reGame");
        reGame.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button exitButton = new Button("exit");
        exitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        reGame.setOnAction(event -> setReGame());
        exitButton.setOnAction(event -> System.exit(1));
        buttons.getChildren().addAll(reGame,exitButton);
        buttons.setHgrow(reGame,Priority.ALWAYS);
        buttons.setHgrow(exitButton,Priority.ALWAYS);
        root.getChildren().addAll(text,buttons);
        root.setVgrow(text,Priority.ALWAYS);
        end.setScene(new Scene(root));
        end.show();
    }
    private void undoBoard(){//一手戻る関数
        if(board.getTurn() <= 2)return;
        board.copy(holdBoard);
        boardCopy.copy(board);
        holdBoard.copy(boardCopy);
        holdText = scoreText;
        scoreText = textCopy;
        textCopy = holdText;
        score.setText(scoreText);
        board.putCheck();
        boardGUIImage();
    }
    private void setInfomation(){//Infomationページを作る関数
        infoPage.setMinWidth(420);
        infoPage.setMinHeight(270);
        infoPage.setMaxWidth(420);
        infoPage.setMaxHeight(270);
        Label topTitle = new Label("Reversi画面説明");
        topTitle.setFont(new Font(40));
        Label leftInfo = new Label("　先手は黒で、後手は白です。\n　薄く丸が表示されているところに石を置くことができます");
        Label rightInfo = new Label("　turn: 現在石を置ける人です。\n　●:_ ○:_ 現在石が置かれている数です。\n　空欄には今まで打ってきた手が表示されます。\n");
        Label buttonInfo = new Label("　undo:一手戻れます。もう一度押すと一手戻る前の状態になります。\n　info:この画面が表示されます。\n　exit:ゲームを終了します。\n");
        //Label copyright = new Label("Copyright ©️ 2021 Hiromasa");
        Button close = new Button("close");
        close.setOnAction(event -> infoPage.close());
        VBox mainText = new VBox();
        mainText.getChildren().addAll(leftInfo,rightInfo,buttonInfo);
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(topTitle,mainText,/*copyright,*/close);
        infoPage.setScene(new Scene(root));
    }
    private void startGame(){//ゲームを開始する関数
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                button[i][j].setDisable(false);
            }
        }
        firstMove.setDisable(true);
        behind.setDisable(true);
        if(!first){
            isEnemy = true;
            callEnemy.play();
        }
    }
}
