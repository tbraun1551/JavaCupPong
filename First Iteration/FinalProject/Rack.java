public class Rack{
    Cup[] rac;
    int r;
    int cupCount;
    boolean racked;
    public Rack(){
        cupCount = 10;
        rac = new Cup[10];
        int j = 0;
        for (int i = 0; i<10; i++){
            j++;
            rac[i] = new Cup(j);
        }
    }
    public void makeDead(int i){
        rac[i].makeDead();
        cupCount--;
    }
    ////////Changes the current rack arrangement of the cups
    public void changeRack(int a){
        racked = true;
    }
}