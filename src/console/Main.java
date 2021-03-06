package console;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;

import resources.CSResource;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// StackPane root = (StackPane)FXMLLoader.load(getClass().getResource("MainGuiLayout.fxml"));
				
			String dir = System.getProperty("user.dir");
			
//			public static String strCSE118BARIMAGE = "CSE118Bar184x56.png";
//			public static String strCSE118Icon = "CSE118Icon.png";
//			public static String strCSE118BKGD01 = "CSE118BKGRD01.jpg";
//			public static String strCSE118BKGD02 = "CSE118BKGRD02.jpg";
//			public static String strCSE118BKGD03 = "CSE118BKGRD03.jpg";
			
			String imgPath1 = dir + "/src/resources/images/" + CSResource.strCSE118BKGD01;
			ImageView imageView = new ImageView(new Image(new FileInputStream(imgPath1)));
			imageView.setFitHeight(1200);
		    imageView.setFitWidth(2000);
		    BorderPane imageBP = new BorderPane();
			imageBP.setCenter(imageView);
		    
			// final ImageView imageView = new ImageView(new Image(new FileInputStream("src\\resources\\sccc-virtual-tours.jpg")));
			BorderPane bpCLOS = (BorderPane)FXMLLoader.load(getClass().getResource("CLOGuiLayout.fxml"));	
			bpCLOS.setStyle("-fx-background-color: rgba(50, 180, 255, 0.550); -fx-background-radius: 10;");
			
			StackPane spRoot = new StackPane();
			spRoot.setStyle("-fx-padding: 12;");
			spRoot.getChildren().addAll(imageBP, bpCLOS);
			
			Scene scene = new Scene(spRoot, 1280, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
