package data_model;

import java.util.HashSet;

public interface IModelSource {

    HashSet<Cell> getNeighboursWithState(int i, int j, int state);
}
