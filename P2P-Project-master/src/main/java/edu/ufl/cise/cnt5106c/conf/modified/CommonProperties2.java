package edu.ufl.cise.cnt5106c.conf.modified;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
public enum CommonProperties2 {

    NumberOfPreferredNeighbors,
    UnchokingInterval,
    OptimisticUnchokingInterval,
    FileName,
    FileSize,
    PieceSize;

    public static final String CONFIG_FILE_NAME = "Common.cfg";
    public static Properties read (Reader reader) throws Exception {

        final Properties conf = new Properties () {
            @Override
            public synchronized void load(Reader reader)
                    throws IOException {
                BufferedReader in = new BufferedReader(reader);
                int i = 0;
                String query;
                while((query=in.readLine())!=null){
                    query = query.trim();
                    if ((query.length() <= 0) || (query.startsWith ("#"))) {
                        continue;
                    }
                    String[] str = query.split("\\s+");
                    if (str.length != 2) {
                        throw new IOException (new ParseException (query, i));
                    }
                    setProperty(str[0].trim(), str[1].trim());
                    i+=1;
                }                
            }
        };

        conf.load (reader);

        // Check whether config file contains all the needed properties
        for (CommonProperties2 prop : CommonProperties2.values()) {
            if (!conf.containsKey(prop.toString())) {
                throw new Exception ("Property not found " + prop);
            }
        }

        return conf;
    }
}
class PeerInfo {

    public static final String CONFIG_FILE_NAME = "PeerInfo.cfg";
    private final String COMMENT_CHAR = "#";
    private final Collection<RemotePeerInfo> _peerInfoVector = new LinkedList<>();

    public void read (Reader reader) throws FileNotFoundException, IOException, ParseException {
        BufferedReader in = new BufferedReader(reader);
        int i = 0;
        for (String line; (line = in.readLine()) != null;) {
            line = line.trim();
            if ((line.length() <= 0) || (line.startsWith (COMMENT_CHAR))) {
                continue;
            }
            String[] tokens = line.split("\\s+");
            if (tokens.length != 4) {
                throw new ParseException (line, i);
            }
            final boolean bHasFile = (tokens[3].trim().compareTo("1") == 0);
            _peerInfoVector.add (new RemotePeerInfo(tokens[0].trim(), tokens[1].trim(),
                    tokens[2].trim(), bHasFile));
            i++;
        }
    }

    public Collection<RemotePeerInfo> getPeerInfo () {
        return new LinkedList<>(_peerInfoVector);
    }
}
class RemotePeerInfo {
    public final List<String> peerinfo=new ArrayList<String>(); //Stores pId, pAddress, pPort and hasFile
    public AtomicInteger _bytesDownloadedFrom;
    public BitSet _receivedParts;
    private final AtomicBoolean _interested;

    public RemotePeerInfo (int peerId) {
        this (Integer.toString (peerId), "127.0.0.1", "0", false);
    }

    public RemotePeerInfo(String pId, String pAddress, String pPort, boolean hasFile) {
        peerinfo.add(pId);
        peerinfo.add(pAddress);
        peerinfo.add(pPort);
        peerinfo.add(String.valueOf(hasFile));
        _bytesDownloadedFrom = new AtomicInteger (0);
        _receivedParts = new BitSet();
        _interested = new AtomicBoolean (false);
    }

    public int getPeerId() {
        return Integer.parseInt(peerinfo.get(0));
    }

    public int getPort() {
        return Integer.parseInt(peerinfo.get(1));
    }

    public String getPeerAddress() {
        return peerinfo.get(2);
    }

    public boolean hasFile() {
        return Boolean.parseBoolean(peerinfo.get(3));
    }

    public boolean isInterested() {
        return _interested.get();
    }

    public void setInterested() {
        _interested.set (true);
    }

    public void setNotIterested() {
        _interested.set (false);
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof RemotePeerInfo) {
            return (((RemotePeerInfo) obj).peerinfo.get(0).equals (peerinfo.get(0)));
        }
        return false;
    }
    public int setcount(int bkey){
        if(bkey==0){
            return 0;
        }
        else{
            return(n+1)+setcount(bkey>>1);
        }
    }

    @Override
    public int hashCode() {             //It has to be the same in the receiving side
        int key=Integer.parseInt(this.peerinfo.get(0));     // Get the key
        Integer bkey=Integer.parseInt(Integer.toBinaryString(key));       // Convert into binary        
        int n=setcount(bkey);       //counts the number of 1's in the binary
        key=key*n + Objects.hashCode(this.peerinfo.get(0)); //multiplies that with the hashcode of the pId
        return key;
    }

    @Override
    public String toString() {
        return new StringBuilder (peerinfo.get(0)).append(" address:").append (peerinfo.get(2)).append(" port: ").append(peerinfo.get(1)).toString();
    }

    public static Collection<Integer> toIdSet (Collection<RemotePeerInfo> peers) {
        Set<Integer> ids = new HashSet<>();
        for (RemotePeerInfo peer : peers) {
            ids.add(peer.getPeerId());
        }
        return ids;
    }
}