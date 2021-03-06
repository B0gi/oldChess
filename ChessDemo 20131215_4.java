import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
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
	ControlPanelLeft leftControl;
	ControlPanelRight rightControl;
	private static int length = 950;
	private static int width = 450;
	private static Message message;
	//****	遊戲畫面	****//
	public void startGame(){
		message = new Message();
		Panel panelL = new Panel();
		Panel panelM = new Panel();
	    Panel panelR = new Panel();
	    frame = new Frame("Five");	//建立新的介面frame title為Five
		frame.setSize(length, width);	//設置介面的大小
		frame.setResizable(false);	//固定介面大小
		frame.setLocation(10, 10);	//設置介面出現位置
		board = new ChessBoard();	//新增棋盤
		leftControl = new ControlPanelLeft();	//新增 左邊的控制鍵
		rightControl = new ControlPanelRight();	//新增 右邊的控制鍵
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
	public Message getMessage(){
		return message;
	}
	public ControlPanelLeft getControlPanelLeft(){
		return leftControl;
	}
}

class Player{
	int[] color = new int[2];
	int player = 1;
	int step = 0;
	boolean setColor = false;
	
	public void reset(){
		setColor = false;
	}
	public void changePlayer(){
		if(player == 1)  player = 2;
		else if(player == 2) player = 1;
	}
	public void setColor(int theColor){
		if(setColor) return;
		if(theColor == 0)  {color[0] = 0; color[1] = 1;}
		else if(theColor == 1)  {color[0] = 1; color[1] = 0;}
		setColor = true;
	}
	public void addStep(){
		step++;
	}
	public void decStep(){
		step--;
	}
	public int getPlayer(){
		return player;
	}
	public int getColor(){
		return color[player-1];
	}
	public String getSColor(){
		if(color[player-1] == 0) return "黑方";
		else return "紅方";
	}
	public int getStep(){
		return step;
	}
	public boolean getSetColor(){
		return setColor;
	}
	public boolean check(Chess chess){
		if(chess.getColor() == color[player-1])
			return true; 
		return false;
	}
}

class Coordinate{
	Chess A;
	Chess B;
	int X1;
	int Y1;
	int X2;
	int Y2;
	
	public Coordinate(Chess A, Chess B, int X1, int Y1, int X2, int Y2){
		this.A = A;
		this.B = B;
		this.X1 = X1;
		this.Y1 = Y1;
		this.X2 = X2;
		this.Y2 = Y2;
	}
	
	public Chess getA(){
		return A;
	}
	public Chess getB(){
		return B;
	}
	public int getX1(){
		return X1;
	}
	public int getY1(){
		return Y1;
	}
	public int getX2(){
		return X2;
	}
	public int getY2(){
		return Y2;
	}
}

class Regret{
	ArrayList<Coordinate> regret = new ArrayList<Coordinate>();
	
	public Coordinate pop(){
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		ChessAction action = board.getChessAction();
		Player player = action.getPlayer();
		if(regret.isEmpty()) return null;
		int index = regret.size()-1;
		if(index == 0) {
			player.reset();
		}
		return regret.remove(index);
	}
	public void add(Coordinate c){
		regret.add(c);
	}
}

class ChessAction{
	int ROWS = 4;
	int COLS = 8; 
	int step = 0;
	int tmpX, tmpY;
	boolean gameStatus;
	Player player = new Player();
	Regret regret = new Regret();
	Coordinate coordinate;
	Chess A;
	Chess B;
	
