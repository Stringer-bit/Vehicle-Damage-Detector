package acsse.vehicle.Node;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Graph class - Core graph data structure with adjacency list and edge map
 * Provides O(1) vertex and edge operations using HashMap storage
 * @param <E> type of edge elements (weights)
 * @param <V> type of vertex elements (image segments)
 */
public class Graph<E, V> 
{/* 	protected int numVertices;
	protected int numEdges;
	ArrayList vertexList;
	ArrayList EdgesList;
	HashMap edgesMappingToVerteces;*/
	private ArrayList<Vertex<E, V>> vertexList;
	private ArrayList<Edge<E, V>> edgesList;
	protected HashMap<Vertex<E, V>, ArrayList<Edge<E, V>>> adjacencyList;
	protected HashMap<String, Edge<E, V>> edgeMap;  // Maps "u,v" to edge for O(1) lookup
	protected boolean isDirected;
	protected int numVertices;
	protected int numEdges;

	public Graph() {
		this.setVertexList(new ArrayList<>());
		this.setEdgesList(new ArrayList<>());
		this.adjacencyList = new HashMap<>();
		this.edgeMap = new HashMap<>();
		this.isDirected = false;
		this.numVertices = 0;
		this.numEdges = 0;
	}
	/**
	 * Function that returns the number of vertices of the
	 * graph
	 * 
	 * @return size
	 */
	public int getnumVertices() {
		return numVertices;
	}
	
	public int getnumEdges() {
		return numEdges;
	}
	
	/**
	 * Returns an iteration of all the vertices in the
	 * graph
	 * 
	 * @return vertex iterator
	 */
	public Iterable<Vertex<E, V>> getVertices() {
		return getVertexList();
	}
	
	
	
	/**
	 *  Returns an iteration of all the edges of the graph
	 *  
	 * @return edge Iterator
	 */
	public Iterable<Edge<E, V>> getEdges() {
		return getEdgesList();
	}
	
	/**
	 * Returns the edge from vertex u to vertex v, if one
	 * exists; otherwise return null. For an undirected
	 * graph, there is no difference between getEdge(u,v)
	 * and getEdge(v,u)
	 * 
	 * @param u origin vertex/ end point for edge we want
	 * @param v destination vertex/ end point for edge we want
	 * @return edges with endpoints u and v
	 */
	public Edge<E, V> getEdge(Vertex<E, V> u, Vertex<E, V> v) {
		return edgeMap.get(getEdgeKey(u, v));
	}
	
	/**
	 * Function to return an array that contains the two
	 * end point vertices of the given edge, if the vertex
	 * is directed the index 0 is the origin and index 1 is
	 * the destination
	 * 
	 * @param e being the edge we want end points for
	 * @return an array containing the endpoints of the given edge
	 */
	Vertex<E, V>[] endVerticies(Edge<E, V> e) {
		  return e.getEndpoints();
	}
	
	/**
	 * For edge e which is an incident to vertex v, this
	 * function returns the other vertex of the given edge,
	 * an error occurs if e is not incident to v
	 * 
	 * @param v one end point of the edge
	 * @param e edge with the opposite end point we want
	 * @return the vertex of the edge that isn't v
	 */
	public Vertex<E, V> opposite(Vertex<E, V> v, Edge<E, V> e) throws IllegalArgumentException 
    {
        Vertex<E, V>[] endpoints = e.getEndpoints();
        
        if (endpoints[0].equals(v))
            return endpoints[1];
        else if (endpoints[1].equals(v))
            return endpoints[0];
        else
            throw new IllegalArgumentException("Vertex is not incident to edge");
    }
	
	/**
	 * Returns the number of outgoing edges from the given 
	 * vertex
	 * 
	 * @param v, the vertex that we want number of edges for
	 * @return number of edges
	 */
	 public int outDegree(Vertex<E, V> v) {
	        return adjacencyList.getOrDefault(v, new ArrayList<>()).size();
	    }
	
	/**
	 * returns the number of incoming edges for the given
	 * vertex, if the graph is directed this returns the
	 * same value as outDegree
	 * 
	 * @param v is the vertex whose in coming edges we want
	 * @return number of outgoing edges
	 */
	  public int inDegree(Vertex<E, V> v) {
	        if (isDirected)
	            return adjacencyList.getOrDefault(v, new ArrayList<>()).size();
	        else
	            return outDegree(v);
	    }
	
	/**
	 * Returns an iteration of all outgoing edges from 
	 * the given vertex v.
	 * 
	 * @param v is the vertex for which we want the edges for
	 * @return an iterator of the outgoing edges
	 */
	public Iterable<Edge<E, V>> outgoingEdges(Vertex<E, V> v) {
        return adjacencyList.getOrDefault(v, new ArrayList<>());
    }
	
