import java.awt.EventQueue;
import javax.swing.JFrame;

public class SSudoku extends JFrame {

    public SSudoku() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new Board());
        
        setResizable(false);
        pack();
        
        setTitle("SSudoku");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new SSudoku();
            ex.setVisible(true);
        });
    }
}