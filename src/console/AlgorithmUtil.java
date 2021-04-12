package console;
//package Model;

import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/*import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;*/

public class AlgorithmUtil {


	/*******************************************************************************************************************************************/
	/***************************************** This is only for reading and writing files and can be ignored for now ***************************/
	/*******************************************************************************************************************************************/
	
	/*
	 * This method returns a String of the contents of the file specified
	 */
	/*
	public static String readFile(String fileName) {
		
		StringBuilder question = new StringBuilder();
		
		try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);

            List<XWPFParagraph> paragraphs = document.getParagraphs();


            for (XWPFParagraph para : paragraphs) {
                question.append(para.getText());
            }
            fis.close();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		System.out.println(question);
		return question.toString();
	}
	
	/*
	 * this method writes a file based on the contents of each filename specified in the array of questions determined in the algorithm
	 */
	/*
	public static void writeFile(String folderName, String fileName, String[] questions, String professorName, String courseName) throws IOException {
		
		File folder = new File(folderName);
		if (!folder.exists()) {
			folder.mkdir();
			System.out.println("Creating " + folderName + "... ");
		}
		
		FileOutputStream fos = new FileOutputStream(new File(folderName + fileName));
		XWPFDocument document = new XWPFDocument();

		// write the paragraph for the heading stuff		
		XWPFParagraph heading = document.createParagraph();
		XWPFRun nameRun = heading.createRun();
		nameRun.setText("Name ___________");
		nameRun.addCarriageReturn();
		
		XWPFRun professorRun = heading.createRun();
		professorRun.setText(professorName);
		professorRun.addCarriageReturn();
	
		XWPFRun courseRun = heading.createRun();
		courseRun.setText(courseName);
		courseRun.addCarriageReturn();
		
		// write the paragraphs for the questions
		XWPFParagraph tempParagraph = document.createParagraph();
		for (int questionNum = 1; questionNum <= questions.length; questionNum++) {

			
			XWPFRun tempRun = tempParagraph.createRun();
			tempRun.setText(questionNum + ". " + questions[questionNum - 1]);
			tempRun.addCarriageReturn();
			tempRun.addCarriageReturn();
			
		}
		document.write(fos);
		fos.close();
		document.close();

	}
	*/
	
	/*******************************************************************************************************************************************/
	/****************************************** Methods You May Need In The Algorithm **********************************************************/
	/*******************************************************************************************************************************************/
	
	/*
	 * I left all of the methods public for now because you may have use for some of the ones
	 * I created as supporting methods (made with intention to be called inside other methods)
	 */
	
	/*
	 * this method returns an int array of the tags with the clos stored in indices [0-length-3], a unique id in index [length-2], and difficulty in index [length-1]
	 */
	public static int [] determineTags(String fileName) { // file name is formatted: 	id#-CLO1_..._N-difficulty#.docx

/*			 returns an array of the tags: 	{ CLO_1, CLO_2, ... , CLO_10, Objective Number, Difficulty rating }
 *										ex:	{1, 4, 5, 6} means 	The question covers CLO1 and CLO4, 
 *																The questions objective number is 5, 
 *																And the difficulty rating is a 6
 */
		
		String [] components = fileName.split("-");						// break up the string into substrings based on the location of "-"
		components[components.length-1] = components[components.length-1].substring(0,components.length-2);
		
		String [] CLOsArr = components[1].split("_");
		CLOsArr[0] = CLOsArr[0].substring(3,CLOsArr[0].length());		// remove the chars "CLO" to leave an integer
		int [] returnArr = new int[CLOsArr.length + 2];
		
		int i;
		for (i = 0; i <CLOsArr.length; i++) {
			returnArr[i] = Integer.parseInt(CLOsArr[i]);
		}
		returnArr[returnArr.length-2] = Integer.parseInt(components[0]);	// sets the second to last index to the objective number
		returnArr[returnArr.length-1] = Integer.parseInt(components[2]);	// sets the last index to the difficulty level
		

		return returnArr;
	}

