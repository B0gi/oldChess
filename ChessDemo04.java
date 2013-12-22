import java.awt.*;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class ChessDemo {
	public static void main(String[] args){
		MainFrame game = new MainFrame();
		game.startGame();	//�}�l�C��
	}
}

//****	���l�ѹC��	****//
class MainFrame {
	private static Frame frame;
	private static ChessBoard board;
	private static int length = 830;
	private static int width = 400;
	
	//****	�C���e��	****//
	public void startGame(){
		Panel panelL = new Panel();
		Panel panelM = new Panel();
	    Panel panelR = new Panel();
	    frame = new Frame("Five");	//�إ߷s������frame title��Five
		frame.setSize(length, width);	//�]�m�������j�p
		frame.setResizable(false);	//�T�w�����j�p
		frame.setLocation(10, 10);	//�]�m�����X�{��m
		board = new ChessBoard();	//�s�W�ѽL
		ControlPanelLeft leftControl = new ControlPanelLeft();	//�s�W ���䪺������
    	ControlPanelRight rightControl = new ControlPanelRight();	//�s�W �k�䪺������
		panelL.add(leftControl, BorderLayout.WEST);
		panelM.add(board, BorderLayout.CENTER);
		panelR.add(rightControl, BorderLayout.EAST);
		frame.add(panelL, BorderLayout.WEST);	//���䪺������ �[�줶������
		frame.add(board, BorderLayout.CENTER);	//�ѽL �[�줶������
		frame.add(panelR, BorderLayout.EAST);	//�k�䪺������ �[�줶���k��
		
		frame.setVisible(true);	//���farme����
		frame.addWindowListener(new AdapterDemo());	//���k�W���e�e������frame
	}

	public Frame getFrame(){
		return frame;
	}
	public ChessBoard getBoard(){
		return board;
	}
}

class ChessAction{
	int ROW = 4;
	int COL = 8; 
	boolean gameStatus;
	int player = 1;
	int step = 0;
	
	Chess A;
	Chess B;
	public void check(int X, int Y){
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		
		if(X < COL && Y < ROW){
			System.out.print(X +" "+ Y +"�G");
			System.out.println(board.getChess(X, Y).toString());
		}
		if(X < COL && Y < ROW){
			if(board.hasChess(X ,Y)){
				if(step == 0){
					A = board.getChess(X ,Y);
					step = 1;
					return;
				}else if(step == 1){
					B = board.getChess(X ,Y);
					step = 2;
				}
			}
		}
		if(step == 1){

		}
		if(step == 2){
			System.out.println(A.toString() +"eat"+ B.toString());
			step = 0;
		}
	}

	
	public boolean eat(Chess A, Chess B){
		return false;
	}
	
	public boolean move(){
		return false;
	}
	
	public void regret(){
		
	}
	
	public void setGameStatus(boolean theGameStatus){
		this.gameStatus = theGameStatus;
	}
	
	public int getPlayer(){
		return player;
	}
	public boolean getGameStatus(){
		return gameStatus;
	}
}

//****	�ѽL	****//
class ChessBoard extends JComponent{
	private final int MARGIN=20;
	private final int GRID_SPAN=75;
	protected static int ROWS=4;
	protected static int COLS=8;
	boolean[][] hasChess = new boolean[ROWS][COLS];
	public Chess[][] chess = new Chess[ROWS][COLS];
	public AllChess allChess = new AllChess();
	public ChessAction chessAction = new ChessAction();
	
	public ChessBoard(){
		addMouseListener(new MouseClicked());
		setBoard();
	}
	
	//****	set	****//
	public void setBoard(){
		chessAction.setGameStatus(true);
		for(int i = 0; i < ROWS; i++)
			for (int j = 0; j < COLS; j++)
				hasChess[i][j] = true;
		
		for(int i = 0; i < ROWS; i++){
			for (int j = 0; j < COLS;){
				int X = (int) (Math.random()*2);
				int Y = (int) (Math.random()*16);
				//System.out.println(X+" "+Y);
				Chess tmp = allChess.getChess(X, Y);
				if(tmp.isDie()){
					tmp.setDie(false);
					chess[i][j] = tmp;
					j++;
				}
			}
		}
		
		for(int i = 0; i < ROWS; i++){
			for (int j = 0; j < COLS; j++){
				System.out.printf("%3s",chess[i][j].toString());
			}
			System.out.println();
		}
	}
	
