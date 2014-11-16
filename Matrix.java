import java.util.ArrayList;

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
