package com.markusfeng.Shared;

import java.util.Map;

/**
 * Represents a Command that can be passed with a name and a map of arguments.
 *
 * @author Markus Feng
 */
public interface Command{

	/**
	 * Returns the name of the command
	 * @return the name of the command
	 */
	String getName();

	/**
	 * Returns the arguments of the command
	 * @return the arguments of the command
	 */
	Map<String, String> getArguments();
}
