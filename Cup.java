public class Cup{
    boolean alive;
    int location;

    public Cup(int l){
        this.location = l;
        alive = true;
    }

    public void makeDead(){
        alive = false;
    }
    
    public void changePos(int a){
        location = a;
    }
}