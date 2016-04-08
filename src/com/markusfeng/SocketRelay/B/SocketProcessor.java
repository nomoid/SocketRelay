package com.markusfeng.SocketRelay.B;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.Pipe.SocketPipeOut;

/**
 * Used to process data to and from Sockets.
 * Servers as an intermediate ground between program and Socket.
 * Can contain one or more SocketHanders to transfer information to and from.
 *
 * @author Markus Feng
 *
 * @param <T> The type of the objects to read and write.
 */
public interface SocketProcessor<T>extends Closeable, SocketPipeOut<T>{

	/**
	 * Called by a SocketHandler when input is recieved
	 * @param in
	 */
	void input(T in);

	/**
	 * Adds a SocketHandler to this SocketProcessor
	 * @param handler a SocketHandler to be added to this SocketProcessor
	 */
	void attachHandler(SocketHandler<T> handler);

	/**
	 * Removes a SocketHandler from this SocketProcessor
	 * @param handler a SocketHandler to be removed from this SocketProcessor
	 */
	void removeHandler(SocketHandler<T> handler);

	/**
	 * Adds a collection of SocketHandlers to this SocketProcessor
	 * @param handlers SocketHandlers to be added to this SocketProcessor
	 */
	void attachHandlers(Collection<SocketHandler<T>> handlers);

	/**
	 * Removes a collection of SocketHandler from this SocketProcessor
	 * @param handlers SocketHandlers to be removed from this SocketProcessor
	 */
	void removeHandlers(Collection<SocketHandler<T>> handlers);

	/**
	 * Returns the Set of handlers that is associated with this SocketProcessor
	 * This set may be an unmodifiable set, so do not attempt any mutating operations.
	 * @return the Set of handlers that is associated with this SocketProcessor
	 */
	Set<SocketHandler<T>> getHandlers();

	/**
	 * Clears the Set of handlers that is associated with this SocketProcessor
	 */
	void removeAllHandlers();

	/**
	 * This method can be called to output any data from the program to the output.
	 * @param out the data to output
	 * @param block whether to block the current thread when attepting to output
	 * @throws IOException
	 */
	void output(T out, boolean block) throws IOException;

	/**
	 * Returns whether other methods calling the input method should assume it blocks
	 * @return whether other methods calling the input method should assume it blocks
	 */
	boolean isInputBlockingEnabled();
}
