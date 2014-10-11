package com.markusfeng.SocketRelay.A;

import java.io.IOException;
import java.net.Socket;

import com.markusfeng.SocketRelay.ClientMachineSocket;

/**
 * Operations half of the SocketHandler.
 *
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public interface SocketOperator<T>{

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
