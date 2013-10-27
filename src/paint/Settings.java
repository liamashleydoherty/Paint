package paint;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * This abstract class contains default settings for the 
 * application.
 * @author Liam
 * @version 25.10.13
 */
public abstract class Settings
{
	//Main window
	/*Start X & Y*/
	public static final int MAINWINDOW_XPOS = 200;
	public static final int MAINWINDOW_YPOS = 100;
	/*Size X & Y*/
	public static final int MAINWINDOW_X = 600;
	public static final int MAINWINDOW_Y = 500;
	
	//Tool box
	/*Default active tool*/
	public static final String DEFAULT_ACTIVE_TOOL = "Free";
	public static final int DEFAULT_BRUSH_THICKNESS = 5;
	
	//Brush strokes
	/*Default brush stroke*/
	public static final BasicStroke getBrushStroke()
	{
		return new BasicStroke(2, 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}
	/*Default brush stroke name*/
	public static final String DEFAULT_BRUSH_STROKE_NAME = "0";
	
	//Palette
	/*Default active colour*/
	public static final Color DEFAULT_ACTIVE_COLOUR = Color.white;
	/*Default second colour*/
	public static final Color DEFAULT_SECOND_COLOUR = Color.black;
}