	/*
	 * this method returns a hashmap that stores the tags of each question in the array ( determineTags(String) ) mapped to the key of that file's Name
	 */
	public static HashMap<String, int[]> createHashMap(String [] fileNames){
		int i = 2;
		
		while (i < fileNames.length) {
			i*=2;
		}
		HashMap<String, int[]> hmTags = new HashMap<String, int[]>(i);
		
		for (String fileName: fileNames) {
			hmTags.put(fileName, determineTags(fileName));
			
		}
		return hmTags;
	}
	
	public static String[] listFileNames(String folderName) throws FileNotFoundException {		// can set the folder name as a constant IF we choose to store the questions in the project folder*
		String[] files;
		File folder = new File(folderName);
		files = folder.list();
		
		return files;
	}
	
	
	
	/*
	 * This method checks the difficulty level of a question and returns true if the question's difficulty matches the the specified difficulty
	 */
	
	/*
	 * covers multiple CLOs
	 */
	
	public static String[] multiCLO(HashMap<String, int[]> questionMap, String[] files) {
		LinkedList<String> ll = new LinkedList<String>();
		for (String filename: files) {
			if (questionMap.get(filename).length > 3) {
				ll.add(filename);
			}
		}
		String [] arr = new String [ll.size()];
		int count = 0;
		for (String filename: ll) {
			arr[count++] = filename;
		}
		return arr;
		
	}
	
	/*
	 * covers one CLO
	 */
	public static String[] oneCLO(HashMap<String, int[]> questionMap, String[] files) {
		LinkedList<String> ll = new LinkedList<String>();
		for (String filename: files) {
			if (questionMap.get(filename).length == 3) {
				ll.add(filename);
			}
		}
		String [] arr = new String [ll.size()];
		int count = 0;
		for (String filename: ll) {
			arr[count++] = filename;
		}
		return arr;
		
	}
	
	
	
	public static boolean matchesDifficulty(HashMap<String, int[]> questionMap, String fileName, int difficultyLow, int difficultyHigh) { // O(1)
		
		/*
		 * a hash map with a key value pair of <String, int[]> (The string is the filename, the value is an array of the tags
		 * called questionMap will be accessed by the method (questionMap will be a static variable instantiated in the beginning of the algorithm
		 * with: 
		 * "HashMap<String,int[]> questionMap = createHashMap(listFileNames(folderName));" this line will create an String array of the fileNames contained
		 * inside the Folder, and for each fileName found, the tags will be determined and an array will be created to store the tags and that array 
		 * is mapped to the key of String fileName
		 */
		
		int[] arr = questionMap.get(fileName);
		int questionDifficulty = arr[arr.length-1];
		return difficultyLow <= questionDifficulty && difficultyHigh >= questionDifficulty;
		
	}


	/*
	 * This method checks the CLOs of a question and returns true if one of the question's CLOs matches the the specified CLO
	 */
	public static boolean matchesCLO(HashMap<String, int[]> questionMap, String fileName, int CLO) { // O(n)
		int[] arr = questionMap.get(fileName);
		for (int i = 0; i < arr.length-2; i++) {
			if (CLO == arr[i]) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * This method returns an array of questions that match the desired CLO 
	 */
	public static String[] createArrayForCLO(HashMap<String, int[]> questionMap, int CLOnum, String[] fileNames){	// O(n^2)

		LinkedList<String> cloMatches = new LinkedList<>();
		
		for (String fileName: fileNames) {
			if(matchesCLO(questionMap, fileName, CLOnum)) {
				cloMatches.add(fileName);
			}
		}
		String [] matchArray = new String[cloMatches.size()];
		int counter = 0;
		for (String match: cloMatches) {
			matchArray[counter++] = match;
		}
		return matchArray;
	}

	
	
	
	

}
