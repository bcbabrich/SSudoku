import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.Math; 

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 900;
    private final int B_HEIGHT = 900;
    private final int DELAY = 140;
    private boolean BGCOLOR = false;
    
    // cells are always square, so their w/h are equal
    private final int CELL_W_H = 50;
    
    // border height equal cell height, so no variable for it
    private final int BORDER_W = 2;
    
    // global offsets
    private final int GLOBAL_OFFSET_X = 200;
    private final int GLOBAL_OFFSET_Y = 200;
    
    // vertical offset for completed number images
    private final int CN_Y_OFFSET = 460;

    private Timer timer;
    
    // number tiles (unhighlighted)
    // 10 and not 9 because we include 0 
    private Image numTiles[] = new Image[10];
    
    // highlighted blank tile
    private Image blankHighlightedTile;
    
    // reference circles down at the bottom of the board
    private Image refCircles[] = new Image [9];
    
    // cell and box border images
    private Image borders;
    
    // completed number red x
    private Image red_x;
    
    // game over message
    private Image game_over;
    
    // font is only set once
    private Font f = new Font("Dialog", Font.PLAIN, 50);
    
    // combo timer variables
    private int activeNumType = -1;
    private long start_time = System.currentTimeMillis();;
    private long end_time;
    private int combo_timer = 0;
    private int interim_time = 0;
    private int combo_counter = 0;
    
    // global timer
    private int global_timer = 0;
    private int final_time;
    
    // score counter
    private int score = 0;
    
    private Color BGCOL = new Color(188, 184, 255);
    
    // initialize puzzle object
    // FILENAME SHOULD NOT BE PASSED IN HERE
    // IN THE FUTURE IT WILL BE SET ELSEWHERE
    private Puzzle puzzle = new Puzzle("puzzle_1");
    

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {
        setBackground(this.BGCOL);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        addMouseListener(new CellsAdapter());
        addKeyListener(new TAdapter());
        loadImages();
        initGame();
    }

    private void loadImages() {

    	// load tiles
    	ImageIcon im;
    	for (int i = 0; i < 10; i++) {
    		im = new ImageIcon("src/resources/" + i + ".png");
    		this.numTiles[i] = im.getImage();
    		if (i > 0) {
        		im = new ImageIcon("src/resources/" + i + "c.png");
        		this.refCircles[i-1] = im.getImage();
    		}
    	}
    	
    	// load blank highlighted tile
    	im = new ImageIcon("src/resources/0s.png");
    	this.blankHighlightedTile = im.getImage();
        
        // load borders image
        im = new ImageIcon("src/resources/box_and_cell_borders.png");
        this.borders = im.getImage();
        
        // load red x image
        im = new ImageIcon("src/resources/red_x.png");
        this.red_x = im.getImage();
        
        // load game over image
        im = new ImageIcon("src/resources/game_over.png");
        this.game_over = im.getImage();
        
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
    
    // DO DRAWING
    private void doDrawing(Graphics g) {
    	// highlighting variables
    	// TODO: these should probably be global
    	g.setColor(Color.red);
    	AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
    	Graphics2D g2d = (Graphics2D) g.create();
    	g2d.setComposite(alcom);
    	
    	// TILES AND TILE HIGHLIGHTS
    	for (int row = 0; row < 9; row++) {
    		for (int col = 0; col < 9; col++) {
    			
    			// map board cell coords to pixel coords
    			int num_x = col * this.CELL_W_H + this.BORDER_W + this.GLOBAL_OFFSET_X;
    			int num_y = row * this.CELL_W_H + this.BORDER_W + this.GLOBAL_OFFSET_Y;
    			
    			// draw tiles
    			g.drawImage(this.numTiles[puzzle.puzzleArr[0][row][col]], 
    													num_x, num_y, this);
    			
    			// draw blank highlighted tile
    			if(this.puzzle.puzzleArr[1][row][col] == 2) {
    				g.drawImage(this.blankHighlightedTile, num_x, num_y, this);
    			}
    			
    			// draw highlights
    			if (this.puzzle.puzzleArr[1][row][col] == 1) {
    				g2d.fillRect(col * this.CELL_W_H + this.BORDER_W + this.GLOBAL_OFFSET_X, 
    						row * this.CELL_W_H + this.BORDER_W + this.GLOBAL_OFFSET_X, 
    						48, 48); // magic numbers?
    			}
    			
    		}
    	}
    	
    	// BORDERS
    	g.drawImage(this.borders, this.GLOBAL_OFFSET_X, this.GLOBAL_OFFSET_Y, this);
    	
    	// REFERENCE NUMBERS
    	for (int c = 0; c < 9; c++) {
    		// draw reference circles
    		g.drawImage(this.refCircles[c], 
    						c * this.CELL_W_H + this.GLOBAL_OFFSET_X, 
    						this.CN_Y_OFFSET + this.GLOBAL_OFFSET_Y, this);
    		
    		// draw ref circle highlights
    		if (c+1 == this.puzzle.highlightedNumType) {
    			g2d.fillOval(c * this.CELL_W_H + this.GLOBAL_OFFSET_X - 1,
            			this.CN_Y_OFFSET + this.GLOBAL_OFFSET_Y - 1,
            			49, 49);
    		}
    		
    		// draw red_x if number is completed
    		if (this.puzzle.completedNums[c]) {
        		g.drawImage(this.red_x, 
        				c * this.CELL_W_H + this.GLOBAL_OFFSET_X, 
        				this.CN_Y_OFFSET + this.GLOBAL_OFFSET_Y, this);
    		}
    		
    	}
    	
    	// GLOBAL TIMER
    	g.setFont(this.f);
    	g.setColor(Color.white);
    	g.drawString("time:", 10, 50);
    	if (!this.puzzle.end_game) {
    		g.drawString("" + this.global_timer, 150, 50);
    	} else {
    		g.drawString("" + this.final_time, 150, 50);
    	}
    	
    	
    	
    	// COMBO TIMER + COUNTER
    	g.setColor(Color.blue);
    	g.drawString("COMBO COUNT:", this.GLOBAL_OFFSET_X, 100);
    	g.drawString("" + this.combo_counter, this.GLOBAL_OFFSET_X + 400, 100);
    	g.drawString("COMBO TIMER:", this.GLOBAL_OFFSET_X, 175);
    	
    	int time_elapsed = (int) ((System.currentTimeMillis() - this.start_time) / 1000 );
    	this.global_timer = time_elapsed;
    	if (!this.puzzle.end_game) {
    		this.final_time = this.global_timer;
    	}
    	
    	if (this.activeNumType != -1 && time_elapsed != this.interim_time) {
    		this.combo_timer--;
    		this.interim_time = time_elapsed;
    	} else if (this.activeNumType != -1 && this.combo_timer <= 0) {
    		this.activeNumType = -1;
    	}
    	
    	g.drawString("" + this.combo_timer, this.GLOBAL_OFFSET_X + 375, 175);
    	
    	// SCORE
    	if (this.combo_timer == 0) { // this should really be somewhere else...
    		this.score += this.combo_counter * 5;
    		this.combo_counter = 0;
    	}
    	
    	g.setColor(Color.red);
    	puzzle.check_EGS();
    	if (puzzle.end_game) {
    		g.drawImage(this.game_over, this.GLOBAL_OFFSET_X, this.GLOBAL_OFFSET_Y + 100, this);
    		g.drawString(this.score + " - " + this.final_time + " = " + (this.score - this.final_time), 
    				this.GLOBAL_OFFSET_X + 50, 
    				this.GLOBAL_OFFSET_Y + 325);
    	} else {
        	g.drawString("score:", this.GLOBAL_OFFSET_X, 775);
        	g.drawString("" + this.score, this.GLOBAL_OFFSET_X + 150, 775);
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
            x = x < GLOBAL_OFFSET_X ? GLOBAL_OFFSET_X + CELL_W_H * 9 : x; // prevent negative cell coords
            y = y < GLOBAL_OFFSET_Y ? GLOBAL_OFFSET_Y + CELL_W_H * 9 : y;
            puzzle.setHighlights((x - GLOBAL_OFFSET_X) / CELL_W_H, (y - GLOBAL_OFFSET_X) / CELL_W_H);

            }
        }
    
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
        	int numToEnter = -1;
        	
            switch(e.getKeyCode()) {
            	case KeyEvent.VK_1: numToEnter = 1; break;
            	case KeyEvent.VK_2: numToEnter = 2; break;
            	case KeyEvent.VK_3: numToEnter = 3; break;
            	case KeyEvent.VK_4: numToEnter = 4; break;
            	case KeyEvent.VK_5: numToEnter = 5; break;
            	case KeyEvent.VK_6: numToEnter = 6; break;
            	case KeyEvent.VK_7: numToEnter = 7; break;
            	case KeyEvent.VK_8: numToEnter = 8; break;
            	case KeyEvent.VK_9: numToEnter = 9; break;
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
        					puzzle.setHighlights(col, row);
        					
        					// check if this number type is now completed
        					puzzle.setCompletedNumTypes(numToEnter);
        					
        					// update combo counter/timer
        					if (activeNumType == -1) {
        						activeNumType = numToEnter;
        						combo_timer = 7;
        						combo_counter++;
        					} else if (activeNumType == numToEnter) {
        						combo_counter++;
        						combo_timer = 7;
        					}
        					
        					if (puzzle.completedNums[numToEnter - 1]) {
        						combo_timer = 0;
        						score += combo_counter * 5;
        					}
        					
        					
        					repaint();
        					
        					break;
        				}
        			}
        		}
            }
            
        }
    }

}