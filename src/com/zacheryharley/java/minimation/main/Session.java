package com.zacheryharley.java.minimation.main;

import java.util.ArrayList;

import com.zacheryharley.java.logger.LogType;
import com.zacheryharley.java.logger.Logger;

import javafx.scene.paint.Color;

/**
 * 
 * @author Zachery Harley (zacheryharley@live.co.uk)
 * Store all the data the user is currently using. All segments frames and layers can be accessed from the current session.
 */
public class Session {
	
	
	////////////////
	// Constants. //
	////////////////
	
	//Store your constants here if you have any
	
	//////////////////////
	// Class variables. //
	//////////////////////
		
	
	////////////////////
	// Class methods. // 
	////////////////////
	
	//these are the static methods that are part of the class
	
	/////////////////////////
	// Instance variables. //
	/////////////////////////
	
	///// Session Data \\\\\
	private ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		
	///// Active Data \\\\\
	private Color activeColor = Color.BLACK; //Users current color
	private Sequence activeSequence = null; //The active frame is contained within the active sequence
	
	///////////////////
	// Constructors. //
	///////////////////
	
	public Session() {
		Logger.log("Creating application session");
	}
	
	////////////////////////////
	// Read/Write properties. //
	////////////////////////////
	
	/**
	 * Get the active color.
	 * @return The active color
	 */
	public Color getActiveColor() {
		return this.activeColor;
	}
	
	/**
	 * Sets the active color that will be used when the user draws onto the canvas
	 * @param newColor The new active color
	 */
	public void setActiveColor(Color newColor) {
		if(newColor == null) {
			IllegalArgumentException e = new IllegalArgumentException("newColor cannot be Null");
			Logger.log(LogType.ERROR, e, "Session.setActiveColor: Null color passed");
			newColor = Color.BLACK;
		}
		activeColor = newColor;
	}
	
	/**
	 * Get the active sequence
	 * @return The active sequence
	 */
	public Sequence getActiveSequence() {
		return activeSequence;
	}
	
	/**
	 * Return a list of all sequences currently contained in the current
	 * session. <p>
	 * NOTE: This list is a clone and therefore can be modified without risk, however
	 * the elements contained are not. 
	 * @return A clone of the sequence array list
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Sequence> getSequenceList() {
		return (ArrayList<Sequence>) sequences.clone();
	}
	
	/**
	 * Set the active sequence currently being worked on by the user.
	 * @param sequence The active sequence
	 */
	public void setActiveSequence(Sequence sequence) {
		if(!sequences.contains(sequence) && sequence != null) {
			Logger.log("New sequence added: " + sequence.getName());
			sequences.add(sequence);
		}
		
		if(sequence == null) {
			Logger.log("Active sequence set to: NULL");
		} else {
			Logger.log("Active sequence set to: " + sequence.getName());
		}
		
		activeSequence = sequence;
	}
	
	///////////////////////////
	// Read-only properties. //
	///////////////////////////
	
	//Methods to access read only properties
	
	//////////////
	// Methods. //
	//////////////
}
