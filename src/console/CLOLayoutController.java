package console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import resources.CSResource;

public class CLOLayoutController implements Initializable {

	ObservableList<String> loginList = FXCollections.observableArrayList(CSResource.strGUEST, CSResource.strFACULTY,
			CSResource.strADMINISTOR);
	/*
	 * contains the textfields that will be dynamically created and pushed to view when a test has been generated
	 */
	private LinkedList<String> questions; 
	private LinkedList<TextField> questionFields;
	
	private final Node cseIcon01 = new ImageView(new Image(getClass().getResourceAsStream("CSE118.png")));
	private final Node cseIcon02 = new ImageView(new Image(getClass().getResourceAsStream("CSE02.png")));
	private final Node cseIcon03 = new ImageView(new Image(getClass().getResourceAsStream("CSE03.png")));
	private Test test = null;
	
	@FXML
	private WebView webViewer;
	@FXML
	private HBox hbTop;
	@FXML
	private ComboBox cbSignOn;
	@FXML
	private TreeView tvCSELOs;
	@FXML
	private TreeView tvChapLOs;
	@FXML
	private TextField difNum;
	@FXML
	private TextField lenNum;
	@FXML
	private TextField profName;
	@FXML
	private TextField fileName;
	@FXML
	private TextField course;
	@FXML
	private AnchorPane testPane;
	@FXML
	private TextArea createdTest;
	@FXML
	private Button cancelBtn;
	@FXML
	private Button saveBtn;
	@FXML
	private ScrollPane scrollView;
	

	// private ScrollPane spContainer;
	WebView browser = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		final String currWorkPath = System.getProperty("user.dir");
		testPane.setVisible(false);

		try {

			/*
			 * if (cseIcon01 == null) { String icon01Path = currWorkPath +
			 * "\\bin\\resources\\images\\" + CSResource.strCSE118Icon; cseIcon01 = new
			 * ImageView(new Image(new FileInputStream(icon01Path)));
			 * 
			 * String icon02Path = currWorkPath + "\\bin\\resources\\images\\" +
			 * CSResource.strCSE118BKGD02; cseIcon02 = new ImageView(new Image(new
			 * FileInputStream(icon02Path)));
			 * 
			 * String icon03Path = currWorkPath + "\\bin\\resources\\images\\" +
			 * CSResource.strCSE118BKGD03; cseIcon03 = new ImageView(new Image(new
			 * FileInputStream(icon03Path))); }
			 */

			String pathToLogoImage = currWorkPath + "/bin/resources/images/" + CSResource.strCSE118LOGO;
			FileInputStream scccLogoImage = new FileInputStream(pathToLogoImage);
			// create a image
			Image image = new Image(scccLogoImage);
			BackgroundImage backgroundimage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
			Background background = new Background(backgroundimage);
			hbTop.setBackground(background);
			// hbTop.setStyle("-fx-background-color:rgba(200, 200, 200, 0.5);");
		} catch (Exception e) {
			e.printStackTrace();
		}

		cbSignOn.setValue(CSResource.strGUEST);
		cbSignOn.setItems(loginList);

