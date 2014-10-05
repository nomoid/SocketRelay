package com.markusfeng.SocketRelay.B;

import java.util.function.Supplier;

/**
 * An interface used to create SocketProcessors used in conjunction with SocketHandlers.
 * The instances returned are used in place of a constructor and need not necessarily
 * be unique.
 * @author Markus Feng
 *
 * @param <T> The type of SocketProcessor generated.
 */
@FunctionalInterface
public interface SocketProcessorGenerator<T extends SocketProcessor<?>> extends Supplier<T>{
	//uses T get() from superclass
	/**
	 * Returns an instance of a SocketProcessor.
	 * This instance may or may not be returned before by this method,
	 * as long as it contains the parameter's socket.
	 *
	 * @return the SocketProcessor generated
	 */
	@Override
	T get();
}
