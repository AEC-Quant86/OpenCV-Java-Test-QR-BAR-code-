package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.image.BufferedImage;

public class QrCode {
	
	
	
		public static void scan(BufferedImage frame)   {
			
				
			    LuminanceSource source = new BufferedImageLuminanceSource(frame);
			    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			    Reader reader = new MultiFormatReader();
			    GenericMultipleBarcodeReader readerM = new GenericMultipleBarcodeReader(reader);
			    //Result result;
				try {
					Result[] result = readerM.decodeMultiple(bitmap);
					// System.out.println("Barcode text is " + result.getText());
					for (Result r : result) {
						
						System.out.println("Barcode text is " + r.getText());
					}
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					
				}	
		}
		
		public static Mat rotateImg(Mat img, double deg) {
			//Mat img = new Mat();
	
			Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2BGRA);
			// Трансформация вращения
			//double angle = 45;
			Mat M = Imgproc.getRotationMatrix2D(
			         new Point(img.width() / 2, img.height() / 2), deg, 1);
			// Расчет размеров и положения
			Rect rect = new RotatedRect(
			         new Point(img.width() / 2, img.height() / 2),
			         new Size(img.width(), img.height()), deg).boundingRect();
			// Корректировка матрицы трансформации
			double[] arrX = M.get(0, 2);
			double[] arrY = M.get(1, 2);
			arrX[0] -= rect.x;
			arrY[0] -= rect.y;
			M.put(0, 2, arrX);
			M.put(1, 2, arrY);
			// Трансформация
			Mat img2 = new Mat();
			Imgproc.warpAffine(img, img2, M, rect.size(),
			      Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT,
			      new Scalar(255, 255, 255, 255));
		
			//img.release(); 
			M.release();
			//img2.release();


			
			return img2;
		}

		public static void rotaitingScan(Mat img1) {
			for (int i=0; i<=360;i+=15 ) {
				
			
				Mat img2 = rotateImg(img1,i);
				BufferedImage frame1 = CvUtils.MatToBufferedImage(img2);
				scan(frame1);
			}
			
		}
}
