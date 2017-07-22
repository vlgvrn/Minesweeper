package data_model;

import java.util.HashSet;
import java.util.Iterator;
import java.math.*;

public class SubCluster {

    private HashSet<Cell> openCells;
    private HashSet<Cell> closedCells;
    //private int maxNumberOfBombs;
    private int currentBombs;

    public SubCluster(HashSet<Cell> openCells, HashSet<Cell> closedCells){

        this.openCells = openCells;
        this.closedCells = closedCells;
    }

    public HashSet<Cell> getOpenCells(){

        return openCells;
    }

    public HashSet<Cell> getClosedCells(){

        return closedCells;
    }

    public int getCurrentNumberOfBombs(){

        return currentBombs;
    }

    public void setOpenCells(HashSet<Cell> openCells){

        this.openCells = openCells;
    }

    public void setClosedCells(HashSet<Cell> closedCells){

        this.closedCells = closedCells;
    }

    public void setCurrentNumberOfBombs(int currentBombs){

        this.currentBombs = currentBombs;
    }

    public int maxNumberOfBombs(){

        int min = 8;
        for(Cell opCell: openCells){

            if(min > opCell.getNumberAdjacentBombs()){

                min = opCell.getNumberAdjacentBombs();
            }
        }

        return Math.min(min, openCells.size());
    }

    @Override

    public int hashCode(){

        Cell[] arrOpen = new Cell[openCells.size()];
        int i = 0;
        Iterator<Cell> openIter = openCells.iterator();
        while(openIter.hasNext()){

            arrOpen[i] = openIter.next();
            i++;
        }

        for(int k = 1; k < arrOpen.length; k++){

            int j = k;
            Cell e = arrOpen[k];
            while((j > 0) && (arrOpen[j - 1].larger(e))){

                arrOpen[j] = arrOpen[j - 1];
                j--;

            }

            arrOpen[j] = e;
        }

        StringBuilder str = new StringBuilder();
        for(int j = 0; j < arrOpen.length; j++){

            str.append(arrOpen[j].getX());
            str.append(",");
            str.append(arrOpen[j].getY());
            str.append(",");
        }

        return str.toString().hashCode();
    }

    public boolean equals(Object t){

        if( t == null )
            return false;
        if (t.getClass() != this.getClass())
            return false;
        SubCluster subCl = (SubCluster)t;

        return((subCl.getOpenCells().equals(openCells)) && (subCl.getClosedCells().equals(closedCells)));

    }

}
