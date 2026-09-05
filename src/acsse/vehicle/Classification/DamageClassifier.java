package acsse.vehicle.Classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import acsse.vehicle.Node.PatchNode;

public class DamageClassifier 
{
	/**
	 * Function that extracts damaged regions from an image
	 * 
	 * @param patches array of patches
	 * @param patchIDs the map that tells us which patch the corresponding pixel relates to
	 * @param width of the actual image
	 * @param height of the actual image
	 * @return list 
	 */
	public List<DamageRegion> getDamageRegions(PatchNode[] patches, int[][] patchID, int width, int height)
	{
		/*
		 * map that essentially creates a list of all the damaged regions we find
		 */
		Map<Integer, DamageRegion> rMap = new HashMap<>();
		boolean[] isVisited = new boolean[patches.length];
		int id = 0;
		
		for(int i = 0; i < patches.length; i++)
		{
			if(patches[i].isDamaged && !isVisited[i])
			{
				DamageRegion region = new DamageRegion();
				breadthFirstSearch(i, patches, isVisited, region);
				
				if(region.getPatches().size() > 0)
				{
					//Determining the type of Damage that the region is
					determineDamageType(region);
					detDamageSeverity(region);
					rMap.put(id++, region);
				}
			}
		}
		
		return new ArrayList<>(rMap.values());
	}
	
	private void breadthFirstSearch(int start, PatchNode[] patches, boolean[] isVisited, DamageRegion region)
	{
		Queue<Integer> queue = new LinkedList<>();
		queue.add(start);
		isVisited[start] = true;
		
		while(!queue.isEmpty())
		{
			try
			{
				int current = queue.remove();
				region.getPatches().add(patches[current]);
				
				/*
				 * creating a square box around the pixels
				 */
				for(int[] pixels : patches[current].pixels)
				{
					int x = pixels[0];
					int y = pixels[1];
					
					region.setMinX(Math.min(region.getMinX(), x));
					region.setMinY(Math.min(region.getMinY(), y));
					region.setMaxX(Math.max(region.getMaxX(), x));
					region.setMaxY(Math.max(region.getMaxY(), y));
				}
				
				/*
				 * Checking all of the current node's neighbours
				 */
				checkNeighbour(patches[current].getUp(), isVisited, queue);
				checkNeighbour(patches[current].getDown(), isVisited, queue);
				checkNeighbour(patches[current].getLeft(), isVisited, queue);
				checkNeighbour(patches[current].getRight(), isVisited, queue);
				
			}catch(NoSuchElementException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Helper function to check if a neighbour is damaged and unvisited
	 * 
	 * @param neighbour
	 * @param isVisited
	 * @param queue
	 */
	private void checkNeighbour(PatchNode neighbour, boolean[] isVisited, Queue<Integer> queue)
	{
		/*
		 * Checking that our neighbour exists and is damaged
		 * as well as unVisited yet, and if the condition is met
		 * add it to the queue that was passed
		 */
		if(neighbour != null && neighbour.isDamaged() && !isVisited[neighbour.id])
		{
			isVisited[neighbour.id] = true;
			queue.add(neighbour.id);
		}
	}
	
	/**
	 * Asserting what the type of damage is of the region 
	 * that was passed 
	 * 
	 * @param region we want to now the damage type of
	 */
	public void determineDamageType(DamageRegion region)
	{
		if(region.getPatches().isEmpty())
			return;
		
		/*
		 * Using the box we made around the region to determine
		 * if the shape of the damage region
		 */
		double width = region.getMaxX() - region.getMinX();
		double height = region.getMaxY() - region.getMinY();
		
		/*
		 * Making sure width and height can't be 0
		 */
		if(width == 0.0)
			width = 1;
		
		if(height == 0.0)
			height = 1;
		
		/*
		 * Determining a ratio of height to width such that
		 * we can determine shape based on this ratio, using
		 * math.min to ensure the ratios are consistent
		 */
		double aspectRatio = Math.max(width, height)/Math.min(width, height);
		
		double aveIntensityDiff = 0;
		double aveR = 0;
		double aveG = 0;
		double aveB = 0;
		
		for(PatchNode patch : region.getPatches())
		{
			aveIntensityDiff += patch.getAvgIntensity();
			aveR += patch.avgR;
			aveG += patch.avgG;
			aveB += patch.avgB;
		}
		
		int count = region.getPatches().size();
		aveIntensityDiff = aveIntensityDiff/count;
		aveR = aveR/count;
		aveG = aveG/count;
		aveB = aveB/count;
		
		/*
		 * Classifying the region
		 */
		if(count <= 3)
		{
			/*
			 * If patch is small and aspect ratio is small it
			 * is likely a crack or scratch
			 */
			if (aspectRatio > 3.0) 
			{
                region.setType(DAMAGE_TYPE.SCRATCH);
            } else 
            {
                region.setType(DAMAGE_TYPE.CRACK);
                
            }
			
		/*
		 * More square based damage, likely do be a dent
		 */
		}else if(aspectRatio < 2.0 && count > 5)
		{
			region.setType(DAMAGE_TYPE.DENT);
			
		/*
		 * Shape is longer so it is more likely to be a scratch
		 */
		}else if(aspectRatio > 3.0)
		{
			region.setType(DAMAGE_TYPE.SCRATCH);
		}
	}
	
	public void detDamageSeverity(DamageRegion region)
	{
		/*
		 * getting the number of patches in the region
		 */
		int count = region.getPatches().size();
		
		/*
		 * Determining a score between 1 and 0 for the damage
		 * region to determine how severe the damage is based
		 * on the number of patches in the region
		 * 
		 * 1 patch being little and 20 patches being a lot
		 */
		double score = count/20.0;
		
		/*
		 * If score is greater then 1 we set score to 1.0
		 * to essentially say it is has reached maximum severity
		 */
		if(score > 1.0)
			score = 1.0;
		
		/*
		 * Variable to store total intensity
		 */
		double tIntensity = 0;
		
		/*
		 * For each patch in in our region
		 */
		for (PatchNode patch : region.getPatches())
		{
			//add average intensity to the total intensity
			tIntensity += patch.getAvgIntensity();
		}
		
		/*
		 * Getting the average intensity of the region
		 */
		double aveIntensity = tIntensity / count;
		/*
		 * Making be a value between 0 and 1 such that it can
		 * be weighed with score correctly
		 */
		double intensityScore = aveIntensity / 255;
		
		/*
		 * Creating a weighted score because number of patches 
		 * or intensity alone isn't enough to determine the actual 
		 * intensity
		 */
		region.setSeverityScore((score * 0.5) + (intensityScore * 0.5));
	}
}
