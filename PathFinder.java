// PROG2 VT2023, Inlamningsuppgift, del 2
// Grupp 072
// Walid Alzohili waal7171.
// Lisa Ninic Svensson lini4321.
// Emilia Sullivan emsu7232.


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;


public class PathFinder extends Application {
    
    protected static ListGraph<Circle> graph = new ListGraph<>();
    
    protected static Stage stage;
    protected static Scene scene;
    
    protected static String graphFilePath = "file:europa.graph";
    protected static String mapFilePath = "file:europa.gif";
    
    protected static boolean unsavedChanges = false;
    protected static Image image;
    
    
    /* create the containers. */
    protected static VBox root = new VBox();
    protected static HBox secondMenu = new HBox();
    protected static MenuBar menuBar = new MenuBar();
    protected static Pane mapContainer = new Pane();
    protected static ImageView imageView = new ImageView();
    
    
    /* create the buttons. */
    protected static Button findPathButton = new Button("Find Path");
    protected static Button showConnectionButton = new Button("Show Connection");
    

    protected static Button newPlaceButton = new Button("New Place");
    protected static Button newConnectionButton = new Button("New Connection");
    
    protected static Button changeConnectionButton = new Button("Change Connection");
    
    
    @Override
    public void start(Stage stage) {
        PathFinder.stage = stage;
        
        stage.setTitle("PathFinder");
        
        /* specify size of stage. */
        stage.setWidth(800);
        stage.setHeight(400);


        /* specify the position of the stage relative to the screen. */
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();
        stage.setX((screenWidth - stage.getWidth()) / 2);
        stage.setY((screenHeight - stage.getHeight()) / 2);
        
        
        /* specify if you want resizable stage or not. */
        stage.setResizable(true);
        
        
        root.getChildren().add(menuBar);
        root.getChildren().add(secondMenu);
        
        scene1();
        stage.show();
    }
    
    
    
    
    public static void scene1() {
        
        scene = new Scene(root);
        
        //this HBox will center the image.
        HBox center = new HBox();
        center.setAlignment(Pos.CENTER);
        center.getChildren().add(mapContainer);
        mapContainer.setId("outputArea");
        mapContainer.getChildren().add(imageView);
        root.getChildren().add(center);
        
        
        createMenuBar();
        createSecondMenu();
        

        /* calling the method when the user presses the exist button of window. */
        stage.setOnCloseRequest(event -> {
            /* stop the window from closing.*/
            event.consume();
            /* call the method to determine if we should close or not. */
            exit();
        });
        
        
        /* put the scene in the stage. */
        stage.setScene(scene);
    }
    
    
    
    private static void createMenuBar() {
        
        Menu fileMenu = new Menu("File");
        MenuItem newMapMenuItem = new MenuItem("New Map");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem saveImageMenuItem = new MenuItem("Save Image");
        MenuItem exitMenuItem = new MenuItem("Exit");
        
        
        newMapMenuItem.setOnAction(event -> MenuBarController.loadMap(mapFilePath));
        openMenuItem.setOnAction(even -> MenuBarController.open(graphFilePath));
        saveMenuItem.setOnAction(event -> MenuBarController.save(graphFilePath, mapFilePath));
        saveImageMenuItem.setOnAction(event -> MenuBarController.saveImage(mapContainer).handle(event));
        exitMenuItem.setOnAction(event -> exit());
        
        
        menuBar.setId("menu");
        fileMenu.setId("menuFile");
        newMapMenuItem.setId("menuNewMap");
        openMenuItem.setId("menuOpenFile");
        saveMenuItem.setId("menuSaveFile");
        saveImageMenuItem.setId("menuSaveImage");
        exitMenuItem.setId("menuExit");
        
        
        fileMenu.getItems().addAll(newMapMenuItem, openMenuItem, saveMenuItem, saveImageMenuItem, exitMenuItem);
        menuBar.getMenus().add(fileMenu);
    }
    
    
    
