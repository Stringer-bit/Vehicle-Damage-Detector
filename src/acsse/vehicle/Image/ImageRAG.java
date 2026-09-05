package acsse.vehicle.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import acsse.vehicle.Node.Edge;
import acsse.vehicle.Node.Graph;
import acsse.vehicle.Node.Node;  // Your custom Node class (from segmentation)
import acsse.vehicle.Node.PatchNode;

import acsse.vehicle.Node.Vertex;


public class ImageRAG<E, T> extends Graph<E, PatchNode>
{  
	private boolean isDirected;


    public ImageRAG() {
        super();  // Calls Graph constructor to initialize lists
        this.isDirected = false;
    }
	
    
	
   
    
  
    
 

 
    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ImageRAG {\n");
        sb.append("  Vertices: ").append(numVertices).append("\n");
        sb.append("  Edges: ").append(numEdges).append("\n");
        sb.append("  Directed: ").append(isDirected).append("\n");
        sb.append("}\n");
        return sb.toString();
    }

 
    public ImageRAG(ArrayList<PatchNode> patches)
    
    {
        // Initialize graph data structures
        this.setVertexList(new ArrayList<>());
        this.setEdgesList(new ArrayList<>());
        this.adjacencyList = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.isDirected = false;
        this.numVertices = 0;
        this.numEdges = 0;
        
        // Create vertices from patches
        for (int i = 0; i < patches.size(); i++)
        {
            PatchNode patch = patches.get(i);
            
            
       
            Vertex<E, PatchNode> vertex = new Vertex<>(patch, isDirected);
            getVertexList().add(vertex);
            adjacencyList.put(vertex, new ArrayList<>());
            numVertices++;
        }
        
        // Create edges between adjacent patches
        for (int i = 0; i < patches.size(); i++)
        {
            PatchNode currentPatch = patches.get(i);
            Vertex<E, PatchNode> currentVertex = getVertexList().get(i);
            
            
         // Right neighbor
            if (currentPatch.getRight() != null)
            {
                int j = patches.indexOf(currentPatch.getRight());
                Vertex<E, PatchNode> vertexRight = getVertexList().get(j);

                if (getEdge(currentVertex, vertexRight) == null)
                {
                    E weight = computeBoundaryWeight(currentPatch, currentPatch.getRight());
                    this.insertEdge(currentVertex, vertexRight, weight);
                }
            }

            // Down neighbor
            if (currentPatch.getDown() != null)
            {
                int j = patches.indexOf(currentPatch.getDown());
                Vertex<E, PatchNode> vertexDown = getVertexList().get(j);

                if (getEdge(currentVertex, vertexDown) == null)
                {
                    E weight = computeBoundaryWeight(currentPatch, currentPatch.getDown());
                    this.insertEdge(currentVertex, vertexDown, weight);
                }
            }
        }
    }
    
    /**
     * Compute boundary weight for PatchNode version
     */
    @SuppressWarnings("unchecked")
    public E computeBoundaryWeight(PatchNode patch1, PatchNode patch2)
    {
        return (E) calculateWeight(patch1, patch2);
    }
    
   
    public Integer calculateWeight(PatchNode p1, PatchNode p2)
    {
        double diff = Math.abs(p1.getAvgIntensity() - p2.getAvgIntensity());
        return (int) Math.round(diff);
    }
       
    
    public void scoreSuspicians(double EDGE_THRESHOLD)
    {
        Map<PatchNode, Double> weightSum = new HashMap<>();
        Map<PatchNode, Integer> edgeCount = new HashMap<>();

       
        for (Vertex<E, PatchNode> v : getVertexList())
        {
            PatchNode patch = (PatchNode) v.getElement();
            weightSum.put(patch, 0.0);
            edgeCount.put(patch, 0);
        }

        // process edges
        for (Edge<E, PatchNode> edge : getEdgesList())
        {
            double weight = ((Number) edge.getWeight()).doubleValue();//Weight MUST be double

            if (weight > EDGE_THRESHOLD)
            {
                PatchNode a = (PatchNode) edge.getSource().getElement();
                PatchNode b = (PatchNode) edge.getDestination().getElement();

                weightSum.put(a, weightSum.get(a) + weight);
                weightSum.put(b, weightSum.get(b) + weight);

                edgeCount.put(a, edgeCount.get(a) + 1);
                edgeCount.put(b, edgeCount.get(b) + 1);
            }
        }

        // calc average
        for (Vertex<E, PatchNode> v : getVertexList())
        {
            PatchNode patch = (PatchNode) v.getElement();

            int count = edgeCount.get(patch);
            if (count > 0) {
                double score = weightSum.get(patch) / count;
                patch.setAnomalyScore(score);
            }
        }
    }
    
    
    
    public int flagDamagedPatches(double DAMAGE_THRESHOLD)
    {
        int count = 0;

        for (Vertex<E, PatchNode> v : getVertexList())
        {
            PatchNode patch = v.getElement();

            if (patch.getAnomalyScore() > DAMAGE_THRESHOLD)
            {
                patch.setDamaged(true);
                count++;
            }
        }

        return count;
    }


  

   
}

