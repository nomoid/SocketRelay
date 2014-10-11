package com.markusfeng.SocketRelay.A;

import java.io.Closeable;

import com.markusfeng.Shared.Pair;
import com.markusfeng.SocketRelay.L.SocketListenerHandler;

/**
 * Used to handle Sockets in conjunction with SocketMachines.
 * Implementations read objects from a user defined input
 * and write objects to a user defined input. Reading methods
 * are defined per-user, while writing methods are defined in the handler.
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public interface SocketHandler<T> extends Runnable, Closeable,
SocketHandleable<T>, SocketOperator<T>, SocketListenerHandler<Pair
<SocketHandler<T>, T>>{

}