	public void check(int X, int Y){
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		Message message = mainFrame.getMessage();
		if(X < COLS && Y < ROWS){
			if(!board.getChess(X, Y).getState()){
				board.getChess(X, Y).setState(true);
				step = 0;
				player.setColor(board.getChess(X, Y).getColor());
				player.changePlayer();
				player.addStep();
				System.out.println("翻開("+ X +","+ Y +"):"+ board.getChess(X, Y).toString());
				message.setText(player.getSColor() +" step"+ (player.getStep()+1)/2 +": 翻開("+ X +","+ Y +"):"+ board.getChess(X, Y).toString() +"\n");
				coordinate = new Coordinate(null, null, X, Y, -1, -1);
				regret.add(coordinate);
			}else{
				if(board.hasChess(X ,Y)){
					if(step == 0 || A.getColor() == board.getChess(X ,Y).getColor()){
						A = board.getChess(X ,Y);
						if(!player.check(A)) return;
						tmpX = X;
						tmpY = Y;
						step = 1;
					}else if(step == 1 && move(tmpX, tmpY, X, Y)){
						B = board.getChess(X ,Y);
						if(eat(tmpX, tmpY, X, Y)){
							board.chess[X][Y] = A;
							board.hasChess[tmpX][tmpY] = false;
							B.setDie(true);
							step = 0;
							player.changePlayer();
							player.addStep();
							System.out.println("("+ tmpX +","+ tmpY +"):"+ A.toString() +" 吃 ("+ X +","+ Y +"):"+B.toString());
							message.setText(player.getSColor() +" step"+ (player.getStep()+1)/2 +":"+ A.toString() +"("+ tmpX +","+ tmpY +"): 吃 ("+ X +","+ Y +"):"+B.toString() +"\n");
							coordinate = new Coordinate(A, B, tmpX, tmpY, X, Y);
							regret.add(coordinate);
						}else{
							message.setText(A.toString() +"("+ tmpX +","+ tmpY +"): 不能吃 ("+ X +","+ Y +"):"+B.toString() +"\n");
						}
					}else{
						message.setText(A.toString() +"("+ tmpX +","+ tmpY +"): 不能這樣吃 ("+ X +","+ Y +"):"+board.getChess(X, Y).toString() +"\n");
					}
				}else if(step == 1 && move(tmpX, tmpY, X, Y)){
					board.hasChess[X][Y] = true;
					board.chess[X][Y] = A;
					board.hasChess[tmpX][tmpY] = false;
					step = 0;
					player.changePlayer();
					player.addStep();
					System.out.println("("+ tmpX +","+ tmpY +"):"+ A.toString() +" 移動到 ("+ X +","+ Y +")");
					message.setText(player.getSColor() +" step"+ (player.getStep()+1)/2 +":"+ A.toString() +"("+ tmpX +","+ tmpY +"): 移動到 ("+ X +","+ Y +")\n");
					coordinate = new Coordinate(A, null, tmpX, tmpY, X, Y);
					regret.add(coordinate);
				}else{
					message.setText(A.toString() +"("+ tmpX +","+ tmpY +"): 不能移動到 ("+ X +","+ Y +")\n");
				}
			}
			board.paint(board.getGraphics());
			if(win()){
				setGameStatus(false);
			}
		}
	}

	public boolean eat(int X1, int Y1, int X2, int Y2){
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		int priorityA = A.getPriority();
		int priorityB = B.getPriority();
		int colorA = A.getColor();
		int colorB = B.getColor();
		if(colorA == colorB){
			new HasChess("吃");
			return false;
		}
		if(priorityA == 1) {
			int count = 0;
			if(X1 < X2){
				for(int i=X1;i<X2;i++) 
					if(board.hasChess[i][Y2])	count++;
			}else if (X1 > X2){
				for(int i=X1;i>X2;i--) 
					if(board.hasChess[i][Y2])	count++;
			}else if (Y1 < Y2){
				for(int i=Y1;i<Y2;i++) 
					if(board.hasChess[X2][i])	count++;
			}else if (Y1 > Y2){
				for(int i=Y1;i>Y2;i--) 
					if(board.hasChess[X2][i])	count++;
			}
			if(count == 2) return true;
		}else if(priorityA == 0 && priorityB == 6){
			return true;
		}else if(priorityA == 6 && priorityB == 0){
			new HasChess("吃");
			return false;
		}else if(priorityA >= priorityB){
			return true;
		}
		new HasChess("吃");
		return false;
	}
	
