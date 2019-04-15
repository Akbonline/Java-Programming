package Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Shae (esmaeili@ufl.edu)
 */

public class LogMain
{
	private int peerID;   //Level of access may need to be changed.
	
	public LogMain (int peerID) {
		this.peerID = peerID;
}
	
	//TCP Connection:
	
	//Whenever a peer makes a TCP connection to other peer
	public String logTCPConnection (int otherPeer) 
	{
		String message = getSystemTime() + ": Peer " + this.peerID + " makes a connection to Peer " + otherPeer + ".";
		return message;
	}
	
	//Whenever a peer is connected form another peer
	public String logTCPConnected (int otherPeer) 
	{
		String message = getSystemTime() + ": Peer " + this.peerID + " is connected from Peer " + otherPeer + ".";
		return message;
	}
	
	//Change of preferred neighbors:
	//Whenever a peer changes its preferred neighbors
	public String logChangeOfPrefNeighbors(String listPrefNeighbors)
	{ //assuming that the input is a list of preferred neighbors being separated by comma ','. 
		String message = getSystemTime() + ": Peer " + this.peerID + " has the preferred neighbors " + listPrefNeighbors + ".";
		return message;
	}
	
	//Change of optimistically unchoked neighbor
	//Whenever a peer changes its optimistically unchoked neighbor
	public String logOptUnchokNeighbor (int optUnchokNeighbor) 
	{
		String message = getSystemTime() + ": Peer " + this.peerID + " has the optimistically unchoked neighbor " + optUnchokNeighbor + ".";
		return message;
	}
	
	//Unchoking
	//Whenever a peer is unchoked by a neighbor
	//When a peer receives a unchoking message from a neighbor
	public String logUnchoked (int neighborID) 
	{
		String message = getSystemTime() + ": Peer " + this.peerID + " is unchoked by " + neighborID + ".";
		return message;
	}
	
	//Choking
	//Whenever a peer is unchoked by a neighbor
	//When a peer receives a unchoking message from a neighbor
	public String logChoked (int neighborID) 
	{
		String message = getSystemTime() + ": Peer " + this.peerID + " is choked by " + neighborID + ".";
		return message;
	}
	
	//Receiving "have" message
	//Whenever a peer receives a "have" message
	public String logHave(int senderID, int index) 
	{
		String message = getSystemTime() + ": Peer " + this.peerID + " received the 'have' message from " + senderID + " for the piece " + index + ".";
		return message;
		
	}
	
	//Receiving "interested" message
	//Whenever a peer receives an 'interested' message
	public String logInterested(int senderID) 
	{
		String message = getSystemTime() + ": Peer " + this.peerID + " received the 'interetsed' message from " + senderID + ".";
		return message;
		
	}
	
	
	
	//Formatting the system time as YYYY/MM/DD_HH:mm:ss
	public String getSystemTime () 
	{
		//Credit goes to: https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime()); 
		return timeStamp;
	}
	
	
	
	

}
