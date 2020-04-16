package Models.Screen;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import Models.Sample.Location;

import java.io.IOException;
import java.util.ArrayList;

public class MinMaxMapDrawer extends MapDrawer{

    // Data Fields
    private double minRadius;
    private double maxRadius;

    // Constructor
    public MinMaxMapDrawer(double MAP_HEIGHT, double MAP_WIDTH, double RATIO, double user_x, double user_y) throws IOException {
        super(MAP_HEIGHT, MAP_WIDTH, RATIO, user_x, user_y);
        minRadius = min();
        maxRadius = max();
    }

    // Method
    @Override
    public Parent getDrawScene() throws Exception {
        Pane mapPane = (Pane) super.getDrawScene();
        Circle minRadiusOfInterested = new Circle((minRadius + 50) / this.getRATIO());
        minRadiusOfInterested.setCenterX(this.getMAP_WIDTH() / 2);
        minRadiusOfInterested.setCenterY(this.getMAP_HEIGHT() / 2);
        minRadiusOfInterested.setOpacity(0.3);
        minRadiusOfInterested.setFill(Color.GREEN);

        Circle maxRadiusOfInterested = new Circle((maxRadius + 50) / this.getRATIO());
        maxRadiusOfInterested.setCenterX(this.getMAP_WIDTH() / 2);
        maxRadiusOfInterested.setCenterY(this.getMAP_HEIGHT() / 2);
        maxRadiusOfInterested.setOpacity(0.3);
        maxRadiusOfInterested.setFill(Color.YELLOW);
        mapPane.getChildren().addAll(minRadiusOfInterested, maxRadiusOfInterested);

        return mapPane;
    }
    public double min() {
        double relX, relY;
        ArrayList<Double> a = new ArrayList<>();
        for (Location l : getLocs()) {
            relX = this.relUser(l.getX(), 'x');
            relY = this.relUser(l.getY(), 'y');
            if(relX != 0 && relY != 0)
                a.add(Math.sqrt((relX*relX) + (relY*relY)));
            else if(relX == 0 && relY != 0)
                a.add(Math.abs(relY));
            else if(relX != 0 && relY == 0)
                a.add(Math.abs(relX));
            else
                a.add(0.0);
        }
        double min = a.get(0);
        for (int i=0;i<a.size();i++) {
            if(min>a.get(i))
                min = a.get(i);
            else{}
        }
        return min;
    }
    public double max() {
        double relX, relY;
        ArrayList<Double> a = new ArrayList<>();
        for (Location l : getLocs()) {
            relX = this.relUser(l.getX(), 'x');
            relY = this.relUser(l.getY(), 'y');
            if(relX != 0 && relY != 0)
                a.add(Math.sqrt((relX*relX) + (relY*relY)));
            else if(relX == 0 && relY != 0)
                a.add(Math.abs(relY));
            else if(relX != 0 && relY == 0)
                a.add(Math.abs(relX));
            else
                a.add(0.0);
        }
        double max = a.get(0);
        for (int i=0;i<a.size();i++) {
            if(max<a.get(i))
                max = a.get(i);
            else{}
        }
        return max;
    }
}
