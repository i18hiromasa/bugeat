public class MonteCarlo{
    public static int allPlayouts = 0;//playoutを行った回数
    public static final int NODE_MAX = 15000;//Nodeを最大で作れる数
    public static Node node[] = new Node[NODE_MAX];//Nodeを保持
    public static int nodeNum = 0;//Nodeを作った数
    public static int playout(Reversi board){//playoutを行うメソッド
        allPlayouts++;
        Reversi boardCopy = new Reversi();
        board.copy(boardCopy);
        int end = 0;
        while(true){
            boardCopy.putCheck();
            //boardCopy.printBoard();
            boolean pass = boardCopy.putRandom();
            if(pass){
                boardCopy.updateBoard();
                end = 0;
            }
            else{
                boardCopy.reverseIsTurn();
                board.clearCanPut();
                end++;
                if(end  > 1)break;
            }
        }
        int black = boardCopy.getNumBlackStone();
        int white = boardCopy.getNumWhiteStone();
        if(board.getIsTurn()){
            if(black > white)return 1;
            return 0;
        }
        else {
            if(white > black)return 0;
            return -1;
        }
    }
    public static int searchUct(Reversi board, int num){//UCBに基づいて手を探すメソッド
        Node n = node[num];
        int select = -1;
        double maxUcb;// = Double.MIN_VALUE;
        maxUcb = -9999;
        int pass = 1;
        //System.out.println("childnum:" + n.getChildNum());
        if(n.getChildNum() == 0){
            pass = -1;
            board.reverseIsTurn();
            board.clearCanPut();
            board.putCheck();
            int next = createNode(board);
            n = node[next];
            if(n.getChildNum() == 0){
                int black = board.getNumBlackStone();
                int white = board.getNumWhiteStone();
                if(board.getIsTurn()){
                    if(black > white)return 1;
                    return 0;
                }
                else {
                    if(white > black)return 0;
                    return -1;
                }
            }
        }
        for(int i = 0; i < n.getChildNum(); i++){
            Child c = n.getChild(i);
            int xy = c.getXY();
            //System.out.println("xy:"+ xy);
            int x = xy / 100;
            int y = xy % 100;
            if(board.getBoard(x,y) != Reversi.CAN_PUT_STONE)continue;
            double ucb = 0;
            if(c.getGames() == 0){
                ucb = 10000 + Math.random();
            }
            else {
                final double C = 0.17;//0.31
                ucb = c.getRate() + C * Math.sqrt(Math.log(n.getGameSum()) / c.getGames());
            }
            //System.out.println("ucb:" + ucb);
            if(ucb > maxUcb){
                maxUcb = ucb;
                select = i;
            }
        }
        //System.out.println("maxucb:" + maxUcb + " select:" + select);
        //System.out.println("getChildnum():" + n.getChildNum());
        Child c = n.getChild(select);
        int win;
        int xy = c.getXY();
        int x = xy / 100;
        int y = xy % 100;
        board.putStone(x,y);
        board.updateBoard();
        board.putCheck();
        if(c.getGames() == 0){
            win = -playout(board) * pass;
        }
        else {
            if(c.getNext() == -1)c.setNext(createNode(board));
            win = -searchUct(board, c.getNext()) * pass;
        }
        c.setRate((c.getRate() * c.getGames() + win) / (c.getGames() + 1));
        c.addGames();
        n.addGameSum();
        return win;
    }
    public static int selectBestUct(Reversi board){//選ばれた手の中で一番選ばれた回数が多い手を返すメソッド
        nodeNum = 0;
        int next = createNode(board);
        final int uctLoop = 7000;
        Reversi copy = new Reversi();
        board.copy(copy);
        for(int i = 0; i < uctLoop; i++){
            searchUct(copy,next);
            board.copy(copy);
        }
        Node n = node[next];
        int bestXY = -1;
        int max = -9999;
        for(int i = 0; i < n.getChildNum(); i++){
            Child c = n.getChild(i);
            int games = c.getGames();
            if(max < games){
                bestXY = c.getXY();
                max = games;
            }
        }
        /*
        double bestRate = -9999.0;
        for(int i = 0; i < n.getChildNum(); i++){
            Child c = n.getChild(i);
            double rate = c.getRate();
            if(bestRate <= rate){
                bestXY = c.getXY();
                bestRate = rate;
            }
        } */
        return bestXY;
    }
    public static int createNode(Reversi board){//Nodeを作るメソッド
        Node n = new Node();
        for(int i = 1; i < 11; i++){
            for(int j = 1; j < 11; j++){
                if(board.getBoard(i, j) == Reversi.CAN_PUT_STONE){
                    int xy = (i * 100) + j;
                    //System.out.println("childNum:" + n.getChildNum() + " xy:" + xy);
                    n.createChild(n.getChildNum(), xy);
                }
            }
        }
        node[nodeNum] = new Node();
        node[nodeNum].copy(n);
        nodeNum++;
        return nodeNum - 1;
    }

    public static int searchUct(Reversi board, int num, double C){//UCBに基づいて手を探すメソッド,定数測定用
        Node n = node[num];
        int select = -1;
        double maxUcb;// = Double.MIN_VALUE;
        maxUcb = -9999;
        int pass = 1;
        //System.out.println("childnum:" + n.getChildNum());
        if(n.getChildNum() == 0){
            pass = -1;
            board.reverseIsTurn();
            board.clearCanPut();
            board.putCheck();
            int next = createNode(board);
            n = node[next];
            if(n.getChildNum() == 0){
                int black = board.getNumBlackStone();
                int white = board.getNumWhiteStone();
                if(board.getIsTurn()){
                    if(black > white)return 1;
                    return 0;
                }
                else {
                    if(white > black)return 0;
                    return -1;
                }
            }
        }
        for(int i = 0; i < n.getChildNum(); i++){
            Child c = n.getChild(i);
            int xy = c.getXY();
            //System.out.println("xy:"+ xy);
            int x = xy / 100;
            int y = xy % 100;
            if(board.getBoard(x,y) != Reversi.CAN_PUT_STONE)continue;
            double ucb = 0;
            if(c.getGames() == 0){
                ucb = 10000 + Math.random();
            }
            else {
                ucb = c.getRate() + C * Math.sqrt(Math.log(n.getGameSum()) / c.getGames());
            }
            //System.out.println("ucb:" + ucb);
            if(ucb > maxUcb){
                maxUcb = ucb;
                select = i;
            }
        }
        //System.out.println("maxucb:" + maxUcb + " select:" + select);
        //System.out.println("getChildnum():" + n.getChildNum());
        Child c = n.getChild(select);
        int win;
        int xy = c.getXY();
        int x = xy / 100;
        int y = xy % 100;
        board.putStone(x,y);
        board.updateBoard();
        board.putCheck();
        if(c.getGames() == 0){
            win = -playout(board) * pass;
        }
        else {
            if(c.getNext() == -1)c.setNext(createNode(board));
            win = -searchUct(board, c.getNext()) * pass;
        }
        c.setRate((c.getRate() * c.getGames() + win) / (c.getGames() + 1));
        c.addGames();
        n.addGameSum();
        return win;
    }
    public static int selectBestUct(Reversi board, double C){//選ばれた手の中で一番選ばれた回数が多い手を返すメソッド,定数測定用
        nodeNum = 0;
        int next = createNode(board);
        final int uctLoop = 10000;
        Reversi copy = new Reversi();
        board.copy(copy);
        for(int i = 0; i < uctLoop; i++){
            searchUct(copy, next, C);
            board.copy(copy);
        }
        Node n = node[next];
        int bestXY = -1;
        double bestRate = -9999.0;
        for(int i = 0; i < n.getChildNum(); i++){
            Child c = n.getChild(i);
            if(bestRate <= c.getRate()){
                bestXY = c.getXY();
                bestRate = c.getRate();
            }
        }
        return bestXY;
    }
}