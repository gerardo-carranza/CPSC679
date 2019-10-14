package createEarthDEM;


import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.vecmath.Vector3d;


/**
 * A simple, but flexible and memory efficient exporter for binary STL files.
 * Custom color support is implemented via the STLcolorModel interface and the
 * exporter comes with the 2 most common format variations defined by the
 * DEFAULT and MATERIALISE constants.
 * 
 * The minimal design of this exporter means it does not build an extra list of
 * faces in RAM and so is able to easily export models with millions of faces.
 * 
 * http://en.wikipedia.org/wiki/STL_(file_format)
 * based on work by
 * @author kschmidt
 * 
 */
public class STLWriter {

	//public static final STLColorModel DEFAULT = new DefaultSTLColorModel();

	//public static final STLColorModel MATERIALISE = new MaterialiseSTLColorModel(
	//		0xffffffff);

	protected DataOutputStream ds;

	protected Vector3d scale = new Vector3d(1, 1, 1);

	protected boolean useInvertedNormals = false;

	protected byte[] buf = new byte[4];

	//protected STLColorModel colorModel;

	public STLWriter() {
		//this(DEFAULT);
	}

//	public STLWriter(STLColorModel cm) {
//		colorModel = cm;
//	}

	public void beginSave(String fn, int numFaces) {
		try {
			ds = new DataOutputStream(new FileOutputStream(fn));
			writeHeader(numFaces);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void endSave() {
		try {
			ds.flush();
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void face(Vector3d a, Vector3d b, Vector3d c) {
		face(a, b, c, 0);
	}

	public void face(Vector3d a, Vector3d b, Vector3d c, int rgb) {
		// normal
		//subtracting vertices basically
		//mutable vertices
		//DO NOT USE Vector c_mut = c b/c that will make the Vector point to it
		//If c_mut changes, so does c
		Vector3d c_mut = new Vector3d(c.x,c.y,c.z);
		Vector3d b_mut = new Vector3d(b.x,b.y,b.z);
		c_mut.sub(a);
		b_mut.sub(a);
		Vector3d normal = new Vector3d();
		//MAY HAVE TO SWITCH IF I END UP GETTING NEGATIVE
		normal.cross(c_mut,b_mut);
		normal.normalize();
//			if (useInvertedNormals) {
//				normal.invert();
//			}
		//System.out.println("normal = " + normal.x + " " + normal.y + " " + normal.z);
		writeVector(normal);
		// vertices
		//System.out.println("'a' = " + a.x + " " +a.y + " " + a.z);
		//System.out.println("'b' = " + b.x + " " +b.y + " " + b.z);
		//System.out.println("'c' = " + c.x + " " +c.y + " " + c.z);
		writeVector(a);
		writeVector(b);
		writeVector(c);
		// vertex attrib (color)
			if (rgb != 0) {
				System.out.println("Error:Entered RGB if statement");
				//writeShort(colorModel.formatRGB(rgb));
			} else {
				try {
					writeShort(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	private final void prepareBuffer(int a) {
	//Do not fully understand this 
		buf[3] = (byte) (a >>> 24);
		buf[2] = (byte) (a >> 16 & 0xff);
		buf[1] = (byte) (a >> 8 & 0xff);
		buf[0] = (byte) (a & 0xff);
	}

	public void setScale(float s) {
		scale.set(s, s, s);
	}

	public void setScale(Vector3d s) {
		scale.set(s);
	}

	public void useInvertedNormals(boolean state) {
		useInvertedNormals = state;
	}

	
	protected void writeFloat(float a) throws IOException {
		prepareBuffer(Float.floatToRawIntBits(a));
		ds.write(buf, 0, 4);
	}

	protected void writeHeader(int num) throws IOException {
		byte[] header = new byte[80];
		//colorModel.formatHeader(header);
		ds.write(header, 0, 80);
		writeInt(num);
	}

	protected void writeInt(int a) throws IOException {
		prepareBuffer(a);
		ds.write(buf, 0, 4);
	}

	protected void writeShort(int a) throws IOException {
		ds.writeByte(a & 0xff);
		ds.writeByte(a >> 8 & 0xff);
	}

	protected void writeVector(Vector3d v) {
		try {
//			writeFloat(v.x * scale.x);
//			writeFloat(v.y * scale.y);
//			writeFloat(v.z * scale.z);
			//System.out.println("float = " + (float)(v.x)+ "double = " + v.x);
			writeFloat((float)(v.x * scale.x));
			writeFloat((float)(v.y * scale.y));
			writeFloat((float)(v.z * scale.z));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}