package acsse.vehicle.Image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import acsse.vehicle.Node.Node;
import acsse.vehicle.Node.PatchNode;
import acsse.vehicle.Node.Vertex;
import acsse.vehicle.list.LinkedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class VehicleImageHandler<T> extends Pane
{
	 static final int NUM_SUPERPIXELS = 200;
	 static final double COMPACTNESS = 5.0;
	 static final int SLIC_ITERATIONS = 10;
	
	FileChooser fc = null;
	Stage primaryStage = null;
	BufferedImage carImage = null;
	Image fxGreyImg = null;
	double[][] grayScaleImage = null;
	Node<Integer>[][] NodeImage;
	ImageRAG GreyScaleIamgeRAG ;
	
	public VehicleImageHandler() 
	{
		
	}
	//------------

	public VehicleImageHandler(Stage primaryStage)
	{
		fc = new FileChooser();
		this.primaryStage = primaryStage;
	}
	

    static double square(double x) 
    {
        return x * x;
    }

	
	//ASHTON
	public int[][] convertToGrey(BufferedImage carImage)
	{
		int width = carImage.getWidth();
		int height = carImage.getHeight();
		
		int[][] grayscale = new int[height][width];
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				int rgb = carImage.getRGB(x, y);
				int r = (rgb >> 16) & 0xff;// >> shifts bits right by 16| 0xff keeps only last 8 bits
				int g = (rgb >> 8) & 0xff;//
				int b = rgb & 0xff;
				
				int grey = (r + g + b)/3;
				
				grayscale[y][x] = grey;//brightness only
			}
		}
		return grayscale;
	}
