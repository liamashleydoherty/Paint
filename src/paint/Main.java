package paint;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * Version 7.
 * 
 * @version 27.10.13 Version 7
 * The array list of points now only holds
 * the current operation i.e. from when the mouse 
 * is clicked to when it is released. Once the 
 * mouse is the released the contents of the panel
 * are rendered to a buffered image and the points
 * array is re-instantiated.
 * 
 * 19:47 27.10.13
 * Trying to implement undo functionality using a stack
 * or previous buffered images
 * 
 * @author Liam
 *
 */
public class Main
{	
	/**
	 * Instantiates local object field the adds a basic
	 * tool box and drawing canvas to the screen.
	 */
	public Main()
	{
		//Start main program thread
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				new MainFrame();
			}
		});
	}
	
	/**
	 * Program entry point. This method is called
	 * as soon as the application is executed.
	 * @param args
	 */
	public static void main(String[] args)
	{
		//Create a new main class
		new Main();
	}

	/**
	 * This class is the foundations of the UI. 
	 * It boots up the Main Frame window that holds
	 * all the UI components. 
	 * @author Liam
	 * @version 23.10.13 Version 5
	 */	
	private class MainFrame extends JFrame
	{
		//Not sure what this is yet
		private static final long serialVersionUID = 1L;
		
		//Local fields
		/*What tool is active*/
		private String toolBoxActiveButton;
		/*What colour is active*/
		private Color paletteActiveColour;
		/*What second colour is active*/
		private Color paletteSecondColour;
		/*What brush stroke is active*/
		private BasicStroke activeStroke;
		/*Name of active brush stroke*/
		private String activeStrokeName;
		/*Stack of previous buffered images*/
		private Stack<BufferedImage> prevBuffer;
		/*Current image buffer*/
		private BufferedImage buffer;
		/*Canvas*/
		private Canvas canvas;
		
		/**
		 * MainFrame constructor. Used
		 * to set up JFrame to default parameters.
		 * In future versions the constructor may
		 * take arguments to define frame settings.
		 */
		public MainFrame()
		{
			//Instantiate local fields
			/*Panel to contain tool box and palette*/
			JPanel tools = new JPanel();
			/*Prev image buffer*/
			prevBuffer = new Stack<BufferedImage>();
			/*Current image buffer. Instantiate null*/
			buffer = null;
			/*Tool box*/
			toolBoxActiveButton 	= Settings.DEFAULT_ACTIVE_TOOL;
			/*Palette*/
			paletteActiveColour		= Settings.DEFAULT_ACTIVE_COLOUR;
			paletteSecondColour		= Settings.DEFAULT_SECOND_COLOUR;
			/*Brush*/
			activeStroke			= Settings.getBrushStroke();
			activeStrokeName		= Settings.DEFAULT_BRUSH_STROKE_NAME;
			
			//Set up components
			//Main menu
			this.add(new MainMenu(), BorderLayout.PAGE_START);
			//Tools
			/*Set layout*/
			tools.setLayout(new GridLayout(3, 1));
			/*Add tool box to top*/
			tools.add(new ToolBox());
			/*Add brush selector to middle*/
			tools.add(new BrushSelector());
			/*Add palette to bottom*/
			tools.add(new Palette());
			/*Add tools to west of screen*/
			this.add(tools, BorderLayout.WEST);
			//Canvas
			/*Create the canvas*/
			canvas = new Canvas();
			/*Add canvas to centre*/
			this.add(canvas, BorderLayout.CENTER);
			
			//Setup JFrame
			/*Set boundaries*/
			this.setBounds(Settings.MAINWINDOW_XPOS, Settings.MAINWINDOW_YPOS, 
					Settings.MAINWINDOW_X, Settings.MAINWINDOW_Y);
			/*Set it as focusable*/
			this.setFocusable(true);
			/*Set what to do what the frame is closed*/
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			/*Maximise the screen*/
			this.setExtendedState(MAXIMIZED_BOTH);
			/*Set the frame visible*/
			this.setVisible(true);
		}
		
		/**
		 * Nested class that builds the main menu.
		 * As of this version only has two menu options
		 * to exit the application and clear the screen.
		 * Scope issues resolved in this version and 
		 * listeners add to menu items.
		 * @author Liam
		 * @version 22.10.13 Version 2
		 */
		private class MainMenu extends JMenuBar
		{
			private static final long serialVersionUID = 1L;
			
			/**
			 * Main menu constructor
			 */
			public MainMenu()
			{
				//File Menu
				/*Creates the file menu*/
				JMenu file = new JMenu("File");
				/*Creates exit option in file menu in position 1*/
				JMenuItem exit = new JMenuItem("exit", 1);
				/*Add functionality to item*/
				exit.addActionListener(new ActionListener()
				{
					/*If exit is clicked exit the application*/
					@Override
					public void actionPerformed(ActionEvent e)
					{
						System.exit(0);
					}
				});
				/*add item to the menu*/
				file.add(exit);
				/*Add file menu to main menu*/
				this.add(file);
				
				//Edit Menu
				/*Creates the edit menu*/
				JMenu edit = new JMenu("Edit");
				/*Create undo option in edit menu in position 1*/
				JMenuItem undo = new JMenuItem("Undo", 1);
				/*Add functionality to item*/
				undo.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						/*If the stack has more than one element in it*/
						if(prevBuffer.size() > 0)
						{
							/*Pop the last element of the stack and set the buffer equal to it*/
							 buffer = prevBuffer.pop();
							 /*Repaint the canvas*/
							 canvas.repaint();
						}
					}
				});
				/*Add undo to edit menu*/
				edit.add(undo);
				/*Add edit menu to main menu*/
				this.add(edit);
			}
		}
	
		/**
		 * Nest class that builds the tool box.
		 * In this version 8 buttons are created
		 * in the tool box and the tool box is 
		 * located to the left of the screen.
		 * Scope issues resolved in this version by adding
		 * individual action listeners to each item
		 * @author Liam
		 * @version 22.10.13 Version 5
		 */
		private class ToolBox extends JPanel
		{
			private static final long serialVersionUID = 1L;

			//Local fields
			/*Array of button names*/
			private String[] buttonNames = {"Free", "Brush", "Spray", "Fill", "Line", "Ellipse",
					"Square", "Oval"};
			/*Array of buttons*/
			private JToggleButton[] buttons;

			/**
			 * Constructor for ToolBox class.
			 * Sets up the panel and adds buttons to the
			 * panel.
			 */
			public ToolBox()
			{
				//Instantiate local fields
				/*Array of buttons to the size of the array of names*/
				buttons = new JToggleButton[buttonNames.length];
				
				//Setup buttons
				/*Iterate through buttons*/
				for(int i=0;i<buttonNames.length;i++)
				{
					/*Create a new button*/
					buttons[i] = new JToggleButton(buttonNames[i]);
					/*Add functionality when clicked*/
					buttons[i].addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							/*If button is clicked pass the object to clicked method*/
							clicked(arg0);
						}
					});
					/*Add buttons*/
					this.add(buttons[i]);
				}
				/*Do click if default button*/
				for(JToggleButton button : buttons)
				{
					if(button.getText().equalsIgnoreCase(toolBoxActiveButton)) button.doClick();
				}
								
				//Set up JFrame
				/*Set layout for amount of buttons split into two columns*/
				this.setLayout(new GridLayout(buttonNames.length/2, 2));			
			}
			
			/**
			 * Method that deals with button clicks
			 * @param index
			 */
			private void clicked(ActionEvent e)
			{
				/*Iterate over buttons*/
				for(JToggleButton button : buttons)
				{
					/*Found the button object that triggered the event*/
					if(button.equals(e.getSource())) 
					{
						/*Mark selected and set tool box active button*/
						toolBoxActiveButton = button.getText();
						button.setSelected(true);
					}
					/*De-select all other button*/
					else button.setSelected(false);
				}
			}
		}

		/**
		 * Nested class for the bush selector.
		 * This creats a set of buttons similar to
		 * the tool box and palette that lets you
		 * select different brush styles. There
		 * are four different sizes for swquare and
		 * round brushes.
		 * @author liam
		 * @version 1 25.10.13
		 */
		private class BrushSelector extends JPanel
		{
			private static final long serialVersionUID = 1L;
			
			//Local fields
			/*Array of strokes*/
			private BasicStroke[] strokes = {
					/*0: Square thickness 2*/
					new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER),
					/*1: Square thickness 4*/
					new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER),
					/*2: Square thickness 6*/
					new BasicStroke(6, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER),
					/*3: Square thickness 8*/
					new BasicStroke(8, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER),
					/*4: Round thickness 2*/
					new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER),
					/*5: Round thickness 4*/
					new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER),
					/*6: Round thickness 6*/
					new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER),
					/*7: Round thickness 8*/
					new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)
					};
			/*Array of JToggleButtons*/
			private JToggleButton[] buttons;
			
			/**
			 * Brush selector constructor.
			 * Instantiates fields, and adds buttons
			 * to a grid.
			 */
			public BrushSelector()
			{
				//Instantiate local fields
				/*Set array size to number of strokes*/
				buttons = new JToggleButton[strokes.length];
				
				//Setup buttons
				/*Iterate through buttons*/
				for(int i=0;i<strokes.length;i++)
				{
					/*Create a new button*/
					buttons[i] = new JToggleButton();
					/*Set name*/
					buttons[i].setName(String.valueOf(i));
					/*Add functionality when clicked*/
					buttons[i].addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							/*If button is clicked pass the object to clicked method*/
							clicked(arg0);
						}
					});
					/*Add buttons*/
					this.add(buttons[i]);
				}
				/*Do click if default button*/
				for(JToggleButton button : buttons)
				{
					if(button.getName().equalsIgnoreCase(activeStrokeName)) button.doClick();
				}
								
				//Set up JFrame
				/*Set layout for amount of buttons split into two columns*/
				this.setLayout(new GridLayout(2, strokes.length/2));			
			}
			
			/**
			 * Method triggered by action listener
			 * added to buttons.
			 * @param e
			 */
			private void clicked(ActionEvent e)
			{
				/*iterate over buttons*/
				for(int i=0;i<buttons.length;i++)
				{
					/*Find the button that triggered the event*/
					if(buttons[i].equals(e.getSource())) 
					{
						/*Set the new active stroke name*/
						activeStrokeName = buttons[i].getName();
						/*Mark as selected*/
						buttons[i].setSelected(true);
						/*Set active stroke*/
						activeStroke = strokes[i];
					}
					/*De-select all other buttons*/
					else buttons[i].setSelected(false);
				}
			}
		}
		
		/**
		 * Nested class that builds the canvas
		 * to paint on. Contains the code that
		 * draws to the canvas and triggers a
		 * repaint.
		 * @author liam
		 * @version 25.10.13
		 */
		private class Canvas extends JPanel
		{
			private static final long serialVersionUID = 1L;
			
			//Local fields
			/*Is drawing in progress*/
			private boolean drawing;
			/*Array list of points*/
			private ArrayList<Points> points;
		
			/**
			 * Canvas constructor.
			 * Sets up the JPanel and adds functionality
			 * for drawing.
			 */
			public Canvas()
			{
				//Instantiate local fields
				/*Drawing is false by default*/
				drawing = false;
				/*Instantiate points array list*/
				points = new ArrayList<Points>();
				/*Buffered image for screen render*/
				buffer = null;
				
				//Set up JPanel
				/*Sets background colour*/
				this.setBackground(paletteSecondColour);
				/*Add mouse listener functionality*/
				this.addMouseListener(new MouseListener()
				{
					/**
					 * Repaint the canvas if click detected
					 */
					@Override
					public void mouseClicked(MouseEvent arg0)
					{
						repaint();
					}

					@Override
					public void mouseEntered(MouseEvent arg0)
					{
						
					}

					@Override
					public void mouseExited(MouseEvent arg0)
					{
						
					}

					/**
					 * Enable drawing. i.e. begin adding
					 * points to the points array list
					 */
					@Override
					public void mousePressed(MouseEvent arg0)
					{
						/*If not already drawing push current buffer onto stack*/
						if(!drawing) prevBuffer.push(buffer);
						/*enable drawing*/
						drawing = true;
						/*Local field for colour*/
						Color colour;
						colour = paletteActiveColour;
						
						/*If drawing add points*/
						if(drawing)
						{
							/*Action to take if free is active*/
							if(toolBoxActiveButton.equalsIgnoreCase("Free"))
							{
								points.add(new Points(arg0.getX(), arg0.getY(), 
										colour, new BasicStroke(1), false));
							}
							/*Action to take if Brush is active*/
							if(toolBoxActiveButton.equalsIgnoreCase("Brush"))
							{
								points.add(new Points(arg0.getX(), arg0.getY(), 
										colour, activeStroke, false));
							}
						}
						repaint();
					}

					/**
					 * Disable drawing. i.e. stop adding points
					 * to the points array list
					 */
					@Override
					public void mouseReleased(MouseEvent arg0)
					{
						/*Disable drawing*/
						drawing = false;
						/*Flag last bit as end*/
						if(points.size() >1) points.get(points.size() -1 ).markEnd();
						/*Repaint*/
						repaint();
						if(points.size() == 1) points.get(0).markEnd();
						/*Render*/
						buffer = render();
					}	
				});
				/*Add mouse motion listener functionality*/
				this.addMouseMotionListener(new MouseMotionListener()
				{
					/**
					 * If the mouse is moving and drawing is
					 * enabled at points to the points array
					 * list
					 * Repaint the canvas here
					 */
					@Override
					public void mouseDragged(MouseEvent arg0)
					{
						/*Local field for colour*/
						Color colour;
						colour = paletteActiveColour;
						
						/*If drawing add points*/
						if(drawing)
						{
							/*Action to take if free is active*/
							if(toolBoxActiveButton.equalsIgnoreCase("Free"))
							{
								points.add(new Points(arg0.getX(), arg0.getY(), 
										colour, new BasicStroke(1), false));
							}
							/*Action to take if Brush is active*/
							if(toolBoxActiveButton.equalsIgnoreCase("Brush"))
							{
								points.add(new Points(arg0.getX(), arg0.getY(), 
										colour, activeStroke, false));
							}
						}
						repaint();
					}
	
					@Override
					public void mouseMoved(MouseEvent arg0) 
					{
				
					}					
				});
			}
		
			/**
			 * paint method. called by repaint
			 */
			@Override
			public void paint(Graphics g)
			{
				/*Pass g to super class*/
				super.paint(g);
				
				/*Type cast g to Graphics2D to enable drawing of 2D shapes*/
				Graphics2D g2d = (Graphics2D)g;
				
				/*Paint image buffer*/
				g2d.drawImage(buffer, 0, 0, null);
				
				/*Iterate over points and connect them if can connect to next*/
				for(int i=0;i<points.size();i++)
				{
					/*Set the colour*/
					g2d.setColor(points.get(i).getColour());
					/*Set the stroke*/
					g2d.setStroke(points.get(i).getStroke());
					/*If this point isn't the end, then draw a line to the next point if one exists*/
					if(!points.get(i).isEnd() && i < points.size() - 1)
					{
						g2d.drawLine(points.get(i).getX(), points.get(i).getY(), 
								points.get(i + 1).getX(), points.get(i + 1).getY());
					}else{
						/*Else draw a point on the screen*/
						g2d.drawLine(points.get(i).getX(), points.get(i).getY(), 
								points.get(i).getX(), points.get(i).getY());
					}
				}
			}
			
			private BufferedImage render()
			{
			    // Create BufferedImage of JPanel
			    BufferedImage bi = new BufferedImage(this.getWidth(), 
			    this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			    Graphics2D g2d = bi.createGraphics();
			    this.paint(g2d);
			    g2d.dispose();
			         
			    // Scale dimension size of BufferedImage and return it
			    AffineTransform at = new AffineTransform();
			    at.scale(1, 1);
			    AffineTransformOp scaleOp = new AffineTransformOp(at, 
			    AffineTransformOp.TYPE_BILINEAR);
			    points = new ArrayList<Points>();
			    return scaleOp.filter(bi, null);
			}
		}
	
		/**
		 * Nested class that builds a colour
		 * palette.
		 * @author Liam
		 * @version 25.10.13 
		 */
		private class Palette extends JPanel
		{
			private static final long serialVersionUID = 1L;
			
			//Local fields
			/*Array list of button colours*/
			private Color[] colours = {Color.black, Color.white, Color.gray, Color.darkGray,
					Color.blue, Color.cyan, Color.red, Color.orange, Color.yellow, Color.green};
			/*Array list of Toggle buttons for colours*/
			private JButton[] buttons;
			
			/**
			 * Constructor for palette class.
			 * Sets up the JPanel, then sets
			 * up a grid layout to accommodate
			 * eight different colours
			 */
			public Palette()
			{
				//Instantiate local fields
				buttons = new JButton[colours.length];
				
				//Setup buttons
				/*Iterate through buttons*/
				for(int i=0;i<colours.length;i++)
				{
					/*Create a new button*/
					buttons[i] = new JButton();
					buttons[i].setBackground(colours[i]);
					/*Add functionality when clicked*/
					buttons[i].addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent arg0) 
						{
							/*If button is clicked pass the object to clicked method*/
							clicked(arg0);
						}
					});
					/*Add buttons*/
					this.add(buttons[i]);
				}

				//Set up JFrame
				/*Set grid layout for two rows of four buttons*/
				this.setLayout(new GridLayout(colours.length/2, 2));		
			}
			
			/**
			 * Method that deals with button clicks
			 * @param index
			 */
			private void clicked(ActionEvent e)
			{
				/*Iterate through buttons to find which one triggered the event*/
				for(JButton button : buttons)
				{
					/*Set the active colour to the background colour of the button that triggered the event*/
					if(e.getSource().equals(button)) paletteActiveColour = button.getBackground();
				}
			}
		}
	}
}