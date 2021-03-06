package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class CSResource {
	
	public static String strGUEST = "Guest";
	public static String strFACULTY = "Faculty";
	public static String strADMINISTOR = "Administrator";
	
	public static String [] chapObjs = {
			"-Chap01: Introduction",
			"-Chap02: Elementary Programming",
			"-Chap03: Selections",
			"-Chap04: Math Methods, Chars, Strings",
			"-Chap05: Loops",
			"-Chap06: Methods",
			"-Chap07: One-Dimensional Arrays",
			"-Chap08: Multidimensional Arrays",
	};
	
	public static String [] cseCLOs = {
			"Course Learning Outcomes",
			"Course Contents",
			"Course Mappings",
	};

	// background/icon images
	public static String strCSE118LOGO = "CSE118Logo184x56.png";
	public static String strCSE118Icon = "CSE118Icon.png";
	public static String strCSE118BKGD01 = "CSE118BKGRD01.jpg";
	public static String strCSE118BKGD02 = "CSE118BKGRD02.jpg";
	public static String strCSE118BKGD03 = "CSE118BKGRD03.jpg";
	
	// CLOs & LOs (.html) files
	public static String strCSE118CLOSFILE = "CSE118-CLOs.html";
	public static String strCSE118CLOMAPPINGFILE = "CSE118-CLO-Mapping.html";
	public static String strCSE118LOSLEAD = "CSE118_Chap0";
	public static String strCSE118LOSTAIL = "_LOS.html";
	public static String strNEWTEST = "NewTest.html";
	
	public static Image getImage(String imageName) {
		
		Image img = null;
		try {
		
			final String currDir = System.getProperty("user.dir");
			String pathToImage = currDir + "/bin/resources/images/" + imageName;
			img = new Image(new FileInputStream(pathToImage));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			return img;
		}
	}
}
