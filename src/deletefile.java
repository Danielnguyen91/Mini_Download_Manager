/* Name : Toan Nguyen
 * Final Project : Download Manager
 * Delete file is used to delete the file
 * either in the table or in hard drive
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
public class deletefile extends JFrame {
	private JLabel textlabel;
	private JButton delete;
	private JButton deleteall;
	public String filename;
	private static Integer ind;
	public deletefile(int ix)
	{
		ind = ix;
		textlabel = new JLabel("Do you want to completely delete the file?");
		JPanel card1 = new JPanel();
		card1.add(textlabel);
		delete = new JButton("No");
		delete.addActionListener(new ActionListener ()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				dispose();
				
		     }
		 });
		deleteall = new JButton("Yes");
		deleteall.addActionListener(new ActionListener ()
		 {
			 public void actionPerformed(ActionEvent e)
				{
				   Main open = new Main();
				   filename = open.filearray.get(ind);
				   String path = open.labelarray.get(ind).concat("\\") + filename;
				   System.out.println(path);
				   File file = new File(path);
				   file.delete();
				   JOptionPane.showMessageDialog(null, "Sucessfully delete the file " + filename);
				 
				   dispose();
				}
		 });
		 JPanel card2 = new JPanel();
	     card2.add(delete);
	     card2.add(deleteall);
	     
	     add(card1, BorderLayout.PAGE_START);
	     add(card2,BorderLayout.CENTER);
	}

}
