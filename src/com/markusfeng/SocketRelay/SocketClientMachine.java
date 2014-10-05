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
	 * Returns the handler that this client uses for its connections
	 * @return the handler that this client uses for its connections
	 */
	T getHandler();

	@Override
	ClientMachineSocket getSocket();
}
