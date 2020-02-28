public class Rack{
    Cup[] rac;
    int cupCount;

    public Rack(){
        cupCount = 0;
        rac = new Cup[10];
        int j = 0;
        for (int i = 0; i<10; i++){
            j++;
            rac[i] = new Cup(j);
        }
    }
    public void makeDead(int i){
        if(rac[i].alive){
            rac[i].makeDead();
            cupCount++;
        }
        else{
            System.out.println("Already dead");
        }
    }
}



