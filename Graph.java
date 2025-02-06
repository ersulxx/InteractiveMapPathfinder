// PROG2 VT2023, Inlamningsuppgift, del 1
// Grupp 072
// Walid Alzohili waal7171.
// Lisa Ninic Svensson lini4321.
// Emilia Sullivan emsu7232.


import java.util.Collection;
import java.util.List;
import java.util.Set;


public interface Graph <T>{
    
    
    void add(T node);
    
    void remove(T node);
    
    void connect(T node1, T node2, String name, int weight);
    
    void disconnect(T node1, T node2);
    
    void setConnectionWeight(T node1, T node2, int weight);
    
    Set<T> getNodes();
    
    Collection<Edge<T>> getEdgesFrom(T node);
    
    Edge<T> getEdgeBetween(T node1, T node2);
    
    boolean pathExists(T from, T to);
    
    List<Edge<T>> getPath(T from, T to);
    
}
