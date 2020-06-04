import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 900;
    private final int B_HEIGHT = 900;
    private final int DELAY = 140;
    private boolean BGCOLOR = false;
    
    // cells are always square, so their w/h are equal
    private final int CELL_W_H = 50;
    
    // border height equal cell height, so no variable for it
    private final int BORDER_W = 2;
    
    // vertical offset for completed number images
    private final int CN_Y_OFFSET = 460;

    private Timer timer;
    
    // unhighlighted number image objects go in here
    // 10 and not 9 because we include 0
    private Image numImgArr[][] = new Image[7][10];
    
    // cell and box border images
    private Image borders;
    
    
    // initialize puzzle object
    // FILENAME SHOULD NOT BE PASSED IN HERE
    // IN THE FUTURE IT WILL BE SET ELSEWHERE
    private Puzzle puzzle = new Puzzle("puzzle_1");

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {
        setBackground(Color.blue);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        addMouseListener(new CellsAdapter());
        addKeyListener(new TAdapter());
        loadImages();
        initGame();
    }

    private void loadImages() {

    	// load number images
    	// 10 and not 9 because we include 0
    	ImageIcon im;
    	for (int i = 0; i < 10; i++) {
    		im = new ImageIcon("src/resources/" + i + ".png");
    		this.numImgArr[0][i] = im.getImage();
    		im = new ImageIcon("src/resources/" + i + "h.png");
    		this.numImgArr[1][i] = im.getImage();
    		if (i > 0) {
        		im = new ImageIcon("src/resources/" + i + "c.png");
        		this.numImgArr[3][i] = im.getImage();
        		im = new ImageIcon("src/resources/" + i + "cc.png");
        		this.numImgArr[4][i] = im.getImage();
        		im = new ImageIcon("src/resources/" + i + "ch.png");
        		this.numImgArr[5][i] = im.getImage();
        		im = new ImageIcon("src/resources/" + i + "cch.png");
        		this.numImgArr[6][i] = im.getImage();
    		}

    	}
    	im = new ImageIcon("src/resources/0s.png");
    	this.numImgArr[2][0] = im.getImage();
        
        // load borders image
        im = new ImageIcon("src/resources/box_and_cell_borders.png");
        borders = im.getImage();
   
        
    }

    private void initGame() {
    	// Every DELAY miliseconds,
    	// actionPerformed is called
    	// not sure how "this" translates to actionPerformed being called
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
    	
    	
    	// draw numbers from puzzle array
    	for (int row = 0; row < 9; row++) {
    		for (int col = 0; col < 9; col++) {
    			// paint image corresponding to value in puzzle array
    			// paint this image at corresponding coords
    			int num_x = col * this.CELL_W_H + this.BORDER_W;
    			int num_y = row * this.CELL_W_H + this.BORDER_W;
    			g.drawImage(this.numImgArr[puzzle.puzzleArr[1][row][col]][puzzle.puzzleArr[0][row][col]], num_x, num_y, this);	
    		}
    	}
    	
    	// draw borders
    	g.drawImage(this.borders, 0, 0, this);
    	
    	// draw completed numbers
    	for (int c = 1; c < 10; c++) {
    		g.drawImage(this.numImgArr[puzzle.completedNums[c-1]][c], (c-1) * this.CELL_W_H, this.CN_Y_OFFSET, this);
    	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	// not exactly sure what this does
    	// pretty sure it's a member of JPanel
        repaint();
    }
    
    private class CellsAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        	
        	// get coordinates of click
            int x = e.getX();
            int y = e.getY();
            
            // send CELL coords over to Puzzle to perform highlighting
            puzzle.setHighlights(x / CELL_W_H, y / CELL_W_H);

            }
        }
    
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
        	int numToEnter = -1;
        	
            switch(e.getKeyCode()) {
            	case KeyEvent.VK_1:
            		numToEnter = 1;
            		break;
            	case KeyEvent.VK_2:
            		numToEnter = 2;
            		break;
            	case KeyEvent.VK_3:
            		numToEnter = 3;
            		break;
            	case KeyEvent.VK_4:
            		numToEnter = 4;
            		break;
            	case KeyEvent.VK_5:
            		numToEnter = 5;
            		break;
            	case KeyEvent.VK_6:
            		numToEnter = 6;
            		break;
            	case KeyEvent.VK_7:
            		numToEnter = 7;
            		break;
            	case KeyEvent.VK_8:
            		numToEnter = 8;
            		break;
            	case KeyEvent.VK_9:
            		numToEnter = 9;
            		break;
            }
            
            // if a numeric key from [1-9] was entered,
            // search puzzleArr for a selected cell
            if (numToEnter != -1) {
        		for (int row = 0; row < 9; row++) {
        			for (int col = 0; col < 9; col++) {
        				// if we find a selected cell,
        				// set its num type to the key entered
        				if (puzzle.puzzleArr[1][row][col] == 2){
        					puzzle.puzzleArr[0][row][col] = numToEnter;
        					
        					// call set highlights on the cell we just entered a number in
        					// the +5 is to ensure the coords we send are not on a border
        					//    - I don't think this is strictly necessary
        					puzzle.setHighlights(col, row);
        					
        					// check if this number type is now completed
        					puzzle.setCompletedNumTypes(numToEnter);
        					
        					repaint();
        				}
        			}
        		}
            }
            
        }
    }

}