	//****	get	****//
	public int getMargin(){
		return MARGIN;
	}
	public int getGridSpan(){
		return GRID_SPAN;
	}
	public int getRows(){
		return ROWS;
	}
	public int getCols(){
		return COLS;
	}
	public Chess getChess(int X, int Y){
		return chess[Y][X];
	}
	public ChessAction getChessAction(){
		return chessAction;
	}
	
	
	public boolean hasChess(int X, int Y){
		if(hasChess[Y][X] == true)
			return true;
		else
			return false;
	}
	
	//****	�e�X�ѽL	****//
	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		for(int i=0;i<=ROWS;i++){  
	        g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);  
	    }  
	    for(int i=0;i<=COLS;i++){  
	        g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+ROWS*GRID_SPAN);  
	    }
	    g.drawImage(new ImageIcon("C01.jpg").getImage(),30,30+GRID_SPAN,this);
	    g.drawImage(new ImageIcon("C02.jpg").getImage(),30+GRID_SPAN*2,30+GRID_SPAN,this);
	    g.drawImage(new ImageIcon("C01.jpg").getImage(),30+GRID_SPAN,30+GRID_SPAN*2,this);
	    g.drawImage(new ImageIcon("C02.jpg").getImage(),30+GRID_SPAN,30+GRID_SPAN,this);
	}
}

//****	��������	****//
class ControlPanelLeft extends Panel implements ActionListener {  
    int tm_unit=200;
    int tm_sum =0;
    int sec=0;
    private JLabel color = new JLabel("    �¤l");
    private JLabel steps = new JLabel("  �U�l�� = ");
    private JLabel time = new JLabel("  �ɶ� = ");
    private Timer timer = new Timer(tm_unit, this);
    MainFrame mainFrame = new MainFrame();
	ChessBoard board = mainFrame.getBoard();
	ChessAction chessAction = board.getChessAction();
    public ControlPanelLeft(){   
    	timer.restart();
        setLayout(new GridLayout(14,1,10,20));
        add(new Label("                            ")); 
        add(color);
        add(new Label());
        add(new Label());
        add(new Label());
        add(steps);
        add(time);
    }
    public void paint(Graphics g){
    	super.paint(g);
    }
    private void player_event(){
    	if(chessAction.getPlayer() == 1)
    		color.setText("    �¤�U");
    	else
    		color.setText("    ����U");
    }
    private void steps_event(){
    	if(chessAction.getPlayer() == 1)
    		steps.setText("  �¤�� = ");
    	else
    		steps.setText("  ����� = ");
    }
    private void timer_event(){
		if ((tm_sum += tm_unit) >= 1000 && chessAction.getGameStatus() == true){
			tm_sum -= 1000;
			sec+=1;
			time.setText("  �ɶ�= " + sec +"s");
		}
    }
	public void actionPerformed(ActionEvent e){
			player_event();
			timer_event();
			steps_event();
	}
}

//****	�k������	****//
class ControlPanelRight extends Panel implements ActionListener {
	Button b0 = new Button("�]  �m");
    Button b1 = new Button("��  ��"); 
    Button b2 = new Button("��  �s");   
    Button b3 = new Button("��  �U");   
    Button b4 = new Button("��  �}"); 
    
    public ControlPanelRight(){
    	setLayout(new GridLayout(0, 1, 0, 5));
        add(new Label());
    	add(b0);
        add(new Label());     
        add(new Label());
        add(new Label());  
        add(b1);
        add(new Label());      
        add(new Label());   
        add(b2);   
        add(b3);   
        add(b4);   
        b0.addActionListener(this);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
    }
    
	public void actionPerformed(ActionEvent e){
		Button button = (Button) e.getSource();
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		ChessAction chessAction = board.getChessAction();
		
		if(button == b0){
			new Set();
		}else if(button == b1){
			if(chessAction.getGameStatus() == true)
				chessAction.regret();
		}else if(button == b2){
			new Renew();
		}else if(button == b3){
			new Help();
		}else if(button == b4){
			new Exit();
		}
	}
}

