import org.opencv.core.Core;

public class Main {

public static void main(String[] args) {
		// Load OpenCV libraries and start program
	    System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		new VideoProcessing();
	}

}
