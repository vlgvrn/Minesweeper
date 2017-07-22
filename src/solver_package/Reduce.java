package solver_package;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import data_model.Cell;
import data_model.Cluster;
import data_model.IModelSource;

public class Reduce {

    HashSet<Cell> openCells;
    HashMap<Cell, Cluster> allClusters;
    IModelSource model;
    public HashSet<Cell> safeCells;
    public HashSet<Cell> bombedCells;

    public Reduce(HashSet<Cell> openCells, HashMap<Cell, Cluster> allClusters, IModelSource model){

        this.openCells = new HashSet<Cell>(openCells);
        this.allClusters = new HashMap<Cell, Cluster>(allClusters);
        this.model = model;
        safeCells = new HashSet<Cell>();
        bombedCells = new HashSet<Cell>();
    }

    Cluster getCluster(Cell cell){

        return allClusters.get(cell);
    }

    private HashSet<Cluster> getOverlappingClusters(Cluster cluster){

        HashSet<Cluster> result = new HashSet<Cluster>();

        for(Cell closedCell: cluster.getSetClosedCells()){

            HashSet<Cell> adjacentOpenCells = model.getNeighboursWithState(closedCell.getX(), closedCell.getY(), Cell.STATE_OPEN);

            adjacentOpenCells.remove(cluster.getOpenCell());

            for(Cell el: adjacentOpenCells){

                result.add(getCluster(el));
            }
        }

        return result;
    }

    private HashSet<Cell> getOverlappingOpen(Cell cell){

        HashSet<Cell> result = new HashSet<Cell>();

        HashSet<Cell> clCells = getCluster(cell).getSetClosedCells();

        for(Cell closedCell: clCells){

            HashSet<Cell> openNeighbourCells = model.getNeighboursWithState(closedCell.getX(), closedCell.getY(), Cell.STATE_OPEN);

            result.addAll(openNeighbourCells);

        }

        result.remove(cell);
        return result;
    }

    private HashSet<Cluster> getOverlaps(HashSet<Cell> cells, Cluster cluster){

        HashSet<Cluster> result = new HashSet<Cluster>();
        HashSet<Cluster> overlappings = getOverlappingClusters(cluster);
        for(Cluster cl: overlappings){

            if(!Collections.disjoint(cl.getSetClosedCells(), cells)){

                result.add(cl);
            }
        }

        result.remove(cluster);
        return result;
    }

    private HashSet<Cluster> getNestedClusters(Cluster cluster){

        HashSet<Cluster> result = new HashSet<Cluster>();

        for(Cluster overlappingCluster: getOverlappingClusters(cluster)){

            if(overlappingCluster.isContainedIn(cluster)){

                result.add(overlappingCluster);
            }
        }


        return result;
    }

    private Cluster getMaxNestedCluster(Cluster cluster){

        HashSet<Cluster> nested = getNestedClusters(cluster);
        Cluster result = null;
        int maxCells = 0;
        int minBombs = 8;

        for(Cluster cl: nested){

            if(cl.getSetClosedCells().size() > maxCells){

                maxCells = cl.getSetClosedCells().size();
                minBombs = cl.getOpenCell().getNumberAdjacentBombs();
                result = cl;
            }

            else{

                if(cl.getSetClosedCells().size() == maxCells){

                    if(minBombs > cl.getOpenCell().getNumberAdjacentBombs()){

                        minBombs = cl.getOpenCell().getNumberAdjacentBombs();
                        result = cl;
                    }
                }
            }
        }

        return result;
    }

    private boolean eliminateClusters(){

        boolean hasChanges = false;
        for(Cell cell : openCells){

            Cluster cluster = getCluster(cell);

            if(!getNestedClusters(cluster).isEmpty()){

                hasChanges = true;

                Cluster nestedCl = getMaxNestedCluster(cluster);
                Cell nested = nestedCl.getOpenCell();

                int m = cell.getNumberAdjacentBombs();
                int n = nested.getNumberAdjacentBombs();
                cell.setNumberAdjacentBombs(m - n);
                cluster.subtract(nestedCl);
            }

        }

        return hasChanges;
    }

