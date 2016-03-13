package com.markusfeng.SocketRelay.A;

import java.net.Socket;

import com.markusfeng.SocketRelay.Compatibility.Function;
import com.markusfeng.SocketRelay.Compatibility.FunctionalInterface;

/**
 * An interface used to create SocketHandlers used in conjunction with SocketMachines.
 * The instances returned are used in place of a constructor and need not necessarily
 * be unique.
 * @author Markus Feng
 *
 * @param <T> The type of SocketHandler generated.
 */
@FunctionalInterface
public interface SocketHandlerGenerator<T extends SocketHandler<?>>extends Function<Socket, T>{
	//uses T apply(Socket socket) from superclass
	/**
	 * Returns an instance of a SocketHandler.
	 * This instance may or may not be returned before by this method,
	 * as long as it contains the parameter's socket.
	 *
	 * @param socket the Socket to create the SocketHandler with
	 * @return the SocketHandler generated
	 */
	@Override
	T apply(Socket socket);
}
