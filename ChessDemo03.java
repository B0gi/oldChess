public class ChessDemo {
	public static void main(String[] args) {
		Chess[] chess = new Chess[5];
		
		chess[0] = new Chess(0, 9);
		chess[1] = new Chess(1, 9);
		chess[2] = new Chess(0, 14);
		chess[3] = new Chess(1, 8);
		chess[4] = new Chess(0, 0);
		
		for(Chess s:chess){
			System.out.println(s);
		}
		
	}
}


/****	棋子	****/
class Chess {
	int priority; //權重
	int identify; //編號
	int color;
	String name;
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
		case 1: case 2:
			this.priority = 5;
			if(color == 1){
				name = "士";
			} else
				name = "仕";
			break;
		case 3: case 4:
			this.priority = 4;
			if(color == 0){
				name = "象";
			} else
				name = "相";
			break;
		case 5: case 6:
			this.priority = 3;
			if(color == 0){
				name = "車";
			} else
				name = "俥";
			break;
		case 7: case 8:
			this.priority = 2;
			if(color == 0){
				name = "馬";
			} else
				name = "傌";
			break;
		case 9: case 10:
			this.priority = 1;
			if(color == 0){
				name = "包";
			} else
				name = "炮";	
			break;
		case 11: case 12: case 13: case 14: case 15:
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
		s = "棋: " + this.name + "\t編號： " + identify + "\t權重： " + priority;
		return s;
	}

}