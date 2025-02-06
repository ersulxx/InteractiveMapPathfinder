// PROG2 VT2023, Inlamningsuppgift, del 1
// Grupp 072
// Walid Alzohili waal7171.
// Lisa Ninic Svensson lini4321.
// Emilia Sullivan emsu7232.


import java.io.Serializable;
import java.util.*;


public class ListGraph<T> implements Graph<T>, Serializable {
    
    /* the graph implemented as an adjacencyList. */
    private final Map<T, LinkedList<Edge<T>>> graph = new LinkedHashMap<>();
    
    
    /* puts a node to the graph. */
    public void add(T node) {
        
        /* check if node already exists, if so return (do nothing). */
        if (graph.containsKey(node)) {
            return;
        } else {
            /* create List that will have the edges. */
            LinkedList<Edge<T>> edges = new LinkedList<>();
            graph.putIfAbsent(node, edges);
        }
    }
    
    
    /* takes a node, and removes it from the graph. */
    public void remove(T node) {
        
        LinkedList<Edge<T>> edgesToRemove = graph.remove(node);
        
        if (edgesToRemove == null) {
            throw new NoSuchElementException();
        }
        
        /* remove all edges that involve the node. */
        for (LinkedList<Edge<T>> edges : graph.values()) {
            edges.removeIf(edge -> edge.getDestination().equals(node));
        }
    
    }
    
    
    /*
        takes two nodes and connects with an edge.
        This graph is undirected, thus every connection has to be represented with two edges that have the same weight:
        - an edge from the sourceNode to the destinationNode.
        - an edge from the destinationNode to the sourceNode.
    */
    public void connect(T sourceNode, T destinationNode, String connectionName, int weight) {
        
        
        LinkedList<Edge<T>> sourceEdges = graph.get(sourceNode);
        LinkedList<Edge<T>> destinationEdges = graph.get(destinationNode);
    
        /* node does not exist */
        if (sourceEdges == null || destinationEdges == null) {
            throw new NoSuchElementException();
        }
    
        /* weight is negative. */
        if (weight < 0) {
            throw new IllegalArgumentException();
        }
    
        /* edge already exists. */
        if (getEdgeBetween(sourceNode, destinationNode) != null) {
            throw new IllegalStateException();
        }
    
        /* add the edge from the sourceNode to the destinationNode. */
        Edge<T> edgeSourceToDestination = new Edge<>(destinationNode, connectionName, weight);
        sourceEdges.add(edgeSourceToDestination);
    
        /* add the edge from the destinationNode to the sourceNode. */
        Edge<T> edgeDestinationToSource = new Edge<>(sourceNode, connectionName, weight);
        destinationEdges.add(edgeDestinationToSource);
        
    }
    
    
    /* takes two nodes and removes the edge that connects them.
        because the graph is undirected, each connection is represented with two edges,
        - one from the sourceNode to the destinationNode
        - and one from the destinationNode to the sourceNode,
        thus both of those edges have to be removed.
    */
    public void disconnect(T sourceNode, T destinationNode) {
        
        LinkedList<Edge<T>> sourceEdges = graph.get(sourceNode);
        LinkedList<Edge<T>> destinationEdges = graph.get(destinationNode);
    
        /* one of the nodes does not exist */
        if (sourceEdges == null || destinationEdges == null) {
            throw new NoSuchElementException();
        }
        
        /* edge does not exist. */
        if (getEdgeBetween(sourceNode, destinationNode) == null) {
            throw new IllegalStateException();
        }
        
        
        
        // remove the edge from sourceNode to destinationNode
        sourceEdges.removeIf(edge -> edge.getDestination().equals(destinationNode));
    
        // remove the edge from destinationNode to sourceNode
        destinationEdges.removeIf(edge -> edge.getDestination().equals(sourceNode));
        
        
        
        /*
    
            /* get the the edge from sourceNode to destinationNode.
            package_1.Edge<T> edge = getEdgeBetween(sourceNode, destinationNode);
            /* get the the edge from destinationNode to sourceNode.
            package_1.Edge<T> edge2 = getEdgeBetween(destinationNode, sourceNode);
            
    
            /* remove edge from sourceNode to the destinationNode.
            graph.get(sourceIndex).remove(edge);
            /* remove edge from destinationNode to the sourceNode.
            graph.get(destinationIndex).remove(edge2);
            
        */
    }
    
    
    
    /* takes two nodes and updates the weight of the edge between them. */
    public void setConnectionWeight(T sourceNode, T destinationNode, int weight) {
        LinkedList<Edge<T>> sourceEdges = graph.get(sourceNode);
        LinkedList<Edge<T>> destinationEdges = graph.get(destinationNode);
    
        /* node does not exist. */
        if (sourceEdges == null || destinationEdges == null) {
            throw new NoSuchElementException();
        }
    
        // update the weight of the edge from sourceNode to destinationNode
        boolean edgeFound = false;
        for (Edge<T> edge : sourceEdges) {
            if (edge.getDestination().equals(destinationNode)) {
                edge.setWeight(weight);
                edgeFound = true;
                break;
            }
        }
        if (!edgeFound) {
            throw new NoSuchElementException();
        }
    
        // update the weight of the edge from destinationNode to sourceNode
        edgeFound = false;
        for (Edge<T> edge : destinationEdges) {
            if (edge.getDestination().equals(sourceNode)) {
                edge.setWeight(weight);
                edgeFound = true;
                break;
            }
        }
        if (!edgeFound) {
            throw new NoSuchElementException();
        }
        
    }
    
    
    /* returns a copy of all nodes in the graph.
        we take a copy so that the caller can not modify the graph using this method.
    */
    public Set<T> getNodes() {
        Set<T> nodes = new HashSet<>();
        for (T node : graph.keySet()) {
            nodes.add(node);
        }
        return nodes;
    }
    
    
    
    
    /* takes a node and returns a copy of all edges that are connected to it.
        we take a copy so that the caller can not modify the graph using this method.
    */
    public LinkedHashSet<Edge<T>> getEdgesFrom(T node) {
    
        /* get all edges in the node. */
        LinkedList<Edge<T>> edges = graph.get(node);
        
        /* node does not exist. */
        if (edges == null) {
            throw new NoSuchElementException();
        }
        /* create a copy. LinkedHashSet maintains order. */
        return new LinkedHashSet<>(edges);
        
    }
    
    
    
