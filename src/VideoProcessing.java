import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;



public class VideoProcessing extends JFrame {
	
    private BufferedImagePanel imgPanel1;
    private BufferedImagePanel imgPanel2;
    private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

	
	public VideoProcessing() {
		imgPanel1 = null;
		imgPanel2 = null;

		createFrame();
		processShowVideo();

	}

	private String getFilePathName() {
		// Begin: Get file path and name from "getRessource"
		// File name determination using getResource (seems to be buggy)
/*		String filePathName = getClass().getResource("./Landscape.avi").getPath();
		filePathName = filePathName.substring(1);  // remove the bug
*/		// End: Get file path and name from "getRessource"

		
		// Begin: Set relative path and file name 
//		String filePathName = "videos\\Landscape.avi";
		// End: Set relative path and file name 
		
		// Choose file path and file name via a file selector box
        int returnVal = fileChooser.showOpenDialog(this); 
        if(returnVal != JFileChooser.APPROVE_OPTION) {
            return null;  // cancelled
        }
        File selectedFile = fileChooser.getSelectedFile();
        String filePathName = selectedFile.getPath();		
		// End: Choose file path and file name via a file selector box
	    
	    System.out.println("Video file name: " + filePathName);
		return filePathName;
	}
	
	/**
	 * @param imgMat image matrix to be written to a file
	 * @param filename name of the file to be created
	 */
	private void writeImage(Mat imgMat, String filename) {
		String filePathName = "videos/" + filename;
	    Imgcodecs.imwrite(filePathName, imgMat, 
	    		new MatOfInt(Imgcodecs.CV_IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY,
	    					 Imgcodecs.CV_IMWRITE_PNG_STRATEGY_FIXED));			    
	}
	
	
	/**
	 * Create the JFrame to be displayed, displaying two images.
	 */
	private void createFrame() {

		setTitle("Original and processed video stream");
		
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new FlowLayout());
		
		imgPanel1 = new BufferedImagePanel();
		contentPane.add(imgPanel1);
		imgPanel2 = new BufferedImagePanel();
		contentPane.add(imgPanel2);
				
	       // place the frame at the center of the screen and show
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Reades a video stream from a file or camera, displays the original frames,
	 * processes the frames and displays the result.
	 */
	private void processShowVideo() {

		// BEGIN: Prepare streaming from internal web cam
//    	VideoCapture cap = new VideoCapture(0);
		// END: Prepare streaming from internal web cam

		// BEGIN: Prepare streaming from video file
		//String filePathName = getFilePathName();
		VideoCapture cap = new VideoCapture(0);
		// END: Prepare streaming from video file

		Mat frame = new Mat();
		// Check of file or camera can be opened
		if (!cap.isOpened())
			throw new CvException("The Video File or the Camera could not be opened!");

		cap.read(frame);
		System.out.println("  First grabbed frame: " + frame);
		System.out.println("  Matrix columns: " + frame.cols());
		System.out.println("  Matrix rows: " + frame.rows());
		System.out.println("  Matrix channels: " + frame.channels());

		frameRate(cap, frame);

		cap.release();

	}

	   public void frameRate(VideoCapture cap, Mat frame) {

		while (cap.read(frame)) {
			Mat tmp;
		}
    	   // display original frame from the video stream
    	   imgPanel1.setImage(Mat2BufferedImage(frame));

    	   // Show processed image
    	   imgPanel2.setImage(Mat2BufferedImage(frame));
    	   pack();
	}


    public BufferedImage Mat2BufferedImage(Mat imgMat){
    	int bufferedImageType = 0;
    	switch (imgMat.channels()) {
		case 1:
			bufferedImageType = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			bufferedImageType = BufferedImage.TYPE_3BYTE_BGR;
			break;
		default:
    		throw new IllegalArgumentException("Unknown matrix type. Only one byte per pixel (one channel) or three bytes pre pixel (three channels) are allowed.");
		}
    	BufferedImage bufferedImage = new BufferedImage(imgMat.cols(), imgMat.rows(), bufferedImageType);
    	final byte[] bufferedImageBuffer = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    	imgMat.get(0, 0, bufferedImageBuffer);
    	return bufferedImage;
    }
}
