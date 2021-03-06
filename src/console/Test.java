package console;

public class Test {
	
	private int difNum;
	private int lenNum;
	
	
	public Test(int difNum, int lenNum) {
		super();
		this.difNum = difNum;
		this.lenNum = lenNum;
	}


	public String testMe() {
		System.out.println("testing");
		System.out.println(difNum);
		System.out.println(lenNum);
		return "testing " + "Dif:"+difNum + " len:" + lenNum;
	}
	
}