    /* takes two nodes and returns the edge between them. */
    public Edge<T> getEdgeBetween(T source, T destination) {
        
        LinkedList<Edge<T>> edges = graph.get(source);
        LinkedList<Edge<T>> edges2 = graph.get(destination);
        
        /* node does not exist. */
        if (edges == null || edges2 == null) {
            throw new NoSuchElementException();
        }
        
        /* the edge equality is based on nodes, so it does not matter what weight we put here. */
        Edge<T> destinationEdge = new Edge<>(destination, "", 0);
        
        for (Edge<T> edge : edges) {
            if (edge.equals(destinationEdge)) {
                return edge;
            }
        }
        
        /* edge not found */
        return null;
        
    }
    
    
    /* takes two nodes and returns true if there is a path between them. */
    public boolean pathExists(T source, T destination) {
        
        LinkedList<Edge<T>> sourceEdges = graph.get(source);
        LinkedList<Edge<T>> destinationEdges = graph.get(destination);
    
        /* node does not exist. */
        if (sourceEdges == null || destinationEdges == null) {
            return false;
        }
        
        /* get the path. */
        List<Edge<T>> path = getPath(source, destination);
        
        /* path does not exist. */
        if (path == null) {
            return false;
        }
        
        return true;
    }
    
    
    /* takes two nodes and returns a list of edge/edges that represent a path between the nodes.
        this function does not do anything, it only uses the method depthFirstSearch and returns its value, it was done
        this way because the requirement say that the function has to be called getPath, but this function can be expanded
        in the future to take the search-method as an argument so that we can use this method to for example search
        the graph with depth-first-search or breadth-first-search etc.
    */
    public List<Edge<T>> getPath(T source, T destination) {
        
        List<Edge<T>> path = depthFirstSearch(source, destination);
        
        return path;
    }
    
    
    public String toString() {
        StringBuilder str = new StringBuilder();
        
        for (Map.Entry<T, LinkedList<Edge<T>>> entry : graph.entrySet()) {
            T source = entry.getKey();
            
            LinkedList<Edge<T>> edges = entry.getValue();
            
            
            str.append(String.format(source + " -> "));
            for (Edge<T> edge : edges) {
                str.append(String.format(edge.toString() + " -> "));
            }
            /* Remove the last " -> " */
            final int characterToRemove = 4;
            str.setLength(str.length() - characterToRemove);
            str.append("\n");
        }
        
        return str.toString();
    }
    
    
    /* gives the number of nodes (not in requirements but used for testing). */
    public int getSize() {
        return graph.size();
    }
    
    
    /**
     helper methods.
     **/
    
    
    
    /* returns the path from one node to the other.
        if no path exists will return null.
    */
    private List<Edge<T>> depthFirstSearch(T source, T destination) {
        
        /* node does not exist. */
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            return null;
        }
    
        /* store visited nodes. */
        Set<T> visited = new HashSet<>();
        /* store the path. */
        List<Edge<T>> path = new ArrayList<>();
    
        boolean pathExists = depthFirstSearchHelper(source, destination, visited, path);
    
        if (pathExists) {
            return path;
        }
    
        /* path does not exist. */
        return null;
        
    }
    
    
    /* will search the graph.
        return true if there is a path.
        if there is a path, it will insert all the edges that make the path (in place).
        in this method we only add the edges to the path after that we have found the path, this is more efficient
        than always adding the edges and then removing them if the neighbor node does not lead to destination.
    */
    private boolean depthFirstSearchHelper(T source, T destination, Set<T> visited, List<Edge<T>> path) {
        /* base case: path is found. */
        if (source.equals(destination)) {
            return true;
        }
    
        /* mark the node as visited. */
        visited.add(source);
        /* get the edges connected to the source node. */
        LinkedList<Edge<T>> edges = graph.get(source);
    
        /* loop through the edges of the source node. */
        for (Edge<T> edge : edges) {
            /* store destination of the edge. */
            T neighborNode = edge.getDestination();
    
            /* explore neighbors that have not been visited before. */
            if (visited.contains(neighborNode)) {
                continue;
            }
    
            /* explore the neighbor. */
            boolean pathExists = depthFirstSearchHelper(neighborNode, destination, visited, path);
    
            /* post recursive calls.
               if the path was found add all the edges and return true.
               we add all edges in the beginning of the list, because this is the recursive calls, so it is in reverse
               order.
               adding to the beginning of a list is not efficient so this can be improved by using a different data
               structure or a different approach.
            */
            if (pathExists) {
                path.add(0, edge);
                return true;
            }
        }
    
        return false;
    
    }
    
    
    
    /* * extra methods. * */
    
    
    /* empties the graph. */
    public void clear(){
        Set<T> nodes = getNodes();
        for(T node : nodes){
            remove(node);
        }
    }
    
    
}
