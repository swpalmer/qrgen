/*
 * Copyright 2021 Scott W. Palmer
 * All Rights Reserved.
 */
package com.analogideas.qrgen.gui;

import com.analogideas.qrgen.BitMatrix;
import com.analogideas.qrgen.DataCapacity;
import com.analogideas.qrgen.ECL;
import com.analogideas.qrgen.QRCode;
import com.analogideas.qrgen.QRCodeGen;
import com.analogideas.qrgen.png.PngWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Generate QR Codes based on ISO/IEC 18004:2015
 *
 * @author scott
 */
public class QRCodeFX extends Application {

    private Stage stage;
    private ImageView imageView;
    private ComboBox<Integer> versionCombo;
    private TextField payloadTF;
    private QRCode qrCode;

    private int pixelSize = 4;

    /** Constructs a new QRCodeFX instance. */
    public QRCodeFX() {}

    /**
     * Standard entry point.
     * @param args the command line arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        imageView = new ImageView();

        // ObservableList<Integer> versionList = FXCollections.observableArrayList();
        // for (int v = 1; v <= 40; v++) versionList.add(v);
        // versionCombo = new ComboBox<>(versionList);
        // versionCombo.setValue(1);
        // var next = new Button("Next");
        // next.setOnAction(ae -> {
        //     versionCombo.setValue(Math.min(40, versionCombo.getValue() + 1));
        //     test(ae);
        // });

        payloadTF = new TextField();
        payloadTF.setPromptText("enter payload string");
        payloadTF.setMaxWidth(Double.MAX_VALUE);
        // versionCombo.disableProperty().bind(Bindings.not(Bindings.isEmpty(payloadTF.textProperty())));

        var button = new Button("Generate");
        var saveButton = new Button("Save...");
        saveButton.setOnAction(this::save);
        saveButton.disableProperty().bind(Bindings.isNull(imageView.imageProperty()));
        var root = new VBox(
            4,
            // new HBox(4, versionCombo, next),
            payloadTF,
            new HBox(8, button, saveButton),
            imageView
        );
        root.setPadding(new Insets(8));
        button.disableProperty().bind(Bindings.not(Bindings.isEmpty(payloadTF.textProperty())));
        button.setOnAction(this::generate);
        stage.setScene(new Scene(root));
        stage.setTitle("QR Code Image Generator");
        stage.setMinWidth(160);
        stage.setMinHeight(200);
        stage.show();
    }

    private void generate(ActionEvent ae) {
        String payload = payloadTF.getText();
        QRCode qrCode = QRCodeGen.generate(payload, ECL.M);
        imageView.setImage(getImage(qrCode, pixelSize));
        stage.setMinWidth(160 + imageView.getImage().getWidth());
        stage.setMinHeight(200 + imageView.getImage().getHeight());
    }

    private void save(ActionEvent ae) {
        if (qrCode == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save QR Code Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                PngWriter.write(qrCode, pixelSize, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Converts a {@link QRCode} into a {@link Image} for display.
     *
     * @param code the QR code to convert
     * @param pixelSize the size of each module in the image
     * @return the image representation of the QR code
     */
    public static Image getImage(QRCode code, int pixelSize) {
        final int PIXEL_SIZE = Math.max(1, pixelSize);
        BitMatrix matrix = code.getMatrix();
        int d = matrix.dim();
        // image with 4px border on each side (quiet zone)
        var image = new WritableImage(PIXEL_SIZE * (d + 8), PIXEL_SIZE * (d + 8));
        Rectangle background = new Rectangle(image.getWidth(), image.getHeight());
        background.setFill(Color.WHITE);
        background.snapshot(null, image);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int y = 0; y < d; y++) {
            for (int x = 0; x < d; x++) {
                if (matrix.get(x, y)) {
                    for (int i = 0; i < PIXEL_SIZE; i++) {
                        for (int j = 0; j < PIXEL_SIZE; j++) pixelWriter.setColor(
                            (x + 4) * PIXEL_SIZE + j,
                            (y + 4) * PIXEL_SIZE + i,
                            Color.BLACK
                        );
                    }
                }
            }
        }
        return image;
    }
}
