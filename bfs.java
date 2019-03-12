import java.util.*;
import java.util.concurrent.*;

class Graph{
    private int V;
    private LinkedList<Integer> adj[];

    Graph(int v){
        V=v;
        adj= new LinkedList[v];
        //You create a new Linked List
        for(int i=0;i<v;i++){
            adj[i]= new LinkedList();  //array of linked list
        }}
        void addEdge(int v, int w){
            adj[v].add(w);          //add an edge to the linked list
        }
        void display(){
            for(int i=0;i<V;i++){
                for(int j=0;j<adj[j].size();j++){
                    System.out.print(adj[j]);
                }
            }
        }
    

    
    public static void main(String[] args){
        Graph g= new Graph(4); //Create a graph of 4 nodes
        g.addEdge(0,1);
        g.addEdge(0,2);
        g.addEdge(1,0);
        g.addEdge(1,2);
        g.addEdge(1,3);
        g.addEdge(2,0);
        g.addEdge(2,1);
        g.addEdge(3,2);
        g.addEdge(3,1);

    }
}