    private static void createSecondMenu() {
        SecondMenuController scene1Controller = new SecondMenuController();
        
        secondMenu.setPrefHeight(50);
        secondMenu.setMaxHeight(50);
        secondMenu.setMinHeight(50);
        secondMenu.setSpacing(10);
        secondMenu.setAlignment(Pos.CENTER);
        secondMenu.setPadding(new Insets(10, 10, 10, 10));
        
        
        /* add event-listeners. */
        findPathButton.setOnAction(scene1Controller::findPath);
        showConnectionButton.setOnAction(scene1Controller::showConnection);
        newPlaceButton.setOnAction(scene1Controller::newPlace);
        newConnectionButton.setOnAction(scene1Controller::newConnection);
        changeConnectionButton.setOnAction(scene1Controller::changeConnection);
        
        /* add IDs. */
        findPathButton.setId("btnFindPath");
        showConnectionButton.setId("btnShowConnection");
        newPlaceButton.setId("btnNewPlace");
        newConnectionButton.setId("btnNewConnection");
        changeConnectionButton.setId("btnChangeConnection");
        
        
        /* disable(until an image is loaded). */
        findPathButton.setDisable(true);
        showConnectionButton.setDisable(true);
        newPlaceButton.setDisable(true);
        newConnectionButton.setDisable(true);
        changeConnectionButton.setDisable(true);
        
        
        secondMenu.getChildren().addAll(findPathButton, showConnectionButton, newPlaceButton, newConnectionButton, changeConnectionButton);
    }
    
    
    
    
    public static void renderMap(String mapFilePath){
        /* remove old map if it exists. */
        if(mapContainer != null){
            mapContainer.getChildren().clear();
            mapContainer.getChildren().add(imageView);
        }
        
        
        /* enable buttons. */
        findPathButton.setDisable(false);
        showConnectionButton.setDisable(false);
        newPlaceButton.setDisable(false);
        newConnectionButton.setDisable(false);
        changeConnectionButton.setDisable(false);
        
        
        
        image = new Image(mapFilePath);
        imageView.setImage(image);
        
        
        
        /* note: there is not need to cast to int, but this was needed to pass the automatic tests in vpl. */
        // calculating the desired width and height of the image based on the screen width and height.
        int screenWidth = (int) Screen.getPrimary().getVisualBounds().getWidth();
        int screenHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();
        int desiredMaximumImageWidth = (int) ( screenWidth * 0.8);
        int desiredMaximumImageHeight = (int) (screenHeight * 0.70);
        
        // these are the scaling factor for width and height.
        double scalingFactorWidth = desiredMaximumImageWidth / image.getWidth();
        double scalingFactorHeight = desiredMaximumImageHeight / image.getHeight();
        
        // calculating the scaling factor that is appropriate to preserve the aspect ratio of the image.
        double scalingFactor = Math.min(scalingFactorWidth, scalingFactorHeight);

        
        // using the scaling factor to calculate the appropriate width and height.
        int imageWidthAfterScaling = (int) ( image.getWidth() * scalingFactor );
        int imageHeightAfterScaling = (int) ( image.getHeight() * scalingFactor );
        
        // here we actually specify the dimensions of the image.
        imageView.setFitWidth(imageWidthAfterScaling);
        imageView.setFitHeight(imageHeightAfterScaling);
        
        
        // specify the stage dimensions based on the imageview dimensions.
        stage.setWidth(imageView.getFitWidth() );
        stage.setHeight(imageView.getFitHeight() + menuBar.getHeight() + secondMenu.getHeight() + 50);
        
        /* if the image is too small, then the stage will be too small for the buttons, so we scale it. */
        if(stage.getWidth() < 600){
            stage.setWidth(600);
        }

    }
    
    
    
    /* removes all the dots and connections, and redraws the graph. */
    public static void renderGraph(String mapFilePath){
        
        /* render image. */
        renderMap(mapFilePath);
        
        Set<Circle> circles = PathFinder.graph.getNodes();
        
        /* render connections. */
        for(Circle dot : circles){
            LinkedHashSet<Edge<Circle>> edges = PathFinder.graph.getEdgesFrom(dot);
            
            for(Edge<Circle> circle : edges){
                Line line = new Line(dot.getCenterX(), dot.getCenterY(),
                        circle.getDestination().getCenterX(), circle.getDestination().getCenterY());
                line.setStrokeWidth(3);
                mapContainer.getChildren().add(line);
            }
        }
        
        /* render nodes (after connections so that connections do not appear on top.). */
        for(Circle dot : circles){
            mapContainer.getChildren().add(dot);
        }
    }
    
    
    
    /* error message. */
    public static void error(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error!");
        errorAlert.setHeaderText(message);
        //errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
    
    
    
    
    public static boolean overrideUnsavedChanges(String message){
        /* check if there are unsaved changes. */
        if(unsavedChanges){
            boolean discard;
            discard = warning(message);
            
            /* override. */
            if(discard){
                unsavedChanges = false;
                return true;
            }
            /* do not override. */
            else{
                return false;
            }
        }
        
        /* no unsaved changes. */
        return true;
    }
    
    
    
    /* warning message. */
    public static boolean warning(String message){
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        
        /* create two buttons. */
        warningAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        
        warningAlert.setHeaderText(null);
        //warningAlert.setHeaderText(message);
        warningAlert.setContentText(message);
        //errorAlert.setContentText("Unsaved changes, continue anyway?");
        
        Optional<ButtonType> result = warningAlert.showAndWait();
        
        /* user pressed ok. */
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        }
        
        return false;
    }

    
    public static void exit(){
        
        if(!unsavedChanges){
            // we only have one stage, thus this will close the whole app.
            stage.close();
            //stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            //Platform.exit();
            //System.exit(0);
        }
        else {
            /* check if there are unsaved changes and if we should override them. */
            boolean override = overrideUnsavedChanges("Unsaved changes, exit anyway?");
            
            if(override){
                // we only have one stage, thus this will close the whole app.
                stage.close();
                //stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                //Platform.exit();
                //System.exit(0);
            }
        }

    }
    
    
    public static void main(String[] args) {
        launch();
    }
    
    
}

