CYPHER MINI PROJECT

README

Link to Youtube Video: https://youtu.be/e2nbtlAw3e0

Our project implements the graph stream library so working with it may be taxing

For your convenience we uploaded an image for you can use in the project folder titled "Test Image.jpeg" and if you intend to add other images please ensure they are jpg or jpeg

INSTRUCTIONS:

CMD:

1) ensure your java version is 25.0 and higher (java -version)

2) Navigate to the "Vehicle Damage Detection" folder where you can see all the program files(i.e. src, bin, README, ss)
The reason you don't open it in the dist folder where the jar is locataed is because jvm won't be able to find the data folder that loads images to use for similarity

3) Open up cmd

4) Paste the following in the cmd line:

java --module-path "C:\Users\pc\OneDrive\Documents\CSC3A\Pracs\Vehicle Damage Detection\openjfx-21.0.2_windows-x64_bin-sdk\javafx-sdk-21.0.2\lib" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing -cp "dist/mini.jar;gs-core-2.0.jar;gs-ui-javafx-2.0.jar" acsse.main.Main

Only ensure to replace the folder location "C:\Users\pc\OneDrive\Documents\CSC3A\Pracs\Vehicle Damage Detection\openjfx-21.0.2_windows-x64_bin-sdk\javafx-sdk-21.0.2\lib", to where the javafx lib folder's whole path is on your system

5) Press enter

ECLIPSE:

1) Open Eclipse IDE

2) Import the project

3) Edit the build path of the project

4) Select module path and select add external JARs

5) Add the javafx jars and apply

6) Open the build path, select class path and select "Add External Class Folder"

7) Then add the "gs-core-2.0.jar" and "gs-ui-javafx-2.0.jar" folders which are both located in the project folder (Same folder as src)

8) Then open the build path

9) Then add "--module-path "\Vehice Damage Detection\openjfx-21.0.2_windows-x64_bin-sdk\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing" to the vm arguments
