package Space;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Space_ImportExport {

	private static char coordinateSeperator = ' ';
	private static char vertexSeperator = '-';
	private static char lineSeperator = '_';
	
	public static void Export(Room room) {
		FileFilter filter = new FileNameExtensionFilter("Geometric Algorithm Files","txt");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		
		switch (fc.showSaveDialog(new JFrame()))
        {
           case JFileChooser.APPROVE_OPTION:
              JOptionPane.showMessageDialog(new JFrame(), "Selected: "+
                                            fc.getSelectedFile(),
                                            "FCDemo",
                                            JOptionPane.OK_OPTION);
              exportToFile(fc.getSelectedFile(), translateToData(room));
              break;

        }
	}
	
	private static String translateToData(Room room) {
		String Result = "";
		
		for(RoomFragment Fragment : room.getFragments()) {
			for(Vertex v :Fragment.getVertices()) {
				Result += "" + v.getX() + coordinateSeperator + v.getY() + vertexSeperator;
			}
			Result += ""+lineSeperator;
		}
		
		return Result;//.trim();
	}
	
	private static void exportToFile(File fileName, String content) {
		try {
			if (!fileName.getName().endsWith(".txt")) {
				fileName = new File(fileName.getAbsolutePath() + ".txt");
			}
			fileName.createNewFile();
			
			try(  PrintWriter out = new PrintWriter( fileName )  ){
				for(String s : content.split(lineSeperator+"")) {
					out.println( s );
				}
			}	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void Import(Room room) {
		FileFilter filter = new FileNameExtensionFilter("Geometric Algorithm Files","txt");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		
		switch (fc.showSaveDialog(new JFrame()))
        {
           case JFileChooser.APPROVE_OPTION:
        	  translateToRoom(room, importFromFile(fc.getSelectedFile()));
              break;

        }
	}
	
	private static void translateToRoom(Room room, String Content) {
		String[] Lines = Content.split("\n");
		String[] Data1;
		
		System.out.println("TranslateToRoom" + Content);
		
		room.clear();
		
		for(int i=0; i<Lines.length; i++) {
			System.out.println(i);
			
			ArrayList<Vertex> Vertices = new ArrayList<Vertex>();
			Vertex prev = null;
			for(String VertexString : Lines[i].split(vertexSeperator+"")) {
				System.out.println("vertex: "+VertexString);
				
				prev = new Vertex(
						Double.parseDouble(VertexString.split(coordinateSeperator + "")[0]),
						Double.parseDouble(VertexString.split(coordinateSeperator + "")[1]),
						prev);
				Vertices.add(prev);
			}
			prev.setNext(Vertices.get(0));
			Vertices.get(0).setPrevious(prev);
			
			room.addFragment(new RoomFragment(Vertices));
			
		}
		
	}
	
	private static String importFromFile(File fileName) {
		try {
			if (!fileName.getName().endsWith(".txt")) {
				return null;
			}
			String result = "";
			String line;
			
			try(  BufferedReader in = new BufferedReader( new FileReader(fileName.getAbsolutePath()) )  ){
				while((line = in.readLine()) != null)
				{
				    result+=line.trim()+"\n";
				}
				in.close();
			}	
			
			return result.trim();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
}
