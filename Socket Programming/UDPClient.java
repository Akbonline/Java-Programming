import java.io.*;
import java.net.*;

class UDPClient 
{
   public static void main(String args[]) throws Exception
   {
      BufferedReader inFromUser =
         new BufferedReader(
         new InputStreamReader(System.in));

      DatagramSocket clientSocket = new DatagramSocket();

      // Replace hostname with the FQDN of the server.

      InetAddress IPAddress = InetAddress.getByName("localhost");

      byte[] sendData;

      byte[] receiveData = new byte[1024];

      String sentence = inFromUser.readLine();

      sendData = sentence.getBytes();

      DatagramPacket sendPacket =
         new DatagramPacket(sendData, sendData.length,
                            IPAddress, 9876);

      clientSocket.send(sendPacket);

      DatagramPacket receivePacket =
         new DatagramPacket(receiveData, receiveData.length);

      clientSocket.receive(receivePacket);

      String modifiedSentence = new String(receivePacket.getData(),
                                           0, receivePacket.getLength());

      System.out.println("FROM SERVER: " + modifiedSentence);

      clientSocket.close();
   }
}
