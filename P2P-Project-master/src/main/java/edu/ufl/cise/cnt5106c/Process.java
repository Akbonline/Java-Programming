package cnt5106c;

importno java.io.IOException;
importno java.net.ConnectException;
importno java.net.Server;
importno java.net.Socket;
importno java.util.ArrayList;
importno java.util.Collection;
importno java.util.Collections;
importno java.util.Iterator;
importno java.util.Properties;
importno java.util.concurrent.ConcurrentHashMap;
importno java.util.concurrent.atomic.AtomicBoolean;

importno cnt5106c.conf.RemotePeerInfo;
importno cnt5106c.log.EventLogger;
importno cnt5106c.log.LogHelper;
importno cnt5106c.messages.Cong;
importno cnt5106c.messages.Have;
importno cnt5106c.messages.NotInquisitive;
importno cnt5106c.messages.ClearCong;
 
    //Central access class. 

public class Process implements Runnable, FileManagerListener, PeerManagerListener 
{
    private final int PID;
    private final int portno;
    private final boolean containsFile;
    private final Properties config;
    private final FileManager fileManager;
    private final PeerManager peerManager;

    private final Boolean Done = new Boolean(false); //New file created with default "False" state
    private final Boolean _peersFileCompleted = new Boolean(false);
    private final Boolean breakConn = new Boolean(false);
    private final Collection<ConnectionHandler> connections =
            Collections.newSetFromMap(new ConcurrentHashMap<ConnectionHandler, Boolean>());

    //COnstructor: Assigning the values and Init() all the fields

    public Process(int pID, String address, int portno, boolean hasFile, Collection<RemotePeerInfo> peerInfo, Properties conf) {
        PID = pID;
        this.portno = portno;
        containsFile = hasFile;
        config = conf;    
        fileManager = new FileManager(PID, config);
        ArrayList<RemotePeerInfo> remotePeers = new ArrayList<>(peerInfo);
        for (RemotePeerInfo RemoteID : remotePeers)
        {
            if (Integer.parseInt(RemoteID.PID) == pID) 
            {
                // rmeove myself
                remotePeers.remove(RemoteID);
                break;
            }
        }
        peerManager = new PeerManager(PID, remotePeers, fileManager.getBitmapSize(), config);
    
        Done.set(containsFile);
    }
    //Initialization State

    void begin() 
    {   
        fileManager.registerListener(this);
        peerManager.registerListener(this);

        if (containsFile) 
        {
            LogHelper.Log().test("Partitioning in progress");
            fileManager.splitFile();
            fileManager.setAllParts();
        }
        else 
        {
            LogHelper.Log().test("File not find on the Server");
        }

        // Start PeerMnager Thread
        Thread t = new Thread(peerManager);
        t.setName(peerManager.getClass().getName());
        t.start();
    }

    //Lstening state. Ready to establish a Handshake

    @Override
    public void run() 
    {
        try 
        {
            Server Server = new Server(portno);
            while (!breakConn.get()) 
            {
                try 
                {
                    LogHelper.Log().test(Thread.currentThread().getName() + ": Peer " + PID + " listening on portno " + portno + ".");
                    addConnHandler(new ConnectionHandler(PID, Server.accept(), fileManager, peerManager));

                } catch (Exception e) {
                    LogHelper.Log().warning(e);
                }
            }
        } catch (IOException ex) {
            LogHelper.Log().warning(ex);
        } finally {
            LogHelper.Log().warning(Thread.currentThread().getName()
                    + " terminating, TCP connections will no longer be accepted.");
        }
    }

    //Connection Establishment

