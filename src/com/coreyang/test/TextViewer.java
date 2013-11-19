package com.coreyang.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;

import com.coreyang.tool.CoreyangUtil;
 
public class TextViewer {
 
    public static void main(final java.lang.String[] args) {
        java.awt.EventQueue.invokeLater(new java.lang.Runnable(){
                @Override public void run(){
                    final JFrame frame = new JFrame("Text Viewer");
                    final JButton loadUnusualWords = new JButton("添加过滤关键字");
                    final JButton loadExcel = new JButton("添加EXCEL");
                    final JButton runButton = new JButton("开始过滤");
                    final JLabel filename = new JLabel("");
                    
                    final JTextArea textarea = new JTextArea(25, 80);
                    textarea.setLineWrap(true);
                    textarea.setWrapStyleWord(true);
                    final JScrollPane scroller = new JScrollPane(textarea);
                    loadUnusualWords.addActionListener(new ActionListener(){
                            private JFileChooser filechooser = null;
                            private DefaultEditorKit kit = new DefaultEditorKit();
                            @Override public void actionPerformed(ActionEvent e){
                                if (filechooser == null) {
                                    filechooser = new JFileChooser(System.getProperty("user.home"));
                                }
                                filechooser.setFileFilter(new FileNameExtensionFilter("Text Files","txt","text","java"));
                                if (filechooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                                    filename.setText(filechooser.getSelectedFile().getAbsolutePath());
                                    InputStreamReader reader = null;
                                    try {
                                    	FileInputStream fis = new FileInputStream(filechooser.getSelectedFile());
                                    	String charSet = CoreyangUtil.getCharset(filechooser.getSelectedFile().getAbsolutePath());
                                        reader = new InputStreamReader(fis,charSet);
                                        textarea.setText(charSet);
                                        kit.read(reader,textarea.getDocument(),0);
                                    } catch (Exception xe) {
                                        System.err.println(xe.getMessage());
                                    } finally {
                                        if (reader != null) {
                                            try {
                                                reader.close();
                                            } catch (IOException ioe) {
                                                System.err.println(ioe.getMessage());
                                            }
                                        }
                                    }
                                    textarea.setCaretPosition(0);
                                }
                                return;
                            }
                        });
 
                    final Box top = Box.createHorizontalBox();
                    top.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                    top.add(loadUnusualWords);
                    top.add(Box.createHorizontalStrut(5));
                    top.add(filename);
                    top.add(loadExcel);
                    top.add(runButton);
                    frame.add(top,BorderLayout.PAGE_START);
                    frame.add(scroller);
                    frame.pack();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
    }
    
    public String getCharset(String fileName) throws IOException{   
        
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));     
        int p = (bin.read() << 8) +bin.read();     
           
        String code = null;     
           
        switch (p) {     
            case 0xefbb:     
                code = "UTF-8";     
                break;     
            case 0xfffe:     
                code = "Unicode";     
                break;     
            case 0xfeff:     
                code = "UTF-16BE";     
                break;     
            default:     
                code = "GBK";     
        }     
        return code;   
  }    
    
    public String readALine(String file) throws Exception {  
        InputStream is = this.getClass().getResourceAsStream(file);  
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");  
        BufferedReader br = new BufferedReader(isr);  
        StringBuilder sb = new StringBuilder();
        String line = "";  
        while ((line = br.readLine()) != null) {//下面做处理  
        	sb.append(line);
        }  
        return sb.toString();
    }  
}