//****	�]�m�e��	****//
class Set implements ActionListener {
	Panel p0 = new Panel();
	Panel p1 = new Panel();
	Panel p2 = new Panel();
	Panel p3 = new Panel();
	Frame frameSet = new Frame("�]  �m");
	
	public Set(){
		frameSet.setSize(200, 100);
		frameSet.setResizable(false);
		frameSet.setLocation(300, 300);

		frameSet.setVisible(true);
		frameSet.addWindowListener(new AdapterDemo());
	}
	
	public void actionPerformed(ActionEvent e){
		Button button = (Button) e.getSource();
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		Frame frame = mainFrame.getFrame();
		
		
		frame.dispose();
		frameSet.dispose();
		mainFrame.startGame();
	}
}

//****	���s�}�l	****//
class Renew implements ActionListener {
	Panel p0 = new Panel();
	Panel p1 = new Panel();
	Panel p3 = new Panel();
	Frame frameRenew = new Frame("��  �s");
	Button b0 = new Button("��  ��");
	Button b1 = new Button("�T  �w");
	JTextArea text = new JTextArea("�T�w�n���s�}�l?");
	
	public Renew(){
		frameRenew.setSize(200, 100);
		frameRenew.setResizable(false);
		frameRenew.setLocation(300, 300);
		p0.add(b0);
		p1.add(b1);
		p3.add(text, BorderLayout.CENTER);
		b0.addActionListener(this);
		b1.addActionListener(this);
		frameRenew.add(p3, BorderLayout.NORTH);
		frameRenew.add(p0, BorderLayout.EAST);
		frameRenew.add(p1, BorderLayout.WEST);
		frameRenew.setVisible(true);
		frameRenew.addWindowListener(new AdapterDemo());
	}
	
	public void actionPerformed(ActionEvent e){
		Button button = (Button) e.getSource();
		MainFrame mainFrame = new MainFrame();
		Frame frame = mainFrame.getFrame();
		
		if(button == b0){
			frameRenew.dispose();
		}else if(button == b1){
			frameRenew.dispose();
			frame.dispose();
			mainFrame.startGame();
		}
	}
}

//****	���U�e��	****//
class Help implements ActionListener {
	Frame frameHelp = new Frame("��  �U");
	Button b0 = new Button("�T�w");
	JTextArea text = new JTextArea(
			" �Ѥl�ƶq�`�@32��A���B�¤�U16��F�C���ѽL�榡�� 4 x 8 �A\n" +
			" �@�}�l�Ѥl�һ\�ۡC�Ѥl���j�p�̧Ǭ��G\n" +
			" �� > �K > �� > �� > �X > �� > �L�F�N > �h > �H > �� > �� > �] > ��C\n" +
			" ���ѮɡA�Ҧ��Ѥl�ҥu�ઽ��(�ξ)�@�B��Ů�(�Υi�Y���Ѥl��m)�A\n" +
			" �u���b��\\�]�Y�ѮɡA�i�H���V�����@��(�w½�}�λ\�Ѭҥi)�A\n" +
			" ����\\�]�b���Y�Ѥl�����p�U�A��u�ઽ��(�ξ)�@�B�C");
	
	public Help(){
		frameHelp.setSize(400, 160);
		frameHelp.setResizable(false);
		frameHelp.setLocation(300, 300);
		b0.addActionListener(this);
		frameHelp.add(text, BorderLayout.NORTH);
		frameHelp.add(b0, BorderLayout.SOUTH);
		frameHelp.setVisible(true);
		frameHelp.addWindowListener(new AdapterDemo());
	}
	
	public void actionPerformed(ActionEvent e){
		Button button = (Button) e.getSource();
		
		if(button == b0){
			frameHelp.dispose();
		}
	}
}

//**** ���}�e��	****//
class Exit implements ActionListener {
	Panel p0 = new Panel();
	Panel p1 = new Panel();
	Panel p3 = new Panel();
	Frame frameExit = new Frame("��  �}");
	Button b0 = new Button("��  ��");
	Button b1 = new Button("�T  �w");
	JTextArea text = new JTextArea("�T�w�n���}?");
	