	/**
	 * Returns an iteration of all incoming edges for given 
	 * vertex v. For an undirected graph, this 
	 * returns the same collection as does outgoingEdges(v).
	 * 
	 * @param v is the vertex for which we want the iterator for
	 * @return iterator of incoming edges
	 */
	 public Iterable<Edge<E, V>> incomingEdges(Vertex<E, V> v) {
	        if (isDirected)
	            return adjacencyList.getOrDefault(v, new ArrayList<>());
	        else
	            return (Iterable<Edge<E, V>>) outgoingEdges(v);
	    }
	/**
	 * Creates and returns a new Vertex storing the given
	 * element x.
	 * 
	 * @param element that we want added to our graph in a vertex
	 * @return the vertex we created and added to the graph
	 */
	  public Vertex<E, V> insertVertex(V element) {
	        Vertex<E, V> vertex = new Vertex<>(element, isDirected);
	        getVertexList().add(vertex);
	        adjacencyList.put(vertex, new ArrayList<>());
	        numVertices++;
	        return vertex;
	    }
	
	/**
	 * Creates and returns a new Edge from vertex u to 
	 * vertex v, storing element x; an error occurs if 
	 * there already exists an edge from u to v.
	 * 
	 * @param u is the origin for new edge we are creating
	 * @param v is the destination for the new edge we are creating
	 * @param element we store in the edge we are creating
	 * @return the new edge we created
	 */
	  public Edge<E, V> insertEdge(Vertex<E, V> u, Vertex<E, V> v, E element) 
		        throws IllegalArgumentException 
		    {
		        if (getEdge(u, v) != null)
		            throw new IllegalArgumentException("Edge already exists");
		        
		        Edge<E, V> edge = createEdge(u, v, element);
		        getEdgesList().add(edge);
		        adjacencyList.get(u).add(edge);
		        adjacencyList.get(v).add(edge);
		        edgeMap.put(getEdgeKey(u, v), edge);
		        numEdges++;
		        
		        return edge;
		    }
	
	/**
	 * Removes vertex v and all its incident edges from 
	 * the graph.
	 * 
	 * @param v is the vertex we want removed
	 */
	  public void removeVertex(Vertex<E, V> v) {
	        ArrayList<Edge<E, V>> incidentEdges = new ArrayList<>(adjacencyList.get(v));
	        for (Edge<E, V> edge : incidentEdges) {
	            removeEdge(edge);
	        }
	        getVertexList().remove(v);
	        adjacencyList.remove(v);
	        numVertices--;
	    }
	/**
	 * Removes edge e from the graph.
	 * 
	 * @param e is the edge we want removed
	 */
	  public void removeEdge(Edge<E, V> e) {
	        Vertex<E, V>[] endpoints = e.getEndpoints();
	        Vertex<E, V> u = endpoints[0];
	        Vertex<E, V> v = endpoints[1];
	        
	        adjacencyList.get(u).remove(e);
	        adjacencyList.get(v).remove(e);
	        
	        u.getOutgoing().remove(v);
	        if (isDirected) {
	            v.getIncoming().remove(u);
	        } else {
	            v.getOutgoing().remove(u);
	        }
	        
	        edgeMap.remove(getEdgeKey(u, v));
	        getEdgesList().remove(e);
	        numEdges--;
	    }
	/**
     * Creates an edge between two vertices
     */
	public Edge<E, V> createEdge(Vertex<E, V> u, Vertex<E, V> v, E weight)
    {
        Edge<E, V> edge = new Edge<>(u, v, weight);
        u.getOutgoing().put(v, edge);
        if (isDirected) {
            v.getIncoming().put(u, edge);
        } else {
            v.getOutgoing().put(u, edge);
        }
        return edge;
    }
	   /**
     * Creates unique key for edge lookup
     */
    public String getEdgeKey(Vertex<E, V> u, Vertex<E, V> v) {
        int uid = getVertexList().indexOf(u);
        int vid = getVertexList().indexOf(v);
        
        if (uid < vid)
            return uid + "," + vid;
        else
            return vid + "," + uid;
    }
	public ArrayList<Vertex<E, V>> getVertexList() {
		return vertexList;
	}
	public void setVertexList(ArrayList<Vertex<E, V>> vertexList) {
		this.vertexList = vertexList;
	}
	public ArrayList<Edge<E, V>> getEdgesList() {
		return edgesList;
	}
	public void setEdgesList(ArrayList<Edge<E, V>> edgesList) {
		this.edgesList = edgesList;
	}

}