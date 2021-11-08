import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

public class Comment {
	static String text[]=new String[3];
	public static  String comment(String a){
		Scanner stdIn = new Scanner(a);
		StringBuilder sb = new StringBuilder();
		while(stdIn.hasNext()){
			String line = stdIn.nextLine();
			String Comment="";
			if(line.contains(";")){
				if(line.substring(line.indexOf(";")).equals(";")){
					String str=line;
					String l=str.substring(0,str.indexOf("\t"));
					str=str.substring(str.indexOf("\t")+1);
					String r=str.substring(0,str.indexOf("\t"));
					str=str.substring(str.indexOf("\t")+1);
					str=str.replaceAll(" ","");
					str=str.replaceAll("\t","");
					ArrayList<String> al=new ArrayList<String>();
					while(!str.equals(";")){
						if(str.contains(",")){
							al.add(str.substring(0,str.indexOf(",")));
							str=str.substring(str.indexOf(",")+1);
						}
						else {
							al.add(str.substring(0,str.indexOf(";")));
							str=";";
						}
						
					}
					switch(al.size()){
					case 1:
						if(r.equals("DC")){
							Comment =Com("DC",l,al.get(0));
						}
						else if(r.equals("DS")){
							Comment =Com("DS",l,al.get(0));
						}
						else Comment=Com(r,al.get(0));
						break;
					case 2:
						if(al.get(1).length()==3){
							if(al.get(1).substring(0,2).equals("GR")){
								String com=Com(r,al.get(0),al.get(1));
								com=com.replace("(", "");
								com=com.replace(")", "");
								Comment=com;
							}
							else Comment=Com(r,al.get(0),al.get(1));
						}
						else Comment=Com(r,al.get(0),al.get(1));
						break;
					case 3:
						Comment=Com(r,al.get(0),al.get(1),al.get(2));
						break;
					}
				}
				
			}
			sb.append(line).append(Comment).append("\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Read(1);
		Read(2);
		Read(3);
		JTextArea ta = new JTextArea();
		ta.setFont(new Font("Dialog",Font.PLAIN,12));
		ta.setTabSize(4);
		MyUndoManager1 ud = new MyUndoManager1();
        ta.getDocument().addUndoableEditListener(ud);
        ta.addKeyListener(ud);
		EditorMenu em = new EditorMenu(ta);
		EditFace ef = new EditFace(ta,em);
		
	}
	
	
	static String Com(String meirei,String A){
		String com="";
		if(text[0].contains("["+meirei+"]")){
			com=text[0].substring(text[0].indexOf("["+meirei+"]"));
			com=com.substring(com.indexOf("]")+1,com.indexOf(","));
			com=com.replaceAll("a",A);
		}
		return com;
		
	}
	
	static String Com(String meirei,String A,String B){
		String com="";
		if(text[1].contains("["+meirei+"]")){
			com=text[1].substring(text[1].indexOf("["+meirei+"]"));
			com=com.substring(com.indexOf("]")+1,com.indexOf(","));
			com=com.replaceAll("a",A);
			com=com.replaceAll("b",B);
		}
		return com;
	}
	
	static String Com(String meirei,String A,String B,String C){
		String com="";
		if(text[2].contains("["+meirei+"]")){
			com=text[2].substring(text[2].indexOf("["+meirei+"]"));
			com=com.substring(com.indexOf("]")+1,com.indexOf(","));
			com=com.replaceAll("a",A);
			com=com.replaceAll("b",B);
			com=com.replaceAll("c",C);
		}
		return com;
	}
	
	static void Read(int num){
		try{
			  String fileName="text"+num+".txt";
		      File file = new File(".\\DATA\\"+fileName);
		      FileReader filereader = new FileReader(file);
		      StringBuilder sb=new StringBuilder();
		      int ch;
		      while((ch = filereader.read()) != -1){
		        sb.append((char)ch);
		      }
		      text[num-1]=sb.toString();
		      filereader.close();
		    }catch(FileNotFoundException e){
		    }catch(IOException e){
		    }
	}
}

class EditFace extends JFrame {
	
	EditFace(JTextArea ta,EditorMenu em){
		super("CASL2");
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(em,BorderLayout.NORTH);
		JScrollPane sp = new JScrollPane(ta);
		c.add(sp,BorderLayout.CENTER);
		
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}

class EditorMenu extends JMenuBar {
	
	EditorMenu(JTextArea ta){
		super();
		
		FileMenu m1 = new FileMenu(ta);
		EditMenu m2 = new EditMenu(ta);
		ViewMenu m3 = new ViewMenu(ta);
              HelpMenu m4 = new HelpMenu(ta);

		add(m1);
		add(m2);
		add(m3);
              add(m4);
	}
}

class FileMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	JMenuItem mi3;
	JMenuItem mi4;
	JMenuItem mi5;
	
	FileMenu(JTextArea ta){
		super("ファイル");
		this.ta = ta;
		
		mi1 = new JMenuItem("新規作成");
		mi2 = new JMenuItem("開く");
		mi3 = new JMenuItem("上書き保存");
		mi4 = new JMenuItem("名前をつけて保存");
		mi5 = new JMenuItem("閉じる");
		
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		mi5.addActionListener(this);
		
		add(mi1);
		add(mi2);
		add(mi3);
		add(mi4);
		add(mi5);
	}
	
	public void actionPerformed(ActionEvent e){
		EditFileAccess efa = new EditFileAccess();
		
		Object o = e.getSource();
		
		if (o == mi1){
			ta.setText("");
		}else if (o == mi2){
			ta.setText("");
			efa.fileOpen(ta);
		}else if (o == mi3){
			if (EditorStatus.FILENAME.equals("")){
				efa.fileSave(ta);
			}else{
				efa.overWrite(ta);
			}
		}else if (o == mi4){
			efa.fileSave(ta);
		}else if (o == mi5){
			System.exit(0);
		}
	}
}

class EditMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	JMenuItem mi3;
    JMenuItem mi4;
    JMenuItem mi5;
    
	EditMenu(JTextArea ta){
		super("編集");
		this.ta = ta;
		
		mi1 = new JMenuItem("切り取り");
		mi2 = new JMenuItem("コピー");
		mi3 = new JMenuItem("ペースト");
		mi4 = new JMenuItem("すべて選択");
		mi5 = new JMenuItem("自動コメント");
		
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		mi5.addActionListener(this);
		
		add(mi1);
		add(mi2);
		add(mi3);
		add(mi4);
		add(mi5);
	}
	
	
	public void actionPerformed(ActionEvent e){
		
		Object o = e.getSource();
		
		if (o == mi1){
			ta.cut();
		}else if (o == mi2){
			ta.copy();
		}else if (o == mi3){
			ta.paste();
		}else if (o == mi4){
			ta.selectAll();
		}else if (o == mi5){
			ta.setText(Comment.comment(ta.getText()));
		}
	}
}

class ViewMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	JMenuItem mi3;
	JMenuItem mi4;
	