    private boolean removeFullClusters(){

        boolean hasChanges = false;
        Iterator<Cell> cellsIterator = openCells.iterator();

        while(cellsIterator.hasNext()){

            Cell cell = cellsIterator.next();
            Cluster cluster = getCluster(cell);
            if(cluster.isFull()){

                hasChanges = true;
                HashSet<Cell> clCells = cluster.getSetClosedCells();

                HashSet<Cell> openNeigh = getOverlappingOpen(cell);
                for(Cell openCell: openNeigh){

                    Cluster clust = getCluster(openCell);
                    int m = cluster.getClustersIntersection(clust).size();
                    int n = openCell.getNumberAdjacentBombs();
                    openCell.setNumberAdjacentBombs(n - m);
                    HashSet<Cell> set = clust.getClustersIntersection(cluster);
                    clust.removeSet(set);

                }
                for(Cell el: clCells){

                    el.setState(Cell.STATE_BOMBED);
                    bombedCells.add(el);
                }
                cell.setState(Cell.STATE_ELIMINATED);
                cell.setNumberAdjacentBombs(0);
                cellsIterator.remove();
            }
        }

        return hasChanges;
    }

    private boolean removeEmptyClusters(){

        boolean hasChanges = false;
        Iterator<Cell> cellsIterator = openCells.iterator();

        while(cellsIterator.hasNext()){

            Cell cell = cellsIterator.next();
            Cluster cluster = getCluster(cell);
            if(cluster.isEmpty()){

                hasChanges = true;
                HashSet<Cell> closedCells = cluster.getSetClosedCells();
                HashSet<Cell> openNeigh = getOverlappingOpen(cell);
                for(Cell openCell: openNeigh){

                    Cluster clust = getCluster(openCell);
                    HashSet<Cell> set = clust.getClustersIntersection(cluster);
                    clust.removeSet(set);

                }
                for(Cell el: closedCells){

                    el.setState(Cell.STATE_SAFE);
                    safeCells.add(el);
                }

                cell.setState(Cell.STATE_ELIMINATED);
                cellsIterator.remove();
            }
        }

        return hasChanges;
    }

    private boolean removeSpecialOverlappingClusters(){

        boolean hasChanges = false;
        Iterator<Cell> cellsIterator = openCells.iterator();
        while(cellsIterator.hasNext()){

            Cell cell = cellsIterator.next();
            int m = cell.getNumberAdjacentBombs();
            Cluster cluster = getCluster(cell);
            HashSet<Cell> overlappingCells = getOverlappingOpen(cell);

            for(Cell el: overlappingCells){

                Cluster clusterEl = getCluster(el);
                HashSet<Cell> difference = clusterEl.getClosedSetsDifference(cluster);
                if(difference.size() == el.getNumberAdjacentBombs() - m){

                    hasChanges = true;
                    for(Cell diffCell: difference){

                        diffCell.setState(Cell.STATE_BOMBED);
                        bombedCells.add(diffCell);
                    }

                    HashSet<Cluster> overlaps = getOverlaps(difference, clusterEl);
                    for(Cluster overl: overlaps){

                        HashSet<Cell> diff = new HashSet<Cell>(overl.getSetClosedCells());
                        diff.retainAll(difference);
                        Cell overlCell = overl.getOpenCell();
                        overlCell.setNumberAdjacentBombs(overlCell.getNumberAdjacentBombs() - diff.size());
                        overl.getSetClosedCells().removeAll(diff);
                    }

                    HashSet<Cell> intersection = cluster.getClustersIntersection(clusterEl);
                    clusterEl.removeSet(difference);
                    el.setNumberAdjacentBombs(m);
                    cluster.removeSet(intersection);
                    cell.setNumberAdjacentBombs(0);

                }
            }
        }

        return hasChanges;
    }

    public void reduce(){

        boolean changedElim = false;
        boolean changedFull = false;
        boolean changedEmpty = false;
        boolean changedSpecial = false;

        do{

            changedElim = eliminateClusters();
            changedFull = removeFullClusters();
            changedEmpty = removeEmptyClusters();
            changedSpecial = removeSpecialOverlappingClusters();


        } while(changedElim || changedFull || changedEmpty || changedSpecial);
    }
}
