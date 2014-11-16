import java.util.*;

public class Matrix {
	ArrayList<BigIntAndFactors> smoothNumbers = new ArrayList<BigIntAndFactors>();

	public Matrix(ArrayList<BigIntAndFactors> smoothNumbers){
		this.smoothNumbers = smoothNumbers;
	}
	
	public int getFreq(int row, int col) {
		return smoothNumbers.get(col).getFactorFreq(row);
	}

	public int getNumCols() {
		return smoothNumbers.size();
	}

	public int getNumRows() {
		return Quant.primes.size();
	}

	public int[][] getMatrix() {
		int[][] matrix = new int[getNumRows()][getNumCols()];

		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) {
				matrix[row][col] = getFreq(row, col);
			}
		}

		return matrix;
	}

	public void gaussEliminate(){
		gaussEliminateDown();		
		gaussEliminateUp();
	}

	private void gaussEliminateDown(){
		System.out.println("DOWN");
		HashSet<Integer> usedRows = new HashSet<Integer>();
		for(int col = 0; col < getNumCols(); col ++){
			
			System.out.println("col " + col);
			int rowWithFirstOne = -1;
			HashSet<Integer> additionalRowsWithAOne = new HashSet<Integer>();
			for(int row = 0; row < getNumRows() ; row ++){
				if(usedRows.contains(row)){
					continue;
				}
				if(getFreq(row, col) == 1){
					rowWithFirstOne = row;
					usedRows.add(row);
					break;
				}
			}
			System.out.println("found row : " + rowWithFirstOne);
			if(rowWithFirstOne != -1){
				for(int row = rowWithFirstOne + 1; row < getNumRows(); row ++){
					if(getFreq(row, col) == 1){
						additionalRowsWithAOne.add(row);
					}
				}
				System.out.println("sub " + rowWithFirstOne + " from  " + additionalRowsWithAOne);
				subtractRowFromOthers(rowWithFirstOne, additionalRowsWithAOne);
			}
			System.out.println("After col " + col + ", m == \n" + toString());
		}
	}

	private void gaussEliminateUp(){
		System.out.println("UP");
		for(int col = getNumCols() - 1; col >= 0; col --){
			
			System.out.println("col " + col);
			int rowWithFirstOne = -1;
			HashSet<Integer> additionalRowsWithAOne = new HashSet<Integer>();
			for(int row = getNumRows() - 1; row >= 0 ; row --){
				if(getFreq(row, col) == 1){
					rowWithFirstOne = row;
					break;
				}
			}
			System.out.println("found row : " + rowWithFirstOne);
			if(rowWithFirstOne != -1){
				for(int row = rowWithFirstOne - 1; row >= 0; row --){
					if(getFreq(row, col) == 1){
						additionalRowsWithAOne.add(row);
					}
				}
				System.out.println("sub " + rowWithFirstOne + " from  " + additionalRowsWithAOne);
				subtractRowFromOthers(rowWithFirstOne, additionalRowsWithAOne);
			}
			System.out.println("After col " + col + ", m == \n" + toString());
		}
	}

	private void subtractRowFromOthers(int rowIndex, HashSet<Integer> otherRowIndices){
		for(int colIndex = 0; colIndex < smoothNumbers.size(); colIndex ++){
			if(getFreq(rowIndex, colIndex) == 1){ //if 0, subtraction has no effect
				BigIntAndFactors col = smoothNumbers.get(colIndex);
				BigIntAndFactors newCol = new BigIntAndFactors(col.getPrime());
				// BigIntAndFactors newCol = (BigIntAndFactors) col.clone();
				for(int otherIndex = 0; otherIndex < getNumRows(); otherIndex ++){
					if(otherRowIndices.contains(otherIndex)){
						if(col.getFactorFreq(otherIndex) == 0){
							newCol.addFactorWithIndex(otherIndex); //0 turned to 1
						}
					} else{
						if(col.getFactorFreq(otherIndex) == 1){
							newCol.addFactorWithIndex(otherIndex); //1 stayed a 1
						}
					}
				}
				smoothNumbers.set(colIndex, newCol);
			}
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