	ViewMenu(JTextArea ta){
		super("表示");
		this.ta = ta;
		
		mi1 = new JMenuItem("文字を拡大");
		mi2 = new JMenuItem("文字を縮小");
		mi3 = new JMenuItem("フォントのサイズを指定");
		mi4 = new JMenuItem("タブのサイズを指定");
		
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		
		add(mi1);
		add(mi2);
		add(mi3);
		add(mi4);
	}
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		
		if (o == mi1){
			int size = ta.getFont().getSize();
			size++;
			String name = ta.getFont().getName();
			int style = ta.getFont().getStyle();
			ta.setFont(new Font(name,style,size));
		}else if (o == mi2){
			int size = ta.getFont().getSize();
			size--;
			String name = ta.getFont().getName();
			int style = ta.getFont().getStyle();
			ta.setFont(new Font(name,style,size));
		}else if (o == mi3){
			String sizeString = JOptionPane.showInputDialog
                                 ("フォントサイズを入力してください");
			if (sizeString == null){
				return;
			}else{
				int size = Integer.parseInt(sizeString);
				String name = ta.getFont().getName();
				int style = ta.getFont().getStyle();
				ta.setFont(new Font(name,style,size));
			}
		}else if (o == mi4){
			String tabString = JOptionPane.showInputDialog
                                     ("タブサイズを入力してください");
			if (tabString == null){
				return;
			}else{
				int tabs = Integer.parseInt(tabString);
				ta.setTabSize(tabs);
			}
		}
	}
}

class HelpMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	
	HelpMenu(JTextArea ta){
		super("ヘルプ");
		this.ta = ta;
		
		mi1 = new JMenuItem("バージョン情報");
	
		
		mi1.addActionListener(this);
	       
		
		add(mi1);
		
		
	}
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		
		if (o == mi1){
			JOptionPane.showMessageDialog(
	                  (Component)null,
	                  "はじめてのJava入門\nVersjon 1\n"+
	"Copyright (C) 2012 by takeharu narita\nuser takeharu narita\n"+
	"-------------------------------------------------\n"+
	"http://www1.bbiq.jp/takeharu/",
	                  "バージョン情報",
	                  JOptionPane.INFORMATION_MESSAGE);
			}
		
	}
}

class EditFileAccess {
	public void fileOpen(JTextArea ta){
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File f = fc.getSelectedFile();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String s;
			while ((s = br.readLine()) != null){
				ta.append(s + '\n');
			}
		}
		catch(IOException e){
			return;
		}
		
		EditorStatus.FILENAME = f.getPath();
	}
	
	public void fileSave(JTextArea ta){
		JFileChooser fc = new JFileChooser();
		fc.showSaveDialog(null);
		File f = fc.getSelectedFile();
		
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(f,false));
			
			String s = ta.getText();
			String st[] = s.split("\n");
			int limit = st.length;
			for (int i = 0;i < limit;i++){
				pw.println(st[i]);
			}
			
			pw.close();
		}
		catch(IOException e){
			return;
		}
		
		EditorStatus.FILENAME = f.getPath();
	}
	
	public void overWrite(JTextArea ta){
		File f = new File(EditorStatus.FILENAME);
		
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(f,false));
			
			String s = ta.getText();
			String st[] = s.split("\n");
			int limit = st.length;
			for (int i = 0;i < limit;i++){
				pw.println(st[i]);
			}
			
			pw.close();
		}
		catch(IOException e){
			return;
		}
	}
}
class EditorStatus {
	static String FILENAME = "";
}

class MyUndoManager1 extends UndoManager implements KeyListener {

    public MyUndoManager1() {

        super();
    }

    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
        case KeyEvent.VK_Z:    //CTRL+Zのとき、UNDO実行
            if (e.isControlDown() && this.canUndo()) {

                this.undo();
                e.consume();
            }
            break;
        case KeyEvent.VK_Y:    //CTRL+Yのとき、REDO実行
            if (e.isControlDown() && this.canRedo()) {

                this.redo();
                e.consume();
            }
            break;
        }
    }

    public void keyReleased(KeyEvent e) {
        // NOP
    }

    public void keyTyped(KeyEvent e) {
        // NOP
    }
}