	public boolean move(int X1, int Y1, int X2, int Y2){
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		if(A.getPriority() == 1 && board.hasChess[X2][Y2]){
			if((X1 > X2 || X1 < X2) && Y1 == Y2) return true;
			if((Y1 > Y2 || Y1 < Y2) && X1 == X2) return true;
		}else{
			if((X1+1 == X2 || X1-1 == X2) && Y1 == Y2) return true;
			if((Y1+1 == Y2 || Y1-1 == Y2) && X1 == X2) return true;
		}
		new HasChess("移動");
		return false;
	}
	
	public void regret(){
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		Message message = mainFrame.getMessage();
		if(!getGameStatus()) return;
		Coordinate c = regret.pop();
		if(c == null)	return;
		Chess A = c.getA();
		Chess B = c.getB();
		int X1 = c.getX1();
		int Y1 = c.getY1();
		int X2 = c.getX2();
		int Y2 = c.getY2();
		if(A == null){
			message.setText("!悔棋! 翻開("+ X1 +","+ Y1 +"):"+ board.getChess(X1, Y1).toString() +"\n");
			board.getChess(X1, Y1).setState(false);
			player.decStep();
			player.changePlayer();
		}else if(B == null){
			message.setText("!悔棋! "+ A.toString() +"("+ X1 +","+ Y1 +"): 移動到 ("+ X2 +","+ Y2 +")\n");
			board.hasChess[X2][Y2] = false;
			board.chess[X1][Y1] = A;
			board.hasChess[X1][Y1] = true;
			player.changePlayer();
			player.decStep();
		}else{
			message.setText("!悔棋! "+ A.toString() +"("+ X1 +","+ Y1 +"): 吃 ("+ X2 +","+ Y2 +"):"+B.toString() +"\n");
			board.chess[X1][Y1] = A;
			board.hasChess[X1][Y1] = true;
			board.chess[X2][Y2] = B;
			board.hasChess[X2][Y2] = true;
			B.setDie(false);
			player.changePlayer();
			player.decStep();
		}
		board.paint(board.getGraphics());
	}
	
	public void setGameStatus(boolean theGameStatus){
		this.gameStatus = theGameStatus;
	}
	public boolean getGameStatus(){
		return gameStatus;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public boolean win(){
		MainFrame mainFrame = new MainFrame();
		ChessBoard board = mainFrame.getBoard();
		AllChess allChess = board.getAllChess();
		int count;
		
		for(int j = 0; j < 2; j++){
			count = 16;
			for(int i=0; i<16; i++){
				Chess chess = allChess.getChess(j, i);
				if(chess.isDie()==true){
					count--;
				}
			}
			if(count==0){
				new Win(j);
				return true;
			}
		}
		return false;
	}
}

//****	棋盤	****//
class ChessBoard extends JComponent{
	private final int MARGIN=20;
	private final int GRID_SPAN=90;
	protected static int ROWS=4;
	protected static int COLS=8;
	boolean[][] hasChess = new boolean[COLS][ROWS];
	public Chess[][] chess = new Chess[COLS][ROWS];
	public AllChess allChess = new AllChess();
	public ChessAction chessAction = new ChessAction();
	
	public ChessBoard(){
		addMouseListener(new MouseClicked());
		setBoard();
	}
	
	//****	set	****//
	public void setBoard(){
		chessAction.setGameStatus(true);
		for(int i = 0; i < COLS; i++)
			for (int j = 0; j < ROWS; j++)
				hasChess[i][j] = true;
		
		for(int i = 0; i < COLS; i++){
			for (int j = 0; j < ROWS;){
				int X = (int) (Math.random()*2);
				int Y = (int) (Math.random()*16);
				Chess tmp = allChess.getChess(X, Y);
				if(tmp.isDie()){
					tmp.setDie(false);
					chess[i][j] = tmp;
					j++;
				}
			}
		}
		
		for (int j = 0; j < ROWS; j++){
			for(int i = 0; i < COLS; i++){
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
		g.setColor(Color.BLACK);
		for(int i=0;i<=ROWS;i++){  
	        g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);  
	    }  
	    for(int i=0;i<=COLS;i++){  
	        g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+ROWS*GRID_SPAN);  
	    }
	    putChess(g);
	}
	
