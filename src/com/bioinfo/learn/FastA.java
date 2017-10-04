package com.bioinfo.learn;

import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FastA {
	
	
	private static String s=null;
	private static String t=null;
	private static int k=0;
	
	private static JButton buttonConfirm =  new JButton("确定");
	private static JTextField textK = new JTextField();
	private static JTextArea textResult = new JTextArea();
	
	
	public static void main(String[] args) {
		
		init();
		
		buttonConfirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				k = Integer.valueOf(textK.getText());
				if (s!=null && t!=null && k>0) {
					
					Map<String,ArrayList<Integer>> map = generateSearchTable(k);

					int deta = calDisplacement(k,map);
					String result=null;
					if (deta>0) {
						result = "t串向右移动"+deta+"位，匹配程度最大";
						for (int i = 0; i < deta; i++) {
							t="  "+t;
						}
					}else {
						result = "t串向左移动"+(-deta)+"位，匹配程度最大";					
						for (int i = 0; i < deta; i++) {
							s="  "+s;
						}
					}
					textResult.setText(result);
					
				}
				
			}
		});
		
	}
	
	
	private static void init() {
		
		
		JFileChooser jFileChooser = new JFileChooser();
		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		JFrame jFrame = new JFrame("选择文件");
		jFrame.setLocation((int)lx/2-200,(int) ly/2-200);
		jFrame.setSize(400,500);
		JTabbedPane jTabbedPane = new JTabbedPane();
		jFrame.setContentPane(jTabbedPane);
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		
		
		Container container = new Container();
		JLabel labelSDir = new JLabel("s串所在文件 :");
		JTextField textSDir = new JTextField();
		JButton buttonSDir = new JButton("...");
		
		JLabel labelTDir = new JLabel("t串所在文件 :");
		JTextField textTDir = new JTextField();
		JButton buttonTDir = new JButton("...");
		
		
		JLabel labelK = new JLabel("请输入k：");
		
		
		
		labelSDir.setBounds(10,10,90,20);
		textSDir.setBounds(110,10,150,20);
		buttonSDir.setBounds(270,10,70,20);
		
		labelTDir.setBounds(10, 40, 90, 20);
		textTDir.setBounds(110, 40, 150, 20);
		buttonTDir.setBounds(270,40,70,20);
		
		labelK.setBounds(10,70 ,90,20);
		textK.setBounds(110, 70, 150,20);
		
		buttonConfirm.setBounds(170,180,60,20);
		textResult.setBounds(10, 210, 380, 80);
		
		container.add(labelSDir);
		container.add(textSDir);
		container.add(buttonSDir);
		container.add(labelTDir);
		container.add(textTDir);
		container.add(buttonTDir);
		container.add(labelK);
		container.add(textK);
		container.add(buttonConfirm);
		container.add(textResult);

		jTabbedPane.add("选择s串和t串的文件", container);	
		
		
		jFileChooser.setCurrentDirectory(new File("D:\\myCode\\javaCode\\FASTA"));
		buttonSDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int state = jFileChooser.showOpenDialog(null);
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					textSDir.setText(file.getAbsolutePath());
					s = readFile(file);
				}
			}
		});
		
		buttonTDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int state = jFileChooser.showOpenDialog(null);
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					textTDir.setText(file.getAbsolutePath());
					t = readFile(file);
				}
			}
		});
	}
	
	private static String readFile(File file) {
		String result="";

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				result = result+scanner.nextLine();
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private static Map<String, ArrayList<Integer>> generateSearchTable(int k) {
		Map<String,ArrayList<Integer>> map = new HashMap<>();
		for (int i = 0; i < s.length()-k+1; i++) {
			String p = s.substring(i, i+k);
			if (map.get(p) == null) {
				ArrayList<Integer> pos = new ArrayList<>();
				pos.add(i+1);
				map.put(p,pos);
				
			}else if (p!=null) {
				map.get(p).add(i+1);
			}
		}
		
		return map;		
	}
	
	private static int calDisplacement(int k,Map<String,ArrayList<Integer>> map) {
		Map<Integer,Integer> mapDisToNum = new HashMap<>();
		ArrayList<Integer> detas = new ArrayList<>();
		for (int i = 0; i < t.length()-k+1; i++) {
			String p = t.substring(i, i+k);
			ArrayList<Integer> pos = map.get(p);
			if (pos!=null) {
				for (int j = 0; j < pos.size(); j++) {
					int deta = pos.get(j)-i-1;
					if (mapDisToNum.get(deta)==null) {
						mapDisToNum.put(deta, 1);
						detas.add(deta);
					}else {
						int value = mapDisToNum.get(deta);
						mapDisToNum.put(deta, value+1);
					}
				}
			}		
		}
		int maxNum = mapDisToNum.get(detas.get(0));
		int maxDeta = detas.get(0);

		int num;
		for(int i=1;i<detas.size();i++) {
			num = mapDisToNum.get(detas.get(i));
			if (num>maxNum) {
				maxNum = num;
				maxDeta = detas.get(i);
			}
		}
		
		return maxDeta;
	}
}
