package client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import service_interface.DrawingCommand;

/**
 * Application program to present the user with a GUI comprising a drawing
 * space and a menu of drawing commands.
 * 
 * @author Ian Warren
 *
 */
public class DrawingApp extends JPanel {

	private DrawingSpace fDrawingSpace;
	private JMenu fDrawMenu;

	/**
	 * Creates a new DrawingApp instance.
	 * @param propertiesFileName the name of a properties file that lists the
	 * names of classes that implement the DrawingCommand interface. For each
	 * class named in the properties file, an attempt is made to load and 
	 * instantiate it. Successfully loaded commands are offered to the user by
	 * being added to the application's "Draw" menu.
	 */
	public DrawingApp(String propertiesFileName) {
		try {
			// Construct local drawing space. The DrawingSpace object maintains
			// a reference to this DrawingApp instance so that it can access
			// useful functionality - this may be helpful in the future.
			fDrawingSpace = new DrawingSpace(this);
			
			// Construct GUI.
			JFrame frame = new JFrame("Drawing application");
			frame.setSize(400, 400);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JMenuBar menuBar = new JMenuBar();
			fDrawMenu = new JMenu("Draw");
			menuBar.add(fDrawMenu);
			frame.setJMenuBar(menuBar);
			frame.add(fDrawingSpace);

			// Load drawing commands.
			List<DrawingCommand> drawingCommands = loadDrawingCommands(propertiesFileName);
			for(DrawingCommand command : drawingCommands) {
				addDrawingCommand(command);
				
			}

			// Have window centred on screen.
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = frame.getSize();
			frame.setLocation(screenSize.width / 2 - (frameSize.width / 2),
					screenSize.height / 2 - (frameSize.height / 2));
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attempts to add a new DrawingCommand to the application's "Draw" menu. 
	 * This succeeds only if a DrawingCommand with the same name is not already
	 * listed within the menu. Where successful, the application's DrawingSpace
	 * object is added as a listener to the new DrawingCommand; hence any 
	 * drawing events generated by the DrawingCommand object are received by
	 * DrawingSpace instance.
	 * @param command the new DrawingCommand object.
	 */
	public void addDrawingCommand(DrawingCommand command) {
		int entries = fDrawMenu.getItemCount();
		int index = 0;
		boolean match = false;
		
		while(index < entries && (!match)) {
			JMenuItem item = fDrawMenu.getItem(index);
			DrawingCommand existingCommand = ((DrawingAction)item.getAction()).drawingCommand();
			if(existingCommand.name().equals(command.name())) {
				match = true;
			}
			index++;
		}
		
		if(!match) {
			fDrawMenu.add(new JMenuItem(new DrawingAction(fDrawingSpace, command)));
			command.addDrawingCommandListener(fDrawingSpace);
		}
	}
	
	/**
	 * Main program to run the DrawingApp client. 
	 * @param args a command line argument can be supplied to name a 
	 * properties file that names classes which implement the DrawingCommand
	 * interface. 
	 */
	public static void main(String[] args) {
		if(args.length == 1) {
			// Name of properties file specified on the command line.
			new DrawingApp(args[0]);
		} else {
			new DrawingApp(null);
		}
	}
	
	/**
	 * Returns a list, possibly empty, of DrawingCommand objects. This method
	 * attempts to access a specified properties file that is expected to name
	 * classes that implement the DrawingCommand interface. For each named
	 * DrawingCommand implementation, it is instantiated and returned in the 
	 * list.
	 * @param filename the name of the properties file.
	 * @return a list of DrawingCommand objects.
	 */
	private List<DrawingCommand> loadDrawingCommands(String filename) {
		final String COMMANDS = "drawing_command_classes";
		
		List<DrawingCommand> commands = new ArrayList<DrawingCommand>();
		File file = new File(filename);
		
		try {
			// Create a new properties object.
			Properties props = new Properties();
			
			// Attempt to read property value from file.
			InputStream in = new FileInputStream(file);
			props.load(in);
			in.close();
			
			// Split, based on white space, the list of named classes.
			String allDrawingCommandClassNames = props.getProperty(COMMANDS);
			String[] drawingCommandClassNames = null;
			if(allDrawingCommandClassNames != null) {
				drawingCommandClassNames = allDrawingCommandClassNames.split("\\s+");
			}
			
			// Attempt to load and instantiate classes. In the event of an 
			// exception loading a class, ignore it and move on to the next
			// class.
			for(int i = 0; i < drawingCommandClassNames.length; i++) {
				String className = drawingCommandClassNames[i];
				try {
					Class c = Class.forName(className);
					DrawingCommand command = (DrawingCommand)c.newInstance();
					commands.add(command);
				} catch(ClassNotFoundException e) {
					// Named class does not exist.
					System.err.println("Class " + className + " does not exist.");
				} catch(IllegalAccessException e) {
					// Named class does not have a visible default constructor.
					System.err.println("Class " + className + " cannot be instantiated.");
				} catch(InstantiationException e) {
					// Named class cannot be instantiated, e.g. its abstract.
					System.err.println("Class " + className + " cannot be instantiated.");
				} catch(ClassCastException e) {
					// Named class is not an implementation of DrawingCommand.
					System.err.println("Class " + className + " does not implement DrawingCommand.");
				}
			}
			
		} catch(IOException e) {
			// Properties file not found, or an error has occurred reading the 
			// file.
			System.err.println("File + " + file.getName() + " not found.");
		} catch(NullPointerException e) {
			// Properties key COMMANDS does not appear in the properties file.
			System.err.println("Property key " + COMMANDS + " not found.");
		} 
		
		// Return the list of DrawingCommand objects.
		return commands;
	}
}