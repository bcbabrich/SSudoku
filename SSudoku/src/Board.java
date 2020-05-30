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

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 900;
    private final int B_HEIGHT = 900;
    private final int DELAY = 140;
    private boolean BGCOLOR = false;

    private Timer timer;
    
    // unhighlighted number image objects
    private Image num_0;
    private Image num_1;
    private Image num_2;
    private Image num_3;
    private Image num_4;
    private Image num_5;
    private Image num_6;
    private Image num_7;
    private Image num_8;
    private Image num_9;
    
    // cell and box border images
    private Image ver_cell_border;
    private Image hor_cell_border;
    private Image ver_box_border;
    private Image hor_box_border;
    
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
        loadImages();
        initGame();
    }

    private void loadImages() {

    	// load number images
    	// this code will get cleaned up
        ImageIcon num_im = new ImageIcon("src/resources/0.png");
        num_0 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/1.png");
        num_1 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/2.png");
        num_2 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/3.png");
        num_3 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/4.png");
        num_4 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/5.png");
        num_5 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/6.png");
        num_6 = num_im.getImage();

        num_im = new ImageIcon("src/resources/7.png");
        num_7 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/8.png");
        num_8 = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/9.png");
        num_9 = num_im.getImage();
        
        // load border images
        num_im = new ImageIcon("src/resources/ver_cell_border.png");
        ver_cell_border = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/hor_cell_border.png");
        hor_cell_border = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/ver_box_border.png");
        ver_box_border = num_im.getImage();
        
        num_im = new ImageIcon("src/resources/hor_box_border.png");
        hor_box_border = num_im.getImage();     
        
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
    	
    	
    	// loop over puzzle array
    	for (int row = 0; row < 9; row++) {
    		for (int col = 0; col < 9; col++) {
    			// paint image corresponding to value in puzzle array
    			// paint this image at corresponding coords
    			int num_x = col * 50 + 2;
    			int num_y = row * 50 + 2;
    			switch(puzzle.puzzleArr[row][col]) {
    			  case 0:
    				g.drawImage(num_0, num_x, num_y, this);
    			    break;
    			  case 1:
    				g.drawImage(num_1, num_x, num_y, this);
    			    break;
    			  case 2:
    				g.drawImage(num_2, num_x, num_y, this);
      			    break;
    			  case 3:
    				g.drawImage(num_3, num_x, num_y, this);
      			    break;
    			  case 4:
    				g.drawImage(num_4, num_x, num_y, this);
    			    break;
    			  case 5:
    				g.drawImage(num_5, num_x, num_y, this);
    			    break;
    			  case 6:
    				g.drawImage(num_6, num_x, num_y, this);
      			    break;
    			  case 7:
    				g.drawImage(num_7, num_x, num_y, this);
      			    break;
    			  case 8:
    				g.drawImage(num_8, num_x, num_y, this);
      			    break;
    			  case 9:
    				g.drawImage(num_9, num_x, num_y, this);
      			    break;
    			}
    			
    			// paint borders of image differently if we're at a cell or a box index
    			
    			// first we'll paint the horizontal borders
    			
    			// border 1 coords
    			int hor_border_1_x = col * 50;
    			int hor_border_1_y = row * 50;
    			
    			// border 2 coords
    			int hor_border_2_x = col * 50;
    			int hor_border_2_y = row * 50 + 48;
    			
    			switch(row % 3) {
    				case 0: // top of a box
    					g.drawImage(hor_box_border, hor_border_1_x, hor_border_1_y, this);
    					g.drawImage(hor_cell_border, hor_border_2_x, hor_border_2_y, this);
    					break;
    				case 1: // middle of a box
    					g.drawImage(hor_cell_border, hor_border_1_x, hor_border_1_y, this);
    					g.drawImage(hor_cell_border, hor_border_2_x, hor_border_2_y, this);
    					break;
    				case 2: // bottom of a box
    					g.drawImage(hor_cell_border, hor_border_1_x, hor_border_1_y, this);
    					g.drawImage(hor_box_border, hor_border_2_x, hor_border_2_y, this);
    			}
    			
    			// now we'll paint the vertical borders
    			
    			// border 1 coords
    			int ver_border_1_x = col * 50;
    			int ver_border_1_y = row * 50;
    			
    			// border 2 coords
    			int ver_border_2_x = col * 50 + 48;
    			int ver_border_2_y = row * 50;
    			
    			switch(col % 3) {
					case 0: // left of a box
						g.drawImage(ver_box_border, ver_border_1_x, ver_border_1_y, this);
						g.drawImage(ver_cell_border, ver_border_2_x, ver_border_2_y, this);
						break;
					case 1: // middle of a box
						g.drawImage(ver_cell_border, ver_border_1_x, ver_border_1_y, this);
						g.drawImage(ver_cell_border, ver_border_2_x, ver_border_2_y, this);
						break;
					case 2: // right of a box
						g.drawImage(ver_cell_border, ver_border_1_x, ver_border_1_y, this);
						g.drawImage(ver_box_border, ver_border_2_x, ver_border_2_y, this);
    			}
    		}
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

            int x = e.getX();
            int y = e.getY();
            
            System.out.println("mouse coords: " + x + " " + y);

            boolean doRepaint = false;
            
            if (100 < x && x < 150 &&
            	100 < y && y < 150) {
            	doRepaint = true;
            	BGCOLOR = !BGCOLOR;
            	if (BGCOLOR) {
            		setBackground(Color.white);
            	} else {
            		setBackground(Color.blue);
            	}
            	
            }

                if (doRepaint) {
                    repaint();
                }
            }
        }

}