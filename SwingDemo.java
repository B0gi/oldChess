
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

    Point p = new Point(0, 0); // 座標點

    public DragObjectListener() {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        // 如果是左鍵則做一下操作
        if (SwingUtilities.isLeftMouseButton(e)) {
            Component jl = (Component) e.getSource();
            // 座標轉換
            Point newP = SwingUtilities.convertPoint(jl, e.getPoint(),
                    jl.getParent()); // 轉換座標系統
            jl.setLocation(jl.getX() + (newP.x - p.x), jl.getY()
                    + (newP.y - p.y)); // 設置標籤的新位置
            p = newP; // 更改座標點
            // jl.getParent().repaint();

        } else if (SwingUtilities.isRightMouseButton(e)) {// 右鍵操作

        } else if (SwingUtilities.isMiddleMouseButton(e)) {// 中鍵操作，一般鼠標沒有

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
            p = SwingUtilities.convertPoint(jl, e.getPoint(), jl.getParent()); // 得到當前座標點
            // jl.getParent().repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {// 右鍵操作

        } else if (SwingUtilities.isMiddleMouseButton(e)) {// 中鍵操作，一般鼠標沒有

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