package edu.ufl.cise.cnt5106c.log.modified;

import edu.ufl.cise.cnt5106c.conf.RemotePeerInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import edu.ufl.cise.cnt5106c.conf.RemotePeerInfo;

public class LogHelper2{
    
    private Logger SessionID;
    public static List getPeerIdsAsString(Collection<Integer> pID,Collection<RemotePeerInfo> peers){
        StringBuilder peer1AsString = new StringBuilder("");
        StringBuilder peer2AsString = new StringBuilder("");    
        int check=1;
        List<String> peersAsString=new ArrayList();
        for(RemotePeerInfo peer: peers){
            if(check==1){
                check = 0;
            }
            else{
                peer1AsString.append(", "); 
            }
            peer1AsString.append(peer.getPeerId());
        }
        check=1;
        for(Integer peerID:pID){
            if(check==1){
                check = 0;
            }
            else{
                peer2AsString.append(", "); 
            }
            peer2AsString.append(peerID.intValue());
        }
        peersAsString.add(peer1AsString.toString());
        peersAsString.add(peer2AsString.toString());
        return peersAsString;
    }
    public static void configure(int peerId)
            throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Properties properties = new Properties();
        properties.load(LogHelper2.class.getResourceAsStream(PATH));
        Handler handler = new FileHandler ("log_peer_" + peerId + ".log");
        Formatter formatter = (Formatter) Class.forName(properties.getProperty("java.util.logging.FileHandler.formatter")).newInstance();
        handler.setFormatter(formatter);
        handler.setLevel(Level.parse(properties.getProperty("java.util.logging.FileHandler.level")));
        newlog.SessionID.addHandler(handler);
    }

    LogHelper2(Logger id){
        SessionID=id;
    }
    public static LogHelper2 getLogger () {
        return newlog;
    }
    static String PATH= "/edu/ufl/cise/cnt5106c/conf/logger.properties";
    static LogHelper2 newlog=new LogHelper2(Logger.getLogger("CNT5106C"));
    static {
        InputStream in = null;
        try{
            in = LogHelper2.class.getResourceAsStream(PATH);
            LogManager.getLogManager().readConfiguration(in);
        }
        catch (IOException e) {
            System.err.println(LogHelper2.stackTraceToString(e));
            System.exit(1);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {}
            }
        }
    }

    private static char[] stackTraceToString(IOException e) {
        return null;
    }
    private static String stackTraceToString (Throwable t) {
        final Writer sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}