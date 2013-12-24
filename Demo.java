import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.util.ArrayList;


public class Demo {
	public static void main(String[] args) {
		Department D1 = new Department(20);
		Department D2 = new Department(10);
		
		College college = new College(0);
		college.add(D1);
		college.add(D2);
		
		FCUComponent f[] = new FCUComponent[3];
		A__A A[] = new A__A[3];
		
		f[0] = D1;
		f[1] = D2;
		f[2] = college;
		
		A[0] = D1;
		A[1] = D2;
		A[2] = college;
		
		for(int i = 0; i<=2; i++){
			System.out.println(f[i].count());
			A[i].printf();
		}

		
	}
}
interface A__A{
	public void printf();
}
interface FCUComponent{
	public int count();

}

class Department implements FCUComponent,A__A{
	int count;
	public Department(int num){
		count = num;
	}
	public int count() {
		return count;
	}

	public void printf() {
		//System.out.println("I'm Department~");
	}
}
class College implements FCUComponent,A__A{
	int count;
	ArrayList<Department> array = new ArrayList();
	public College(int num){
		count = num;
	}
	public int count() {
		int sum = 0;
		for(int i = 0; i < array.size(); i++){
			sum = array.get(i).count() + sum;
		}
		return sum;
	}
	
	public void add(Department dep){
		array.add(dep);
	}
	
	public void printf() {
		System.out.println("I'm College~");
	}
	
}