package com.markusfeng.SocketRelay.Pipe;

import java.io.IOException;

/**
 * Used to write data to a SocketPipeIn.
 *
 * @author Markus Feng
 *
 * @param <T> The type of the object to write.
 */
public interface SocketPipeOut<T>{

	/**
	 * This method can be called to output any data from the program to the output
	 * of a specific handler.
	 * @param handler the handler to output to
	 * @param out the data to output
	 * @param block whether to block the current thread when attepting to output
	 * @throws IOException
	 */
	void outputToHandler(SocketPipeIn<T> pipeIn, T out, boolean block) throws IOException;
}
