/* Name : Toan Nguyen
 * Final Project : Download Manager
 * Download manager class which running the download task 
 * each file is a task
 */
import java.util.*;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
public class Download_manager extends Observable implements Runnable {
	private static final int MAX_BUFFER_SIZE = 1024;
	public enum STATUSES {Downloading,Paused,Completed,Cancelled,Error};
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	    
	private URL url; // download URL
	private int size; // size of download in bytes
	private int downloaded; // number of bytes downloaded
	private int status; // current status of download
	private double speedinkb; //transfer rate
	private long time; // time left
	public Integer index;
	
	private static String saveDir;
	private static String fileName;
	
	public Download_manager()
	{
		size = -1;
        downloaded = 0;
        status = DOWNLOADING;
        speedinkb = 0.0D;
        time = 0;
      
	}
	public Download_manager(URL url)
	{
		    this.url = url;
	        size = -1;
	        downloaded = 0;
	        status = DOWNLOADING;
	        speedinkb = 0.0D;
	        time = 0;
	        // Begin the download.
	        Download();
	}
	 public Download_manager(URL url, String save, Integer ix) {
		 	this.index = ix;
	        this.url = url;
	        this.saveDir = save;
	        size = -1;
	        downloaded = 0;
	        status = DOWNLOADING;
	        speedinkb = 0.0D;
	        time = 0;
	        // Begin the download.
	         Download();
	    }
	 	public String getdir()
	 	{
	 		return saveDir;
	 	}
	 // Get this download's URL.
	    public String getUrl() {
	        return url.toString();
	    }
	    
	    // Get this download's size.
	    public int getSize() {
	        return size;
	    }
	    
	    // Get this download's progress.
	    public float getProgress() {
	        return ((float) downloaded / size) * 100;
	    }
	    // get filename
	    public String getname()
	    {
	    	return fileName;
	    }
	    // Get this download's status.
	    public int getStatus() {
	        return status;
	    }
	    // Get transfer rate
	    public double getrate()
	    {
	    	return speedinkb;
	    }
	    // Get time left
	    public long gettime()
	    {
	    	return time;
	    }
	    public void pause()
	    {
	    	status = PAUSED;
	    	stateChanged();
	    }
	    public void resume()
	    {
	    	status = DOWNLOADING;
	    	stateChanged();
	    	Download();
	    }
	    public void cancel()
	    {
	    	status = CANCELLED;
	    	stateChanged();
	    }
	    private void error() {
	        status = ERROR;
	        stateChanged();
	    }
	    public void delete()
	    {
	    	deletefile delete = new deletefile(index);
            
       	    delete.setTitle("Delete confirmation");
    		 delete.setSize(400,130);
    		 delete.setLocationRelativeTo(null); // Center the frame     
    	     delete.setVisible(true);    
    	     delete.setAutoRequestFocus(true); 
	    }
	    public void Download()
	    {
	    	Thread th = new Thread(this);
	    	th.start();
	    }
	    private String getFileName(URL url) {
	        fileName = url.getFile();
	        return fileName.substring(fileName.lastIndexOf('/') + 1);
	    }
	    public void run()
	    {
	        RandomAccessFile file = null;
	        FileOutputStream out = null;
	        InputStream stream = null;
	        try
	        {
	        	 HttpURLConnection connection =
	                     (HttpURLConnection) url.openConnection();
	        	 
	        	 // Specify what portion of file to download.
	             connection.setRequestProperty("Range",
	                     "bytes=" + downloaded + "-");
	             
	             // Connect to server.
	             connection.connect();
	             System.out.println("Connected!");
	             
	             // Make sure response code is in the 200 range.
	             int response = connection.getResponseCode();
	            
	            	// if (response / 100 != 2) 
	             	if (response != HttpURLConnection.HTTP_OK || response != HttpURLConnection.HTTP_PARTIAL)
	             	{
	            		 error();
	            	 }
	            	 else
	            	 {
	            	 System.out.println("Response code from the server is " + response);
	            	 }
	             // Check for valid content length.
	            	 int contentLength = connection.getContentLength();
	            	 if (contentLength < 1) {
	            		 error();
	            	 }
	             
	       /* Set the size for this download if it
	          hasn't been already set. */
	             if (size == -1) {
	                 size = contentLength;
	                 stateChanged();
	             }
	             
	             // Open file and seek to the end of it.
	           /*  file = new RandomAccessFile(getFileName(url), "rw");
	             file.seek(downloaded);*/
	             
	             stream = connection.getInputStream();
	             status = DOWNLOADING;
	             if (saveDir != null && !saveDir.isEmpty())
	            	
	             out = new FileOutputStream(saveDir + File.separator + this.getFileName(url),true);
	           
	           
	             long startime = System.nanoTime();
	             while (status == DOWNLOADING)
	             {
	            	  
	            	    byte buffer[];
	                    if (size - downloaded > MAX_BUFFER_SIZE) {
	                        buffer = new byte[MAX_BUFFER_SIZE];
	                    } else {
	                        buffer = new byte[size - downloaded];
	                    }
	                    
	                    // Read from server into buffer.
	                    int read = stream.read(buffer);
	                    if (read == -1)
	                        break;
	                    if (out != null) 
	                    out.write(buffer, 0, read);
	                    else 
	                    file.write(buffer,0,read);
	                    downloaded += read;
	                    stateChanged();
	                 
	                    try {
	                    	long timeinsecs = (System.nanoTime() - startime)/1000000000;
	                    	speedinkb = (downloaded / timeinsecs) / 1024D;
	                    	double sizetoMB = (double)size / (1024 * 1024); 
	                        double round_size = (double) Math.round(sizetoMB * 100.0)/100.0;
	                        double speed = (double) (Math.round((speedinkb / 1000) * 100.0)/100.0);
	                    	double totaltime = round_size / speed;
	                    	time = (long) totaltime - timeinsecs;
	                    	
	                    }
	                    catch(ArithmeticException ae)
	                    {
	                    
	                    }
	             }
	             if (status == DOWNLOADING) {
	                 status = COMPLETE;
	                 stateChanged();
	             }
	             if (status == COMPLETE)
	             {
	            	Openfile open = new Openfile(index);
	            	open.setTitle("Download Completed");
	     			open.setSize(400,130);
	     			open.setLocationRelativeTo(null); // Center the frame     
	     	        open.setVisible(true);    
	     	        open.setAutoRequestFocus(true);  
	             }
	        }
	        catch(Exception e)
	        {
	        	error();
	        }
	        finally {
	            // Close file.
	            if (out != null) {
	                try {
	                   out.close();
	                   file.close();
	                } catch (Exception e) {}
	            }
	            if (file != null) {
	                try {
	                //	out.close();
	                   file.close();
	                } catch (Exception e) {}
	            }
	            
	            // Close connection to server.
	            if (stream != null) {
	                try {
	                    stream.close();
	                } catch (Exception e) {}
	            }
	   	 }
	    
	    }
	    private void stateChanged() {
	        setChanged();
	        notifyObservers();
	    }
	    
}
