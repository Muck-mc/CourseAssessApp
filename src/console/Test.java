package console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class Test {
	
	/*
	 * @param difNum difficulty Number
	 * @param lenNum length Number
	 * @param fileName make your file name
	 * @param course course name
	 * 
	 */
	
	private int difNum;
	private int lenNum;
	private String fileName;
	private String profName;
	private String course;
	private String[] testQuestions;
	private static String folderName = "src/QuestionsForPreliminaryTesting/";
//	private static String folderName = "src/QuestionSetForDemo/";
	
	public Test(int difNum, int lenNum, String fileName, String profName, String course) throws FileNotFoundException {
		super();
		this.difNum = difNum;
		this.lenNum = lenNum;
		this.fileName = fileName;
		this.profName = profName;
		this.course = course;
		
		this.testQuestions = Algorithm.runAlg(lenNum, difNum, difNum, "src/QuestionsForPreliminaryTesting"); // this will return a string arr of questions for now im using a relative folder in next line
//		this.testQuestions = listFileNames("src/QuestionSetForDemo");
	}


	public int getDifNum() {
		return difNum;
	}

	public void setDifNum(int difNum) {
		this.difNum = difNum;
	}

	public int getLenNum() {
		return lenNum;
	}

	public void setLenNum(int lenNum) {
		this.lenNum = lenNum;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getProfName() {
		return profName;
	}

	public void setProfName(String profName) {
		this.profName = profName;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String[] getTestQuestions() {
		return testQuestions;
	}

	public void setTestQuestions(String[] testQuestions) {
		this.testQuestions = testQuestions;
	}

	
	public String testMe() throws IOException {
		System.out.println("testing");
		System.out.println(difNum);
		System.out.println(lenNum);
		System.out.println(fileName);
		System.out.println(profName);
		
		//writeFile("TestCreated/", fileName + ".docx", testQuestions, profName, "CSE118");
		
		String testFormatted = new String();
		
		testFormatted += "Name:_________ \n"+ profName +" \n" + course + "\n\n"; // this will allow an input of Prof Name
		
		for (int i = 0; i < this.testQuestions.length;i++) {
			testFormatted += i+". " + readFile(testQuestions[i]) + "\n\n";
		}
//		return "testing " + "Dif:"+difNum + " len:" + lenNum +"\n" + testFormatted;
		return testFormatted;
	}
	
	
	public static String[] listFileNames(String folderName) throws FileNotFoundException {		// can set the folder name as a constant IF we choose to store the questions in the project folder*
		String[] files;
		File folder = new File(folderName);
		files = folder.list();
		
		return files;
	}
	
	
	/*
	 * Read the contents of the file... 
	 */
	
	public static String readFile(String fileName) {
		
		StringBuilder question = new StringBuilder();
		
		try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(folderName + file);

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
	 * Redo Method to delimit a string into an array of questions using some char and create the word doc from it
	 */
	
	public void writeFile(File file /*String[] questions (question content)*/, String professorName, String courseName, LinkedList<String> strings) throws IOException {
		
		
		FileOutputStream fos = new FileOutputStream(file);
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
		for (int questionNum = 1; questionNum <= strings.size(); questionNum++) {

			
			XWPFRun tempRun = tempParagraph.createRun();
			tempRun.setText(questionNum + ". " + strings.get(questionNum));
			tempRun.addCarriageReturn();
			tempRun.addCarriageReturn();
			
		}
		document.write(fos);
		fos.close();
		document.close();

	}
}
