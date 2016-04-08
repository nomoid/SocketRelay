package com.markusfeng.SocketRelay.L;

import java.util.function.Consumer;

/**
 * A listener for listening to Socket events
 *
 * @author Markus Feng
 *
 * @param <T> The type of the SocketHandler generated by the event.
 */
@FunctionalInterface
public interface SocketListener<T>extends Consumer<T>{
	//uses void accept(T handler) from superclass
	/**
	 * A Socket event involving a SocketHandler occured.
	 * @param handler The SocketHandler involved in the event.
	 */
	@Override
	void accept(T handler);
}