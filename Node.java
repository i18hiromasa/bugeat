public class Node{
    private int childNum;//手の数
    private Child child[] = new Child[60];//手の場所を保持
    private int gameSum;//手が選ばれた回数
    Node(){
        childNum = 0;
        gameSum = 0;
        for(int i = 0; i < 60; i++){
            child[i] = new Child();
        }
    }
    //setter,getter群
    public void setChildNum(int childNum){
        this.childNum = childNum;
    }
    public int getChildNum(){
        return childNum;
    }
    public void addChildNum(){
        childNum++;
    }
    public void createChild(int idx){
        child[idx] = new Child();
        childNum++;
    }
    public void createChild(int idx,int xy){
        Child c = new Child(xy);
        child[idx].setXY(c.getXY());
        child[idx].setGames(c.getGames());
        child[idx].setRate(c.getRate());
        child[idx].setNext(c.getNext());
        childNum++;
        //System.out.println("XY:" + child[idx].getXY());
    }
    public void setChild(Child c,int idx){
        child[idx].setXY(c.getXY());
        child[idx].setGames(c.getGames());
        child[idx].setRate(c.getRate());
        child[idx].setNext(c.getNext());
    }
    public Child getChild(int idx){
        return child[idx];
    }
    public void setGameSum(int gameSum){
        this.gameSum = gameSum;
    }
    public int getGameSum(){
        return gameSum;
    }
    public void addGameSum(){
        gameSum++;
    }
    public void copy(Node n){
        this.childNum = n.getChildNum();
        this.gameSum = n.getGameSum();
        for(int i = 0; i < this.childNum; i++){
            //System.out.println("n.getChild(i).getXY():" + n.getChild(i).getXY());
            this.child[i].copy(n.getChild(i));
        }
    }
}