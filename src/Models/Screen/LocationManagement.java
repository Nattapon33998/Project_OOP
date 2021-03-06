package Models.Screen;

import Models.Sample.Location;
import Models.Utilities.FileWorker;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LocationManagement {

    //Data Fields
    private ArrayList<Location> locationList;
    private Stage locationListStage = new Stage();
    private ScrollPane locationListScroll = new ScrollPane();
    private boolean sortBy = false; // false -> distance, true -> date
    double pickX, pickY;


    // Constructor
    public LocationManagement() throws IOException {
        this.locationListStage.setWidth(400);
        this.locationListStage.setHeight(500);
        this.locationListStage.setTitle("NOPZ Location  |  Shop Management");
        this.locationListStage.getIcons().add(new Image(new FileInputStream(new File("src/Views/resource/Image/Icon.png"))));
        this.locationListStage.setResizable(false);

        this.locationListScroll.setMaxHeight(400);
        this.locationListScroll.setMaxWidth(365);

        renderStage();
    }

    // Setter
    public void setStage(Stage stage) {
        this.locationListStage = stage;
    }

    // Getter
    public Stage getStage() {
        return locationListStage;
    }

    // Method
    private void updateLocationListScroll() throws IOException {
        locationList = FileWorker.readLocationListFromFile();
        if(sortBy) {
            locationList = FileWorker.readUserLocationFromFile().sortByDistanceWith(locationList);
        }

        VBox locationListVbox = new VBox();
        locationListVbox.setPrefHeight(450);

        locationListVbox.setPrefWidth(350);
        for (int i = 0; i < locationList.size() ; i++) {
            int currentIndex = i;
            Location location = locationList.get(i);
            Label nameLocationLabel = new Label("ร้าน ["+ location.getName() + "]");
            nameLocationLabel.setPrefWidth(120);

            Label positionLabel = new Label("อยู่ที่ตำแหน่ง" + " ( " + (int)location.getX() + " , " + (int)location.getY() + ")");
            Label distantLabel = new Label("ระยะห่าง = " + (int) location.distanceWith(FileWorker.readUserLocationFromFile()) + " กิโลเมตร");
            positionLabel.setPrefWidth(200);
            distantLabel.setPrefWidth(200);

            Label deleteLocationLabel = new Label("ลบ");
            deleteLocationLabel.setTextFill(Color.RED);
            deleteLocationLabel.setPrefWidth(30);
            deleteLocationLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deleteLocationLabel.setUnderline(true);
                }
            });
            deleteLocationLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    deleteLocationLabel.setUnderline(false);
                }
            });
            deleteLocationLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage confirmDeleteStage = new Stage();
                    confirmDeleteStage.setTitle("Confirm");
                    confirmDeleteStage.setResizable(false);
                    confirmDeleteStage.setAlwaysOnTop(true);
                    confirmDeleteStage.setHeight(120);
                    confirmDeleteStage.setWidth(250);

                    VBox confirmDeleteContainer = new VBox();
                    confirmDeleteContainer.setAlignment(Pos.CENTER);
                    confirmDeleteContainer.setSpacing(10);
                    confirmDeleteContainer.setPadding(new Insets(10,10,10,10));
                    HBox btnContainer = new HBox();
                    btnContainer.setSpacing(10);

                    Label confirmLabel = new Label("ยืนยันการลบร้านค้า?");
                    confirmLabel.setAlignment(Pos.CENTER);
                    confirmLabel.setPrefWidth(confirmDeleteStage.getWidth());
                    confirmLabel.setPrefHeight(confirmDeleteStage.getHeight()/2);

                    Button okDelete = new Button("ยืนยัน");
                    okDelete.setPrefWidth(confirmDeleteStage.getWidth()/2);
                    okDelete.setPrefHeight(confirmDeleteStage.getHeight()/2);
                    okDelete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            try {
                                FileWorker.deleteLocationInListByIndex(currentIndex);
                                confirmDeleteStage.close();
                                renderStage();
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    });

                    Button cancelDelete = new Button("ยกเลิก");
                    cancelDelete.setPrefWidth(confirmDeleteStage.getWidth()/2);
                    cancelDelete.setPrefHeight(confirmDeleteStage.getHeight()/2);
                    cancelDelete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            confirmDeleteStage.close();
                        }
                    });

                    btnContainer.getChildren().addAll(okDelete, cancelDelete);
                    confirmDeleteContainer.getChildren().addAll(confirmLabel, btnContainer);

                    confirmDeleteStage.setScene(new Scene(confirmDeleteContainer));
                    confirmDeleteStage.show();

                    locationListStage.setOnHidden(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            confirmDeleteStage.close();
                        }
                    });
                }
            });

            HBox locationGroup = new HBox();
            locationGroup.setPrefWidth(400);
            locationGroup.setPrefHeight(60);
            locationGroup.setPadding(new Insets(10,10,10,10));
            locationGroup.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            VBox labelGroup = new VBox();
            labelGroup.setAlignment(Pos.BASELINE_LEFT);
            labelGroup.getChildren().addAll(positionLabel, distantLabel);
            locationGroup.getChildren().addAll(nameLocationLabel, labelGroup,deleteLocationLabel);
            locationListVbox.getChildren().add(locationGroup);
        }
        locationListScroll.setContent(locationListVbox);

    }
    public void renderStage() throws IOException {
        this.updateLocationListScroll();

        VBox mainContainer = new VBox();
        mainContainer.setPrefWidth(locationListStage.getWidth());
        mainContainer.setPrefHeight(locationListStage.getHeight());
        mainContainer.setSpacing(10);
        mainContainer.setPadding(new Insets(10,10,10,10));

        HBox topContainer = new HBox();
        topContainer.setSpacing(10);
        topContainer.setPadding(new Insets(10, 10, 10, 10));
        Label sortBy = new Label("จัดเรียงตามระยะทาง");
        Label changeSortBy = new Label("เปลี่ยน");
        changeSortBy.setTextFill(Color.BLUE);
        topContainer.setAlignment(Pos.TOP_LEFT);
        topContainer.getChildren().addAll(changeSortBy, sortBy);
        changeSortBy.setOnMouseEntered(e->{
            changeSortBy.setUnderline(true);
        });
        changeSortBy.setOnMouseExited(e->{
            changeSortBy.setUnderline(false);
        });
        changeSortBy.setOnMouseClicked(e->{
            if(this.sortBy) {
                sortBy.setText("จัดเรียงตามวันที่");
            } else {
                sortBy.setText("จัดเรียงตามระยะทาง");
            }
            this.sortBy = !this.sortBy;
            try {
                this.updateLocationListScroll();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        HBox btnContainer = new HBox();
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.setSpacing(10);

        Button addLocationBtn = new Button("เพิ่มร้านค้า");
        addLocationBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage addLocationStage = new Stage();
                addLocationStage.setTitle("Add Shop Location");
                addLocationStage.setResizable(false);

                VBox mainContainer = new VBox();
                mainContainer.setAlignment(Pos.CENTER);
                mainContainer.setSpacing(10);
                mainContainer.setPadding(new Insets(10,10,10,10));

                HBox btnContainer = new HBox();
                btnContainer.setAlignment(Pos.CENTER);
                btnContainer.setSpacing(10);

                FormField locationName = new FormField("ชื่อร้าน", 100, false);

                Label pickedLocationLabel = new Label("( ? , ? )");

                Button pickLocation = new Button("เลือกที่อยู่ร้าน");
                pickLocation.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try{
                            double MAP_HEIGHT = 750.f, MAP_WIDTH = 1125.f;
                            double RATIO = 10.0f;
                            Location user = FileWorker.readUserLocationFromFile();
                            SelectableMapDrawer mapDrawer = new SelectableMapDrawer(MAP_HEIGHT, MAP_WIDTH, RATIO, user.getX(), user.getY());
                            mapDrawer.getMapStage().setTitle("NOPZ Location  |  Set Shop Location");
                            mapDrawer.getMapStage().show();

                            mapDrawer.getMapStage().setOnHidden(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent windowEvent) {
                                    try{
                                        if(mapDrawer.isPicked()){
                                            pickX = mapDrawer.getPickX();
                                            pickY = mapDrawer.getPickY();

                                            pickedLocationLabel.setText("( " + (int)pickX + " , " + (int)pickY + " )");
                                        }
                                    }
                                    catch (Exception exception){
                                        System.out.println(exception);
                                    }
                                }
                            });
                        }
                        catch(Exception exception){
                            System.out.println(exception);
                        }
                    }
                });

                Button okBtn = new Button("ตกลง");
                okBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String name = locationName.getEnteredText();

                        try {
                            FileWorker.writeLocationInListToFile(pickX, pickY, name);
                            pickX = 0.f; pickY = 0.f;
                            addLocationStage.close();
                            renderStage();
                        }
                        catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });

                Button cancelBtn = new Button("ปิด");
                cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        addLocationStage.close();
                    }
                });

                btnContainer.getChildren().addAll(okBtn, cancelBtn);
                mainContainer.getChildren().addAll(locationName.getNode(), pickedLocationLabel, pickLocation, btnContainer);

                addLocationStage.setScene(new Scene(mainContainer));
                addLocationStage.show();

                locationListStage.setOnHidden(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        addLocationStage.close();
                    }
                });
            }
        });

        Button exitBtn = new Button("ออก");
        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                locationListStage.close();
            }
        });

        btnContainer.getChildren().addAll(addLocationBtn, exitBtn);
        mainContainer.getChildren().addAll(topContainer, locationListScroll, btnContainer);

        Scene scene = new Scene(mainContainer);
        this.locationListStage.setScene(scene);
        this.locationListStage.show();
    }
}
