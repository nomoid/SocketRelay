package com.markusfeng.SocketRelay.A;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import com.markusfeng.SocketRelay.ClientMachineSocket;

/**
 * Used to handle Sockets in conjunction with SocketMachines.
 * Implementations read objects from a user defined input
 * and write objects to a user defined input. Reading methods
 * are defined per-user, while writing methods are defined in the handler.
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public interface SocketHandler<T> extends Runnable, Closeable{

	/**
	 * Opens SocketHandler, initializing it with the socket
	 * @param socket
	 */
	void openSocket(Socket socket);

	/**
	 * Waits for output to be enabled, then writes to output
	 * @param out the object to be written
	 */
	void push(T out) throws IOException;

	/**
	 * Returns the Client Socket that the SocketHandler is handling
	 * @return the Client Socket that the SocketHandler is handling
	 */
	ClientMachineSocket getSocket();
}
