package com.markusfeng.SocketRelay;

import com.markusfeng.SocketRelay.A.SocketHandler;

/**
 * Represents a client SocketMachine that contains data about the client
 * side socket. Contains a SocketHandler that handles all connections with
 * other sources.
 *
 * @author Markus Feng
 *
 * @param <T> The type of SocketHandler used in this machine.
 */
public interface SocketClientMachine<T extends SocketHandler<?>> extends SocketMachine{

	/**
	 * Attaches the handler to the SocketMachine. May be called
	 * by a constructor.
	 * @param handler the handler to attach.
	 */
	void attachHandler(T handler);

	/**
	 * Attaches the socket to the SocketMachine. May be called
	 * by a constructor.
	 * @param socket The Socket to attach.
	 */
	void attachSocket(ClientMachineSocket socket);

	/**
	 * Returns the handler that this client uses for its connections
	 * @return the handler that this client uses for its connections
	 */
	T getHandler();

	@Override
	ClientMachineSocket getSocket();
}
