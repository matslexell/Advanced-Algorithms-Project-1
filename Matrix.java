import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Matrix {
	//One column is a smooth number
	List<BigIntAndFactors> columns = new ArrayList<BigIntAndFactors>();

	public Matrix(List<BigIntAndFactors> smoothNumbers){
		this.columns = smoothNumbers;
	}
	
	private int getFreq(int row, int col) {
		return columns.get(col).getFactorFreq(row);
	}

	private int getNumCols() {
		return columns.size();
	}

	private int getNumRows() {
		return QuadrSieve.primes.size();
	}

	private int[][] getMatrix() {
		int[][] matrix = new int[getNumRows()][getNumCols()];

		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) {
				matrix[row][col] = getFreq(row, col);
			}
		}

		return matrix;
	}
	
	public List<ArrayList<Integer>> gaussEliminateAndGetSolutions(){
		gaussEliminate();
		return getSomeNonTrivialSolutions();
	}

	public void gaussEliminate(){
		gaussEliminateDown();		
		gaussEliminateUp();
	}

	private void gaussEliminateDown(){
		Printer.MATRIX.print("Matrix dimensions: " + getNumRows() + " x " + getNumCols());
		Printer.MATRIX.print("Gauss elimination (down)...");
		
		HashSet<Integer> usedRows = new HashSet<Integer>();
		for(int col = 0; col < getNumCols(); col ++){
			BigIntAndFactors colVector = columns.get(col);
			
			Iterator<Integer> indicesOfOnes = colVector.getIndicesOfOnes();
			if(indicesOfOnes.hasNext()){
				Integer firstOneInCol = indicesOfOnes.next();
				usedRows.add(firstOneInCol);
				subtractRowFromOthers2(firstOneInCol, indicesOfOnes);
			}
		}
	}

	private void gaussEliminateUp(){
		Printer.MATRIX.print("Gauss elimination (up)...");

		for(int col = getNumCols() - 1; col >= 0; col --){
			BigIntAndFactors colVector = columns.get(col);
			
			Iterator<Integer> indicesOfOnes = colVector.getIndicesOfOnes();
			if(indicesOfOnes.hasNext()){
				Integer firstOneInCol = indicesOfOnes.next();
				subtractRowFromOthers2(firstOneInCol, indicesOfOnes);
			}

		}
	}

	private void subtractRowFromOthers2(int rowIndex, Iterator<Integer> otherRowIndices){
		while(otherRowIndices.hasNext()){
			Integer otherRowIndex = otherRowIndices.next();
			for(int colIndex = 0; colIndex < columns.size(); colIndex ++){
				BigIntAndFactors col = columns.get(colIndex);
				BigIntAndFactors newCol = new BigIntAndFactors(col.getNumber());
				if(getFreq(rowIndex, colIndex) == 1){ //if 0, subtraction has no effect
					if(col.getFactorFreq(otherRowIndex) == 0){
						newCol.addFactor(otherRowIndex); //0 turned to 1
					}
				} else{
					if(col.getFactorFreq(otherRowIndex) == 1){
						newCol.addFactor(otherRowIndex); //1 stayed a 1
					}
					columns.set(colIndex, newCol);
				}
			}
		}
	}
	
	/**
	 * Expects matrix to be gauss eliminated.
	 * Does not find all solutions!
	 * @return
	 */
	public List<ArrayList<Integer>> getSomeNonTrivialSolutions(){
		Printer.MATRIX.print("Extracting solutions from reduced matrix...");
		HashSet<Integer> lockedZeros = new HashSet<Integer>();
		HashMap<Integer, ArrayList<Integer>> equalToSum = new HashMap<Integer, ArrayList<Integer>>();
		HashSet<Integer> unbound = new HashSet<Integer>();
		getHintsAboutMatrixSolution(lockedZeros, equalToSum, unbound);
//		Printer.MATRIX.print("lockedZero: " + lockedZeros);
//		Printer.MATRIX.print("constraints: " + equalToSum);
//		Printer.MATRIX.print("unbound: " + unbound);
		List<ArrayList<Integer>> solutions = new ArrayList<ArrayList<Integer>>();
		
		//There is a critical assumption here.
		//Any var that's in a value of the map (is part of a constraint) 
		//can not also be a key of the map.
		//In that case there is a circular dependency that might not 
		//be respected here.
		//Also, not all solutions are found.
		for(int unboundVar : unbound){
			ArrayList<Integer> solution = new ArrayList<Integer>();
			solution.add(unboundVar);
			for(int constrained : equalToSum.keySet()){
				int sum = equalToSum.get(constrained).stream().mapToInt(x -> solution.contains(x)? 1 : 0).sum();
				if(sum % 2 == 1){
//					Printer.MATRIX.print("add " + constrained + " since sum(" + equalToSum.get(constrained) + ") == " + sum);
					solution.add(constrained);
				}
			}
			solutions.add(solution);
		}
		
		return solutions;
	}
	
	/**
	 * Fill the given data structures with info about the solution, that's used in the next step of the 
	 * algorithm. The arguments should be empty when given to the method.
	 * @param lockedZeros Indices of variables that are 0 in all solutions.
	 * @param equalToSum A mapping (i, [j, k, l]) says that xi = xj + xk + xl
	 * @param unbound
	 */
	private void getHintsAboutMatrixSolution(HashSet<Integer> lockedZeros, HashMap<Integer, ArrayList<Integer>> equalToSum, HashSet<Integer> unbound){
		assert(lockedZeros.isEmpty());
		assert(equalToSum.isEmpty());
		for(int row = 0; row < getNumRows(); row ++){
			int firstColWithAOne = -1;
			for(int col = 0; col < getNumCols(); col ++){
				if(getFreq(row, col) == 1){
					firstColWithAOne = col;
					break;
				}
			}
			if(firstColWithAOne != -1){
				for(int col = firstColWithAOne + 1; col < getNumCols(); col ++){
					if(getFreq(row, col) == 1){
						addToMapping(equalToSum, firstColWithAOne, col);
					}
				}
				if(! equalToSum.containsKey(firstColWithAOne)){
					lockedZeros.add(firstColWithAOne);
				}
			}	
		}
		for(int col = 0; col < getNumCols(); col ++){
			if(! lockedZeros.contains(col) && ! equalToSum.containsKey(col)){
				unbound.add(col);
			}
		}
	}
	
	private <K,V>void addToMapping(Map<K, ArrayList<V>> map, K key, V val){
		if(map.containsKey(key)){
			map.get(key).add(val);
		}else{
			ArrayList<V> list = new ArrayList<V>();
			list.add(val);
			map.put(key, list);
		}
	}
	
	public String toString(){
		int[][] matrix = getMatrix();
		String s = "";
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) {
				s += matrix[row][col] + " ";
				
			}
			s += "\n";
		}
		
		return s;
	}

}
