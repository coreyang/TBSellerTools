package com.coreyang.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class SwingTest {
	private static String labelPrefix = "Number of button clicks: ";
	private int numClicks = 0; // 计数器，计算点击次数

	public Component createComponents() {
		final JLabel label = new JLabel(labelPrefix + "0 ");

		JButton button = new JButton("I'm a Swing button!");
		button.setMnemonic(KeyEvent.VK_I); // 设置按钮的热键为'I'
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton jButton = (JButton)e.getSource();
				jButton.setEnabled(false);
				new JFrame("new window").setVisible(true);
				numClicks++;
				label.setText(labelPrefix + numClicks);// 显示按钮被点击的次数
			}
		});
		label.setLabelFor(button);

		// 在顶层容器及其内容之间放置空间的常用办法是把内容添加到Jpanel上，而Jpanel本身是没有边框的。
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
		pane.setLayout(new GridLayout(0, 1)); // 单列多行
		pane.add(button);
		pane.add(label);
		return pane;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());// 设置窗口风格
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 创建顶层容器并添加内容.
		JFrame frame = new JFrame("SwingApplication");
		SwingTest app = new SwingTest();
		Component contents = app.createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);// 窗口设置结束，开始显示
		frame.setBounds(500, 500, 0, 0);
		frame.addWindowListener(new WindowAdapter() { // 匿名类用于注册监听器
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
		frame.pack();
		frame.setVisible(true);
	}
}
