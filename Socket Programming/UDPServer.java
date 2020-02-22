import java.io.*;
import java.net.*;

class UDPServer 
{
   public static void main(String args[]) throws Exception
   {

      DatagramSocket serverSocket = 
         new DatagramSocket(9876);

      byte[] receiveData = new byte[1024];
      byte[] sendData;

      while(true)
      {
         DatagramPacket receivePacket =
            new DatagramPacket(receiveData, receiveData.length);

         serverSocket.receive(receivePacket);

         String sentence = new String(receivePacket.getData(),
                                      0, receivePacket.getLength());

         InetAddress IPAddress = receivePacket.getAddress();

         int port = receivePacket.getPort();

         String capitalizedSentence = sentence.toUpperCase();

         sendData = capitalizedSentence.getBytes();

         DatagramPacket sendPacket =
            new DatagramPacket(sendData, sendData.length,
                               IPAddress, port);

         serverSocket.send(sendPacket);
      }
   }
}
