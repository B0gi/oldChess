import java.awt.*;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class ChessDemo {
	public static void main(String[] args){
		MainFrame game = new MainFrame();
		game.startGame();	//開始遊戲
	}
}

//****	五子棋遊戲	****//
class MainFrame {
	private static Frame frame;
	private static ChessBoard board;
	private static int length = 830;
	private static int width = 400;
	
	//****	遊戲畫面	****//
	public void startGame(){
		Panel panelL = new Panel();
		Panel panelM = new Panel();
	    Panel panelR = new Panel();
	    frame = new Frame("Five");	//建立新的介面frame title為Five
		frame.setSize(length, width);	//設置介面的大小
		frame.setResizable(false);	//固定介面大小
		frame.setLocation(10, 10);	//設置介面出現位置
		board = new ChessBoard();	//新增棋盤
		ControlPanelLeft leftControl = new ControlPanelLeft();	//新增 左邊的控制鍵
    	ControlPanelRight rightControl = new ControlPanelRight();	//新增 右邊的控制鍵
		panelL.add(leftControl, BorderLayout.WEST);
		panelM.add(board, BorderLayout.CENTER);
		panelR.add(rightControl, BorderLayout.EAST);
		frame.add(panelL, BorderLayout.WEST);	//左邊的控制鍵 加到介面左邊
		frame.add(board, BorderLayout.CENTER);	//棋盤 加到介面中央
		frame.add(panelR, BorderLayout.EAST);	//右邊的控制鍵 加到介面右邊
		
		frame.setVisible(true);	//顯示farme介面
		frame.addWindowListener(new AdapterDemo());	//按右上角叉叉能關閉frame
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
			System.out.print(X +" "+ Y +"：");
			System.out.println(board.getChess(Y, X).toString());
		}
		/*
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		AllChess allChess = board.getAllChess();
		if(X < COL && Y < ROW){
			if(board.hasChess(X ,Y)){
				if(step == 0)
					System.out.println(X+" "+Y);
					A = allChess.getChess(X, Y);
					step = 1;
				}else if(step == 1){
					B = allChess.getChess(X, Y);
					step = 2;
				}
			}
		if(step == 1){
			A.set(X ,Y);
		}
		if(step == 2){
			eat(A, B);
		}	
		*/
	}

	
	public boolean eat(Chess A, Chess B){
		if(move(A.getRow(), A.getCol(), B.getRow(), B.getCol())){
			if(A.getPower() <= B.getPower())
				return true;
			else
				return false;
		}return false;
	}
	
	public boolean move(int ARow, int ACol, int BRow, int BCol){
		
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

//****	棋盤	****//
class ChessBoard extends JComponent{
	private final int MARGIN=20;
	private final int GRID_SPAN=75;
	protected static int ROWS=4;
	protected static int COLS=8;
	boolean[][] hasChess = new boolean[ROWS][COLS];
	Chess[][] chess = new Chess[ROWS][COLS];
	AllChess allChess = new AllChess();
	ChessAction chessAction = new ChessAction();
	
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
			for (int j = 0; j < COLS; j++){
				int X = (int) (Math.random()*2);
				int Y = (int) (Math.random()*16);
				//System.out.println(X+" "+Y);
				Chess tmp = allChess.getChess(X, Y);
				if(tmp.getDie()){
					tmp.setdie(false);
					chess[i][j] = tmp;
				}else j--;
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
		return chess[X][Y];
	}
	public ChessAction getChessAction(){
		return chessAction;
	}
	public AllChess getAllChess(){
		return allChess;
	}
	
	
	public boolean hasChess(int X, int Y){
		if(hasChess[X][Y] == true)
			return true;
		else
			return false;
	}
	
	//****	畫出棋盤	****//
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.BLACK);
		for(int i=0;i<=ROWS;i++){  
	        g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);  
	    }  
	    for(int i=0;i<=COLS;i++){  
	        g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+ROWS*GRID_SPAN);  
	    }
	}
}

