package createEarthDEM;

import java.awt.image.Raster;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.TIFFDirectory;
import com.sun.media.jai.codecimpl.TIFFImageDecoder;

import javafx.geometry.Point3D;

public class getEarthDEM{
	
	FileSeekableStream stream;
	
	public getEarthDEM(String filename) throws IOException{
		stream = new FileSeekableStream(filename);
	}
	//public static void main(String[] args) throws IOException{
		
		
		//stream for TIFF
		//FileSeekableStream stream = null;
		//stream = new FileSeekableStream(args[0]);
		
	public Point3D [][] getPointCloud() throws IOException{	
		ParameterBlock params = new ParameterBlock();
		params.add(stream);
		
		TIFFDecodeParam decodeParam = new TIFFDecodeParam();
		decodeParam.setDecodePaletteAsShorts(true);
		TIFFImageDecoder decoder = new TIFFImageDecoder(stream,decodeParam);
		Raster georaster = decoder.decodeAsRaster();
		
		
		 //We assume there is only 1 directory, therefore
		 //the index of the directory is 0
		 TIFFDirectory geodirectory = new TIFFDirectory(stream,0);
		 //TIFFField[] geofield = geodirectory.getFields();		 
		 //int [] x = geodirectory.getTags();
		 //System.out.println(Arrays.toString(x));
		 //Tag 257 = Image Length/Height
		 int imgheight  = geodirectory.getField(257).getAsInt(0);
		 //Tag 256 = ImageWidth
		 int imgwidth  = geodirectory.getField(256).getAsInt(0);
		 //Tag 33550 = ModelPixelScale
		 double scale = geodirectory.getField(33550).getAsDouble(0);
		 scale = 1; 
		 //Tag 33922 = ModelTiePointTag, Format  double[6] (I,J,K,X,Y,Z)
		 //Values represent image/model corners
		 double imgXVal = geodirectory.getField(33922).getAsDouble(0);
		 double imgYVal = geodirectory.getField(33922).getAsDouble(1);
		 double imgZVal = geodirectory.getField(33922).getAsDouble(2);
		 double modelXVal = geodirectory.getField(33922).getAsDouble(3);
		 double modelYVal = geodirectory.getField(33922).getAsDouble(4);
		 double modelZVal = geodirectory.getField(33922).getAsDouble(5);
		 System.out.println("Model left-top corner is:" + modelXVal +"," + modelYVal);
		 double x_coor;
		 double y_coor;
		 double z_coor;
		 if(imgZVal !=0)
		 {
			 System.out.println("Note: imgZVal != 0");
		 }
		 if(imgXVal!=0 || imgYVal!=0)
		 {
			 System.out.println("Note: ModelTiePoint is not centered at (0,0)");
			 System.exit(0);
		 }
		 if(modelZVal != 0)
		 {
			 System.out.println("Note: modelZVal != 0");
		 }
		 int [] geopixel1 = new int[3];
		 
		 //Change depending on size (N*N) of TIFF, for 6000 pixels, n = 100 is good
		 //determines resolution of picture
		 int n = imgwidth/60;
		 int reswidth = imgwidth/n;
		 int resheight = imgheight/n;
		 Point3D [][] earthDEM = new Point3D[reswidth][resheight];
		 //conversion factor to change from decimal degrees to meters
		 double xyconv= 1;
		 double sizeAdjust = .01;
		 for(int i=0; i<reswidth; i++)
		 {
			 for(int j=0; j<reswidth; j++)
			 {
				 z_coor= georaster.getPixel(i*n,j*n, geopixel1)[0] +modelZVal;
	
				//may have to cast geopixel2 from int to double
				if(z_coor<0)
				{
					z_coor=0;
					System.err.println("Error: negative z values");
				}
				x_coor = sizeAdjust*xyconv*(180+modelXVal+scale*(i*n));
				//System.out.println(90+modelYVal-scale*(j*n));
				y_coor = sizeAdjust*xyconv*(90+modelYVal-scale*(j*n));
				z_coor = sizeAdjust*z_coor;
				//Necessary to add 180/90 to longitudes/latitude since STL coord must be > 0
				 earthDEM[i][j] = new Point3D(x_coor,y_coor,z_coor);
			 }
		 }
		 return earthDEM;
	}
//		 Point3D l = earthDEM[599][599];
//		 System.out.println(l.getX() + "," + l.getY() + "," + l.getZ());
//		 System.out.println(65-scale*6000);
//		}
		 
	}
