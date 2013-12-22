public class ChessDemo {
	public static void main(String[] args) {
		AllChess allChess = new AllChess();
		Chess A = allChess.getChess(0);
		Chess B = allChess.getChess(1);
		Chess C = allChess.getChess(2);
		Chess D = allChess.getChess(3);
		
		System.out.println("test A, B");
		test(A, B);
		test(B, A);
		System.out.println("test A, C");
		test(A, C);
		test(C, A);
		System.out.println("test B, C");
		test(B, C);
		test(C, B);
		System.out.println("test A, D");
		test(A, D);
		test(D, A);
	}
	
	public static void test(Chess tmp1, Chess tmp2){
		Action action = new Action();
		if(action.eat(tmp1, tmp2)){
			System.out.println(tmp1.toString() +" eat "+ tmp2.toString());
		}else{
			System.out.println(tmp1.toString() +" can't eat "+ tmp2.toString());
		}
	}
}

class Action{
	public boolean eat(Chess A, Chess B){
		if(A.getPower() <= B.getPower())
			return true;
		else
			return false;
	}
	
	public boolean move(){
		return false;
	}
	
	//other methods...~
}

class AllChess{
	ChessA A = new ChessA();//�N
	ChessB B = new ChessB();//�h
	ChessC C = new ChessC();//�H
	ChessA D = new ChessA();//��
	Chess[] chess = {A, B, C, D};
	
	public AllChess(){
		//�¦�G1
		//����G2
		A.setColor(1);
		B.setColor(1);
		C.setColor(2);
		D.setColor(2);
	}
	public Chess getChess(int index){
		return chess[index];
	}
}

class Chess{
	int color;
	int power;
	int whatever;
	
	public Chess(){}
	
	public void setColor(int theColor){
		this.color = theColor;
	}
	public int getPower(){
		return power;
	}
}

class ChessA extends Chess{
	public ChessA(){
		power = 1;
	}
	public String toString(){
		if(color == 1)
			return "�N";
		else
			return "��";
	}
}

class ChessB extends Chess{
	public ChessB(){
		power = 2;
	}
	
	public String toString(){
		if(color == 1)
			return "�h";
		else
			return "�K";
	}
}

class ChessC extends Chess{
	public ChessC(){
		power = 3;
	}
	public String toString(){
		if(color == 1)
			return "�H";
		else
			return "��";
	}
}