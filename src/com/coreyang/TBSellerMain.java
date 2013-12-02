package com.coreyang;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

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

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.coreyang.tb.seller.CheckStoreCtrl;
import com.coreyang.tb.seller.DownloadPicAndMkDescCtrl;
import com.coreyang.tb.seller.SearchTopKeyWords;
import com.coreyang.test.SwingTest;
import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;

public class TBSellerMain {

	public static Logger logger = Logger.getLogger(TBSellerMain.class);
	
	public static String sid = "";

	public static void main(String[] args) {
		logger.info("TBSellerTools Main begin...");
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
		/*System.out.println("******淘宝卖家工具功能类型******");
		System.out.println("1：搜索数据魔方关键词");
		System.out.println("2：分析csv数据包，下载宝贝图片，生成相应的宝贝详情描述");
		System.out.println("3：检查库存");
		System.out.println("输入\"bye\"，系统自动退出!");
		System.out.println("请输入功能类型：");
		chooseFunc();*/
		logger.info("TBSellerTools Main end...");
	}
	

	/**
	 * 根据输入，选择功能
	 */
	public static void chooseFunc() {
		String readLine = CoreyangUtil.getInputStr(
				CoreyangHelper.INPUT_TYPE_NUMBER, true);
		if ("1".equals(readLine.trim())) {
			// 调用1功能模块
			logger.info("choose func 1");
			initSid();
			SearchTopKeyWords.run();
		} else if ("2".equals(readLine.trim())) {
			// 调用2功能模块
			logger.info("choose func 2");
			DownloadPicAndMkDescCtrl.run();
		}else if ("3".equals(readLine.trim())) {
			// 调用3功能模块
			logger.info("choose func 3");
			CheckStoreCtrl.run();
		}else{
			System.out.println("不支持该功能，请重新输入：");
			logger.info("can not find func");
		}
		chooseFunc();
	}
	
	public static void getSid(){
		try {
			sid = CoreyangUtil.getSid();
		} catch (HttpException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static void initSid(){
		if("".equals(sid.trim())){
			logger.info("load sid begin...");
			getSid();
			logger.info("load sid end... : "+sid);
		}
	}
	
	public Component createComponents() {
		final JLabel labelKeywords = new JLabel("1.搜索魔方数据");
		final JLabel labelAnaysy = new JLabel("2.分析数据包，下载宝贝详情");
		final JLabel labelCheckStore = new JLabel("3.检查库存");
		final JTextField keywords = new JTextField();
		JButton searchButton = new JButton("搜索");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initSid();
				String keywordsStr = keywords.getText();
				if(null!=keywordsStr&&!"".equals(keywordsStr.trim()))
					SearchTopKeyWords.run(keywordsStr);
				else
					JOptionPane.showMessageDialog(keywords.getParent(),"请输入关键词");
			}
		});
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
}
