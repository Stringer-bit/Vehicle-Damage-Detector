
package acsse.vehicle.Similarity;

import acsse.vehicle.Image.ImageRAG;
import acsse.vehicle.Node.PatchNode;
import acsse.vehicle.Node.Edge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimilarityChecker 
{
	public static double determineSimilarity(ImageRAG<Integer,?> queryRAG, List<PatchNode> queryPatches, ReferenceDamage reference)
	{

        double sizeSim = compareSize(queryPatches.size(), reference.getPatchCount());
        double nodeSim = compareNodeProperties(queryPatches, reference);
        double edgeSim = compareEdgeWeights(queryRAG, queryPatches, reference);
        return (sizeSim * 0.25) + (nodeSim * 0.45) + (edgeSim * 0.30);
	}
	
	private static double compareSize(int querySize, int referenceSize) 
	{
        int max = Math.max(querySize, referenceSize);
        if (max == 0) 
        	return 1.0;
        
        return 1.0 - (Math.abs(querySize - referenceSize) / (double) max);
    }
	
	
	private static double compareNodeProperties(List<PatchNode> patches, ReferenceDamage reference) 
	{
        
        // Average intensity
        double queryAvgIntensity = 0;
        double queryAvgTexture = 0;
        double queryAvgAnomaly = 0;
        
        for (PatchNode p : patches) 
        {
            queryAvgIntensity += p.avgIntensity;
            queryAvgTexture += p.textureVariance;
            queryAvgAnomaly += p.anomalyScore;
        }
        
        int count = patches.size();
        queryAvgIntensity /= count;
        queryAvgTexture /= count;
        queryAvgAnomaly /= count;
        
        // Compare to reference
        double intensitySim = 1.0 - Math.abs(queryAvgIntensity - reference.getAveIntensity()) / 255.0;
        double textureSim = 1.0 - Math.abs(queryAvgTexture - reference.getAveTextureVariance()) / Math.max(queryAvgTexture, reference.getAveTextureVariance());
        double anomalySim = 1.0 - Math.abs(queryAvgAnomaly - reference.getAveAnomalyScore()) / Math.max(queryAvgAnomaly, reference.getAveAnomalyScore());
        
        return (intensitySim + textureSim + anomalySim) / 3.0;
    }
	
	private static double compareEdgeWeights(ImageRAG<Integer,?> rag, List<PatchNode> queryPatches,ReferenceDamage reference) 
	{
        
        // Get edges connected to damaged patches
        double queryAvgEdgeWeight = 0;
        int edgeCount = 0;
        
        Set<Integer> damagedIds = new HashSet<>();
        for (PatchNode p : queryPatches) {
            damagedIds.add(p.id);
        }
        
        for (Edge<Integer, PatchNode> edge : rag.getEdges()) 
        {
            PatchNode source = edge.getSource().getElement();
            PatchNode dest = edge.getDestination().getElement();
            
            // Only count edges where BOTH ends are damaged
            if (damagedIds.contains(source.id) && damagedIds.contains(dest.id)) {
                if (edge.getElement() instanceof Number) {
                    queryAvgEdgeWeight += ((Number) edge.getElement()).doubleValue();
                    edgeCount++;
                }
            }
        }
        
        if (edgeCount > 0) {
            queryAvgEdgeWeight /= edgeCount;
        }
        
        // Compare to reference
        double maxWeight = Math.max(queryAvgEdgeWeight, reference.getAveEdgeWeight());
        if (maxWeight == 0) return 1.0;
        return 1.0 - Math.abs(queryAvgEdgeWeight - reference.getAveEdgeWeight()) / maxWeight;
    }
	
}