	public Exit(){
		frameExit.setSize(200, 100);
		frameExit.setResizable(false);
		frameExit.setLocation(300, 300);
		p0.add(b0);
		p1.add(b1);
		p3.add(text, BorderLayout.CENTER);
		b0.addActionListener(this);
		b1.addActionListener(this);
		frameExit.add(p3, BorderLayout.NORTH);
		frameExit.add(p0, BorderLayout.EAST);
		frameExit.add(p1, BorderLayout.WEST);
		frameExit.setVisible(true);
		frameExit.addWindowListener(new AdapterDemo());
	}
	
	public void actionPerformed(ActionEvent e){
		Button button = (Button) e.getSource();
		
		if(button == b0){
			frameExit.dispose();
		}else if(button == b1){
			System.exit(0);
		}
	}
}

//**** ��Ĺ�e�� ****//
class Win implements ActionListener {
	MainFrame mainFrame = new MainFrame();
	ChessBoard board = mainFrame.getBoard();
	ChessAction chessAction = board.getChessAction();
	private Frame win = new Frame("Win");
	private Button enter = new Button("�T�w");
	private JTextArea textWin = new JTextArea("");
	public Win(int status){
		Panel p = new Panel();
		chessAction.setGameStatus(false);
		win.setSize(200, 100);
		win.setResizable(false);
		win.setLocation(300, 300);
		if(status == 0)
			textWin = new JTextArea("!! �M      �� !!");
		else if(status == 1)
			textWin = new JTextArea("�� �� �� !!");
		else
			textWin = new JTextArea("�� �� �� !!");
		
		p.add(textWin, BorderLayout.CENTER);
		enter.addActionListener(this);
		win.add(p, BorderLayout.NORTH);
		win.add(enter, BorderLayout.SOUTH);
		win.setVisible(true);
		win.addWindowListener(new AdapterDemo());
	}

	public void actionPerformed(ActionEvent e){
		Button button = (Button) e.getSource();
		
		if(button == enter){
			win.dispose();
		}
	}
}

//**** �Ѥl���ʿ��~�e�� ****//
class HasChess implements ActionListener {
	ChessBoard game = new ChessBoard();
	private Frame hasChess = new Frame("");
	private static JTextArea textHasChess = new JTextArea("����o�˲���!!");
	private Button enter = new Button("�T�w");
	
	public HasChess(){
		hasChess.setSize(200, 100);
		hasChess.setResizable(false);
		hasChess.setLocation(300, 300);
		enter.addActionListener(this);
		hasChess.add(textHasChess, BorderLayout.NORTH);
		hasChess.add(enter, BorderLayout.SOUTH);
		hasChess.setVisible(true);
		hasChess.addWindowListener(new AdapterDemo());
	}
	
	public void actionPerformed(ActionEvent e){
		Button button = (Button) e.getSource();
		
		if(button == enter){
			hasChess.dispose();
		}
	}
}

//****	�����ƹ��I��	****//
class MouseClicked extends MouseAdapter {
	private int X;
	private int Y;
	public MouseClicked(){}
		
	public void mousePressed(MouseEvent e){
		/*
	    System.out.print("screen x: " + e.getXOnScreen());
	    System.out.print(", y: " + e.getYOnScreen() + "\n");
	    */
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		ChessAction chessAction = board.getChessAction();
	    if (e.getButton() == MouseEvent.BUTTON1){
	        //System.out.println("left button");
	        X = (e.getX()-board.getMargin())/board.getGridSpan();
	        Y = (e.getY()-board.getMargin())/board.getGridSpan();
	        chessAction.check(X, Y);	//�ˬd
	    }
	    if (e.getButton() == MouseEvent.BUTTON2){
	        System.out.println("middle button");
	    }
	    if (e.getButton() == MouseEvent.BUTTON3){
	        System.out.println("right button");
	    }
	    /*
	    System.out.println("mouse position: " + e.getPoint());
	    System.out.println("mouse screen position: " + e.getLocationOnScreen());
	    System.out.println("mouse clicks: " + e.getClickCount());
	    */
	}
}	
class Chess {
	int priority; //�v��
	int identify; //�s��
	int color;
	String name;
	boolean isDie = true;
	boolean isOpen = false; //�ȶ}�F��~
	