	public void putChess(Graphics g){
	    for(int i = 0; i < COLS; i++){
	    	for(int j = 0; j < ROWS; j++){
	    		if(!hasChess[i][j]){
	    			g.drawImage(new ImageIcon("10.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
	    		}else if(!chess[i][j].getState()){
	    			g.drawImage(new ImageIcon("0.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
	    		}else{
	    			switch(chess[i][j].getColor()){
		    			case 0:
		    				if(chess[i][j].getIdentify() == 0){
		    					g.drawImage(new ImageIcon("1.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 1){
		    					g.drawImage(new ImageIcon("2.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 2){
		    					g.drawImage(new ImageIcon("3.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 3){
		    					g.drawImage(new ImageIcon("4.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 4){
		    					g.drawImage(new ImageIcon("5.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 5){
		    					g.drawImage(new ImageIcon("6.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else{
		    					g.drawImage(new ImageIcon("7.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}
		    				break;
		    			case 1:
		    				if(chess[i][j].getIdentify() == 0){
		    					g.drawImage(new ImageIcon("11.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 1){
		    					g.drawImage(new ImageIcon("12.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 2){
		    					g.drawImage(new ImageIcon("13.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 3){
		    					g.drawImage(new ImageIcon("14.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 4){
		    					g.drawImage(new ImageIcon("15.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else if(chess[i][j].getIdentify() == 5){
		    					g.drawImage(new ImageIcon("16.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}else{
		    					g.drawImage(new ImageIcon("17.jpg").getImage(),30+GRID_SPAN*i,30+GRID_SPAN*j,this);
		    				}
		    				break;
		    		}
	    		}
	    	}
		}
	}
}


class Chess {
	int priority; //權重
	int identify; //編號
	int color;
	String name;
	boolean isDie = true;
	boolean isOpen = false; //掀開了嗎~
	
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
	public void setState(boolean change){
		isOpen = change;
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
	
	/*判斷權重*/
	private int judgePriority(int identify){
		switch(identify){
		case 0: 
			this.priority = 6;
			if(color == 0){
				name = "將";
			} else
				name = "帥";
			break;
		case 1:
			this.priority = 5;
			if(color == 1){
				name = "士";
			} else
				name = "仕";
			break;
		case 2:
			this.priority = 4;
			if(color == 0){
				name = "象";
			} else
				name = "相";
			break;
		case 3:
			this.priority = 3;
			if(color == 0){
				name = "車";
			} else
				name = "庫";
			break;
		case 4:
			this.priority = 2;
			if(color == 0){
				name = "馬";
			} else
				name = "瑪";
			break;
		case 5:
			this.priority = 1;
			if(color == 0){
				name = "包";
			} else
				name = "炮";	
			break;
		case 6:
			this.priority = 0;
			if(color == 0){
				name = "卒";
			} else
				name = "兵";
			break;
		}
		return priority;
	}
	public String toString(){
		String s;
		//s = "棋: " + this.name + "\t編號： " + identify + "\t權重： " + priority +" \n";
		s = this.name;
		return s;
	}

}

class AllChess{
	Chess A1 = new Chess(0, 0);//將
	Chess A2 = new Chess(0, 1);//士
	Chess A3 = new Chess(0, 1);//士
	Chess A4 = new Chess(0, 2);//象
	Chess A5 = new Chess(0, 2);//象
	Chess A6 = new Chess(0, 3);//車
	Chess A7 = new Chess(0, 3);//車
	Chess A8 = new Chess(0, 4);//馬
	Chess A9 = new Chess(0, 4);//馬
	Chess A10 = new Chess(0, 5);//包
	Chess A11 = new Chess(0, 5);//包
	Chess A12 = new Chess(0, 6);//卒
	Chess A13 = new Chess(0, 6);//卒
	Chess A14 = new Chess(0, 6);//卒
	Chess A15 = new Chess(0, 6);//卒
	Chess A16 = new Chess(0, 6);//卒
	
	Chess B1 = new Chess(1, 0);//帥
	Chess B2 = new Chess(1, 1);//仕
	Chess B3 = new Chess(1, 1);//仕
	Chess B4 = new Chess(1, 2);//相
	Chess B5 = new Chess(1, 2);//相
	Chess B6 = new Chess(1, 3);//紅車
	Chess B7 = new Chess(1, 3);//紅車
	Chess B8 = new Chess(1, 4);//紅馬
	Chess B9 = new Chess(1, 4);//紅馬
	Chess B10 = new Chess(1, 5);//炮
	Chess B11 = new Chess(1, 5);//炮
	Chess B12 = new Chess(1, 6);//兵
	Chess B13 = new Chess(1, 6);//兵
	Chess B14 = new Chess(1, 6);//兵
	Chess B15 = new Chess(1, 6);//兵
	Chess B16 = new Chess(1, 6);//兵
	Chess[][] chess = 
		{{A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16},
		{B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12, B13, B14, B15, B16}};
	
	public Chess getChess(int color, int index){
		return chess[color][index];
	}
}

//****	左控制鍵	****//
class ControlPanelLeft extends Panel implements ActionListener {
    int tm_unit=200;
    int tm_sum =0;
    int sec=0;
    private JLabel color = new JLabel("");
    private JLabel steps = new JLabel("  步數");
    private JLabel time = new JLabel("  時間 = ");
    private Timer timer = new Timer(tm_unit, this);
    MainFrame mainFrame = new MainFrame();
	ChessBoard board = mainFrame.getBoard();
	ChessAction chessAction = board.getChessAction();
	Player player = chessAction.getPlayer();
    public ControlPanelLeft(){
    	timer.restart();
        setLayout(new GridLayout(20,1,10,30));
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
    	Graphics g = getGraphics();
    	if(!player.getSetColor()){
    		g.drawImage(new ImageIcon("10.jpg").getImage(),1,22,this);
    	}else if(player.getColor() == 0){
    		g.drawImage(new ImageIcon("1.jpg").getImage(),1,22,this);
    	}else if(player.getColor() == 1){
    		g.drawImage(new ImageIcon("11.jpg").getImage(),1,22,this);
    	}
    }
    private void steps_event(){
    	if(!player.getSetColor()){
    		steps.setText("  步數");
    	}else if(player.getColor() == 0){
    		steps.setText("  黑方步數 = "+ (player.getStep()+1)/2);
    	}else if(player.getColor() == 1){
    		steps.setText("  紅方步數 = "+ (player.getStep()+1)/2);
    	}
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
		Message message = mainFrame.getMessage();
		if(button == b0){
			frameRenew.dispose();
		}else if(button == b1){
			frameRenew.dispose();
			message.dispose();
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
	public Win(int color){
		Panel p = new Panel();
		chessAction.setGameStatus(false);
		win.setSize(200, 100);
		win.setResizable(false);
		win.setLocation(300, 300);
		if(color == 1)
			textWin = new JTextArea("黑 方 勝 !!");
		else if(color == 0)
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
	private Frame hasChess = new Frame("");
	private static JTextArea textHasChess = new JTextArea();
	private Button enter = new Button("確定");
	
	public HasChess(String s){
		hasChess.setSize(200, 100);
		hasChess.setResizable(false);
		hasChess.setLocation(300, 300);
		enter.addActionListener(this);
		textHasChess.setText("不能這樣"+ s +"!!");
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

class Message{
	Frame frame;
	JTextPane text;
	String s = "";
	public Message(){
		frame = new Frame();
		ScrollPane JSP = new ScrollPane();
		Panel panel = new Panel();
		text = new JTextPane();
	    frame = new Frame("Message");
		frame.setSize(200, 450);
		frame.setResizable(false);
		frame.setLocation(1000, 10);
		panel.add(text);
		JSP.add(panel);
		frame.add(JSP);
		
		frame.setVisible(true);
		frame.addWindowListener(new AdapterDemo());
	}
	
	public void setText(String s){
		this.s = this.s + s;
		text.setText(this.s);
	}

	public void dispose(){
		frame.dispose();
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

//****	叉叉關閉視窗		****//
class AdapterDemo extends WindowAdapter {
  public void windowClosing(WindowEvent e){
  	e.getWindow().dispose();
  }
}
