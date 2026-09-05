package acsse.main;

import java.util.List;

import acsse.vehicle.Similarity.ImageDataset;
import acsse.vehicle.Similarity.ReferenceDamage;
import acsse.vehicle.gui.vehicleDamageGui;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class Main extends Application
{
	
	public static List<ReferenceDamage> referenceDatabase;
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
			launch(args);
	}

	@Override  
	public void start(Stage primaryStage) throws Exception 
	{
		// TODO Auto-generated method stub
		// Build reference database at startup
        System.out.println("Initializing damage references");
        System.out.println("out");
        
        // Try to build from reference images folder
        referenceDatabase = ImageDataset.buildFromImages("data");
		
		@SuppressWarnings("rawtypes")
		vehicleDamageGui root = new vehicleDamageGui(primaryStage);
		
		Scene scene = new Scene(root, 1500, 600);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
