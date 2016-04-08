package com.markusfeng.SocketRelay.A;

import java.net.Socket;

import com.markusfeng.SocketRelay.ClientMachineSocket;
import com.markusfeng.SocketRelay.Pipe.SocketPipeIn;

/**
 * Operations half of the SocketHandler.
 *
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public interface SocketOperator<T>extends SocketPipeIn<T>{

	/**
	 * Opens SocketHandler, initializing it with the socket
	 * @param socket
	 */
	void openSocket(Socket socket);

	/**
	 * Returns the Client Socket that the SocketHandler is handling
	 * @return the Client Socket that the SocketHandler is handling
	 */
	ClientMachineSocket getSocket();
}
