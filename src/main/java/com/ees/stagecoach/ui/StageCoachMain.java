package com.ees.stagecoach.ui;

import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * Book: Pro Java FX 2 Java 8.
 * Chapter 2
 * 
 * It is not a copy and paste.
 * 
 * @author eschlei
 *
 */
public class StageCoachMain extends Application {

    private StringProperty title = new SimpleStringProperty();

    private Text textStageX;
    private Text textStageY;
    private Text textStageW;
    private Text textStageH;
    private Text textStageF;
    private double dragAnchorX;
    private double dragAnchorY;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

        final StageStyle stageStyle = getStageStyle();

        Group rootGroup = FXUtil.build(new Group(), rGroup -> {

            FXUtil.build(new Rectangle(250, 350), rect -> {
                rect.setArcWidth(50);
                rect.setArcHeight(50);
                rect.setFill(Color.SKYBLUE);
                rGroup.getChildren().add(rect);
            });


            FXUtil.build(new VBox(), vbox -> {
                vbox.setLayoutX(30);
                vbox.setLayoutY(20);
                vbox.setSpacing(10);

                textStageX = createText(VPos.TOP, vbox);
                textStageY = createText(VPos.TOP, vbox);
                textStageW = createText(VPos.TOP, vbox);
                textStageH = createText(VPos.TOP, vbox);
                textStageF = createText(VPos.TOP, vbox);

                textStageX.textProperty().bind(new SimpleStringProperty("x: ").concat(stage.xProperty().asString()));
                textStageY.textProperty().bind(new SimpleStringProperty("y: ").concat(stage.yProperty().asString()));
                textStageW.textProperty()
                        .bind(new SimpleStringProperty("width: ").concat(stage.widthProperty().asString()));
                textStageH.textProperty()
                        .bind(new SimpleStringProperty("height: ").concat(stage.heightProperty().asString()));
                textStageF.textProperty()
                        .bind(new SimpleStringProperty("focused: ").concat(stage.focusedProperty().asString()));

                FXUtil.build(new CheckBox("resizable"), chk -> {
                    chk.setDisable(isResizableDisable(stageStyle));
                    chk.selectedProperty().bindBidirectional(stage.resizableProperty());
                    vbox.getChildren().add(chk);
                });
                
                FXUtil.build(new CheckBox("fullScreen"), chk -> {
                    chk.selectedProperty().addListener((ov, oldV, newV) -> {
                        stage.setFullScreen(chk.selectedProperty().getValue());
                    });
                    vbox.getChildren().add(chk);
                });

                FXUtil.build(new HBox(), hbox -> {
                    hbox.getChildren().add(new Label("title"));
                    FXUtil.build(new TextField("Stage Coach"), txt -> {
                        txt.setPrefColumnCount(15);
                        title.bind(txt.textProperty());
                        hbox.getChildren().add(txt);
                    });
                    vbox.getChildren().add(hbox);
                });


                FXUtil.build(new Button("toBack()"), btn -> {
                    btn.setOnAction(ae -> {
                        stage.toBack();
                    });
                    vbox.getChildren().add(btn);
                });

                FXUtil.build(new Button("toFront()"), btn -> {
                    btn.setOnAction(ae -> {
                        stage.toFront();
                    });
                    vbox.getChildren().add(btn);
                });

                FXUtil.build(new Button("close()"), btn -> {
                    btn.setOnAction(ae -> {
                        stage.close();
                    });
                    vbox.getChildren().add(btn);
                });

                rGroup.getChildren().add(vbox);
            });

            rGroup.setOnMousePressed(me -> {
                dragAnchorX = me.getScreenX() - stage.getX();
                dragAnchorY = me.getScreenY() - stage.getY();
            });
            // When screen is dragged, translate it accordingly
            rGroup.setOnMouseDragged(me -> {
                stage.setX(me.getScreenX() - dragAnchorX);
                stage.setY(me.getScreenY() - dragAnchorY);
            });
        });

        Scene scene = new Scene(rootGroup, 270, 370);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.titleProperty().bind(title);
        stage.initStyle(stageStyle);
        stage.setResizable(true);

        stage.setOnCloseRequest((we) -> {
            System.out.println("Stage is closing");
        });

        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 4);
    }

    private boolean isResizableDisable(StageStyle stageStyle) {
        return stageStyle == StageStyle.TRANSPARENT || stageStyle == StageStyle.UNDECORATED;
    }

    private Text createText(final VPos pos, VBox vbox) {
        return FXUtil.build(new Text(), text -> {
            text.setTextOrigin(pos);
            vbox.getChildren().add(text);
        });
    }

    private StageStyle getStageStyle() {
        StageStyle stageStyle = StageStyle.DECORATED;
        List<String> unnamedParams = this.getParameters().getUnnamed();
        if (!unnamedParams.isEmpty()) {
            String stageStyleParam = unnamedParams.get(0);
            switch (stageStyleParam) {
            case "transparent":
                stageStyle = StageStyle.TRANSPARENT;
                break;
            case "undecorated":
                stageStyle = StageStyle.UNDECORATED;
                break;
            case "utility":
                stageStyle = StageStyle.UTILITY;
                break;
            }
        }
        return stageStyle;
    }
}
