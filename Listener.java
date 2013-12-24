import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

//****	主視窗	****//
public class ABC extends Frame{	
	public static void main(String[] args) {
		Frame frame = new Frame("ABC");	//new 視窗
		frame.setLayout(new FlowLayout());	//設定排版
		frame.setLocation(10, 10);	//設置介面出現位置
		frame.setSize(200, 120);	//設定視窗大小
		frame.setResizable(false);	//固定視窗大小
		TheButton button = new TheButton();	//new 按鍵
		frame.add(button);	//TheButton 加到視窗
		frame.setVisible(true);	//設定視窗顯示為 true
		frame.addWindowListener(new AdapterDemo());	//叉叉關閉視窗
	}
}

//****	開關	****//
class TheButton extends JPanel implements ActionListener{	
	String On_Off = "Off";	//預設開關為off
	JLabel label = new JLabel();	//開關下顯示的文字
	Button button = new Button("ON"); //預設開關名稱為ON
	public TheButton(){
		setLayout(new GridLayout(0,1,0,5));	//設定排版 
		JPanel p = new JPanel();	//new JPanel
		label.setText("off"); //顯示預設為off
		p.add(label);	//label加入JPanel
		add(button);	//extends JPanel 所以直接add 加入按鈕
		add(p);	//同上  加入label
		button.addActionListener(this);	//設定傾聽者
	}
	
	//傾聽者
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand() == "OFF"){
			On_Off = "Off";
			label.setText("Off");
			button.setLabel("ON");
		}else if(arg0.getActionCommand() == "ON"){
			On_Off = "On";
			label.setText("On");
			button.setLabel("OFF");
		}
	}
}

//****	叉叉關閉視窗		****//
class AdapterDemo extends WindowAdapter {
	public void windowClosing(WindowEvent e){
		System.exit(0);
	}
}