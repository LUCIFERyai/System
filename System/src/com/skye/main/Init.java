package com.skye.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.skye.util.Component;
import com.skye.util.FileTree;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Init {

	public static String absolutePath;

	public TreeItem<String> rootItem;
	public TreeView<String> treeView;

	private static ContextMenu functionMenu, fileMenu;
	private static MenuItem createFile, createFolder, open, rename, delete, property;

	private static final String FILE_PATH = "directory";

	AnchorPane root = new AnchorPane();
	static FlowPane fileTreePane = new FlowPane(300, 400);
	static FlowPane contentPane = new FlowPane(300, 400);

	public Init() {
	}

	public Init(Stage primaryStage) throws Exception {
		createTreeView();
		initComponentStyles();
		initContentPane();
		initContextMenuFunction();

		fileTreePane.getChildren().add(treeView);
		root.getChildren().addAll(fileTreePane, contentPane);
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();
	}

	/**
	 * 创建鼠标右键单击显示的菜单
	 */
	private void initContentPane() {
		createFile = new MenuItem("新建文件");
		createFolder = new MenuItem("新建文件夹");

		open = new MenuItem("打开");
		delete = new MenuItem("删除");
		rename = new MenuItem("重命名");
		property = new MenuItem("属性");

		fileMenu = new ContextMenu(createFile, createFolder);
		functionMenu = new ContextMenu(open, delete, rename, property);

		contentPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
			if (me.getButton() == MouseButton.SECONDARY && !functionMenu.isShowing()) {
				fileMenu.show(contentPane, me.getScreenX(), me.getScreenY());
			} else {
				fileMenu.hide();
			}
		});

	}

	/**
	 * 鼠标右键 创建文件夹菜单，给菜单选项添加点击事件
	 * 
	 * @throws Exception
	 */
	private void initContextMenuFunction() throws Exception {
		createFile.setOnAction((ActionEvent e) -> {
			Component.entryName(absolutePath, "file");
		});

		createFolder.setOnAction((ActionEvent e) -> {
			Component.entryName(absolutePath, "folder");
		});
	}

	/**
	 * 鼠标右键 创建文件菜单，给菜单选项添加点击事件
	 * 
	 * @param button
	 * @throws Exception
	 */

	private static void initFunctionMenuFunction(Button button) throws Exception {
		open.setOnAction((ActionEvent e) -> {
			System.out.println(absolutePath + "\\" + button.getText());
			try {
				Component.open(absolutePath + "\\" + button.getText());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});

		delete.setOnAction((ActionEvent e) -> {
			boolean flag = new File(absolutePath + "\\文件夹").mkdir();
			try {
				refreshContentPane(new File(absolutePath));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		rename.setOnAction((ActionEvent e) -> {
			boolean flag = new File(absolutePath + "\\文件夹").mkdir();
			try {
				refreshContentPane(new File(absolutePath));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		property.setOnAction((ActionEvent e) -> {
		});

	}

	/**
	 * 更新contentPane中的组件，即切换不同文件夹时刷新一次，把文件夹下的内容添加到contentPane
	 * 
	 * @param file
	 */
	public static void refreshContentPane(File file) throws Exception {
		contentPane.getChildren().removeAll(contentPane.getChildren());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				Button button;
				if (file2.isDirectory()) {
					button = new Button(file2.getName(),
							new ImageView(new Image("/com/skye/res/1.png", 40, 40, true, true)));
				} else {
					button = new Button(file2.getName(),
							new ImageView(new Image("/com/skye/res/2.png", 40, 40, true, true)));
				}
				contentPane.getChildren().add(button);

				button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouse) -> {
					if (mouse.getButton() == MouseButton.SECONDARY && !fileMenu.isShowing()) {
						functionMenu.show(contentPane, mouse.getScreenX(), mouse.getScreenY());
						try {
							initFunctionMenuFunction(button);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (mouse.getButton() == MouseButton.PRIMARY && mouse.getClickCount() == 2) {
						try {
							Component.open(absolutePath + "\\" + button.getText());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						functionMenu.hide();
					}
				});
			}
		} else {
//			System.out.println(file);
		}
	}

	/**
	 * 给TreeView目录树添加监听事件
	 */
	private void getSelected() {
		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue,
					TreeItem<String> newValue) {
				absolutePath = FileTree.GetFullPath(treeView.getSelectionModel().getSelectedItem());
				try {
					refreshContentPane(new File(absolutePath));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}

	/*
	 * 初始化各组件的样式
	 */
	void initComponentStyles() {
		treeView.setStyle("-fx-background-color: #ffffff;" + "-fx-border-color: #d3d3d3;" + "-fx-border-width:0.5px;");
		contentPane.setStyle("-fx-border-color:black");// #788CC8
		contentPane.setLayoutX(300);

		contentPane.setMinHeight(500);
		contentPane.setHgap(10);
		contentPane.setVgap(10);
	}

	/**
	 * 创建TreeView目录树
	 */
	void createTreeView() {
		rootItem = new TreeItem<>("directory");
		rootItem.setExpanded(true);
		treeView = new TreeView<>(rootItem);
		FileTree.createTreeView(rootItem);
		getSelected();
	}

}
