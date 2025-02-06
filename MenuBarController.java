// PROG2 VT2023, Inlamningsuppgift, del 2
// Grupp 072
// Walid Alzohili waal7171.
// Lisa Ninic Svensson lini4321.
// Emilia Sullivan emsu7232.


import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;


public class MenuBarController {
    
    public static void loadMap(String fileToLoad) {
        
        /* check if there are unsaved changes and if we should override them. */
        boolean override = PathFinder.overrideUnsavedChanges("Unsaved changes, continue anyway?");
        if(!override){
            return;
        }
        
        
        /* remove any selections from previous map. */
        SecondMenuController.resetSelectedNodes();
        
        PathFinder.graph.clear();
        PathFinder.renderMap(fileToLoad);
        PathFinder.unsavedChanges = true;
    }
    
    
    public static void save(String graphFilePath, String mapFilePath){
        
        String graphFilePathStr = graphFilePath.substring(5);
        
        try(FileWriter fr = new FileWriter(graphFilePathStr); BufferedWriter bufferedOut = new BufferedWriter(fr)){
            
            ListGraph<Circle> graph = PathFinder.graph;
            Set<Circle> nodes = graph.getNodes();
            
            /* write path as first line. */
            bufferedOut.write(mapFilePath + "\n");
            
            /* save nodes as one line. */
            for(Circle node : nodes){
                bufferedOut.write(node.getId() + ";" + node.getCenterX() + ";" + node.getCenterY() + ";");
            }
            bufferedOut.write("\n");
            
            
            /* loop over nodes and saves all edges (each edge in new line). */
            for(Circle node : nodes){
                /* all edges of one node. */
                LinkedHashSet<Edge<Circle>> edges = graph.getEdgesFrom(node);
                
                for (Edge<Circle> edge : edges){
                    bufferedOut.write(node.getId() + ";" + edge.getDestination().getId() + ";" + edge.getName() + ";" +
                            edge.getWeight() + "\n");
                }
            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        
        PathFinder.unsavedChanges = false;
    }
    
    
    
    public static void open(String graphFilePath){
        
        /* check if there are unsaved changes and if we should override them. */
        boolean override = PathFinder.overrideUnsavedChanges("Unsaved changes, continue anyway?");
        if(!override){
            return;
        }
        
        
        String graphFilePathStr = graphFilePath.substring(5);
        
         try (FileReader fr = new FileReader(graphFilePathStr); BufferedReader br = new BufferedReader(fr)){
             
             String line;
             
             
             //firstLine
             String mapFilePath = null;
             String firstLine;
             if((firstLine = br.readLine()) != null){
                 mapFilePath = firstLine;
                 loadMap(mapFilePath);
             }
             
             
             /* add nodes. */
             String secondLine;
             if((secondLine = br.readLine()) != null){
                 String[] nodes = secondLine.split(";");
                 for (int i = 0; i < nodes.length; i += 3) {
                     Circle node = new Circle(Double.parseDouble(nodes[i + 1]), Double.parseDouble(nodes[i + 2]), 8, Color.BLUE);
                     node.setId(nodes[i]);
                     SecondMenuController.addCircleClickListener(node);
                     PathFinder.graph.add(node);
                 }
             }

    
    
             /* add connections. */
             while ((line = br.readLine()) != null) {
                 String[] st = line.split(";");
        
                 Circle node = new Circle();
                 Circle node2 = new Circle();
        
                 for (Circle circle : PathFinder.graph.getNodes()) {
                     if (circle.getId().equals(st[0])) {
                         node = circle;
                     }
                     if (circle.getId().equals(st[1])) {
                         node2 = circle;
                     }
                 }
        
        
                 /* text file has the representation from source to destination and destination to source.
                  * but the graph is undirected so we need to check that no edge exists first. */
                 if (PathFinder.graph.getEdgeBetween(node, node2) == null) {
                     PathFinder.graph.connect(node, node2, st[2], Integer.parseInt(st[3]));
                 }
             }
             
             
             /* remove any selections from previous map. */
             SecondMenuController.resetSelectedNodes();
             
             PathFinder.renderGraph(mapFilePath);
             
             PathFinder.unsavedChanges = false;
        }
        catch (FileNotFoundException e){
             PathFinder.error("No file is saved");
             return;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    
    public static class SaveImage implements EventHandler<ActionEvent> {

        private final Pane node;

        public SaveImage(Pane node){
            this.node=node;
        }


        @Override public void handle(ActionEvent event){
            if (node == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "There is no image to save");
                alert.showAndWait();
                return;
            }
            try{
                WritableImage wimage= node.snapshot(null,null);
                BufferedImage bimage= SwingFXUtils.fromFXImage(wimage, null);
                ImageIO.write(bimage, "png", new File ("capture.png"));

            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fel!");
                alert.showAndWait();
            }

        }
    }

    
    public static EventHandler<ActionEvent> saveImage(Pane node){
        return new SaveImage(node);
    }
    
    
}
