/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studienprojekt.gui;

import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import studienprojekt.InfileHandler;
import studienprojekt.Mapper;

/**
 *
 * @author Michael
 */
public class View extends JFrame implements DropTargetListener
{
    private static final long serialVersionUID = 1L;
    private JProgressBar progressBar;
    private JButton btnHinzufgen;
    private JButton btnStart;
    private JTextArea textArea;
    private JFrame frame;
    private final JFileChooser chooser = new JFileChooser();
    private String path;

    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {               
                try
                {
                    View window = new View();
                    window.frame.setVisible(true);
                } catch (Exception e) { }
            }
        });
    }

    public View() 
    {
	initialize();
    }

    private void initialize() 
    {
	frame = new JFrame("InformatiCup");
	frame.setBounds(100, 100, 450, 300);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);		
	frame.setLocationRelativeTo(null);
	frame.setResizable(false);		
		
	FileNameExtensionFilter filter = new FileNameExtensionFilter("Textdatei","txt");
	
	textArea = new JTextArea();
	textArea.setBounds(10, 131, 424, 119);
	textArea.setEditable(false);
	textArea.setLineWrap(true);
	frame.getContentPane().add(textArea);
		
	DropTarget dt = new DropTarget(textArea,this);
		
	btnStart = new JButton("Start");		
	btnStart.setEnabled(false);
	btnStart.addActionListener((ActionEvent arg0) -> {
            textArea.setText(getPath());
            InfileHandler infileHandler = new InfileHandler(getPath());
            Mapper mapper = new Mapper();
            //mapper.initialize();
        
            // Run the main algorithm
            //mapper.run();
        });
	btnStart.setBounds(281, 53, 153, 23);
	frame.getContentPane().add(btnStart);
		
	btnHinzufgen = new JButton("Hinzuf\u00FCgen ...");
	btnHinzufgen.addActionListener((ActionEvent arg0) -> {
            try
            {
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(filter);
                chooser.showOpenDialog(null);
                textArea.setText("Ihre ausgewählte Datei:\n\n" + chooser.getSelectedFile().getAbsolutePath());
                setPath(chooser.getSelectedFile().getAbsolutePath());
                
                //AUSLAGERN
                String[] text = getPath().split("\\.");
                if(text[2].equals("txt"))
                    btnStart.setEnabled(true);
                else
                    btnStart.setEnabled(false);
            }
            catch(Exception ex){}
        });
	btnHinzufgen.setBounds(10, 53, 146, 23);
	frame.getContentPane().add(btnHinzufgen); 
		
	progressBar = new JProgressBar(0, 100);
	progressBar.setBounds(146, 108, 146, 17);
	progressBar.setVisible(false);
	frame.getContentPane().add(progressBar);
	progressBar.setValue(50);

    }
	
    private void progress()
    {
		
    }

    @Override
    public void dragEnter(DropTargetDragEvent arg0) 
    {
    	// TODO Auto-generated method stub
    }

    @Override
    public void dragExit(DropTargetEvent arg0) 
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void dragOver(DropTargetDragEvent arg0) 
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void drop(DropTargetDropEvent dtde) 
    {
        try 
        {			
            // Ok, get the dropped object and try to figure out what it is
            Transferable tr = dtde.getTransferable();
            DataFlavor[] flavors = tr.getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) 
	    {		    	
                System.out.println("Possible flavor: " + flavors[i].getMimeType());
		       
		// Check for file lists specifically
		if (flavors[i].isFlavorJavaFileListType()) 
		{
                    // Great! Accept copy drops...
		    dtde.acceptDrop(DnDConstants.ACTION_COPY);
		    textArea.setText("Ihre ausgewählte Datei:\n\n");	
		    // And add the list of file names to our text area
		    @SuppressWarnings("rawtypes")
		    List list = (List) tr.getTransferData(flavors[i]);
		    for (int j = 0; j < list.size(); j++) 
		    {
                        textArea.append(list.get(j) + "\n");
		        //AUSLAGERN
			String[] text = list.get(j).toString().split("\\.");
			if(text[2].equals("txt"))
			{
                            btnStart.setEnabled(true);
                            setPath(list.get(j).toString());
                        }
			else
			{
                            btnStart.setEnabled(false);
                            textArea.setText("Bitte eine Textdatei eingeben");
			}
                    }
                    // If we made it this far, everything worked.
		    dtde.dropComplete(true);
		    return;
                }
            }
	    // Hmm, the user must not have dropped a file list
            System.out.println("Drop failed: " + dtde);
            dtde.rejectDrop();
        } 
	catch (UnsupportedFlavorException | IOException e) 
	{
            dtde.rejectDrop();
	}		
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent arg0) 
    {
    // TODO Auto-generated method stub
    }

    public String getPath() 
    {
    	return path;
    }

    public void setPath(String path) 
    {
        this.path = path;
    }	
}

