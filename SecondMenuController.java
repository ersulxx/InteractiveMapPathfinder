// PROG2 VT2023, Inlamningsuppgift, del 2
// Grupp 072
// Walid Alzohili waal7171.
// Lisa Ninic Svensson lini4321.
// Emilia Sullivan emsu7232.


import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SecondMenuController {

    protected static List<Circle> selected = new ArrayList<>();
    
    
    public void newPlace(ActionEvent actionEvent) {
        
        /* change the cursor. */
        PathFinder.scene.setCursor(Cursor.CROSSHAIR);
        /* disable button. */
        ((Button) actionEvent.getSource()).setDisable(true);
        
        
        /* event listener for the image. */
        PathFinder.imageView.setOnMouseClicked(event -> {
        
            /* dialog box. */
            TextInputDialog td = new TextInputDialog("");
            td.setHeaderText("Name of place:");
            Optional<String> result = td.showAndWait();
            String nodeName = td.getEditor().getText();
            
            
            boolean nodeAlreadyExists = false;
            Set<Circle> nodes = PathFinder.graph.getNodes();
            for(Circle circle : nodes){
                if (circle.getId().equals(nodeName)){
                    PathFinder.error("Place with same name already exists");
                    nodeAlreadyExists = true;
                    break;
                }
            }
        
            /* if the pressed ok. */
            if (result.isPresent() && !nodeAlreadyExists) {
                
                /* get coordinates of the mouse click. */
                double x = event.getX();
                double y = event.getY();
        
                
                /* create dot. */
                Circle circle = new Circle(x, y, 8, Color.BLUE);
                
                
                /* add event listener to all dots. */
                addCircleClickListener(circle);
                /* add the ID for each dot. */
                circle.setId(nodeName);
                
                
                /* add the dots to the graph. */
                PathFinder.graph.add(circle);
                PathFinder.unsavedChanges = true;
                /* re-render the graph. */
                PathFinder.renderGraph(PathFinder.mapFilePath);
            }
        

            /* change the cursor back to default. */
            PathFinder.scene.setCursor(Cursor.DEFAULT);
            /* enable the button. */
            ((Button) actionEvent.getSource()).setDisable(false);
            /* remove the event-listener. */
            PathFinder.imageView.setOnMouseClicked(null);
        });
        
    }
    
    
    
    public void newConnection(ActionEvent actionEvent) {
        
        if (selected.size() != 2){
            PathFinder.error("Two places must be selected!");
            return;
        }

        
        Circle place1 = selected.get(0);
        Circle place2 = selected.get(1);
        

        if(PathFinder.graph.getEdgeBetween(place1, place2) != null){
            PathFinder.error("Connection already exists");
            return;
        }
        
        
        /* dialog */
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Connection from " + place1.getId() + " to " + place2.getId());
    
        GridPane grid = new GridPane();
        
        /* labels and textFields. */
        Label label = new Label("Name:");
        Label label2 = new Label("Time:");
        TextField nameField = new TextField();
        TextField timeField = new TextField();

        /* add the labels and texFields to the grid. */
        grid.add(label, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(label2, 0, 1);
        grid.add(timeField, 1, 1);
        
        
        dialog.getDialogPane().setContent(grid);
        Optional<String> result = dialog.showAndWait();
        
        String name = nameField.getText();
        String time = timeField.getText();
        
        
        
        /* user did not press ok. (pressed cancel)*/
        if(!result.isPresent()){
            return;
        }
        /* user did not provide full information. */
        else if(name.isEmpty() || time.isEmpty()){
            PathFinder.error("Name or time not provided");
            return;
        }
        
        
        /* check if time is an integer. */
        int timeAsInt;
        try {
            timeAsInt = Integer.parseInt(time);
        } catch (NumberFormatException e) {
            PathFinder.error("Time must be an integer");
            return;
        }
        
        
        /* make connection. */
        PathFinder.graph.connect(place1, place2, name, timeAsInt);
        PathFinder.unsavedChanges = true;
        PathFinder.renderGraph(PathFinder.mapFilePath);
        
        /* empty the selected list. */
        //resetSelectedNodes();
    }
    
    
    
    public void showConnection(ActionEvent actionEvent) {
        if (selected.size() != 2){
            PathFinder.error("Two places must be selected!");
            return;
        }
        
        Circle place1 = selected.get(0);
        Circle place2 = selected.get(1);
        
        Edge<Circle> edge = PathFinder.graph.getEdgeBetween(place1, place2);
        
        /* no edge. */
        if(edge == null){
            PathFinder.error("Edge does not exist");
            return;
        }

        
        /* dialog */
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Connection from " + place1.getId() + " to " + place2.getId());
    
        GridPane grid = new GridPane();
        
        /* labels and textFields. */
        Label label = new Label("Name:");
        Label label2 = new Label("Time:");
        TextField nameField = new TextField();
        TextField timeField = new TextField();
        
        /* add the labels and texFields to the grid. */
        grid.add(label, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(label2, 0, 1);
        grid.add(timeField, 1, 1);
        
        nameField.setText(edge.getName());
        nameField.setEditable(false);
        
        timeField.setText(String.valueOf(edge.getWeight()));
        timeField.setEditable(false);
        
        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
        
        
        /* empty the selected list. */
        //resetSelectedNodes();
    }

    
    
    public void changeConnection(ActionEvent actionEvent) {
        if (selected.size() != 2){
            PathFinder.error("Two places must be selected!");
            return;
        }
        
        Circle place1 = selected.get(0);
        Circle place2 = selected.get(1);
        Edge<Circle> edge = PathFinder.graph.getEdgeBetween(place1, place2);
        
        if(PathFinder.graph.getEdgeBetween(place1, place2) == null){
            PathFinder.error("Connection does not exist");
            return;
        }
        
        
        /* dialog */
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText("Connection from " + place1.getId() + " to " + place2.getId());
    
        GridPane grid = new GridPane();
        
        /* labels and textFields. */
        Label label = new Label("Name:");
        Label label2 = new Label("Time:");
        TextField nameField = new TextField();
        TextField timeField = new TextField();
        
        /* add the labels and texFields to the grid. */
        grid.add(label, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(label2, 0, 1);
        grid.add(timeField, 1, 1);
        
        nameField.setText(edge.getName());
        nameField.setEditable(false);
        dialog.getDialogPane().setContent(grid);
        
        
        Optional<String> result = dialog.showAndWait();
        String time = timeField.getText();
        
        
        
        /* if the user did not press cancel add connection. */
        if (!result.isPresent()){
            return;
        }
        
        
        /* check if time is an integer. */
        int timeAsInt;
        try {
            timeAsInt = Integer.parseInt(time);
        } catch (NumberFormatException e) {
            PathFinder.error("Time must be an integer");
            return;
        }

        
        PathFinder.graph.setConnectionWeight(place1, place2, Integer.parseInt(time));
        PathFinder.unsavedChanges = true;
        
        /* empty the selected list. */
        //resetSelectedNodes();
    }
    
    
    public void findPath(ActionEvent actionEvent) {
    
        /* nodes not selected. */
        if (selected.size() != 2){
            PathFinder.error("Two places must be selected!");
            return;
        }
        
        Circle place1 = selected.get(0);
        Circle place2 = selected.get(1);

        /* path does not exist. */
        if(!PathFinder.graph.pathExists(place1, place2)){
            PathFinder.error("Path does not exist");
            return;
        }
        
        
        /* edges that represent the path. */
        List<Edge<Circle>> path = PathFinder.graph.getPath(place1, place2);
        
        
        /* create the string that represents the path. */
        StringBuilder str = new StringBuilder();
        int totalTime = 0;
        for(Edge<Circle> edge: path){
            str.append(String.format("to " + edge.getDestination().getId() + " by " + edge.getName() + " takes " + edge.getWeight() + "\n" ));
            totalTime += edge.getWeight();
        }
        str.append(String.format("Total " + totalTime));
        
        
        
        /* dialog. */
        TextInputDialog td = new TextInputDialog("");
        td.setHeaderText("Connection from " + place1.getId() + " to " + place2.getId());

        /* text. */
        TextArea textArea = new TextArea();
        textArea.setText(str.toString());
        textArea.setEditable(false);
        
        /* add textArea to grid. */
        GridPane grid = new GridPane();
        grid.add(textArea, 0, 0);

        /* add grid to the dialogPane. */
        td.getDialogPane().setContent(grid);
        
        td.showAndWait();
    }
    
    
    
    
    /* * helper methods. * */
    
    
    /* add the circle to selected, and changes color. */
    public static void addCircleClickListener(Circle circle) {
        circle.setOnMouseClicked(event -> {
            if (circle.getFill() == Color.BLUE && selected.size() < 2) {
                circle.setFill(Color.RED);
                selected.add(circle);
            } else {
                circle.setFill(Color.BLUE);
                selected.remove(circle);
            }
        });
    }
    
    
    
    public static void resetSelectedNodes(){
        /* check if there are elements first. */
        if(selected.size() >= 1) {
            selected.get(0).setFill(Color.BLUE);
        }
        if(selected.size() >= 2) {
            selected.get(1).setFill(Color.BLUE);
        }
        selected.clear();
    }
    
    
}