import java.util.Arrays;

public class Operation {
    int turn=0;//自增
    int v;
    String []op;

    public Operation(int v){
         this.v=v;
         op=new String[v*v];
     }

    void put(String s){
        if(s.equals(op[this.turn])){
            return;
        }
        else{
            op[turn]=s;
            turn++;
        }
    }

    @Override
    public String toString() {
        return "Operation{" +
                "op=" + Arrays.toString(op) +
                '}';
    }
}
