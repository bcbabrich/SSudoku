import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files


// puzzle class (duh)
public class Puzzle {
	
	// holds cell values of puzzle
	//     - 0 represents a blank spot
	// first dimension of puzzleArr holds number values
	// second dimension holds highlight values
	int [][][] puzzleArr = new int[2][9][9];
	
	// array for completed number types
	// number type can only be highlighted if it is not yet complete
	int [] completedNums = new int[] {3,3,3,3,3,3,3,3,3};
	
	// puzzle constructor
	// populates fileArray from file at filename
    public Puzzle(String filename) {
    	// read in puzzle file into puzzle array
    	readPuzzleFile(filename);

    }

	// reads data from puzzle file into puzzle array
    // obviously, assume puzzle file is a 9x9 grid of characters in [0-9]
    private void readPuzzleFile(String filename) {
		try {
			
			File myObj = new File("src/resources/" + filename + ".txt");
			
			Scanner myReader = new Scanner(myObj);
			
			// Note:
			// Since the puzzle file read in line by line,
			// rows are stored first in the puzzle array rather than lines.
			// this amounts to the y axis taking precedence over the x axis.
			int i = 0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				for (int j = 0; j < 9; j++) {
					this.puzzleArr[0][i][j] = Integer.parseInt(data.substring(j, j+1));
					this.puzzleArr[1][i][j] = 0; // puzzle is read in as unhighlighted
				}
				i++;
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
    }
    
    // given x, y CELL coordinates game board, set highlight dimension of puzzleArr accordingly
    void setHighlights(int cell_x, int cell_y) {
    	// get number clicked on
    	// numType = -1 means click occurred outside of puzzle
    	int numType;
    	
    	// check where click hit
    	//    - click occurred outside of puzzle
    	if ( cell_x > 8 || cell_x > 8) { 
    		numType = -1;
    		
    	//     - click occurred inside of puzzle
    	} else {                        
    		// as noted above, the y axis comes before the x axis due to file reading conventions
    		numType = this.puzzleArr[0][cell_y][cell_x];
    		
    		
    	}
    	
    	// if a non-blank spot was clicked on, set the highlighting dimension accordingly
    	if (numType > 0) {
    		// reset all highlights
        	for (int row = 0; row < 9; row++) {
    			for (int col = 0; col < 9; col++) {
    				this.puzzleArr[1][row][col] = 0;
    			}
    		}
    		
    		// loop over all spots in puzzleArr
    		for (int row = 0; row < 9; row++) {
    			for (int col = 0; col < 9; col++) {
    				// highlight row, column, and box of any index matching numType
    				if (this.puzzleArr[0][row][col] == numType) {
    					// row highlighting
    					for (int i = 0; i < 9; i++) {
    						this.puzzleArr[1][row][i] = 1;
    					}
    					
    					// column highlighting
    					for (int j = 0; j < 9; j++) {
    						this.puzzleArr[1][j][col] = 1;
    					}
    					
    					// box highlighting
    					for (int box_x = 0; box_x < 3; box_x++) {
    						for (int box_y = 0; box_y < 3; box_y++) {
    							this.puzzleArr[1][box_y + row - (row % 3)][box_x + col - (col % 3)] = 1;
    						}
    					}
    				}
    				
    			}
    		}
    		
    		// COMPLETED NUMS HIGHLIGHTING
    		
			// first, reset any highlights in completedNums
			for (int i = 0; i < 9; i++) {
				if (this.completedNums[i] == 5) {
					this.completedNums[i] = 3;
				} else if (this.completedNums[i] == 6) {
					this.completedNums[i] = 4;
				}
			}
			
			
    		if (this.completedNums[numType - 1] == 4) {
    			this.completedNums[numType - 1] = 6;
    		} else {
    			this.completedNums[numType - 1] = 5;
    		}
    		
    	} else if (numType == -1) {
    		// click occurred outside of puzzle,
    		// so reset entire highlight dimension to zero
    		for (int row = 0; row < 9; row++) {
    			for (int col = 0; col < 9; col++) {
    				this.puzzleArr[1][row][col] = 0;
    			}
    		}
    	} else if (numType == 0){
    		// clear any previously selected cells
        	for (int row = 0; row < 9; row++) {
    			for (int col = 0; col < 9; col++) {
    				if (this.puzzleArr[1][row][col] == 2) {
    					this.puzzleArr[1][row][col] = 0;
    				}
    			}
    		}
    		
        	// reset all highlights if empty cell clicked on was highlighted
    		if (this.puzzleArr[1][cell_y][cell_x] == 1) {
            	for (int row = 0; row < 9; row++) {
        			for (int col = 0; col < 9; col++) {
        				this.puzzleArr[1][row][col] = 0;
        			}
        		}
    		}

        	
    		// click occurred on empty cell
    		this.puzzleArr[1][cell_y][cell_x] = 2;
    	}
    }
    
    void setCompletedNumTypes(int numType) {
    	boolean inAllBoxes = true;
		for (int box_row = 0; box_row < 3; box_row++) {
			for (int box_col = 0; box_col < 3; box_col++) {
				
				boolean inCurBox = false;
				for (int box_x = 0; box_x < 3; box_x++) {
					for (int box_y = 0; box_y < 3; box_y++) {
						if (this.puzzleArr[0][(box_col * 3) + box_y][(box_row * 3) + box_x] == numType) {
							inCurBox = true;
						}
					}
				}
				
				if (!inCurBox) {
					inAllBoxes = false;
				}
				
			}
		}
		
		if (inAllBoxes) {
			this.completedNums[numType - 1] = 6;
		}
    	
    }
    
}
