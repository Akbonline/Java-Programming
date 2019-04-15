package Log;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LogMain log = new LogMain (1001);
		System.out.println(log.logTCPConnection(2002));
		System.out.println(log.logTCPConnected(2002));
		System.out.println(log.logChangeOfPrefNeighbors("2002,3002,4003"));
		System.out.println(log.logOptUnchokNeighbor(3003));
		System.out.println(log.logUnchoked(2002));
		System.out.println(log.logHave(2002, 20));
	}

}
