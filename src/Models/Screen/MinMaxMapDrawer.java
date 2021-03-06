package Models.Screen;

import Models.Sample.MinMax;
import Models.Utilities.FileWorker;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import Models.Sample.Location;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.ArrayList;

public class MinMaxMapDrawer extends MapDrawer{

    // Data Fields
    private Location minLocation;
    private Location maxLocation;

    // Constructor
    public MinMaxMapDrawer(double MAP_HEIGHT, double MAP_WIDTH, double RATIO, double user_x, double user_y) throws IOException {
        super(MAP_HEIGHT, MAP_WIDTH, RATIO, user_x, user_y);
        minLocation = MinMax.min();
        maxLocation = MinMax.max();
        Location userLoc = FileWorker.readUserLocationFromFile();
        setUser_x(userLoc.getX()); setUser_y(userLoc.getY());
    }

    // Method
    @Override
    public Parent getDrawScene() throws Exception {
        Pane mapPane = (Pane) super.getDrawScene();
        double relXmax = this.relUser(maxLocation.getX(), 'x');
        double relYmax = this.relUser(maxLocation.getY(), 'y');
        double relXmin = this.relUser(minLocation.getX(), 'x');
        double relYmin = this.relUser(minLocation.getY(), 'y');

        Line maxLine = new Line((getMAP_WIDTH() / 2) - (relXmax / getRATIO()), (getMAP_HEIGHT() / 2) - (relYmax / getRATIO()),
                getMAP_WIDTH() / 2, getMAP_HEIGHT() / 2);
        maxLine.setFill(Color.YELLOW);
        maxLine.setStroke(Color.YELLOW);
        maxLine.setOpacity(0.5);
        mapPane.getChildren().add(maxLine);

        Line minLine = new Line((getMAP_WIDTH() / 2) - (relXmin / getRATIO()), (getMAP_HEIGHT() / 2) - (relYmin / getRATIO()),
                (getMAP_WIDTH() / 2), (getMAP_HEIGHT() / 2));
        minLine.setFill(Color.GREEN);
        minLine.setStroke(Color.GREEN);
        minLine.setOpacity(0.5);
        mapPane.getChildren().add(minLine);

        return mapPane;
    }
}
