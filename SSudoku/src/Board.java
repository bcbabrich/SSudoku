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

    // I guess we need this for... reasons??.
    // TODO: Investigate
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
    
    // sparks images
    private Image combo_sparks[] = new Image[5];
    
    // completed number red x
    private Image red_x;
    
    // game over message
    private Image game_over;
    
    // font is only set once
    private Font f = new Font("Impact", Font.PLAIN, 50);
    
    // score counter
    private int score = 0;
    
    // TODO: Better descriptions for these
    private Color BGCOL = new Color(188, 184, 255);
    
    
    // combo and timer object
    private combo_and_timer combotimer = new combo_and_timer();
    
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
        
        // load combo sparks images
        for (int i = 0; i < 5; i++) {
        	im = new ImageIcon("src/resources/sparks_"+ i +".png");
        	this.combo_sparks[i] = im.getImage();
        }
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
    	// initialize graphics objects
    	g.setColor(Color.red);
    	Graphics2D g2d = (Graphics2D) g.create();
    	
    	// DRAW TILES AND TILE HIGHLIGHTS
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
    			
    			// draw tile highlights
    			float opacity = this.combotimer.activeNumType == -1 ?
							0.3f : ((float)this.combotimer.combo_seconds / 7);
    			AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
    	    	g2d.setComposite(alcom);
    			if (this.puzzle.puzzleArr[1][row][col] == 1) {
    				g2d.fillRect(col * this.CELL_W_H + this.BORDER_W + this.GLOBAL_OFFSET_X, 
    						row * this.CELL_W_H + this.BORDER_W + this.GLOBAL_OFFSET_X, 
    						48, 48); // magic numbers?
    			}
    			
    		}
    	}
    	
    	// DRAW BORDERS
    	g.drawImage(this.borders, this.GLOBAL_OFFSET_X, this.GLOBAL_OFFSET_Y, this);
    	
    	// DRAW REFERENCE NUMBERS
    	for (int c = 0; c < 9; c++) {
    		// draw reference circles
    		g.drawImage(this.refCircles[c], 
    						c * this.CELL_W_H + this.GLOBAL_OFFSET_X, 
    						this.CN_Y_OFFSET + this.GLOBAL_OFFSET_Y, this);
    		
    		// draw ref circle highlights
    		int refNumToHL = this.puzzle.highlightedNumType == 0 // keep ref num highlighted when blank spot is clicked on
    						? this.puzzle.prev_highlighted 
    						: this.puzzle.highlightedNumType; 
    		if (c+1 == refNumToHL) {
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
    	
    	// UPDATE TIMER/COMBO OBJECT
    	this.combotimer.update();
    	
    	// DRAW GLOBAL TIMER
    	g.setFont(this.f);
    	g.setColor(Color.white);
    	g.drawString("time:", 10, 50);
    	if (!this.puzzle.end_game) {
    		g.drawString("" + this.combotimer.global_timer, 150, 50);
    	} else {
    		g.drawString("" + this.combotimer.final_time, 150, 50);
    	}
    	
    	
    	// DRAW COMBO TIMER + COUNTER
    	if (this.combotimer.activeNumType != -1) {
    		// animation for text first appearing
    		// wrap this in some sort of if statement for final animation
    		int cur_tenths = (int) ((System.currentTimeMillis() - this.combotimer.global_start_time) / 100 );
    		if (this.combotimer.combo_timer + 1 == 7 && !this.combotimer.begin_anim) {
    			this.combotimer.begin_anim = true;
    			this.combotimer.tenths_c = 0;
    		}
    		
    		if (this.combotimer.begin_anim) {
    			if (cur_tenths != this.combotimer.interim_tenths) {
    				this.combotimer.tenths_c++;
    				this.combotimer.interim_tenths = cur_tenths;
    			}
    			
    			if (this.combotimer.tenths_c > 4) {
    				this.combotimer.begin_anim = false;
    			}
    		}
    		
    		this.combotimer.tenths_c = this.combotimer.tenths_c > 4 ? 4 : this.combotimer.tenths_c; // cap tenths_c at 4
    		if (this.combotimer.combo_timer + 1 ==  7) {
    			g.drawImage(this.combo_sparks[this.combotimer.tenths_c], this.GLOBAL_OFFSET_X - 155, this.GLOBAL_OFFSET_Y - 215, this);
    		}
    		
    		g.setColor(Color.blue);
        	g.drawString("Combo Count:", this.GLOBAL_OFFSET_X, 100); 
        	g.drawString("" + this.combotimer.combo_counter, this.GLOBAL_OFFSET_X + 400, 100);
        	g.drawString("Combo Timer:", this.GLOBAL_OFFSET_X, 175);
        	g.drawString("" + (8 - this.combotimer.combo_seconds), this.GLOBAL_OFFSET_X + 375, 175);
    	}
    	
    	
    	// DRAW SCORE
    	g.setColor(Color.red);
    	puzzle.check_EGS();
    	if (this.puzzle.end_game) {
    		g.drawImage(this.game_over, this.GLOBAL_OFFSET_X, this.GLOBAL_OFFSET_Y + 100, this);
    		g.drawString(this.score + " - " + this.combotimer.final_time + " = " + (this.score - this.combotimer.final_time), // replaced by combotimer object
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
        					
        					// either combo is newly activated
        					// TODO: this code can probably be refactored
        					if (combotimer.activeNumType == -1) {
        						combotimer.activeNumType = numToEnter;
        						
        						// forcing combo timer's first second to actually be a full second
        						combotimer.combo_start_time = (System.currentTimeMillis() - combotimer.global_start_time);
        						combotimer.combo_seconds = 1;
        						
        						combotimer.combo_timer = 7;
        						combotimer.combo_counter++;
        					} else 
        					
        					// or combo is alive and corresponding num is entered
        					if (combotimer.activeNumType == numToEnter) {
        						combotimer.combo_counter++;
        						combotimer.combo_timer = 7;
        						
        						// forcing combo timer's first second to actually be a full second
        						combotimer.combo_start_time = (System.currentTimeMillis() - combotimer.global_start_time);
        						combotimer.combo_seconds = 1;
        					}
        					
        					repaint();
        					
        					break;
        				}
        			}
        		}
            }
        }
    }
    
    // documenatation
    private class combo_and_timer {
    	private int activeNumType;        // number type for combo
        private long global_start_time;   // used to calculate time since game started
        private int combo_timer;          // self-explanatory
        private int combo_counter; // self-explanatory
        private int global_timer;  // ... time game has been running
        private int final_time;    // final time for game over screen
        boolean begin_anim = false;
        private int interim_tenths;
        private int tenths_c;
        
        // forcing combo timer's first second to actually last a second
        private long combo_start_time;
        private int combo_seconds = 1;
    	
    	public combo_and_timer() {
    		this.activeNumType = -1;
    		this.global_start_time = System.currentTimeMillis();
    	    this.combo_timer = 0;
    	    this.combo_counter = 0;
    	    this.interim_tenths = (int) ((System.currentTimeMillis() - this.global_start_time) / 100 );
    	}
    	
    	// documentation
    	public void update() {
    		// update global timer
    		this.global_timer = (int) ((System.currentTimeMillis() - this.global_start_time) / 1000 );
        	
    		// a combo is currently active. let's update the combo seconds accordingly
    		if (this.activeNumType != -1) {
    			
    			// first, get the miliseconds that have gone by since combo_start_time
    			long cur_time = (System.currentTimeMillis() - this.global_start_time);
    			long ms_since_cst = cur_time - this.combo_start_time;
    			
    			// if more than 7 seconds have passed, or if the current num type is completed, end the combo
    			if (ms_since_cst >= 7000 || puzzle.completedNums[this.activeNumType - 1]) {
        			score += this.combo_counter * 5;
            		this.combo_counter = 0;
            		this.activeNumType = -1;
            		puzzle.resetHighlights();
            		puzzle.highlightedNumType = -1;
    			}
    			
    			// otherwise, check if at least a second has passed and increment combo_seconds if it has
    			// ASSUMES THIS METHOD GETS CALLED MORE THAN ONCE A SECOND
    			else if (ms_since_cst - (1000 * combo_seconds) >= 0) {
    				this.combo_seconds++;
    			}
    			
    		}
    		
        	// always update final time with current global time until game ends
        	if (!puzzle.end_game) {
        		this.final_time = this.global_timer;
        	}
    	}
    	
    }

}