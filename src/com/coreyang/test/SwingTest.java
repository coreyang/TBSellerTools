package com.coreyang.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;

import com.coreyang.TBSellerMain;
import com.coreyang.tb.seller.CheckStoreCtrl;
import com.coreyang.tb.seller.DownloadPicAndMkDescCtrl;
import com.coreyang.tb.seller.SearchTopKeyWords;
import com.coreyang.tool.CoreyangUtil;

public class SwingTest {
	private static String labelPrefix = "Number of button clicks: ";
	private int numClicks = 0; // 计数器，计算点击次数

	public Component createComponents() {
		TBSellerMain.initSid();
		final JLabel labelKeywords = new JLabel("1.搜索魔方数据");
		final JLabel labelAnaysy = new JLabel("2.分析数据包，下载宝贝详情");
		final JLabel labelCheckStore = new JLabel("3.检查库存");
		final JTextField keywords = new JTextField();
		JButton searchButton = new JButton("搜索");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keywordsStr = keywords.getText();
				if(null!=keywordsStr&&!"".equals(keywordsStr.trim()))
					SearchTopKeyWords.run(keywordsStr);
				else
					JOptionPane.showMessageDialog(keywords.getParent(),"请输入关键词");
			}
		});
		//%E7%89%9B%E4%BB%94%E8%A3%A4%20%E7%94%B7
		//%E7%89%9B%E4%BB%94%E8%A3%A4+%E7%94%B7
		final JTextField datagramTxtPath = new JTextField();
		datagramTxtPath.setEnabled(false);
		final JComboBox comboBox = new JComboBox(new Object[] {"FAD", "HG"});
		
		final JButton loadUnusualWords = new JButton("添加数据包文件");
		loadUnusualWords.addActionListener(new ActionListener(){
            private JFileChooser filechooser = null;
            @Override public void actionPerformed(ActionEvent e){
                if (filechooser == null) {
                    filechooser = new JFileChooser(System.getProperty("user.home"));
                }
                filechooser.setFileFilter(new FileNameExtensionFilter("Text Files","txt","text","java","xls"));
                if (filechooser.showOpenDialog(loadUnusualWords.getParent()) == JFileChooser.APPROVE_OPTION) {
                	datagramTxtPath.setText(filechooser.getSelectedFile().getAbsolutePath());
                    
                }
                return;
            }
        });
		final JButton anylizeButton = new JButton("开始分析");
		anylizeButton.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent e){
            	String datagramPathStr = datagramTxtPath.getText();
            	if(null!=datagramPathStr&&!"".equals(datagramPathStr.trim()))
            		DownloadPicAndMkDescCtrl.run(datagramPathStr, comboBox.getSelectedItem().toString());
            	else
            		JOptionPane.showMessageDialog(anylizeButton.getParent(),"请选择数据包文件，再进行分析！");
            }
        });
		
		final JTextField checkSellerNoPath = new JTextField();
		final JButton loadSellerPath = new JButton("添加货号文件");
		checkSellerNoPath.setEnabled(false);
		loadSellerPath.addActionListener(new ActionListener(){
            private JFileChooser filechooser = null;
            @Override public void actionPerformed(ActionEvent e){
                if (filechooser == null) {
                    filechooser = new JFileChooser(System.getProperty("user.home"));
                }
                filechooser.setFileFilter(new FileNameExtensionFilter("Text Files","txt","text","java"));
                if (filechooser.showOpenDialog(loadUnusualWords.getParent()) == JFileChooser.APPROVE_OPTION) {
                	checkSellerNoPath.setText(filechooser.getSelectedFile().getAbsolutePath());
                    
                }
                return;
            }
        });
		final JButton checkButton = new JButton("开始检查");
		checkButton.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent e){
            	String checkPathStr = checkSellerNoPath.getText();
            	if(null!=checkPathStr&&!"".equals(checkPathStr.trim()))
            		CheckStoreCtrl.run(checkPathStr);
            	else
            		JOptionPane.showMessageDialog(anylizeButton.getParent(),"请选择货号文件，再进行检查！");
            }
        });
		
		// 在顶层容器及其内容之间放置空间的常用办法是把内容添加到Jpanel上，而Jpanel本身是没有边框的。
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
		pane.setLayout(new GridLayout(0, 4)); // 单列多行
		
		pane.add(labelKeywords);
		pane.add(new JLabel("  "));
		pane.add(new JLabel("  "));
		pane.add(new JLabel("  "));
		
		pane.add(new JLabel("请输入关键词:"));
		pane.add(keywords);
		pane.add(searchButton);
		pane.add(new JLabel("  "));
		
		pane.add(labelAnaysy);
		pane.add(new JLabel("  "));
		pane.add(new JLabel("  "));
		pane.add(new JLabel("  "));
		
		pane.add(loadUnusualWords);
		pane.add(datagramTxtPath);
		pane.add(comboBox);
		pane.add(anylizeButton);
		
		pane.add(labelCheckStore);
		pane.add(new JLabel("  "));
		pane.add(new JLabel("  "));
		pane.add(new JLabel("  "));
		
		pane.add(loadSellerPath);
		pane.add(checkSellerNoPath);
		pane.add(checkButton);
		pane.add(new JLabel("  "));
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
		JFrame frame = new JFrame("潮人衣吧专用工具");
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
