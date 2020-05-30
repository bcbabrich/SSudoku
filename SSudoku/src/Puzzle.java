import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files


// puzzle class (duh)
public class Puzzle {
	
	// holds cell values of puzzle
	// 0 represents a blank spot
	int [][] puzzleArr = new int[9][9];
	
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
			
			int i = 0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				for (int j = 0; j < 9; j++) {
					this.puzzleArr[i][j] = Integer.parseInt(data.substring(j, j+1));
				}
				i++;
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
    }
    
}
