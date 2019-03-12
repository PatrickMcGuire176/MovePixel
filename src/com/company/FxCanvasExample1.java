package com.company;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.nio.ByteBuffer;
import java.util.Scanner;

public class FxCanvasExample1 extends Application {
    Scanner sc = new Scanner(System.in);

    int counter = 0;

    int xpos = 190;
    int ypos = 90;

    public static void main(String[] args) throws InterruptedException {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws InterruptedException {

        Canvas canvas = new Canvas(400, 200);
        Canvas canvas2 = new Canvas(400, 200);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();
        root.getChildren().addAll(canvas, canvas2);

        Scene scene = new Scene(root);

        writePixels(gc,xpos,ypos, 50,50);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        gc.clearRect(0, 0, 400, 400);
                        writePixels(gc,xpos,ypos, 50,50);
                        System.out.println("runnable " + counter);
                    }
                };
                while (sc.hasNextLine()) {
                    switch(sc.nextLine()){
                        case "UP":
                            counter++;
                            System.out.println("next line " + counter);
                            xpos += 0;
                            ypos -= 10;
                            Platform.runLater(updater);
                            break;
                        case "DOWN":
                            counter++;
                            System.out.println("next line " + counter);
                            xpos += 0;
                            ypos += 10;
                            Platform.runLater(updater);
                            break;
                        case "LEFT":
                            counter++;
                            System.out.println("next line " + counter);
                            xpos -= 10;
                            ypos += 0;
                            Platform.runLater(updater);
                            break;
                        case "RIGHT":
                            counter++;
                            System.out.println("next line " + counter);
                            xpos += 10;
                            ypos += 0;
                            Platform.runLater(updater);
                            break;
                        default:
                            System.out.println("Case Done");
                    }
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        stage.setScene(scene);
        stage.setTitle("Creation of a Canvas");
        stage.show();
        System.out.println("Finish Start");
    }

    private void writePixels(GraphicsContext gc, int xpos, int ypos, int pixelHeight, int pixelWidth) {

        byte[] pixels = new byte[pixelWidth * pixelHeight / 2];

        PixelWriter pixelWriter = gc.getPixelWriter();

        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
        if (pixelHeight >= 10 && pixelWidth >= 10) {
            pixelWriter.setPixels(xpos, ypos, pixelWidth, pixelHeight,
                    pixelFormat, pixels, 0, 0);
        } else {
            pixelWriter.setPixels(10, 10, pixelWidth, pixelHeight,
                    pixelFormat, new byte[100], 0, 0);
        }
    }
}
