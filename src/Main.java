/* Name : Toan Nguyen
 * Final Project : Download Manager
 * Main class which is  a GUI of this project
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.DefaultEditorKit;


	
	public class Main extends JFrame implements Observer  {
	
	private JLabel urllabel;
	private JTextField urltext;
	private JButton download;
	private JButton pause;
	private JButton cancel;
	private JButton resume;
	private JButton delete;
	private JButton save;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu menu1;
	
	private JMenuItem menuItem;
	private JMenuItem menuItem1;
	private JMenuItem menuItem2;
	
	
	public  JTable table; 
	public  TableModel tablemodel;
	private JPanel addPanel;
	private JPanel buttonPanel;
	private JPanel tablePanel;
	private static JLabel savetextlabel = new JLabel();
	private static String filename;
	private boolean clear;
	private JPopupMenu popup;
	private static Color col =  new Color(255,224,196);
	private Download_manager selectdownload;
	
	private JToolBar buttonbar;
	
	public static Vector<String> labelarray = new Vector<String>();
	public static Vector<String> filearray = new Vector<String>();
	
	public static int countfile = 0;
	
	public ImageIcon down = 
		    new ImageIcon(getClass().getResource("download.png"));
	public ImageIcon sav = 
		    new ImageIcon(getClass().getResource("save.jpg"));
	public ImageIcon pau = 
		    new ImageIcon(getClass().getResource("pause.png"));
	public ImageIcon res = 
		    new ImageIcon(getClass().getResource("resume.png"));
	public ImageIcon can = 
		    new ImageIcon(getClass().getResource("cancel.png"));
	public ImageIcon del = 
		    new ImageIcon(getClass().getResource("delete.png"));
	
	private static File newFile;
	
	public Main()
	{
		
		// menu bar to add function
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuItem = new JMenuItem("Add Download",KeyEvent.VK_X);
		menuItem.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		      //  System.exit(0);
		      }
		    });
		menuItem1 = new JMenuItem("Exit",KeyEvent.VK_X);
		menuItem1.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        System.exit(0);
		      }
		    });
		menu1 = new JMenu("Help");
		menuItem2 = new JMenuItem("About",KeyEvent.VK_X);
		menuItem2.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  JOptionPane.showMessageDialog(addPanel, "Download Manager v1.0");
				
			      }
			    });
		
		menu.add(menuItem);
		menu.add(menuItem1);
		menu1.add(menuItem2);
		menuBar.add(menu);
		menuBar.add(menu1);
		setJMenuBar(menuBar);
		
		// content Jpanel to add the function
		addPanel = new JPanel(new FlowLayout());
		urllabel = new JLabel("Url");
		urltext = new JTextField(40);
		addPanel.add(urllabel);
		addPanel.add(urltext);
	 //   addPanel.add(savetextlabel);
	    addPanel.setBackground(col);
		tablemodel = new TableModel();
		table = new JTable(tablemodel);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			  public void valueChanged(ListSelectionEvent e) {
				  tableSelectionChanged();
	            }
		});
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		  
        // put the progress bar to the table
        
        ProgressRenderer barrenderer = new ProgressRenderer(0, 100);
        table.setDefaultRenderer(JProgressBar.class, barrenderer);
        barrenderer.setForeground(Color.GREEN);
        
       
        table.setRowHeight(
                (int) barrenderer.getPreferredSize().getHeight());
        
        
		tablePanel = new JPanel();
		tablePanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
		tablePanel.setLayout(new BorderLayout());
		JScrollPane scroll1 = new JScrollPane(table);
		scroll1.getViewport().setBackground(col);
		tablePanel.add(scroll1, BorderLayout.CENTER);
		
		
		buttonbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		
		save = new JButton("Save", sav);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				actionSave();
			}
		});
		buttonbar.add(save);
		download = new JButton("Download",down);
		download.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        actionAdd();
		      }
		    });
		buttonbar.add(download);
		pause = new JButton("Pause",pau);
	    pause.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	 selectdownload.pause();
		        		 updateButtons();
		            }
		        });
		pause.setEnabled(false);
		buttonbar.add(pause);
		resume = new JButton("Resume",res);
		resume.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	 selectdownload.resume();
	        		 updateButtons();
	            }
	        });
	        resume.setEnabled(false);
	        buttonbar.add(resume);    
	    cancel = new JButton("Cancel",can);
        cancel.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	 selectdownload.cancel();
		        		 updateButtons();
		            }
		        });
		cancel.setEnabled(false);
		buttonbar.add(cancel);
		delete = new JButton("Delete",del);
		delete.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	 clear = true;
		     	     selectdownload.delete();
		     	     tablemodel.clearDownload(table.getSelectedRow());
	        		 clear = false;
	        		 selectdownload = null;
	        		 updateButtons();
	            }
	        });
	        delete.setEnabled(false);
	        buttonbar.add(delete);
	     buttonbar.setBackground(col);
		 getContentPane().setLayout(new BorderLayout());
		 getContentPane().add(addPanel, BorderLayout.SOUTH);
		 getContentPane().add(tablePanel, BorderLayout.CENTER);
		 getContentPane().add(buttonbar, BorderLayout.NORTH);
		 
		 
		  popup = new JPopupMenu();
		 JMenuItem  menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
		 menuItem.setText("Paste");
		 menuItem.setMnemonic(KeyEvent.VK_V);
		 popup.add(menuItem);
		 JMenuItem  menuItem1 = new JMenuItem(new DefaultEditorKit.CutAction());
		 menuItem1.setText("Cut");
		 menuItem1.setMnemonic(KeyEvent.VK_X);
		 popup.add(menuItem1);
		 JMenuItem  menuItem2 = new JMenuItem(new DefaultEditorKit.CopyAction());
		 menuItem2.setText("Copy");
		 menuItem2.setMnemonic(KeyEvent.VK_C);
		 popup.add(menuItem2);
		menuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){}
		});
		 
		    urltext.addMouseListener(new MouseAdapter(){
		    	  public void mouseReleased(MouseEvent Me){
		    	  if(Me.isPopupTrigger()){
		    	  popup.show(Me.getComponent(), Me.getX(), Me.getY());
		    	  }
		    	  }
		    	  });
	}
	public String getsave()
	{
		return savetextlabel.getText();
	}
	public String getfilename()
	{
		return filename;
	}
	public void actionSave()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("OK");
		fileChooser.setDialogTitle("Specify a file to save");
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION )
		{
		newFile = fileChooser.getSelectedFile();
		}
		else if (result == JFileChooser.CANCEL_OPTION)
		{
			
		}
		/*this.savetextlabel.setText(newFile.getPath());
		labelarray.add(savetextlabel.getText());*/

	}
	private void actionAdd() {
	        URL verifiedUrl = verifyUrl(urltext.getText());
	        if (verifiedUrl != null) {
	          	this.savetextlabel.setText(newFile.getPath());
	    		labelarray.add(savetextlabel.getText());
	        	if (savetextlabel.getText() != "") {
	        	Download_manager new_file = new Download_manager(verifiedUrl,savetextlabel.getText(), countfile);
	            tablemodel.addDownload(new_file);
	        	filename = urltext.getText().substring(urltext.getText().lastIndexOf('/') + 1);
	        	filearray.add(filename);
	      
	        	countfile = countfile + 1;
	      
	            urltext.setText(""); // reset add text field
	        	}
	        	else
	        	{
	        		tablemodel.addDownload(new Download_manager(verifiedUrl));
	        		 urltext.setText("");
	        	}
	        } else {
	            JOptionPane.showMessageDialog(this,
	                    "Invalid URL Link Download", "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
	        System.out.println(filename);
	    }
	 
	
	 // validating the url link
    private URL verifyUrl(String url) {
    	boolean match = true;
    	String regex = "^(https?|http|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    	if (!url.matches(regex))
    	{
    		match = false;
    		return null;
    	}
    	
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        
        if (verifiedUrl.getFile().length() > 2)
        {
        	System.out.println("verify url link");
        }
        else 
        return null;
        
        return verifiedUrl;
    }
    // Called when table row selection changes.
    private void tableSelectionChanged() {
    
        if (selectdownload != null)
        	selectdownload.deleteObserver(Main.this);
        
        if (!clear && table.getSelectedRow() != -1) {
        	selectdownload =
                    tablemodel.getDownload(table.getSelectedRow());
        	selectdownload.addObserver(Main.this);
            updateButtons();
        }
       
    }
    
    private void updateButtons() {
        if (selectdownload != null) {
            int status = selectdownload.getStatus();
            switch (status) {
                case Download_manager.DOWNLOADING:
                    pause.setEnabled(true);
                    resume.setEnabled(false);
                    cancel.setEnabled(true);
                    delete.setEnabled(false);
                    break;
                case Download_manager.PAUSED:
                    pause.setEnabled(false);
                    resume.setEnabled(true);
                    cancel.setEnabled(true);
                    delete.setEnabled(false);
                    break;
                case Download_manager.ERROR:
                    pause.setEnabled(false);
                    resume.setEnabled(true);
                    cancel.setEnabled(false);
                    delete.setEnabled(true);
                    break;
                default: // COMPLETE or CANCELLED
                    pause.setEnabled(false);
                    resume.setEnabled(false);
                    cancel.setEnabled(false);
                    delete.setEnabled(true);
                    
            }
        } else {
            // No download is selected in table.
            pause.setEnabled(false);
            resume.setEnabled(false);
            cancel.setEnabled(false);
            delete.setEnabled(false);
        }
    }
    public void update(Observable o, Object arg) {
        // Update buttons if the selected download has changed.
        if (selectdownload != null && selectdownload.equals(o))
            updateButtons();
    }
    	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main Download = new Main();
		Download.setTitle("Download Application");
		Download.setSize(800,480);
		Download.setLocationRelativeTo(null); // Center the frame     
        Download.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        Download.setVisible(true);    
        Download.setAutoRequestFocus(true);  
	}

}
