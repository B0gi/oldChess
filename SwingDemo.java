
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class SwingDemo {
    public static void main(String[] args) {
        new MainFrame();
    }
}

class MainFrame extends JFrame {
    private static int WIDTH = 500, HEIGHT = 300;
    MainFrame() {
        System.out.println("START");
        this.setTitle("MoveObject");
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(null);
        this.add(new MovePanel());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

class DragObjectListener implements MouseInputListener {

    Point p = new Point(0, 0); // �y���I

    public DragObjectListener() {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        // �p�G�O����h���@�U�ާ@
        if (SwingUtilities.isLeftMouseButton(e)) {
            Component jl = (Component) e.getSource();
            // �y���ഫ
            Point newP = SwingUtilities.convertPoint(jl, e.getPoint(),
                    jl.getParent()); // �ഫ�y�Шt��
            jl.setLocation(jl.getX() + (newP.x - p.x), jl.getY()
                    + (newP.y - p.y)); // �]�m���Ҫ��s��m
            p = newP; // ���y���I
            // jl.getParent().repaint();

        } else if (SwingUtilities.isRightMouseButton(e)) {// �k��ާ@

        } else if (SwingUtilities.isMiddleMouseButton(e)) {// ����ާ@�A�@�빫�ШS��

        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        Component source = (Component) e.getSource();
        ((MovePanel)source).setSelected(false);
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {        
            if (e.getClickCount() == 2) {
                Component source = (Component) e.getSource();
                ((MovePanel)source).setSelected(true);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Component jl = (Component) e.getSource();
            p = SwingUtilities.convertPoint(jl, e.getPoint(), jl.getParent()); // �o���e�y���I
            // jl.getParent().repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {// �k��ާ@

        } else if (SwingUtilities.isMiddleMouseButton(e)) {// ����ާ@�A�@�빫�ШS��

        }
    }

}

class MovePanel extends JComponent  {
    private boolean selected;
    private int positionX = 0, positionY = 0;
    private Point prevPressedPoint;

    MovePanel() {
        this.setBounds(50, 50, 100, 100);
        DragObjectListener listener = new DragObjectListener();
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
    }
    public void paint(Graphics g){
    	g.setColor(Color.BLACK);
        g.fillOval(50, 50, 50, 50);
    }
    public void setSelected(boolean flag) {
        selected = flag;
        System.out.println(flag);
    }
}