package com.Haridroid.firstandroidproject;


import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class EnergyConsumptionPredictor {
    private Interpreter tflite;

    public EnergyConsumptionPredictor(Context context, String modelFilename) throws IOException {
        MappedByteBuffer modelBuffer = loadModelFile(context, modelFilename);
        tflite = new Interpreter(modelBuffer);
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFilename) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float predictEnergyConsumption(float temperature, float humidity, float lightIntensity, int occupancyStatus) {
        float[] inputValues = {temperature, humidity, lightIntensity, occupancyStatus};
        float[][] outputValues = new float[1][1];  // Assuming one output for energy consumption

        tflite.run(inputValues, outputValues);
        return outputValues[0][0];
    }
}