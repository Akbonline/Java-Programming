

public class LogHelper{

    public static String getPeerIdsAsString(Collection<Integer> pID,Collection<RemotePeerInfo> peers){
        StringBuilder peerAsString = new StringBuilder("");
        int check=1;
        for(RemotePeerInfo peer: peers){
            if(check){
                check = 0;
            }
            else{
                sb.append(", "); 
            }
        }
    }
}