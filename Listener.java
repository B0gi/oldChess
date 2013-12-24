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

//****	�D����	****//
public class ABC extends Frame{	
	public static void main(String[] args) {
		Frame frame = new Frame("ABC");	//new ����
		frame.setLayout(new FlowLayout());	//�]�w�ƪ�
		frame.setLocation(10, 10);	//�]�m�����X�{��m
		frame.setSize(200, 120);	//�]�w�����j�p
		frame.setResizable(false);	//�T�w�����j�p
		TheButton button = new TheButton();	//new ����
		frame.add(button);	//TheButton �[�����
		frame.setVisible(true);	//�]�w������ܬ� true
		frame.addWindowListener(new AdapterDemo());	//�e�e��������
	}
}

//****	�}��	****//
class TheButton extends JPanel implements ActionListener{	
	String On_Off = "Off";	//�w�]�}����off
	JLabel label = new JLabel();	//�}���U��ܪ���r
	Button button = new Button("ON"); //�w�]�}���W�٬�ON
	public TheButton(){
		setLayout(new GridLayout(0,1,0,5));	//�]�w�ƪ� 
		JPanel p = new JPanel();	//new JPanel
		label.setText("off"); //��ܹw�]��off
		p.add(label);	//label�[�JJPanel
		add(button);	//extends JPanel �ҥH����add �[�J���s
		add(p);	//�P�W  �[�Jlabel
		button.addActionListener(this);	//�]�w��ť��
	}
	
	//��ť��
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

//****	�e�e��������		****//
class AdapterDemo extends WindowAdapter {
	public void windowClosing(WindowEvent e){
		System.exit(0);
	}
}