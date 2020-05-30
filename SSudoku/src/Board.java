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
    
    // initialize puzzle object
    // FILENAME SHOULD NOT BE PASSED IN HERE
    // IN THE FUTURE IT WILL BE SET ELSEWHERE
    private Puzzle puzzle = new Puzzle("puzzle_1");

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        addMouseListener(new CellsAdapter());
        loadImages();
        initGame();
    }

    private void loadImages() {

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
    			int paint_x = col * 50;
    			int paint_y = row * 50;
    			switch(puzzle.puzzleArr[row][col]) {
    			  case 0:
    				g.drawImage(num_0, paint_x, paint_y, this);
    			    break;
    			  case 1:
    				g.drawImage(num_1, paint_x, paint_y, this);
    			    break;
    			  case 2:
    				g.drawImage(num_2, paint_x, paint_y, this);
      			    break;
    			  case 3:
    				g.drawImage(num_3, paint_x, paint_y, this);
      			    break;
    			  case 4:
    				g.drawImage(num_4, paint_x, paint_y, this);
    			    break;
    			  case 5:
    				g.drawImage(num_5, paint_x, paint_y, this);
    			    break;
    			  case 6:
    				g.drawImage(num_6, paint_x, paint_y, this);
      			    break;
    			  case 7:
    				g.drawImage(num_7, paint_x, paint_y, this);
      			    break;
    			  case 8:
    				g.drawImage(num_8, paint_x, paint_y, this);
      			    break;
    			  case 9:
    				g.drawImage(num_9, paint_x, paint_y, this);
      			    break;
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
            		setBackground(Color.black);
            	}
            	
            }

                if (doRepaint) {
                    repaint();
                }
            }
        }

}