	public Chess(int color, int identify){
		setColor(color);
		setIdentify(identify);
		setPriority();
	}
	
	/* Set Methods */
	public void setColor(int color){
		this.color = color;
	}
	public void setIdentify(int identify){
		this.identify = identify;
	}
	public void setPriority(){
		this.priority = judgePriority(identify);
	}
	public void setDie(boolean change){
		this.isDie = change;
	}
	public void setState(){
		isOpen = true;
	}
	
	/* Get Methods */
	public int getColor(){
		return color;
	}
	public int getPriority(){
		return priority;
	}
	public int getIdentify(){
		return identify;
	}
	public boolean isDie(){
		return isDie;
	}
	public boolean getState(){
		return isOpen;
	}
	
	/*�P�_�v��*/
	private int judgePriority(int identify){
		switch(identify){
		case 0: 
			this.priority = 6;
			if(color == 0){
				name = "�N";
			} else
				name = "��";
			break;
		case 1:
			this.priority = 5;
			if(color == 1){
				name = "�h";
			} else
				name = "�K";
			break;
		case 2:
			this.priority = 4;
			if(color == 0){
				name = "�H";
			} else
				name = "��";
			break;
		case 3:
			this.priority = 3;
			if(color == 0){
				name = "��";
			} else
				name = "�w";
			break;
		case 4:
			this.priority = 2;
			if(color == 0){
				name = "��";
			} else
				name = "��";
			break;
		case 5:
			this.priority = 1;
			if(color == 0){
				name = "�]";
			} else
				name = "��";	
			break;
		case 6:
			this.priority = 0;
			if(color == 0){
				name = "��";
			} else
				name = "�L";
			break;
		}
		return priority;
	}
	public String toString(){
		String s;
		//s = "��: " + this.name + "\t�s���G " + identify + "\t�v���G " + priority +" \n";
		s = this.name;
		return s;
	}

}

class AllChess{
	Chess A1 = new Chess(0, 0);//�N
	Chess A2 = new Chess(0, 1);//�h
	Chess A3 = new Chess(0, 1);//�h
	Chess A4 = new Chess(0, 2);//�H
	Chess A5 = new Chess(0, 2);//�H
	Chess A6 = new Chess(0, 3);//��
	Chess A7 = new Chess(0, 3);//��
	Chess A8 = new Chess(0, 4);//��
	Chess A9 = new Chess(0, 4);//��
	Chess A10 = new Chess(0, 5);//�]
	Chess A11 = new Chess(0, 5);//�]
	Chess A12 = new Chess(0, 6);//��
	Chess A13 = new Chess(0, 6);//��
	Chess A14 = new Chess(0, 6);//��
	Chess A15 = new Chess(0, 6);//��
	Chess A16 = new Chess(0, 6);//��
	
	Chess B1 = new Chess(1, 0);//��
	Chess B2 = new Chess(1, 1);//�K
	Chess B3 = new Chess(1, 1);//�K
	Chess B4 = new Chess(1, 2);//��
	Chess B5 = new Chess(1, 2);//��
	Chess B6 = new Chess(1, 3);//����
	Chess B7 = new Chess(1, 3);//����
	Chess B8 = new Chess(1, 4);//����
	Chess B9 = new Chess(1, 4);//����
	Chess B10 = new Chess(1, 5);//��
	Chess B11 = new Chess(1, 5);//��
	Chess B12 = new Chess(1, 6);//�L
	Chess B13 = new Chess(1, 6);//�L
	Chess B14 = new Chess(1, 6);//�L
	Chess B15 = new Chess(1, 6);//�L
	Chess B16 = new Chess(1, 6);//�L
	Chess[][] chess = 
		{{A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16},
		{B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12, B13, B14, B15, B16}};

	public Chess getChess(int color, int index){
		return chess[color][index];
	}
}


//****	�e�e��������		****//
class AdapterDemo extends WindowAdapter {
  public void windowClosing(WindowEvent e){
  	e.getWindow().dispose();
  }
}
