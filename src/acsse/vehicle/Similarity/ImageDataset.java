package acsse.vehicle.Similarity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import acsse.vehicle.Classification.DAMAGE_TYPE;
import acsse.vehicle.Image.ImageRAG;
import acsse.vehicle.Image.VehicleImageHandler;
import acsse.vehicle.Node.PatchNode;

public class ImageDataset 
{
    
    // Use the SAME thresholds as your application
    private static final double EDGE_THRESHOLD = 170.0;
    private static final double DAMAGE_THRESHOLD = 170;
    
    /**
     * Function to get all the images 
     */
    public static List<ReferenceDamage> buildFromImages(String referenceFolderPath) 
    {
    	/*
    	 * List of the images that will be used for the similarity detection
    	 */
        List<ReferenceDamage> dataset = new ArrayList<>();
        
        /*
         * Creating a variable that stores reference to the folders
         */
        File imageFolder = new File(referenceFolderPath);
        /*
         * Array to get every image in the image folder
         */
        File[] folders = imageFolder.listFiles(File::isDirectory);
        
        /*
         * Fore each folder in folders
         */
        for (File typeFolder : folders) 
        {
        	/*
        	 * 
        	 */
            DAMAGE_TYPE type = getDamageType(typeFolder.getName());
            if (type == null) continue;
            
            File[] images = typeFolder.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".jpg") || 
                name.toLowerCase().endsWith(".png") ||
                name.toLowerCase().endsWith(".jpeg")
            );
            
            if (images != null) 
            {
                for (File imageFile : images) 
                {
                    ReferenceDamage ref = processImage(imageFile, type);
                    if (ref != null) 
                    {
                    	dataset.add(ref);
                        System.out.println("Added reference: " + ref.getLabel());
                    }
                }
            }
        }
        
        return dataset;
    }
    
    private static ReferenceDamage processImage(File imageFile, DAMAGE_TYPE type) 
    {
        try 
        {
        	
            BufferedImage img = ImageIO.read(imageFile);
            if (img == null) return null;
            
            // Run through YOUR pipeline
            VehicleImageHandler handler = new VehicleImageHandler();
            int[][] gray = handler.convertToGrey(img);
            int[][] patchID = handler.slicSegment(img, gray, img.getWidth(), img.getHeight());
            PatchNode[] patches = handler.buildPatchNodes(img, gray, patchID, img.getWidth(), img.getHeight());
            
            // Create patch list (null-safe)
            ArrayList<PatchNode> patchlist = new ArrayList<>();
            for (PatchNode p : patches) 
                if (p != null) patchlist.add(p);
        
            // Create RAG and detect damage
            ImageRAG<Integer,?> rag = new ImageRAG<>(patchlist);
            rag.scoreSuspicians(EDGE_THRESHOLD);
            int damagedCount = rag.flagDamagedPatches(DAMAGE_THRESHOLD);
            
            if (damagedCount == 0) {
                System.out.println("  No damage detected in: " + imageFile.getName());
                return null;
            }
        
            // Extract statistics from damaged patches
            double avgIntensity = 0;
            double avgTexture = 0;
            double avgAnomaly = 0;
            int patchCount = 0;
            
            for (PatchNode patch : patches) {
                if (patch != null && patch.isDamaged) {
                    avgIntensity += patch.avgIntensity;
                    avgTexture += patch.textureVariance;
                    avgAnomaly += patch.anomalyScore;
                    patchCount++;
                }
            }
            
            avgIntensity /= patchCount;
            avgTexture /= patchCount;
            avgAnomaly /= patchCount;
            
            // Calculate average edge weight for damaged region
            double avgEdgeWeight = 0;
            int edgeCount = 0;
            Set<Integer> damagedIds = new HashSet<>();
            for (PatchNode p : patches) {
                if (p != null && p.isDamaged) damagedIds.add(p.id);
            }
            
            // Check if rag.edges() method exists, otherwise use neighbor approach
            try 
            {
                // Try using rag.edges() if available
                java.lang.reflect.Method edgesMethod = rag.getClass().getMethod("edges");
                @SuppressWarnings("unchecked")
                List<acsse.vehicle.Node.Edge<Integer, PatchNode>> edges = 
                    (List<acsse.vehicle.Node.Edge<Integer, PatchNode>>) edgesMethod.invoke(rag);
                
                for (acsse.vehicle.Node.Edge<Integer, PatchNode> edge : edges) {
                    PatchNode source = edge.getSource().getElement();
                    PatchNode dest = edge.getDestination().getElement();
                    
                    if (damagedIds.contains(source.id) && damagedIds.contains(dest.id)) {
                        if (edge.getElement() instanceof Number) {
                            avgEdgeWeight += ((Number) edge.getElement()).doubleValue();
                            edgeCount++;
                        }
                    }
                }
            } catch (Exception e) {
                // Fallback: use neighbor relationships
                avgEdgeWeight = calculateAvgEdgeWeightFromNeighbors(patches, damagedIds);
                edgeCount = 1; // To avoid division by zero
            }
            
            if (edgeCount > 0) avgEdgeWeight /= edgeCount;
            
            String label = imageFile.getName().replaceFirst("[.][^.]+$", "");
            return new ReferenceDamage(label, type, patchCount, avgIntensity, 
                                      avgTexture, avgAnomaly, avgEdgeWeight);
            
        } catch (IOException e) {
            System.out.println("Error processing: " + e.getMessage());
            return null;
        }
    }
    
    private static double calculateAvgEdgeWeightFromNeighbors(PatchNode[] patches, Set<Integer> damagedIds) 
    {
        double totalWeight = 0;
        int count = 0;
        
        for (PatchNode p : patches) 
        {
            if (p == null || !damagedIds.contains(p.id)) continue;
            
            if (p.getRight() != null && damagedIds.contains(p.getRight().id)) 
            {
                totalWeight += Math.abs(p.avgIntensity - p.getRight().avgIntensity);
                count++;
            }
            if (p.getDown() != null && damagedIds.contains(p.getDown().id)) 
            {
                totalWeight += Math.abs(p.avgIntensity - p.getDown().avgIntensity);
                count++;
            }
        }
        
        return count > 0 ? totalWeight / count : 0;
    }
    
    private static DAMAGE_TYPE getDamageType(String folderName) 
    {
        switch (folderName.toUpperCase()) 
        {
            case "DENT": return DAMAGE_TYPE.DENT;
            case "SCRATCH": return DAMAGE_TYPE.SCRATCH;
            case "CRACK": return DAMAGE_TYPE.CRACK;
            case "TAIL_LIGHTS": return DAMAGE_TYPE.TAIL_LIGHTS;
            case "BREAKS": return DAMAGE_TYPE.BREAKS;
            default: return null;
        }
    }
}