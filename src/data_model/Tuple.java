package data_model;

//creates a simple tuple - a pair of integers

public class Tuple {
    private final int x;
    private final int y;

    public Tuple(int a, int b){
        x = a;
        y = b;
    }

    public int getX(){

        return x;
    }

    public int getY(){

        return y;
    }

    @Override
    public int hashCode(){

        StringBuilder s = new StringBuilder();
        s.append(x);
        s.append(',');
        s.append(y);
        return s.toString().hashCode();
        //return 1000*x + y;
    }

    @Override
    public boolean equals(Object t){

        if( t == null )
            return false;
        if (t.getClass() != this.getClass())
            return false;
        Tuple p = (Tuple)t;
        return((p.getX() == x) && (p.getY() == y));
    }

    public boolean larger(Tuple t){

        return((x > t.getX())||((x == t.getX()) && (y > t.getY())));

    }
}
