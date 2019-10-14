package createEarthDEM;

import java.io.IOException;

import javax.vecmath.Vector3d;

import javafx.geometry.Point3D;

public class createSTLFile {
	public static void main(String[] args) throws IOException{
			
	getEarthDEM earth = new getEarthDEM(args[0]);
	Point3D[][] earthDEM = earth.getPointCloud(); 
	int width = earthDEM[0].length;
	int height = earthDEM.length;
	int numFaces = 2*(width-1)*(height-1)+4*(width-1)+4*(height-1)+2;
	STLWriter writer = new STLWriter();
	writer.beginSave(args[0]+".stl",numFaces);
	Vector3d a = new Vector3d();
	Vector3d b = new Vector3d();
	Vector3d c = new Vector3d();
	Vector3d d = new Vector3d();
	//int count = 0;
	for(int i=0; i<width-1; i++){
		for(int j=0; j<height-1; j++){
			//Define 4 vertices
			a = new Vector3d(1*earthDEM[i][j].getX(),1*earthDEM[i][j].getY(),earthDEM[i][j].getZ());
			b = new Vector3d(1*earthDEM[i+1][j].getX(),1*earthDEM[i+1][j].getY(),earthDEM[i+1][j].getZ());
			c = new Vector3d(1*earthDEM[i][j+1].getX(),1*earthDEM[i][j+1].getY(),earthDEM[i][j+1].getZ());
			d = new Vector3d(1*earthDEM[i+1][j+1].getX(),1*earthDEM[i+1][j+1].getY(),earthDEM[i+1][j+1].getZ());
			writer.face(a, c, d);
			writer.face(b, a, d);
		}
	}
	//top edge
	for(int k=0; k<width-1; k++){
		a = new Vector3d(1*earthDEM[k][0].getX(),1*earthDEM[k][0].getY(),earthDEM[k][0].getZ());
		b = new Vector3d(1*earthDEM[k+1][0].getX(),1*earthDEM[k+1][0].getY(),earthDEM[k+1][0].getZ());
		c = new Vector3d(1*earthDEM[k][0].getX(),1*earthDEM[k][0].getY(),0);
		d = new Vector3d(1*earthDEM[k+1][0].getX(),1*earthDEM[k+1][0].getY(),0);
		writer.face(a, b, c);
		writer.face(b, d, c);
	}
	//bottom edge
	for(int l=0; l<width-1; l++){
		a = new Vector3d(1*earthDEM[l][height-1].getX(),1*earthDEM[l][height-1].getY(),earthDEM[l][height-1].getZ());
		b = new Vector3d(1*earthDEM[l+1][height-1].getX(),1*earthDEM[l+1][height-1].getY(),earthDEM[l+1][height-1].getZ());
		c = new Vector3d(1*earthDEM[l][height-1].getX(),1*earthDEM[l][height-1].getY(),0);
		d = new Vector3d(1*earthDEM[l+1][height-1].getX(),1*earthDEM[l+1][height-1].getY(),0);
		writer.face(b, a, c);
		writer.face(b, c, d);
	}
	//left edge
	for(int m=0; m<height-1; m++){
	  a = new Vector3d(1*earthDEM[0][m].getX(),1*earthDEM[0][m].getY(),earthDEM[0][m].getZ());
	  c = new Vector3d(1*earthDEM[0][m+1].getX(),1*earthDEM[0][m+1].getY(),earthDEM[0][m+1].getZ());
	  b = new Vector3d(1*earthDEM[0][m].getX(),1*earthDEM[0][m].getY(),0);
	  d = new Vector3d(1*earthDEM[0][m+1].getX(),1*earthDEM[0][m+1].getY(),0);
	  writer.face(a, b, c);
	  writer.face(b, d, c);
	}
	
	//right edge
	for(int n=0; n<height-1; n++){
	  a = new Vector3d(1*earthDEM[width-1][n].getX(),1*earthDEM[width-1][n].getY(),earthDEM[width-1][n].getZ());
	  c = new Vector3d(1*earthDEM[width-1][n+1].getX(),1*earthDEM[width-1][n+1].getY(),earthDEM[width-1][n+1].getZ());
	  b = new Vector3d(1*earthDEM[width-1][n].getX(),1*earthDEM[width-1][n].getY(),0);
	  d = new Vector3d(1*earthDEM[width-1][n+1].getX(),1*earthDEM[width-1][n+1].getY(),0);
	  writer.face(b, a, c);
	  writer.face(b, c, d);
	}
	
	//very bottom
	a = new Vector3d(1*earthDEM[0][0].getX(),1*earthDEM[0][0].getY(),0);
	b = new Vector3d(1*earthDEM[width-1][0].getX(),1*earthDEM[width-1][0].getY(),0);
	c = new Vector3d(1*earthDEM[0][height-1].getX(),1*earthDEM[0][height-1].getY(),0);
	d = new Vector3d(1*earthDEM[width-1][height-1].getX(),1*earthDEM[width-1][height-1].getY(),0);
	writer.face(a, d, c);
	writer.face(a, b, d);
		
	writer.endSave();
	
	} //main bracket
} //class bracket
