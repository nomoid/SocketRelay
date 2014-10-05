package com.markusfeng.SocketRelay.L;

import java.util.Collection;
import java.util.Set;

import com.markusfeng.SocketRelay.A.SocketHandler;

/**
 * An interface for dispatching Socket events.
 *
 * @author Markus Feng
 *
 * @param <T> The type of the SocketHandler associated with the event.
 */
public interface SocketListenerHandler<T extends SocketHandler<?>>{

	/**
	 * Returns a set that contains the SocketListeners of this SocketListenerHandler.
	 * This set may be an unmodifiable set, so do not attempt any mutating operations.
	 * @return a set that contains the SocketListeners of this SocketListenerHandler.
	 */
	Set<SocketListener<T>> getSocketListenerSet();

	/**
	 * Adds a SocketListener to this SocketListenerHandler.
	 * @param listener the SocketListener to be added to this SocketListenerHandler.
	 */
	void addSocketListener(SocketListener<T> listener);

	/**
	 * Adds a collection of SocketListener to this SocketListenerHandler.
	 * @param listeners the SocketListeners to be added to this SocketListenerHandler.
	 */
	void addSocketListeners(Collection<SocketListener<T>> listeners);

	/**
	 * Removes a SocketListener from this SocketListenerHandler.
	 * @param listener the SocketListener to be removed from this SocketListenerHandler.
	 */
	void removeSocketListener(SocketListener<T> listener);

	/**
	 * Removes a collection of SocketListener from this SocketListenerHandler.
	 * @param listeners the SocketListeners to be removed from this SocketListenerHandler.
	 */
	void removeSocketListeners(Collection<SocketListener<T>> listeners);

	/**
	 * Removes all SocketListeners from this SocketListenerHandler
	 */
	void removeAllSocketListeners();
}
