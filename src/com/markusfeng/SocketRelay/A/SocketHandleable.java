package com.markusfeng.SocketRelay.A;

import java.io.IOException;

/**
 * Handling half of the SocketHandler.
 *
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public interface SocketHandleable<T>{

	/**
	 * Called by the SocketHandler when the input and output are to be initialized.
	 * @throws IOException
	 */
	void initialize() throws IOException;

	/**
	 * Called by the SocketHandler when reading from the input begins
	 * @throws IOException
	 */
	void readFromIn() throws IOException;

	/**
	 * Called by the SocketHandler when an object is to be written to the output
	 * @throws IOException
	 */
	void writeToOut(T obj) throws IOException;
}
