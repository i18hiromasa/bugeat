public class Child{
    private int xy;//手の場所
    private int games;////手を選んだ回数
    private double rate;//勝率
    private int next;//次のNodeの場所
    Child(){
        xy = -1;
        games = 0;
        rate = 0.0;
        next = -1;
    }
    Child(int a){
        xy = a;
        games = 0;
        rate = 0.0;
        next = -1;
    }
    //setter,getter群
    public void setXY(int xy){
        this.xy = xy;
    }
    public int getXY(){
        return xy;
    }
    public void setGames(int games){
        this.games = games;
    }
    public int getGames(){
        return this.games;
    }
    public void addGames(){
        games++;
    }
    public void setRate(double rate){
        this.rate = rate;
    }
    public double getRate(){
        return rate;
    }
    public void setNext(int next){
        this.next = next;
    }
    public int getNext(){
        return next;
    }
    public void copy(Child c){
        this.xy = c.getXY();
        this.games = c.getGames();
        this.rate = c.getRate();
        this.next = c.getNext();
    }
}