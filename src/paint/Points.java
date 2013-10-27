package paint;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * This class is used to hold points on the screen
 * and there relevant information. Such as position,
 * colour and brush stroke style.
 * @author liam
 *
 */
public class Points
{
	//Local fields
	/*X & Y position*/
	private int x, y;
	/*Colour of point*/
	private Color colour;
	/*Stroke of point*/
	private BasicStroke stroke;
	/*Boolean to mark end point*/
	private boolean end;

	/**
	 * Ponts constructor.
	 * @param x
	 * @param y
	 * @param colour
	 * @param stroke
	 * @param end
	 */
	public Points(int x, int y, Color colour, BasicStroke stroke, boolean end)
	{
		//Instantiate local fields
		/*X Pos*/
		this.x = x;
		/*Y Pos*/
		this.y = y;
		/*Colour*/
		this.colour = colour;
		/*Stroke style*/
		this.stroke = stroke;
		/*End marker*/
		this.end = end;
	}
	
	//Getters
	/**
	 * Returns X position
	 * @return x
	 */
	public int getX()
	{
		return this.x;
	}
	
	/**
	 * Returns Y position
	 * @return y
	 */
	public int getY()
	{
		return this.y;
	}
	
	/**
	 * Returns colour
	 * @return colour
	 */
	public Color getColour()
	{
		return this.colour;
	}
	
	/**
	 * Returns stroke style
	 * @return stroke
	 */
	public BasicStroke getStroke()
	{
		return this.stroke;
	}
	
	/**
	 * Returns end marker
	 * @return end
	 */
	public boolean isEnd()
	{
		return this.end;
	}
	
	//Setters
	/**
	 * Marks the point as an end
	 */
	public void markEnd()
	{
		end = true;
	}
}