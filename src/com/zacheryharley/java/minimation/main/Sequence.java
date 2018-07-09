package com.zacheryharley.java.minimation.main;

import com.zacheryharley.java.logger.LogType;
import com.zacheryharley.java.logger.Logger;

public class Sequence {
	
	////////////////
	// Constants. //
	////////////////
	
	//////////////////////
	// Class variables. //
	//////////////////////
	
	//These are the static properties that exists only once no matter the number of classes initialised 
	
	////////////////////
	// Class methods. // 
	////////////////////
	
	//these are the static methods that are part of the class
	
	/////////////////////////
	// Instance variables. //
	/////////////////////////
	
	
	
	///// The sequence data \\\\\
	private String name;
	private int fps = 10;
	
	///////////////////
	// Constructors. //
	///////////////////
	
	//Constructors
	
	////////////////////////////
	// Read/Write properties. //
	////////////////////////////
	
	/**
	 * Get the name of the sequence
	 * @return Name of the sequence
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the sequence
	 * @param name The name of the sequence
	 */
	public void setName(String name) {
		if(name == null) {
			IllegalArgumentException e = new IllegalArgumentException("name cannot be null");
			Logger.log(LogType.ERROR, e, "Sequence.setName passed a Null value: " + this.name);
		}
		this.name = name;
	}
	
	/**
	 * Get the FPS the sequence runs at
	 * @return The sequence fps
	 */
	public int getFPS() {
		return fps;
	}
	
	/**
	 * Set the fps that this sequence should be run at
	 * @param fps The new frames per second to run at
	 */
	public void setFPS(int fps) {
		if(fps <= 0) {
			IllegalArgumentException e = new IllegalArgumentException("FPS cannot be less than 1");
			Logger.log(LogType.ERROR, e, "Sequence.setFPS less than 1 passed");
			fps = 10;
		}
		this.fps = fps;
	}
	
	///////////////////////////
	// Read-only properties. //
	///////////////////////////
	
	//Methods to access read only properties
	
	//////////////
	// Methods. //
	//////////////
	
}