//	//////////////////////////////////////////////////////////////////////////////
	
	
    
   



	
	@SuppressWarnings("unused")
	private boolean isImage(File file)
	{
		try
		{
			return ImageIO.read(file) != null;
			
		}catch(IOException ie)
		{
			ie.printStackTrace();
			return false;
		}
	}
	
	public int[][] slicSegment(BufferedImage image, int[][] gray, int width, int height) 
    {
        int gridStep = (int) Math.sqrt((double) (width * height) / NUM_SUPERPIXELS);

        ArrayList<double[]> centresList = new ArrayList<>();//list of patch centres
        
        for (int y = gridStep / 2; y < height; y += gridStep) 
        {
            for (int x = gridStep / 2; x < width; x += gridStep) 
            {
                Color pixel = new Color(image.getRGB(x, y));
                centresList.add(new double[]{gray[y][x], x, y, pixel.getRed(), pixel.getGreen(), pixel.getBlue()});
            }
        }

        int numPatches = centresList.size();

        int[][]    patchID      = new int[height][width];
        double[][] distanceMap = new double[height][width];

        for (double[] row : distanceMap) 
        {
            Arrays.fill(row, Double.MAX_VALUE);
        }
        for (int[] row : patchID) 
        {
            Arrays.fill(row, -1);
        }

        for (int iteration = 0; iteration < SLIC_ITERATIONS; iteration++) 
        {
            for (double[] row : distanceMap) 
            {
                Arrays.fill(row, Double.MAX_VALUE);
            }

            for (int k = 0; k < numPatches; k++) 
            {
                double[] centre  = centresList.get(k);
                int      centreX = (int) centre[1];
                int      centreY = (int) centre[2];

                int yStart = Math.max(0, centreY - gridStep);
                int yEnd   = Math.min(height - 1, centreY + gridStep);
                int xStart = Math.max(0, centreX - gridStep);
                int xEnd   = Math.min(width - 1, centreX + gridStep);

                for (int y = yStart; y <= yEnd; y++) 
                {
                    for (int x = xStart; x <= xEnd; x++) 
                    {
                        Color pixel = new Color(image.getRGB(x, y));

                        double colourDistance = Math.sqrt(square(gray[y][x] - centre[0]) + square(pixel.getRed() - centre[3]) + square(pixel.getGreen() - centre[4]) +
                        												square(pixel.getBlue() - centre[5]));

                        double spatialDistance = Math.sqrt(square(x - centreX) + square(y - centreY));

                        double totalDistance = colourDistance + (COMPACTNESS / gridStep) * spatialDistance;

                        if (totalDistance < distanceMap[y][x]) 
                        {
                            distanceMap[y][x] = totalDistance;
                            patchID[y][x] = k;
                        }
                    }
                }
            }

            double[][] featureSums = new double[numPatches][6];
            int[]      pixelCounts = new int[numPatches];

            for (int y = 0; y < height; y++) 
            {
                for (int x = 0; x < width; x++) 
                {
                    int clusterIndex = patchID[y][x];
                    if (clusterIndex < 0) 
                    {
                        continue;
                    }

                    Color pixel = new Color(image.getRGB(x, y));
                    featureSums[clusterIndex][0] += gray[y][x];
                    featureSums[clusterIndex][1] += x;
                    featureSums[clusterIndex][2] += y;
                    featureSums[clusterIndex][3] += pixel.getRed();
                    featureSums[clusterIndex][4] += pixel.getGreen();
                    featureSums[clusterIndex][5] += pixel.getBlue();
                    pixelCounts[clusterIndex]++;
                }
            }

            for (int k = 0; k < numPatches; k++) 
            {
                if (pixelCounts[k] > 0) 
                {
                    for (int dimension = 0; dimension < 6; dimension++) 
                    {
                        featureSums[k][dimension] /= pixelCounts[k];
                    }
                    centresList.set(k, featureSums[k]);
                }
            }
        }

        cleanStrayPatch(patchID, numPatches, width, height);
        return patchID;
    }
	
	
	static void cleanStrayPatch(int[][] patchID, int numPatches, int width, int height) 
    {
        int minPatchSize = (width * height) / (numPatches * 4);
        boolean[] visited = new boolean[height * width];
        int[]   deltaX = { 1, -1,  0, 0 };
        int[]   deltaY = { 0,  0,  1, -1 };

        for (int y = 0; y < height; y++) 
        {
            for (int x = 0; x < width; x++) 
            {
                if (visited[y * width + x]) 
                {
                    continue;
                }

                int currentPatchID = patchID[y][x];
                ArrayList<int[]> patchRegion = new ArrayList<>();
                LinkedList<int[]> list = new LinkedList<>();

                list.add(new int[]{ x, y });
                visited[y * width + x] = true;

                while (!list.isEmpty()) 
                {
                    int[] pixel = list.poll();
                    patchRegion.add(pixel);

                    for (int direction = 0; direction < 4; direction++) 
                    {
                        int neighbourX = pixel[0] + deltaX[direction];
                        int neighbourY = pixel[1] + deltaY[direction];

                        if (neighbourX < 0 || neighbourX >= width) 
                        {
                        	continue;
                        }
                        if (neighbourY < 0 || neighbourY >= height) 
                        {
                        	continue;
                        }
                        if (visited[neighbourY * width + neighbourX]) 
                        {
                        	continue;
                        }
                        if (patchID[neighbourY][neighbourX] != currentPatchID) 
                        {
                        	continue;
                        }

                        visited[neighbourY * width + neighbourX] = true;
                        list.add(new int[]{ neighbourX, neighbourY });
                    }
                }

                if (patchRegion.size() < minPatchSize) 
                {
                    int newid = currentPatchID;

                    boolean found = false;

                    for (int[] pixel : patchRegion) 
                    {
                        for (int direction = 0; direction < 4; direction++) 
                        {
                            int neighbourX = pixel[0] + deltaX[direction];
                            int neighbourY = pixel[1] + deltaY[direction];

                            if (neighbourX < 0 || neighbourX >= width) 
                            {
                            	continue;
                            }
                            if (neighbourY < 0 || neighbourY >= height) 
                            {
                            	continue;
                            }

                            if (patchID[neighbourY][neighbourX] != currentPatchID) 
                            {
                                newid = patchID[neighbourY][neighbourX];
                                found = true;
                                break;
                            }
                        }
                        if(found)
                        {
                        	break;
                        }
                    }

                    for (int[] pixel : patchRegion) 
                    {
                        patchID[pixel[1]][pixel[0]] = newid;
                    }
                }
            }
        }
    }

	
	
	public PatchNode[] buildPatchNodes(BufferedImage image,int[][] gray,int[][] patchID,int width,int height) 
    {

        int maxPatches = -1;
        for (int y = 0; y < height; y++) 
        {
            for (int x = 0; x < width; x++) 
            {
                maxPatches = Math.max(maxPatches, patchID[y][x]);
            }
        }

        PatchNode[] patches = new PatchNode[maxPatches + 1];
        for (int i = 0; i <= maxPatches; i++) 
        {
            patches[i]    = new PatchNode();
            patches[i].id = i;
        }

        for (int y = 0; y < height; y++) 
        {
            for (int x = 0; x < width; x++) 
            {
                int id = patchID[y][x];
                if (id < 0 || id >= patches.length) 
                {
                    continue;
                }

                PatchNode pachnode = patches[id];
                Color pixel = new Color(image.getRGB(x, y));

                pachnode.pixels.add(new int[]{x, y});
                pachnode.avgIntensity += gray[y][x];
                pachnode.centroidX += x;
                pachnode.centroidY += y;
                pachnode.avgR += pixel.getRed();
                pachnode.avgG += pixel.getGreen();
                pachnode.avgB += pixel.getBlue();
            }
        }

        for (PatchNode pachnode : patches) 
        {
            if (pachnode.pixels.isEmpty()) 
            {
                continue;
            }

            int pixelCount = pachnode.pixels.size();
            pachnode.area = pixelCount;
            pachnode.avgIntensity /= pixelCount;
            pachnode.centroidX /= pixelCount;
            pachnode.centroidY /= pixelCount;
            pachnode.avgR /= pixelCount;
            pachnode.avgG /= pixelCount;
            pachnode.avgB /= pixelCount;
            pachnode.zoneX = pachnode.centroidX / width;
            pachnode.zoneY = pachnode.centroidY / height;

            double variance = 0;
            for (int[] pix : pachnode.pixels) 
            {
                double diff = gray[pix[1]][pix[0]] - pachnode.avgIntensity;
                variance += diff * diff;
            }
            pachnode.textureVariance = variance / pixelCount;
        }
        // Check right neighbour
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int labelA = patchID[y][x];

                // Check right neighbour
                if (x + 1 < width)
                {
                    int labelB = patchID[y][x + 1];

                    if (labelA != labelB && patches[labelA].getRight() == null)
                    {
                        patches[labelA].setRight(patches[labelB]);
                    }
                }

                // Check down neighbour
                if (y + 1 < height)
                {
                    int labelB = patchID[y + 1][x];

                    if (labelA != labelB && patches[labelA].getDown() == null)
                    {
                        patches[labelA].setDown(patches[labelB]);
                    }
                }
            }
        }
        return patches;
    }

	
	
}
