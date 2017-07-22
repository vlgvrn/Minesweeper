package solver_package;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import data_model.Cell;
import data_model.Cluster;
import data_model.SolverModel;
import data_model.SubCluster;
import data_model.Tuple;

public class Solver {

    private SolverModel model;


    HashMap<Tuple, Double> solution;
    HashSet<Cell> openCells;
    HashSet<Cell> closedCells;
    HashSet<Cell> safeCells;
    HashSet<Cell> bombedCells;
    HashSet<Cell> activeClosedCells;
    HashSet<Cell> currentActiveClosedCells;
    HashMap<Cell, Cluster> allClusters;
    HashSet<Cluster> clustersToRemove;
    HashSet<SubCluster> allSubClusters;
    HashSet<HashMap<Cell, Integer>> cellsPermutations;
    HashSet<HashMap<SubCluster, Integer>> allPermutations;
    FileWriter fw;
    BufferedWriter bw;
    PrintWriter out;

    public Solver(int height, int width, int numberOfBombs){

        model = new SolverModel(height, width, numberOfBombs);
        openCells = new HashSet<Cell>();
        safeCells = new HashSet<Cell>();
        bombedCells = new HashSet<Cell>();
        allClusters = new HashMap<Cell, Cluster>();
        clustersToRemove = new HashSet<Cluster>();
        activeClosedCells = new HashSet<Cell>();
        currentActiveClosedCells = new HashSet<Cell>();
    }

    private void addNewOpenCells(HashMap<Tuple, Integer> newOpenCells){

        Iterator it = newOpenCells.entrySet().iterator();
        openCells = new HashSet<Cell>();
        while(it.hasNext()){

            Map.Entry pair = (Map.Entry)it.next();
            Tuple t = (Tuple) pair.getKey();

            if((int)pair.getValue() != 0){

                if(model.getNeighboursWithState(t.getX(), t.getY(), Cell.STATE_CLOSED).size() != 0){

                    model.board[t.getX()][t.getY()].setState(Cell.STATE_OPEN);

                    this.openCells.add(model.board[t.getX()][t.getY()]);
                }

                else{

                    model.board[t.getX()][t.getY()].setState(Cell.STATE_ELIMINATED);
                }

            }
            else{

                model.board[t.getX()][t.getY()].setState(Cell.STATE_ELIMINATED);
            }

            model.board[t.getX()][t.getY()].setNumberAdjacentBombs((int) pair.getValue() -
                    model.getNeighboursWithState(t.getX(), t.getY(), Cell.STATE_BOMBED).size());


        }

    }

    private void createAllClusters(){         //returns the set of all clusters on the board

        for(Cell cell: openCells){

            allClusters.put(cell, new Cluster(cell, model.getNeighboursWithState(cell.getX(), cell.getY(), Cell.STATE_CLOSED)));
        }

    }

	/*private void createPermutations(HashSet<Cell> activeClosedCells){

		if(!activeClosedCells.isEmpty()){

		Iterator<Cell> cellsIterator = activeClosedCells.iterator();
		while(cellsIterator.hasNext()){

			Cell clCell = cellsIterator.next();
			if(clCell.getState() == )


		}
	  }
	}
*/

	/*private void createSubClusters(){

		HashMap<HashSet<Cell>, HashSet<Cell>> subCl = new HashMap<HashSet<Cell>, HashSet<Cell>>();
		HashSet<Cell> currentClosed = new HashSet<Cell>();

		for(Cell cell: openCells){

			currentClosed.addAll(getCluster(cell).getSetClosedCells());
		}

		for(Cell clCell: currentClosed){

			HashSet<Cell> opNeighbours = model.getNeighboursWithState(clCell.getX(), clCell.getY(), Cell.STATE_OPEN);

			if(subCl.containsKey(opNeighbours)){

				subCl.get(opNeighbours).add(clCell);
			}

			else{

				HashSet<Cell> value = new HashSet<Cell>();
				value.add(clCell);
				subCl.put(opNeighbours, value);
			}
		}

		for(HashSet<Cell> key: subCl.keySet()){

			SubCluster sCl = new SubCluster(key, subCl.get(key));
			allSubClusters.add(sCl);
		}
	}

	*/

    public HashMap<Tuple, Double> processOpenCells(HashMap<Tuple, Integer> openCells1){

        addNewOpenCells(openCells1);

        allClusters = new HashMap<Cell, Cluster>();

        createAllClusters();

        solution = new HashMap<>();

        Reduce reduce = new Reduce(openCells, allClusters, model);

        reduce.reduce();

        openCells = reduce.openCells;

        safeCells.addAll(reduce.safeCells);
        bombedCells.addAll(reduce.bombedCells);

        for(Cell cell: safeCells){

            solution.put(new Tuple(cell.getX(), cell.getY()), 0.0);
        }

        for(Cell cell: bombedCells){

            solution.put(new Tuple(cell.getX(), cell.getY()), 1.0);
        }

        return solution;
    }
}
