/* Name : Toan Nguyen
 * Final Project : Download Manager
 * Table model class used to display the information about files
 * that are downling on the table
 */
import javax.swing.*;
import java.util.*;
import javax.swing.table.*;
public class TableModel extends AbstractTableModel
implements Observer{
	private double speed;
	private static final String[] columnames = {"Url", "Size", "Progress", "Status", "Time left", "Transfer rate" };
	private static final Class[] columnClass = {String.class, String.class, JProgressBar.class,String.class, String.class, String.class};
	private ArrayList Download = new ArrayList();
	
	
	public void addDownload(Download_manager download)
	{
		 download.addObserver(this);
	        Download.add(download);
	     
	        // Fire table row insertion notification to table.
	        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}
	
	 public Download_manager getDownload(int row) {
		 
	        return (Download_manager) Download.get(row);
	 	
	    }
	public void clearDownload(int row)
	{
		Download.remove(row);
		fireTableRowsDeleted(row, row);
	}
	@Override
    public int getColumnCount()
    {
        return columnames.length;
    }
	

    public String getColumnName(int col)
    {
        return columnames[col];
    }
	
    public Class getColumnClass(int col)
    {
    	return columnClass[col];
    }
    @Override
    public int getRowCount()
    {
    	return Download.size();
    }
	@Override
    public Object getValueAt(int row, int column)
    {
		Download_manager download = (Download_manager) Download.get(row);
        switch (column) {
            case 0: // URL
                return download.getUrl();
            case 1: // Size
                int size = download.getSize();
                double sizetoMB = (double)size / (1024 * 1024); 
                double round_size = (double) Math.round(sizetoMB * 100.0)/100.0;
                return (size == -1) ? "" : Double.toString(round_size).concat(" MB");
            case 2: // Progress
                return new Float(download.getProgress());
              
            case 3: // Status
            	return Download_manager.STATUSES.values()[download.getStatus()];
            case 4: // time left
            	int second = 0;
            	long time = download.gettime();
            	if (time > 60)
            	{
            		int mins = (int) time / 60;
            	    second = (int) time - mins * 60;
            		return (second <= -1) ? "0s" : Long.toString(mins).concat("mins").concat(Long.toString(second));
            	}
            	else
            	return (time <= -1) ? "0s" : Long.toString(time).concat(" s");
            case 5: // transfer rate
            	double transfer = download.getrate();
            	if (transfer > 1000)
            	{
            	    speed = (double) (Math.round((transfer / 1000) * 100.0)/100.0);
            	}
            	else 
            		speed = (double) (Math.round(transfer * 1000.0)/1000.0);
            	if (transfer > 1000)
            	{
            		return Double.toString(speed).concat(" MB/s");
            	}
            	else
            		return Double.toString(speed).concat(" kb/s");
            		
         
            	
        }
        return "";
    }
	
	public void update(Observable o, Object arg) {
	        int index = Download.indexOf(o);
	        
	        // Fire table row update notification to table.
	        fireTableRowsUpdated(index, index);
	    }
	
	
}
