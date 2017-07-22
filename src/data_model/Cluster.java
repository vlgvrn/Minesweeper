package data_model;

import java.util.HashSet;

public class Cluster {

    private Cell openCell;
    private HashSet<Cell> setOfClosedCells;

    public Cluster(Cell openCell, HashSet<Cell> setOfClosedCells){

        this.openCell = openCell;
        this.setOfClosedCells = setOfClosedCells;
    }

    public Cell getOpenCell(){

        return openCell;
    }

    public HashSet<Cell> getSetClosedCells(){

        return setOfClosedCells;
    }

    public void setCell(Cell openCell){

        this.openCell = openCell;
    }

    public void setSetOfClosedCells(HashSet<Cell> setOfClosedCells){

        this.setOfClosedCells = setOfClosedCells;
    }

    @Override

    public int hashCode(){

        return openCell.hashCode();
    }

    public boolean equals(Object t){

        if( t == null )
            return false;
        if (t.getClass() != this.getClass())
            return false;
        Cluster cl = (Cluster)t;

        return((cl.getOpenCell().equals(openCell)) && (cl.getSetClosedCells().equals(setOfClosedCells)));

    }

    public boolean isContainedIn(Cluster cluster){

        return(cluster.getSetClosedCells().containsAll(setOfClosedCells));
    }

    public boolean isFull(){

        return(openCell.getNumberAdjacentBombs() == setOfClosedCells.size());
    }

    public boolean isEmpty(){

        return(openCell.getNumberAdjacentBombs() == 0);
    }

    public void setEmpty(){

        openCell.setNumberAdjacentBombs(0);
    }

    public boolean isTrivial(){

        return(setOfClosedCells.isEmpty());
    }

    public void subtract(Cluster cluster){

        setOfClosedCells.removeAll(cluster.getSetClosedCells());

    }

    public HashSet<Cell> getClosedSetsDifference(Cluster cluster){

        HashSet<Cell> difference = new HashSet<Cell>(setOfClosedCells);
        difference.removeAll(cluster.getSetClosedCells());
        return difference;
    }

    public HashSet<Cell> getClustersIntersection(Cluster cluster){

        HashSet<Cell> intersection = new HashSet<Cell>(setOfClosedCells);
        intersection.retainAll(cluster.getSetClosedCells());
        return intersection;
    }

    public void removeSet(HashSet<Cell> set){

        setOfClosedCells.removeAll(set);
    }
}