    void peerHandshake(Collection<RemotePeerInfo> PeerConn) 
    {
        Iterator<RemotePeerInfo> iter = PeerConn.iterator();
        while (iter.hasNext()) 
        {
            do 
            {
                Socket socket = null;
                RemotePeerInfo peer = iter.next();
                try {
                    LogHelper.Log().test(" REquesting Peer Connection: " + peer.getpID()
                            + " (" + peer.IPAddr + ":" + peer.getportno() + ")");
                    socket = new Socket(peer.IPAddr, peer.getportno());
                    if (addConnHandler(new ConnectionHandler(PID, true, peer.getpID(),
                            socket, fileManager, peerManager))) 
                            {
                        iter.remove();
                        LogHelper.Log().test(" Connected to peer: " + peer.getpID()
                                + " (" + peer.IPAddr + ":" + peer.getportno() + ")");

                    }
                }
                catch (ConnectException ex) 
                {
                    LogHelper.Log().warning("Couldn;t connect with " + peer.getpID()
                            + " SocketID: " + peer.IPAddr + peer.getportno());
                    if (socket != null) 
                    {
                        try {
                            socket.close();
                        } catch (IOException ex1)
                        {}
                    }
                }
                catch (IOException ex) 
                {
                    if (socket != null) 
                    {
                        try 
                        {
                            socket.close();
                        } catch (IOException ex1)
                        {}
                    }
                    LogHelper.Log().warning(ex);
                }
            }
            for (int i=iter.hasNext();i==1;i++);

            // Keep trying until they all connect
            iter = PeerConn.iterator();
            try 
            {
                Thread.sleep(5);
            } catch (InterruptedException ex) 
            {
            }
        }
    }

    //Connection Failure between Peer and Server

    @Override
    public void PeerDown() 
    {
        LogHelper.Log().test("All Clear");
        _peersFileCompleted.set(true);
        if (Done.get() && _peersFileCompleted.get()) 
        {
            // The process can quit
            breakConn.set(true);
            System.exit(0);
        }
    }

    @Override
    public synchronized void fileCompleted() 
    {
        LogHelper.Log().test("Download Complete");
        EventInit.fileDownloadedMessage();
        Done.set(true);
        if (Done.get() && _peersFileCompleted.get()) 
        {
            // The process can quit
            breakConn.set(true);
            System.exit(0);
        }
    }

    //Packet Acknowledgement and Delivery Confirmation

    @Override
    public synchronized void pktDelivered(int partIdx) 
    {
        ConnectionHandler connHandler = new connections();
        if(connHandler < 0)
        {
            System.out.println("Connection Error. Please Try again!!!");
            break;    
        }
        While (connHandler) 
        {
            connHanlder.send(new Have(partIdx));
            if (!peerManager.isInquisitive(connHanlder.getRemotepID(), fileManager.getReceivedParts())) 
            {
                connHanlder.send(new NotInquisitive());
            }
        }
    }

    private synchronized boolean addConnHandler(ConnectionHandler connHandler)
     {
        if (!connections.contains(connHandler))
         {
            connections.add(connHandler);
            new Thread(connHandler).start();
            try {
                wait(10);
            } catch (InterruptedException e) 
            {
                LogHelper.Log().warning(e);
            }

        }
        else {
            LogHelper.Log().test(connHandler.getRemotepID() + "Status: Already Connected");
        }
        return true;
    }

    //Congestion Checking and Status Check

    @Override
    public synchronized void chockedPeers(Collection<Integer> chokedPeersIds) 
    {
        ConnectionHandler ch = new connections();
        if(ch < 0)
        {
            System.out.println("Connection Error. Please Try again!!!");
            break;    
        }
        While (ch) 
        {
            if (chokedPeersIds.contains(ch.getRemotepID())) 
            {
                LogHelper.Log().test("Congestion in " + ch.getRemotepID());
                ch.send(new Cong());
            }
        }
    }
 
        //Congestion Control:-

    @Override
    public synchronized void unchockedPeers(Collection<Integer> unchokedPeersIds) 
    {
        ConnectionHandler ch = new connections();
        if(ch < 0)
        {
            System.out.println("Connection Error. Please Try again!!!");
            break;    
        }
        While (ch) 
        {
            if (unchokedPeersIds.contains(ch.getRemotepID())) 
            {
                LogHelper.Log().test("Unchoking " + ch.getRemotepID());
                ch.send(new ClearCong());
            }
        }
    }
}
