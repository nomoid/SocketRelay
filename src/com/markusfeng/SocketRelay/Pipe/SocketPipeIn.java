package com.markusfeng.SocketRelay.Pipe;

import java.io.IOException;

public interface SocketPipeIn<T>{

	/**
	 * Waits for output to be enabled, then writes to output
	 * @param out the object to be written
	 */
	void push(T out) throws IOException;
}