//****	左控制鍵	****//
class ControlPanelLeft extends Panel implements ActionListener {  
    int tm_unit=200;
    int tm_sum =0;
    int sec=0;
    private JLabel color = new JLabel("    黑子");
    private JLabel steps = new JLabel("  下子數 = ");
    private JLabel time = new JLabel("  時間 = ");
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
    		color.setText("    黑方下");
    	else
    		color.setText("    紅方下");
    }
    private void steps_event(){
    	if(chessAction.getPlayer() == 1)
    		steps.setText("  黑方數 = ");
    	else
    		steps.setText("  紅方數 = ");
    }
    private void timer_event(){
		if ((tm_sum += tm_unit) >= 1000 && chessAction.getGameStatus() == true){
			tm_sum -= 1000;
			sec+=1;
			time.setText("  時間= " + sec +"s");
		}
    }
	public void actionPerformed(ActionEvent e){
			player_event();
			timer_event();
			steps_event();
	}
}

//****	右控制鍵	****//
class ControlPanelRight extends Panel implements ActionListener {
	Button b0 = new Button("設  置");
    Button b1 = new Button("悔  棋"); 
    Button b2 = new Button("重  新");   
    Button b3 = new Button("幫  助");   
    Button b4 = new Button("離  開"); 
    
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

//****	設置畫面	****//
class Set implements ActionListener {
	Panel p0 = new Panel();
	Panel p1 = new Panel();
	Panel p2 = new Panel();
	Panel p3 = new Panel();
	Frame frameSet = new Frame("設  置");
	
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

//****	重新開始	****//
class Renew implements ActionListener {
	Panel p0 = new Panel();
	Panel p1 = new Panel();
	Panel p3 = new Panel();
	Frame frameRenew = new Frame("重  新");
	Button b0 = new Button("取  消");
	Button b1 = new Button("確  定");
	JTextArea text = new JTextArea("確定要重新開始?");
	
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

//****	幫助畫面	****//
class Help implements ActionListener {
	Frame frameHelp = new Frame("幫  助");
	Button b0 = new Button("確定");
	JTextArea text = new JTextArea(
			" 棋子數量總共32支，紅、黑方各16支；遊戲棋盤格式為 4 x 8 ，\n" +
			" 一開始棋子皆蓋著。棋子的大小依序為：\n" +
			" 帥 > 仕 > 相 > 車 > 傌 > 炮 > 兵；將 > 士 > 象 > 車 > 馬 > 包 > 卒。\n" +
			" 走棋時，所有棋子皆只能直走(或橫移)一步到空格(或可吃的棋子位置)，\n" +
			" 只有在炮\\包吃棋時，可以跳越中間一格(已翻開或蓋棋皆可)，\n" +
			" 但炮\\包在不吃棋子的情況下，亦只能直走(或橫移)一步。");
	
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

//**** 離開畫面	****//
class Exit implements ActionListener {
	Panel p0 = new Panel();
	Panel p1 = new Panel();
	Panel p3 = new Panel();
	Frame frameExit = new Frame("離  開");
	Button b0 = new Button("取  消");
	Button b1 = new Button("確  定");
	JTextArea text = new JTextArea("確定要離開?");
	
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

//**** 輸贏畫面 ****//
class Win implements ActionListener {
	MainFrame mainFrame = new MainFrame();
	ChessBoard board = mainFrame.getBoard();
	ChessAction chessAction = board.getChessAction();
	private Frame win = new Frame("Win");
	private Button enter = new Button("確定");
	private JTextArea textWin = new JTextArea("");
	public Win(int status){
		Panel p = new Panel();
		chessAction.setGameStatus(false);
		win.setSize(200, 100);
		win.setResizable(false);
		win.setLocation(300, 300);
		if(status == 0)
			textWin = new JTextArea("!! 和      局 !!");
		else if(status == 1)
			textWin = new JTextArea("黑 方 勝 !!");
		else
			textWin = new JTextArea("紅 方 勝 !!");
		
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

//**** 棋子移動錯誤畫面 ****//
class HasChess implements ActionListener {
	ChessBoard game = new ChessBoard();
	private Frame hasChess = new Frame("");
	private static JTextArea textHasChess = new JTextArea("不能這樣移動!!");
	private Button enter = new Button("確定");
	
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

//****	接收滑鼠點擊	****//
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
	        chessAction.check(X, Y);	//檢查
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




class AllChess{
	Chess1 A1 = new Chess1();//將
	Chess2 A2 = new Chess2();//士
	Chess2 A3 = new Chess2();//士
	Chess3 A4 = new Chess3();//象
	Chess3 A5 = new Chess3();//象
	Chess4 A6 = new Chess4();//車
	Chess4 A7 = new Chess4();//車
	Chess5 A8 = new Chess5();//馬
	Chess5 A9 = new Chess5();//馬
	Chess6 A10 = new Chess6();//包
	Chess6 A11 = new Chess6();//包
	Chess0 A12 = new Chess0();//卒
	Chess0 A13 = new Chess0();//卒
	Chess0 A14 = new Chess0();//卒
	Chess0 A15 = new Chess0();//卒
	Chess0 A16 = new Chess0();//卒
	
	Chess1 B1 = new Chess1();//帥
	Chess2 B2 = new Chess2();//仕
	Chess2 B3 = new Chess2();//仕
	Chess3 B4 = new Chess3();//相
	Chess3 B5 = new Chess3();//相
	Chess4 B6 = new Chess4();//紅車
	Chess4 B7 = new Chess4();//紅車
	Chess5 B8 = new Chess5();//紅馬
	Chess5 B9 = new Chess5();//紅馬
	Chess6 B10 = new Chess6();//炮
	Chess6 B11 = new Chess6();//炮
	Chess0 B12 = new Chess0();//兵
	Chess0 B13 = new Chess0();//兵
	Chess0 B14 = new Chess0();//兵
	Chess0 B15 = new Chess0();//兵
	Chess0 B16 = new Chess0();//兵
	Chess[][] chess = 
		{{A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16},
		{B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12, B13, B14, B15, B16}};
	
	public AllChess(){
		//黑色：0
		//紅色：1
		for(int i = 0; i < 16; i++){
			chess[0][i].setColor(1);
			chess[1][i].setColor(2);
		}

	}
	public Chess getChess(int color, int index){
		return chess[color][index];
	}
}

class Chess{
	int color;
	int power;
	int row;
	int col;
	boolean die = true;
	boolean see = false;
	
	public Chess(){}
	
	public void set(int theRow, int theCol){
		this.row = theRow;
		this.col = theCol;
	}
	public void setColor(int theColor){
		this.color = theColor;
	}
	public void setSee(){
		this.see = true;
	}
	public void setdie(boolean change){
		this.die = change;
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
	public int getPower(){
		return power;
	}
	public boolean getSee(){
		return see;
	}
	public boolean getDie(){
		return die;
	}
	
}
class Chess1 extends Chess{
	public Chess1(){
		power = 1;
	}
	public String toString(){
		if(color == 1)
			return "將";
		else
			return "帥";
	}
}
class Chess2 extends Chess{
	public Chess2(){
		power = 2;
	}
	
	public String toString(){
		if(color == 1)
			return "士";
		else
			return "仕";
	}
}
class Chess3 extends Chess{
	public Chess3(){
		power = 3;
	}
	public String toString(){
		if(color == 1)
			return "象";
		else
			return "相";
	}
}
class Chess4 extends Chess{
	public Chess4(){
		power = 4;
	}
	public String toString(){
		if(color == 1)
			return "車";
		else
			return "紅車";
	}
}
class Chess5 extends Chess{
	public Chess5(){
		power = 5;
	}
	public String toString(){
		if(color == 1)
			return "馬";
		else
			return "紅馬";
	}
}
class Chess6 extends Chess{
	public Chess6(){
		power = 6;
	}
	public String toString(){
		if(color == 1)
			return "包";
		else
			return "炮";
	}
}
class Chess0 extends Chess{
	public Chess0(){
		power = 0;
	}
	public String toString(){
		if(color == 1)
			return "卒";
		else
			return "兵";
	}
}


//****	叉叉關閉視窗		****//
class AdapterDemo extends WindowAdapter {
  public void windowClosing(WindowEvent e){
  	e.getWindow().dispose();
  }
}
