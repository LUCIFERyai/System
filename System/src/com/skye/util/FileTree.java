package com.skye.util;

import java.io.File;
import java.net.MalformedURLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 该类用于生成目录树
 * 
 * @author LUCIFER
 *
 */
public class FileTree {

	private static final String FILE_PATH = "directory";
	private static final ImageView folder = new ImageView(new Image("/com/skye/res/folder.png", 20, 20, true, true));

	public FileTree() {
	}

	public static void createTreeView(TreeItem<String> rootItem) {
		File file = new File(FILE_PATH);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			TreeItem<String> item = new TreeItem<String>(files[i].getName(),
					new ImageView(new Image("/com/skye/res/folder.png", 20, 20, true, true)));
			if (files[i].isDirectory()) {
				TreeItem<String> item1 = new TreeItem<String>(files[i].getName(),
						new ImageView(new Image("/com/skye/res/folder.png", 20, 20, true, true)));
				rootItem.getChildren().add(item1);
				addFileTreeNode(files[i], item1);
			} else {
//	        		rootItem.getChildren().add(new TreeItem<String>(files[i].getName()));
			}
		}
	}

	static void addFileTreeNode(File file, TreeItem<String> item) {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
//				System.out.println(files[i].getName());
				TreeItem<String> item1 = new TreeItem<String>(files[i].getName(),
						new ImageView(new Image("/com/skye/res/folder.png", 20, 20, true, true)));
				item.getChildren().add(item1);
				addFileTreeNode(files[i], item1);
			} else {
//				item.getChildren().add(new TreeItem<String>(files[i].getName()));
			}
		}
	}

	public static String GetFullPath(TreeItem<String> node) {
		String fullPath = null;
		TreeItem tmpnode = node;
		if (tmpnode != null) {
			fullPath = node.getValue();
			while (tmpnode.getParent() != null) // 父节点存在
			{
				fullPath = tmpnode.getParent().getValue() + "\\" + fullPath;
				tmpnode = tmpnode.getParent();
			}
		}
		return fullPath;
	}

}
