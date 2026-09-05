package acsse.vehicle.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.View;

import acsse.vehicle.Image.ImageRAG;
import acsse.vehicle.Image.VehicleImageHandler;
import acsse.vehicle.Node.Edge;
import acsse.vehicle.Node.PatchNode;
import acsse.vehicle.Node.Vertex;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.paint.Color;
import acsse.main.Main;
import acsse.vehicle.Classification.DamageClassifier;
import acsse.vehicle.Classification.DamageRegion;
import acsse.vehicle.Similarity.ReferenceDamage;
import acsse.vehicle.Similarity.SimilarityChecker;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.Node;


@SuppressWarnings("unused")
public class vehicleDamageGui<E, T> extends AnchorPane
{
	public static final double EDGE_THRESHOLD = 100.0;
    public static final double DAMAGE_THRESHOLD = 100.0;
   	
    private File loadedFile;
	private AnchorPane pane;
	private BufferedImage ogImg;
	@SuppressWarnings("rawtypes")
	private VehicleImageHandler handler = new VehicleImageHandler();
	private ImageRAG<Integer,T> rag = null;
	private Node ragNode;
	
    TextArea descrip = new TextArea();
    
    private DamageClassifier classifier = new DamageClassifier();
	
	//ASHTON
	public vehicleDamageGui(Stage primaryStage) throws IOException
	{	
		// TODO Auto-generated method stub
		
		
		FileChooser fc = new FileChooser();
	    fc.setTitle("Select Image");
	    
	    //Text Area
	    //descrip.setBorder(Border.stroke(Color.BLACK));
	    descrip.setMaxHeight(600.0);
	    descrip.setMaxWidth(200.0);
	    descrip.setPrefHeight(310);
	    
	    //title
	    Label header = new Label("DAMAGE DETECTOR");
	    header.setFont(Font.font("Lato", FontWeight.BOLD, 30));
	    header.setAlignment(Pos.CENTER);
	    header.setTextFill(Color.WHITE);
	    ////header.setText(Color.WHITE);
	    
	 

		
	    //button
		Button btn = new Button("DETECT DAMAGE");
		btn.setStyle("-fx-base: #FFFFFF;");
		
		Button btnClear = new Button ("CLEAR");
        btnClear.setStyle("-fx-base: #FFFFFF;");
        
        btn.setDisable(true);
        
		btn.setPrefSize(200, 35);
		btnClear.setPrefSize(200, 35);
		
		btn.setTooltip(new Tooltip("Click here to select picture of vehicle"));
	
		//image view
		ImageView ogView = new ImageView();
		ImageView damageView = new ImageView();
		ImageView RAGView = new ImageView();
		
		ogView.setFitWidth(400);
		ogView.setFitHeight(400);
		ogView.setPreserveRatio(true);
		ogView.setStyle("-fx-border-color: black;");
		
		damageView.setFitWidth(400);
		damageView.setFitHeight(400);
		damageView.setPreserveRatio(true);
		damageView.setStyle("-fx-border-color: black;");
		
		RAGView.setFitWidth(400);
		RAGView.setFitHeight(400);
		RAGView.setPreserveRatio(true);
		RAGView.setStyle("-fx-border-color: black;");
		
		Label lblAdd = new Label("Click To Add Image");
		lblAdd.setFont(Font.font("Lato", FontWeight.BOLD, 15));
	    lblAdd.setTextFill(Color.WHITE);
	    
	    Label lblSeg = new Label("Damaged Segments");
	    lblSeg.setFont(Font.font("Lato", FontWeight.BOLD, 15));
	    lblSeg.setTextFill(Color.WHITE);
	    
	    Label lblRag = new Label("Graph Representation");
	    lblRag.setFont(Font.font("Lato", FontWeight.BOLD, 15));
	    lblRag.setTextFill(Color.WHITE);
		
		//Panes used to display borders
		StackPane OGConatainer = new StackPane(ogView);
		OGConatainer.setCursor(javafx.scene.Cursor.HAND);
		OGConatainer.setOnMouseClicked
		(e -> 
			{
				lblAdd.setText("");
				loadedFile = fc.showOpenDialog(primaryStage);
				if(loadedFile != null)
			    {
					try 
					{
						ogImg = ImageIO.read(loadedFile);
						Image fxogImg = SwingFXUtils.toFXImage(ogImg, null);
				        ogView.setImage(fxogImg);
				        descrip.appendText("Image Loaded\n");
				        btn.setDisable(false);
				        
				        handler = new VehicleImageHandler<>();
					     // -----------------------------------------------------
				            // 1. LOAD IMAGE
				            // -----------------------------------------------------  
				            ogImg = ImageIO.read(loadedFile);
				         // -----------------------------------------------------
							//2- convert to grayscale (simplifies computation)
				            int[][] gScale = handler.convertToGrey(ogImg);

				            BufferedImage greyImg = new BufferedImage(ogImg.getWidth(), ogImg.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

					            for(int x = 0; x < ogImg.getWidth(); x++)
					            {
					                for(int y = 0; y < ogImg.getHeight(); y++)
					                {
					                    int grey = gScale[y][x];
					                    int rgb = (grey << 16) | (grey << 8) | grey;
					                    greyImg.setRGB(x, y, rgb);
					                }
					            }

				            //Image fxGreyImg = SwingFXUtils.toFXImage(greyImg, null);//code clean:varaible Note used
				            System.out.println("Reacched");
				            
				          
				            descrip.setText("Greyscale Loaded");
				        
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
			       
			    }
			}
	    	
		);

		StackPane damageContainer = new StackPane(damageView);
		StackPane ragContainer = new StackPane(RAGView);
		OGConatainer.setStyle("-fx-border-color: black; -fx-border-width: 2;");
		damageContainer.setStyle("-fx-border-color: black; -fx-border-width: 2;");
		ragContainer.setStyle("-fx-border-color: black; -fx-border-width: 2;");
		OGConatainer.getChildren().add(lblAdd);
		damageContainer.getChildren().add(lblSeg);
		ragContainer.getChildren().add(lblRag);
		

		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(descrip, btn, btnClear);
		//vbox.setPrefHeight(400);

		HBox hbox = new HBox(15); // child spaced by 15px
        hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(OGConatainer, damageContainer, ragContainer,vbox);
		
		AnchorPane.setTopAnchor(hbox, 100.0);
		AnchorPane.setLeftAnchor(hbox, 30.0);
		
		AnchorPane.setTopAnchor(header, 45.0);
		AnchorPane.setLeftAnchor(header, 30.0);
		
		System.out.println(getClass().getResource("/recources/imgbackground.png"));
		
		Image backImg = new Image(getClass().getResourceAsStream("/recources/imgbackground.png"));
		BackgroundImage bImg = new BackgroundImage(backImg,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,new BackgroundSize(1.0, 1.0, true, true, false, false));

		Background bGround = new Background(bImg);
		this.setBackground(bGround);
		
		this.getChildren().addAll(header, hbox);
		
		btn.setOnAction(e -> 
		{
		    
		   

		    if(loadedFile != null)
		    {
		    	lblSeg.setText("");
		    	lblRag.setText("");
		        descrip.setText("Greyscale Loaded\n");
				
				System.out.println("Reacched");

				BufferedImage newimg = detect();
				Image fxDamage = SwingFXUtils.toFXImage(newimg, null);
				System.out.println("Reacched");

				damageView.setImage(fxDamage);
				
				//Rag build
			    ragNode = RagBuild(rag);
				//ragContainer.getChildren().clear();
				ragContainer.getChildren().add(ragNode);
		    }
		});
		
		btnClear.setOnAction(e ->{
			ogView.setImage(null);
			damageView.setImage(null);
			RAGView.setImage(null);
			
			btn.setDisable(true);
            descrip.clear();
			loadedFile=null;
			ogImg = null;
			
	        ragContainer.getChildren().clear();
			ragContainer.setPrefSize(400, 400);
			
			lblAdd.setText("Click To Add Image");
			lblAdd.setFont(Font.font("Lato", FontWeight.BOLD, 15));
		    lblAdd.setTextFill(Color.WHITE);
		    
		    lblSeg.setText("Damaged Segments");
		    lblSeg.setFont(Font.font("Lato", FontWeight.BOLD, 15));
		    lblSeg.setTextFill(Color.WHITE);
		    
		    lblRag.setText("Graph Representation");
		    lblRag.setFont(Font.font("Lato", FontWeight.BOLD, 15));
		    lblRag.setTextFill(Color.WHITE);
	
		    ragContainer.getChildren().add(lblRag);
			
		});
	}
	
	
	private BufferedImage detect() 
    {
		int width  = ogImg.getWidth();
        int height = ogImg.getHeight();

        int[][] gray    = handler.convertToGrey(ogImg);
        int[][] patchID  = handler.slicSegment(ogImg, gray, width, height);
        PatchNode[] patches = handler.buildPatchNodes(ogImg, gray, patchID, width, height);
        
        ArrayList<PatchNode> patchlist = new ArrayList<>();
        for (PatchNode p : patches) 
        {
            patchlist.add(p);
        }
        
        rag = new ImageRAG<>(patchlist);
               
        rag.scoreSuspicians(EDGE_THRESHOLD);
        
        int damagedCount = rag.flagDamagedPatches(DAMAGE_THRESHOLD);
        
        System.out.println("Damaged patches found: " + damagedCount + " out of " + patches.length);
        
        conclusion(damagedCount, patches, patchID, width, height, rag);
        
		BufferedImage resImg = buildDamageView(ogImg, patchID, patches, width, height);
        
        return resImg;
            
    }
	
	public BufferedImage buildDamageView(BufferedImage original,int[][] patchID,PatchNode[] patches,int width,
            								int height) 
	{

        BufferedImage output   = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();
        graphics.drawImage(original, 0, 0, null);

        // Draw PatchNode boundary lines
        for (int y = 0; y < height - 1; y++) 
        {
            for (int x = 0; x < width - 1; x++) 
            {
                boolean rightDiffers = patchID[y][x] != patchID[y][x + 1];
                boolean downDiffers  = patchID[y][x] != patchID[y + 1][x];

                if (rightDiffers || downDiffers) 
                {
                    output.setRGB(x, y, java.awt.Color.GRAY.getRGB());
                }
            }
        }

        // Blend red overlay onto damaged patches
        for (int y = 0; y < height; y++) 
        {
            for (int x = 0; x < width; x++) 
            {
            	int id = patchID[y][x];

                
                int rgb = output.getRGB(x, y);

            	int r = (rgb >> 16) & 0xFF;
            	int g = (rgb >> 8) & 0xFF;
            	int b = rgb & 0xFF;

            	if (id < 0 || id >= patches.length) continue;
            	if (!patches[id].isDamaged) continue;

            	double alpha = 0.6;

            	int newR = (int)((1 - alpha) * r + alpha * 255);
            	int newG = (int)((1 - alpha) * g);
            	int newB = (int)((1 - alpha) * b);

            	int newColor = (255 << 24) | (newR << 16) | (newG << 8) | newB;

            	output.setRGB(x, y, newColor);
                
            }
        }

        graphics.dispose();
        return output;
    }
	
	
	
    private void conclusion(int damagedCount, PatchNode[] patches, int[][] patchID, int width, int height, ImageRAG<Integer,?> rag) 
    {
        if (damagedCount > 5) 
        {
            descrip.appendText("\nDAMAGED\n");
            descrip.appendText("=================\n");
            
            /*
             * Use the classifier class to get the Damages regions from the array of patches given
             */
            List<DamageRegion> regions = classifier.getDamageRegions(patches, patchID, width, height);
            
            /*
             * If the regions exist
             */
            if (regions != null && !regions.isEmpty()) 
            {
                descrip.appendText("\nDamage Breakdown:\n");
                
                /*
                 * Looping through all the images in the region we created
                 */
                for (int i = 0; i < regions.size(); i++) 
                {
                	/*
                	 * Getting the next index
                	 */
                    DamageRegion region = regions.get(i);
                    
                    /*
                     * Displaying the region we're at
                     */
                    descrip.appendText(String.format(
                        "Region %d: %s\n", 
                        i + 1, 
                        region.getType()
                    ));
                    /*
                     * Displaying the severity of the damage
                     */
                    descrip.appendText(String.format(
                        "Severity: %.1f%%\n",
                        region.getSeverityScore() * 100
                    ));
                    /*
                     * Showing the number of patches in the region
                     */
                    descrip.appendText(String.format(
                        "Patches: %d\n",
                        region.getPatches().size()
                    ));
                    
                    /*
                     * 
                     */
                    if (Main.referenceDatabase != null && !Main.referenceDatabase.isEmpty()) 
                    {
                        double bestSimilarity = 0;
                        String bestMatch = "No similar damage found";
                        
                        for (ReferenceDamage ref : Main.referenceDatabase) 
                        {
                            if (ref.getType() == region.getType()) 
                            {
                                double similarity = SimilarityChecker.determineSimilarity(
                                    rag, 
                                    region.getPatches(), 
                                    ref
                                );
                                
                                if (similarity > bestSimilarity) 
                                {
                                    bestSimilarity = similarity;
                                    bestMatch = ref.getLabel();
                                }
                            }
                        }
                        
                        if (bestSimilarity > 0.6) 
                        {
                            descrip.appendText(String.format(
                                "  Match: %s (%.1f%%)\n\n",
                                bestMatch, 
                                bestSimilarity * 100
                            ));
                        }
                        else 
                        {
                            descrip.appendText("No close match found\n\n");
                        }
                    }
                }
                
                descrip.appendText(String.format(
                    "\nTotal: %d damage region(s)\n", 
                    regions.size()
                ));
            }
        } 
        else 
        {
            descrip.appendText("\nNOT DAMAGED\n");
        }
    }
	
    private Graph buildGSGraph(ImageRAG<Integer,?> rag1)
	 {
		 
    	  //Creating an empty graphstream graph
		    Graph graph = new SingleGraph("RAG");
			//Maps patchnode to graphstream
			HashMap<PatchNode,String> map = new HashMap<>();
			
			//scales to make graph fit in container
			Double scaleX = 400.0 / ogImg.getWidth();
			Double scaleY = 400.0 / ogImg.getHeight();
			
			for(Vertex<?,PatchNode> ver : rag1.getVertexList())
			{
			  
			   PatchNode p = ver.getElement();
			   //using patch id as node id in graph
		       String vId =  "" + ver.getElement().id;
		   
		       //Adding nodes to graphstream
		       org.graphstream.graph.Node node = graph.addNode(vId);
		       
		       //image coordinates to graph  nodes
		       double x = p.centroidX * scaleX;
		       double y = 400 -(p.centroidY* scaleY);
		       
		       node.setAttribute("xyz",x,y,0);
		       
		       //Colours damaged nodes red
		       if(p.isDamaged)
		       {
		    	   node.setAttribute("ui.style", "fill-color: red;");
		       }
		       
		       //stores mapping for edge creations
		       map.put(p,vId);
		       
		    }
			
			int edgeId = 0;
	        for (Edge<?, PatchNode> e : rag1.getEdgesList()) 
	        {
	        	//get source and destination node ids
	        	String a = map.get(e.getSource().getElement());
	            String b = map.get(e.getDestination().getElement());

	            //preventing duplicate edges
	           if(graph.getEdge(a + "_" + b) == null)
	           {
	        	   graph.addEdge("E" + edgeId++, a, b);
	           }
	        }

	       return graph;
	 }
    
    public Node RagBuild(ImageRAG<Integer,T> rag)
	{
		Graph graph = buildGSGraph(rag);
		
		//Creating a javafx compatible viewer of graphstream
		FxViewer viewer = new FxViewer(graph,FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
	    
		//Switches off automatic layout to control positioning
		viewer.disableAutoLayout();
		View view = viewer.addDefaultView(false);
		((Region) view).setPrefSize(400,400);
		
		return (Node) view ;
		
	}
	
}
