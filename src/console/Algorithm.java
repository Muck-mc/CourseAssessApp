package console;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Algorithm {

//		private static String questionFolderPath = "src/questions";
	
	public static String[] runAlg(int length, int difficultyLower, int difficultyUpper, String questionFolderPath) throws FileNotFoundException {


		int testSize = length;


		String[] questionsPicked = new String[testSize];
		int[] freqArray = new int[10]; // keeps track of CLOs cover
		ArrayList<String> testQuestions = new ArrayList<String>(); // arrayList of the test questions picked

		String fileName = questionFolderPath; // this file will be a relative path to a folder stored inside the project

		String[] files = AlgorithmUtil.listFileNames(fileName);
		HashMap<String, int[]> questionAttributesHM = AlgorithmUtil.createHashMap(files);
		int difficulty = 0;
		System.out.println();

		int difficultyLow = difficultyLower;		
		int difficultyHigh = difficultyUpper;

		if (testSize < 10) {
			System.out.println("starting alg <10");
			/**
			 * - Walk through the CLO frequency array: - If the element at the specific
			 * index < 1, then randomly pickup a question covering that CLO from its
			 * individual CLO array. - If the element is >=1, do nothing. - Before entering
			 * this for loop, freqArray looks like this: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
			 * index 0 = CLO1, index 1 = CLO2, ... index 9 = CLO10
			 **/

			for (int i = 0; i < freqArray.length; i++) {
				if (freqArray[i] < 1) {
					// pick a random question and add it to the testQuestions arrayList
					testQuestions.add(generateQuestion(i, testQuestions, questionAttributesHM, files, difficultyLow,
							difficultyHigh));
					// Check the question tag of the question we just added, and adds the CLOs it
					// covers to the CLO frequency array
					// (by incrementing the appropriate index)
					checkClos(testQuestions.get(testQuestions.size() - 1), freqArray, questionAttributesHM);

				}
			}
			/**
			 * 
			 * - Continue picking random questions covering current CLO with the lowest
			 * frequency. - If the desired test size is lower than the number of questions
			 * that was generated using the process above then the testQuestions array and
			 * freqArray will be cleared and it will start over.
			 **/
			while (testQuestions.size() != testSize) {
				if (testQuestions.size() > testSize) {
					testQuestions.clear();
					clearFreqArray(freqArray);
					for (int i = 0; i < freqArray.length; i++) {
						if (freqArray[i] < 1) {
							// pick a random question and add it to the testQuestions arrayList
							testQuestions.add(generateQuestion(i, testQuestions, questionAttributesHM, files,
									difficultyLow, difficultyHigh));
							/**
							 * Check the question tag of the question we just added, and adds the CLOs it
							 * covers to the CLO frequency array (by incrementing the appropriate index)
							 **/
							checkClos(testQuestions.get(testQuestions.size() - 1), freqArray, questionAttributesHM);

						}
					}
				} else { // if test size < desired test length; implementation for weight:
					testQuestions.add(generateQuestion(lowestFreqCLO(freqArray), testQuestions, questionAttributesHM,
							files, difficultyLow, difficultyHigh));
					checkClos(testQuestions.get(testQuestions.size() - 1), freqArray, questionAttributesHM);
				}
			}
			for (int i = 0; i < testQuestions.size(); i++) {
				questionsPicked[i] = testQuestions.get(i);
			}
		}

		else {
			if (testSize >= 10) {
				System.out.println("starting alg >=10");
				/********************************
				 * TEST LENGTH = 10
				 ********************************/

				// Loop that picks 10 random questions that cover each CLO
				for (int index = 0; index < 10; index++) {

					// Generates an array with only questions from a specified CLO
					String[] currentArray = AlgorithmUtil.createArrayForCLO(questionAttributesHM, index + 1, files);
					int questionNum = 0;

					// Generates random question until question satisfies difficulty level
					boolean fitsDifficulty = false;
					boolean duplicate = true;

					// Generates random question until question satisfies criteria
					while (!fitsDifficulty || duplicate) {
						fitsDifficulty = false;

						// Picks a random index of currentArray, which corresponds to a question
						questionNum = (int) (Math.random() * currentArray.length);

						// Checks for matching difficulty
						if (AlgorithmUtil.matchesDifficulty(questionAttributesHM, currentArray[questionNum], difficulty - 1,
								difficulty + 1) || difficulty == 0) {
							fitsDifficulty = true;

							// Checks if question is duplicate
							duplicate = false;
							for (int i = 0; i < index; i++) {
								if (questionsPicked[i].contentEquals(currentArray[questionNum])) {
									duplicate = true;
								}
							}
						}
					}

					// Returns all tags in a question in the form (CLOs, id#, difficulty)
					int[] arrayOfIndexes = questionAttributesHM.get(currentArray[questionNum]);

					// Adds 1 to the correct index (the CLO - 1) of weightOfCLOs
					for (int i = arrayOfIndexes.length - 3; i >= 0; i--) {
						freqArray[arrayOfIndexes[i] - 1] += 1;
					}

					// Adds question to questionsPicked array
					questionsPicked[index] = currentArray[questionNum];
				}

				/********************************
				 * TEST LENGTH > 10
				 ********************************/

				// Establishes how many leftover questions there are
				int leftoverQuestions = testSize - 10;

				// Picks required amount of questions
				for (int index = 0; index < leftoverQuestions; index++) {

					// Keeps track of the lowest weight among the CLOs
					int lowestWeight = freqArray[0];

					// Loops through the weightOfCLOs array to find the lowest weight
					for (int index2 = 0; index2 < freqArray.length; index2++) {

						// Updates lowest weight if a new lowest weight is found
						if (freqArray[index2] < lowestWeight) {
							lowestWeight = freqArray[index2];
						}
					}

					// Initializes variables for weight check
					boolean fitsWeight = false;
					int clo = 0;

					// Randomly picks a CLO until it satisfies the weight requirement
					while (fitsWeight == false) {
						clo = (int) (Math.random() * 10);

						if (freqArray[clo] == lowestWeight) {
							fitsWeight = true;
						}
					}

					/** Same algorithm as above **/

					// Picks a random question from that CLO until it fits difficulty level & isn't
					// a duplicate
					String[] currentArray = AlgorithmUtil.createArrayForCLO(questionAttributesHM, clo + 1, files);
					int questionNum = 0;

					// Generates random question until question satisfies difficulty level
					boolean fitsDifficulty = false;
					boolean duplicate = true;

					// Generates random question until question satisfies criteria
					while (!fitsDifficulty || duplicate) {
						fitsDifficulty = false;

						// Picks a random index of currentArray, which corresponds to a question
						questionNum = (int) (Math.random() * currentArray.length);

						// Checks for matching difficulty
						if (AlgorithmUtil.matchesDifficulty(questionAttributesHM, currentArray[questionNum], difficulty - 1,
								difficulty + 1) || difficulty == 0) {
							fitsDifficulty = true;

							// Checks if question is duplicate
							duplicate = false;
							for (int i = 0; i < index + 10; i++) {
								if (questionsPicked[i].contentEquals(currentArray[questionNum])) {
									duplicate = true;
								}
							}
						}
					}

					// Returns all tags in a question in the form (CLOs, id#, difficulty)
					int[] arrayOfIndexes = questionAttributesHM.get(currentArray[questionNum]);

					// Adds 1 to the correct index (the CLO - 1) of weightOfCLOs
					for (int i = arrayOfIndexes.length - 3; i >= 0; i--) {
						freqArray[arrayOfIndexes[i] - 1] += 1;
					}

					// Adds question to questionsPicked array
					questionsPicked[index + 10] = currentArray[questionNum];
				}
			}
		}

		return questionsPicked;
	}

//idea: pass test questions to this array, another method that checks duplicated
	public static String generateQuestion(int clo, ArrayList<String> testQuestions,
			HashMap<String, int[]> questionAttributesHM, String[] files, int difficultyLow, int difficultyHigh)
			throws FileNotFoundException {

		int idx = 0;
		boolean duplicates = false; // we will assume there are no duplicates
//The series of String arrays below contain every question that covers that particular CLO
//{123-CLO1_2_3, 129-CL01_6_7, ... etc.}
		String[] cloArray = AlgorithmUtil.createArrayForCLO(questionAttributesHM, clo + 1, files);
		idx = (int) (Math.random() * cloArray.length);
		duplicates = checkForDuplicates(cloArray[idx], testQuestions, duplicates);
		while (duplicates == true || AlgorithmUtil.matchesDifficulty(questionAttributesHM, cloArray[idx], difficultyLow,
				difficultyHigh) == false) {
			idx = (int) (Math.random() * cloArray.length);
			duplicates = checkForDuplicates(cloArray[idx], testQuestions, duplicates);
			AlgorithmUtil.matchesDifficulty(questionAttributesHM, cloArray[idx], difficultyLow, difficultyHigh);
		}
		return cloArray[idx];

	}

	public static int[] checkClos(String question, int[] freqArray, HashMap<String, int[]> HM) {
		int[] getTags = HM.get(question);
		for (int i = 0; i < getTags.length - 2; i++) {
			freqArray[getTags[i] - 1]++;
		}

		return freqArray;

	}

	public static boolean checkForDuplicates(String question, ArrayList<String> test, boolean duplicates) {
//Takes the question passed into method and compares it to the questions already stored in the arrayList. If the question matches a question in the testQuestions arrayList then this method will return true. If it goes through the testQuestion array and does not find any duplicates, it will return false.
		for (int i = 0; i < test.size(); i++) {
			if (question == test.get(i)) {
				duplicates = true;
				return duplicates;
			} else {
				duplicates = false;
			}
		}
		return duplicates;
	}

	public static void printArray(ArrayList<String> test) {
		for (int i = 0; i < test.size(); i++) {
			System.out.printf("%s \t", test.get(i));
			if ((i + 1) % 5 == 0) {
				System.out.println();
			}
		}
	}

	public static void printArray(int[] freqArray) {
		for (int i = 0; i < freqArray.length; i++) {
			System.out.printf("%d \t", freqArray[i]);
			if ((i + 1) % 5 == 0) {
				System.out.println();
			}
		}
	}

	public static void printArray(String[] freqArray) {
		for (int i = 0; i < freqArray.length; i++) {
			System.out.printf("%s \t", freqArray[i]);
			if ((i + 1) % 5 == 0) {
				System.out.println();
			}
		}
	}

	public static int lowestFreqCLO(int[] cloArray) {
		int lowestFreqIdx = 0;
		for (int i = 0; i < cloArray.length; i++) {
			if (cloArray[i] < cloArray[lowestFreqIdx]) {
				lowestFreqIdx = i;
			}
		}
		return lowestFreqIdx;
	}

	public static void clearFreqArray(int[] freqArray) {

		for (int i = 0; i < freqArray.length; i++) {
			freqArray[i] = 0;
		}

	}

}
