// PROG2 VT2023, Inlamningsuppgift, del 1
// Grupp 072
// Walid Alzohili waal7171.
// Lisa Ninic Svensson lini4321.
// Emilia Sullivan emsu7232.


import java.io.Serializable;

public class Edge <T> implements Serializable {
    
    
    private final T node;
    private final String connectionName;
    private int weight;
    
    
    public Edge(T node, String connectionName, int weight){
        this.node = node;
        this.connectionName = connectionName;
        this.weight = weight;
    }
    
    
    public T getDestination(){
        return node;
    }
    
    
    public int getWeight(){
        return this.weight;
    }

    
    public void setWeight(int weight){
        if (weight < 0){
            throw new IllegalArgumentException();
        }
        else{
            this.weight = weight;
        }
    }
    
    
    public String getName(){
        return this.connectionName;
    }
    
    
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("till " + this.node.toString() + " med " + this.connectionName + " tar " + this.weight));
        
        return str.toString();
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        else if(o instanceof Edge<?> edge){
            return node.equals(edge.node);
        }
        else{
            return false;
        }
    }
    
    
    @Override
    public int hashCode() {
        
        /* choose any prime number. */
        final int primeNumber = 7;
    
        return 31 * primeNumber + node.hashCode();
    }
    
}
