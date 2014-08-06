/* Name : Toan Nguyen
 * Final Project : Download Manager
 * Open file class is used to open the file after completing 
 * downloading this file
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
public class Openfile extends JFrame {
	private JLabel textlabel;
	private JButton Openbutton;
	private JButton Go;
	private JButton Cancel;
	public String filename;
	private static Integer ind;
	public Openfile(Integer ix)
	{
		ind = ix;
		Main open = new Main();
		filename = open.filearray.get(ind);
		
		//System.out.println("countfile" + open.countfile);
		textlabel = new JLabel("You just downloaded file " + filename);
		JPanel card1 = new JPanel();
		card1.add(textlabel);
		
		 Openbutton = new JButton("Open file");
		 Openbutton.addActionListener(new ActionListener ()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 String path1 = null;
				 Main open = new Main();
		
				 try {
			   
			    //String path = open.getsave().concat("\\") + filename;
				String path = open.labelarray.get(ind).concat("\\") + filename;
			    if (!path.startsWith("C")){
				path1 = path.substring(1, path.length());
			    }
			    else
			    path1 = path;
				
				path1 = path1.replace("\\", "\\\\");
				Runtime.getRuntime().exec("cmd /c \"" + path1 + "\"");
				 System.out.println(path1);
				 System.out.println(path);
				 }
				 catch (IOException o)
				 {
					 o.printStackTrace();
				 }
				 dispose();
			 }
		 });
		 Go = new JButton("Go to Directory");
		 Go.addActionListener(new ActionListener ()
		 {
			 public void actionPerformed(ActionEvent e)
				{
				 Main open = new Main();
				 String path = open.labelarray.get(ind);
				 	File folder = new File(path);
				 	Desktop desktop = null;
				 	if (Desktop.isDesktopSupported())
				 	{
				 		desktop = Desktop.getDesktop();
				 	}
				 	try
				 	{
				 		desktop.open(folder);
				 	}
				 	catch (IOException o)
				 	{
				 		
				 	}
				 	dispose();
				}
		 });
		 Cancel = new JButton("Cancel");
		 Cancel.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
				{
				 	dispose();
				}
		 });
		 JPanel card2 = new JPanel();
	     card2.add(Openbutton);
	     card2.add(Go);
	     card2.add(Cancel);
	     
	     add(card1, BorderLayout.PAGE_START);
	     add(card2,BorderLayout.CENTER);
		
	}
	  public static void main(String[] args) {
		   /* Openfile open = new Openfile();
			open.setTitle("Downloaded Completed");
			open.setSize(400,130);
			open.setLocationRelativeTo(null); // Center the frame     
	        open.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
	        open.setVisible(true);    
	        open.setAutoRequestFocus(true);  */
	  }

}
