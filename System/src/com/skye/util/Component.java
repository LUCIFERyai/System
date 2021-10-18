package com.skye.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeSet;

import com.skye.main.Init;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * ������Ҫ�����ļ������ݵ�չʾ����ӵ�contentPane��
 * 
 * @author LUCIFER
 *
 */
public class Component {
	/**
	 * ˫�����ļ��л��ļ�
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void open(String filePath) throws Exception {
		System.out.println("filepath=" + filePath);
		File file = new File(filePath);
		if (file.isDirectory()) {
			Init.absolutePath = filePath;
			openDirectory(file);
		} else {
			openText(file);
		}
	}

	/**
	 * ˫�����ļ���
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void openDirectory(File file) throws Exception {
		Init.refreshContentPane(file);
	}

	/**
	 * ˫�����ļ���Ȼ���޸ģ�����
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void openText(File file) throws Exception {
		Stage primaryStage = new Stage();
		primaryStage.setTitle("Tree View Sample");
		TextArea contentField;
		contentField = new TextArea();
		contentField.setPrefRowCount(25);
		contentField.setWrapText(true);
		contentField.setText(getFileContent(file));
		long length = contentField.getLength();
		StackPane root = new StackPane();
		root.getChildren().add(contentField);
		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				long length1 = contentField.getLength();
				if (length != length1) {
					System.out.println("���޸�");
				} else {
					System.out.println("û���޸�");
				}
			}
		});
		primaryStage.show();
	}

	/**
	 * ��ȡ�ļ�txt���������
	 * 
	 * @param file
	 * @return txt�ı�
	 * @throws Exception
	 */
	public static String getFileContent(File file) throws Exception {
		BufferedReader fr = new BufferedReader(new FileReader(file));
		StringBuilder contentString = new StringBuilder();
		String line;
		while ((line = fr.readLine()) != null) {
			contentString.append(line + "\n");
		}
		return contentString.toString();
	}

	/**
	 * ����Ҫ�������ı����ļ������֣�Ȼ����д���
	 * @param absolutePath
	 * @param type
	 */
	public static void entryName(String absolutePath, String type) {
		Stage primaryStage = new Stage();
		FlowPane flowPane = new FlowPane();
		TextArea textArea = new TextArea();
		flowPane.setLayoutY(50);
		flowPane.setStyle("-fx-background-color: #fff; -fx-background-radius: 10px;");
		textArea.setMaxHeight(5);
		textArea.setMaxWidth(280);
		textArea.setStyle("-fx-background-color: #fff; -fx-background-radius: 10px;");
		Button button = new Button("����");
		button.setOnAction((ActionEvent event) -> {
			String fileName = absolutePath + "\\" + textArea.getText();
			try {
				if ((!isRepeated(absolutePath, textArea.getText(), type)) && "file".equals(type)) {
					boolean flag = new File(fileName + ".txt").createNewFile();
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("prompt:");
					alert.setContentText("�ļ������ɹ���");
					alert.showAndWait();
					try {
						Init.refreshContentPane(new File(absolutePath));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if ((!isRepeated(absolutePath, textArea.getText(), type)) && "folder".equals(type)) {
					boolean flag = new File(fileName).mkdir();
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("prompt:");
					alert.setContentText("�ļ��д����ɹ���");
					alert.showAndWait();
					try {
						Init.refreshContentPane(new File(absolutePath));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Warning:");
					alert.setContentText("�����ظ������������룡");
					alert.showAndWait();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		flowPane.getChildren().addAll(textArea, button);
		primaryStage.setScene(new Scene(flowPane, 350, 200));
		primaryStage.show();
	}

	static boolean isRepeated(String filePath, String fileName, String type) {
		File[] files = new File(filePath).listFiles();
		TreeSet<String> treeSet = new TreeSet<>();
		for (int i = 0; i < files.length; i++) {
			treeSet.add(files[i].getName());
		}
		if ("folder".equals(type)) {
			return treeSet.contains(fileName);
		} else {
			return treeSet.contains(fileName + ".txt");
		}
	}

}
