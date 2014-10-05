package com.markusfeng.SocketRelay;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * A wrapper for a Socket or a ServerSocket to allow both to substitute for
 * the same return object. Used in SocketMachine.
 * All methods are methods shared between Socket and ServerSocket.
 * See those classes for method documentation details.
 *
 * @author Markus Feng
 */
public interface MachineSocket extends Closeable{
	/**
	 * Returns the wrapped Socket.
	 * @return the wrapped Socket.
	 */
	Object get();
	void bind(SocketAddress bindpoint) throws IOException;
	InetAddress getInetAddress();
	int getLocalPort();
	InetAddress getLocalAddress();
	SocketAddress getLocalSocketAddress();
	int getSoTimeout() throws IOException;
	int getReceiveBufferSize() throws SocketException;
	boolean getReuseAddress() throws SocketException;
	int getPort();
	boolean isBound();
	boolean isClosed();
	void setPerformancePreferences(int connectionTime, int latency, int bandwidth);
	void setReceiveBufferSize(int size) throws SocketException;
	void setReuseAddress(boolean on) throws SocketException;
	void setSoTimeout(int timeout) throws SocketException;
}
