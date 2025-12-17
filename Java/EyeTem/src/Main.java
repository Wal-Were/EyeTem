import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Main {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Path to your .tflite model
    static final String MODEL_PATH = "model_unquant.tflite";

    public static MappedByteBuffer loadModelFile(String modelPath) throws Exception {
        File file = new File(modelPath);
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel fileChannel = inputStream.getChannel();
        long size = fileChannel.size();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
    }

    public static void main(String[] args) {
        // Load TFLite model
        Interpreter interpreter;
        try {
            MappedByteBuffer tfliteModel = loadModelFile(MODEL_PATH);
            interpreter = new Interpreter(tfliteModel);
            System.out.println("TensorFlow Lite model loaded!");
        } catch (Exception e) {
            System.err.println("Failed to load model: " + e.getMessage());
            return;
        }

        // Open camera
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.err.println("Camera not found.");
            return;
        }

        Mat frame = new Mat();
        System.out.println("Press ESC to exit.");

        while (true) {
            camera.read(frame);
            if (frame.empty()) continue;

            // Resize frame if necessary (example: 224x224 for model)
            Mat resized = new Mat();
            Imgproc.resize(frame, resized, new Size(224, 224));

            // TODO: Convert resized frame to input tensor for interpreter
            // For now we just skip actual model inference
            String prediction = "Bottle detected!"; // placeholder

            // Draw prediction on frame
            Imgproc.putText(frame, prediction, new Point(10, 30),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 255, 0), 2);

            HighGui.imshow("EyeTem Camera Feed", frame);
            if (HighGui.waitKey(30) == 27) break; // ESC to exit
        }

        camera.release();
        HighGui.destroyAllWindows();
        interpreter.close();
    }
}