		initializeCLOTreeView();
		// setupCLOTrees();
		loadWebPage(webViewer, null);
	}

	@FXML
	private void testScene(ActionEvent event) {
		webViewer.setDisable(true);
		webViewer.setVisible(false);
		
		if(!testPane.isVisible()) {
			scrollView.setVisible(false);
			scrollView.setDisable(true);
			testPane.setDisable(false);
			testPane.setVisible(true);
			cancelBtn.setVisible(true);
			cancelBtn.setDisable(false);
			event.consume();	
		}else {
			scrollView.setVisible(false);
			scrollView.setDisable(true);
			profName.setText("");
			fileName.setText("");
			course.setText("");
			difNum.setText("");
			lenNum.setText("");
		}
		
		
	}

	@FXML
	private void cancelTest(ActionEvent event) {
		webViewer.setDisable(false);
		webViewer.setVisible(true);

		testPane.setDisable(true);
		testPane.setVisible(false);
		event.consume();
	}

	@FXML
	private void cancel(ActionEvent event) {

		testPane.setDisable(false);
		testPane.setVisible(true);
//		createdTest.setDisable(true);
//		createdTest.setVisible(false);
		saveBtn.setVisible(false);
		saveBtn.setDisable(true);
		cancelBtn.setVisible(false);
		cancelBtn.setDisable(true);
		scrollView.setVisible(false);
		scrollView.setDisable(true);
		event.consume();

	}
	@FXML
	private void save(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Save");
		chooser.setInitialFileName(fileName.getText());
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Document files (*.docx)", "*.docx");
        chooser.getExtensionFilters().add(extFilter);
        
       
        File file = chooser.showSaveDialog(cancelBtn.getScene().getWindow());
        try {
			test.writeFile(file, profName.getText(), course.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}

	private void saveFile(File file) {
		//TODO save file using filewriter or Docx API?
		
	}
	@FXML
	private void createTest(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.WARNING);
		int dif = -1;
		int len = -1;
		String prof = "";
		String file = "";
		String crse = "";
		try {
			dif = Integer.parseInt(difNum.getText());
			len = Integer.parseInt(lenNum.getText());
			prof = profName.getText();
			file = fileName.getText();
			crse = course.getText();
			if (dif < 0 || dif > 5) {
				event.consume();
				alert.setContentText("Difficulty not within range");
				alert.show();

			} else if (len < 5 || len > 25) {
				event.consume();
				alert.setContentText("Length not within range");
				alert.show();

			} else if(prof.isBlank() || file.isBlank() || crse.isBlank()) {
				event.consume();
				alert.setContentText("Please check Professor, Course, fileName");
				alert.show();
			} else {
				test = new Test(dif, len, prof, file, crse);
				//createdTest.setText(test.testMe());
				questionFields = new LinkedList<TextField>();
				GridPane grid = new GridPane();

				/*
				 * populates questionFields(LL <TextFields>) with the textField containing a question (not question name).
				 * 
				 * Inside the loop the container can have the element added to it (the container) to be displayed in the view
				 * 
				 * Additional TextFields can be added to the container with "CONTAINERNAME.getChildren().add(new TextField());
				 */
				for (int i = 0 ; i < test.getTestQuestions().length; i++) {
					TextField newTextField = new TextField(Test.readFile(test.getTestQuestions()[i]));
					questionFields.add(newTextField);
					newTextField.setMaxWidth(600);
					newTextField.setPrefWidth(600);
					newTextField.setEditable(false);
					
					Button minus = new Button("-");
					grid.add(minus, 0, i);
					grid.add(newTextField, 1, i);
					grid.setPadding(new Insets(10));
				}
				scrollView.setPrefWidth(650);
				scrollView.setContent(grid);
				scrollView.setVisible(true);
				scrollView.setDisable(false);
				testPane.setVisible(false);
				//createdTest.setVisible(true);
				//createdTest.setDisable(false);
				cancelBtn.setVisible(true);
				saveBtn.setVisible(true);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			alert.setContentText("Not a number!");
			alert.show();
			event.consume();
		}
	}

	private void initializeCLOTreeView() {

		TreeItem<String> rootNode = new TreeItem<>("Course CSE118", cseIcon02);
		rootNode.setExpanded(true);
		tvChapLOs.setRoot(rootNode);
		tvChapLOs.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;");

		for (int i = 0; i < CSResource.cseCLOs.length; i++) {
			if (CSResource.cseCLOs[i].contains("Contents")) {
				TreeItem<String> item = this.makeTreeItemWithIcon(CSResource.cseCLOs[i], rootNode, cseIcon02);
				if (item != null) {
					item.setExpanded(true);
					for (int j = 0; j < CSResource.chapObjs.length; j++) {
						this.makeTreeItem(CSResource.chapObjs[j], item);
					}
				}
			} else {
				if (i == 0)
					this.makeTreeItemWithIcon(CSResource.cseCLOs[i], rootNode, cseIcon01);
				else
					this.makeTreeItemWithIcon(CSResource.cseCLOs[i], rootNode, cseIcon03);
			}
		}

		tvChapLOs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			public void changed(ObservableValue<? extends TreeItem<String>> changed, TreeItem<String> oldVal,
					TreeItem<String> newVal) {
				if (newVal != null) {
					String itemTitle = newVal.getValue();
					loadWebPage(webViewer, newVal.getValue());
					// System.out.println("itme: " + itemTitle);
				}
			}
		});
	}

	private void setupCLOTrees() {
		// treeview: course learning outcomes
		TreeItem<String> rootCLOItem = new TreeItem<>("CSE118 Learning Outcomes");
		rootCLOItem.setExpanded(false);
		tvCSELOs.setRoot(rootCLOItem);
		tvCSELOs.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");
		tvCSELOs.setVisible(true);

		tvCSELOs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			public void changed(ObservableValue<? extends TreeItem<String>> changed, TreeItem<String> oldVal,
					TreeItem<String> newVal) {
				if (newVal != null) {
					String itemTitle = newVal.getValue();
					// setupWebview(spContainer, itemTitle, false);
					// System.out.println("itme: " + itemTitle);
				}
			}
		});

		// treeview: learning objectives for each chapter
		TreeItem<String> rootLOItems = new TreeItem<>("Learning Objectives");
		rootLOItems.setExpanded(true);
		for (int i = 0; i < CSResource.chapObjs.length; i++) {
			makeTreeItem(CSResource.chapObjs[i], rootLOItems);
		}
		tvChapLOs.setRoot(rootLOItems);
		tvChapLOs.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;");
		tvChapLOs.setVisible(true);
		tvChapLOs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			public void changed(ObservableValue<? extends TreeItem<String>> changed, TreeItem<String> oldVal,
					TreeItem<String> newVal) {
				if (newVal != null) {
					String itemTitle = newVal.getValue();
					loadWebPage(webViewer, newVal.getValue());
					System.out.println("itme: " + itemTitle);
				}
			}
		});

		tvCSELOs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			TreeItem<String> nv = (TreeItem<String>) (newValue);
			// System.out.println("Selected Text : " + nv.getValue());
		});

		tvCSELOs.getFocusModel().focusedItemProperty().addListener((observable, oldValue, newValue) -> {
			TreeItem<String> nv = (TreeItem<String>) (newValue);
			// System.out.println("Focused Text : " + nv.getValue());
		});

	}

	private TreeItem<String> makeTreeItem(String itmeTitle, TreeItem<String> parent) {
		TreeItem<String> newItem = new TreeItem<>(itmeTitle);
		boolean result = parent.getChildren().add(newItem);
		return result ? newItem : null;
	}

	private TreeItem<String> makeTreeItemWithIcon(String itmeTitle, TreeItem<String> parent, Node icon) {
		TreeItem<String> newItem = (icon != null) ? new TreeItem<>(itmeTitle, icon) : new TreeItem<>(itmeTitle);
		boolean result = parent.getChildren().add(newItem);
		return result ? newItem : null;
	}

	private void loadWebPage(WebView webViewer, String itemIdxStr) {
		if (webViewer != null) {
			WebEngine webEngine = webViewer.getEngine();
			webViewer.setVisible(true);
			webViewer.setDisable(false);
			String fileName = getCLOURL(itemIdxStr);
			String urlStr = "";
			try {
				URL url = new File(fileName).toURI().toURL();
				urlStr = url.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (urlStr != null) {
				webEngine.load(urlStr);
			}
		}
	}

	private void setupWebview(ScrollPane parent, String itemIdxStr) {
		// parent.setStyle("-fx-background-color: rgba(50, 180, 255, 0.550)");

		if (browser == null) {
			browser = new WebView();
		}
		// browser.setBlendMode(BlendMode.DARKEN);

		// browser.setStyle("-fx-background-color: rgba(50, 180, 255, 0.550)");
		WebEngine webEngine = browser.getEngine();

		String urlStr = null;
		if (itemIdxStr == null) {
			urlStr = getClass().getResource("CSE118-CLOs.html").toString();
		} else {
			urlStr = getClass().getResource(itemIdxStr).toString();
		}

		if (urlStr != null) {
			webEngine.load(urlStr);
			parent.setContent(browser);
		}
	}

	private String getCLOURL(String itemStr) {

		final String currDir = System.getProperty("user.dir");

		// default filename: "CSE118-CLOs.html"
		String urlName = CSResource.strCSE118CLOSFILE;

		if (itemStr != null) {
			int idx = itemStr.indexOf("Chap0");
			if (idx > 0) {
				// filename: CSE118_Chap0XObjectives.html
				String chapIdx = itemStr.substring(idx + 5, idx + 6);
				urlName = CSResource.strCSE118LOSLEAD + chapIdx + CSResource.strCSE118LOSTAIL;
			} else if (itemStr.indexOf("Mapping") > 0) {
				urlName = CSResource.strCSE118CLOMAPPINGFILE;
			} else {
				urlName = CSResource.strNEWTEST;
			}
		}

		return currDir + "/bin/resources/htmls/" + urlName;
	}

	private void setupWebview(ScrollPane parent, String itemStr, boolean dummy) {

		// filename: CSE118_Chap0XObjectives.html
		int idx = itemStr.indexOf("Chap0");
		if (idx > 0) {
			String chapIdx = itemStr.substring(idx + 5, idx + 6);
			String fileName = "CSE118_Chap0" + chapIdx + "_LOS.html";
			setupWebview(parent, fileName);
			return;
		}

		idx = itemStr.indexOf("Learning Outcomes");
		if (idx > 0) {
			String fileName = "CSE118-CLOs.html";
			setupWebview(parent, fileName);
		}
	}

}
