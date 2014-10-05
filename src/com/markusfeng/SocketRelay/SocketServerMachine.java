package com.markusfeng.SocketRelay;

import java.net.ServerSocket;
import java.util.List;

import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketHandlerGenerator;

/**
 * Represents a server SocketMachine that contains data about the server
 * side socket, along with each of the clients. Contains a SocketHandlerGenerator
 * that generates SocketHandlers to handle all connections with other sources.
 *
 * @author Markus Feng
 *
 * @param <T> The type of SocketHandler used in this machine.
 */
public interface SocketServerMachine<T extends SocketHandler<?>>
extends SocketMachine{

	/**
	 * Returns the ServerSocket that is owned by this server
	 * @return the ServerSocket that is owned by this server
	 */
	ServerSocket getServerSocket();

	/**
	 * Returns the List of clients that is owned by this server as SocketHandlers
	 * This list may be an unmodifiable list, so do not attempt any mutating operations.
	 * @return the List of clients that is owned by this server as SocketHandlers
	 */
	List<T> getClientList();

	/**
	 * Returns the SocketHandlerGenerator for generating SocketHandlers
	 * @return the SocketHandlerGenerator for generating SocketHandlers
	 */
	SocketHandlerGenerator<T> getHandlerGenerator();

	@Override
	ServerMachineSocket getSocket();
}
