package improc ; 

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class keys extends JFrame implements KeyListener {

    JLabel label;
    boolean up = false ; 
    boolean down = false ; 
    boolean left = false ;
    boolean right = false ; 
    
    public keys(String s) {
        super(s);
        JPanel p = new JPanel();
        label = new JLabel("Key Listener!");
        p.add(label);
        add(p);
        addKeyListener(this);
        setSize(200, 100);
        setVisible(true);
        setLocation(600,600) ; 

    }

    public void keyTyped(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        ///    System.out.println("Right key typed");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        //    System.out.println("Left key typed");
        }

    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        	right = true ; 
          //  System.out.println("Right key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        	left = true ; 
       //     System.out.println("Left key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
        	up = true ; 
        //    System.out.println("Up key pressed");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        	down = true ; 
         //   System.out.println("Down key pressed");
        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        	right = false ; 
          ////  System.out.println("Right key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        	left = false ; 
          //  System.out.println("Left key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
        	up = false ; 
          //  System.out.println("Up key Released");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        	down = false ; 
          //  System.out.println("Down key Released");
        }
    }

    public static void main(String[] args) {
        new keys("Key Listener Tester");
    }
}