package com.markusfeng.SocketRelay.A;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import com.markusfeng.SocketRelay.ClientMachineSocket;

/**
 * A implementation of a ClientMachineSocket that wraps Socket.
 * See the documentation of Socket for more method details.
 *
 * @author Markus Feng
 */
public class ClientSocketWrapper implements ClientMachineSocket{

	protected Socket socket;

	/**
	 * Creates a new ClientSocketWrapper with the given ClientSocket.
	 * @param socket the Socket to wrap
	 */
	public ClientSocketWrapper(Socket socket){
		this.socket = socket;
	}

	public static ClientSocketWrapper get(Socket socket){
		return new ClientSocketWrapper(socket);
	}

	@Override
	public Socket get(){
		return socket;
	}

	@Override
	public void close() throws IOException{
		socket.close();
	}

	@Override
	public void bind(SocketAddress bindpoint) throws IOException{
		socket.bind(bindpoint);
	}

	@Override
	public InetAddress getInetAddress(){
		return socket.getInetAddress();
	}

	@Override
	public int getLocalPort(){
		return socket.getLocalPort();
	}

	@Override
	public InetAddress getLocalAddress(){
		return socket.getLocalAddress();
	}

	@Override
	public SocketAddress getLocalSocketAddress(){
		return socket.getLocalSocketAddress();
	}

	@Override
	public int getSoTimeout() throws SocketException{
		return socket.getSoTimeout();
	}

	@Override
	public int getReceiveBufferSize() throws SocketException{
		return socket.getReceiveBufferSize();
	}

	@Override
	public boolean getReuseAddress() throws SocketException{
		return socket.getReuseAddress();
	}

	@Override
	public boolean isBound(){
		return socket.isBound();
	}

	@Override
	public boolean isClosed(){
		return socket.isClosed();
	}

	@Override
	public void setPerformancePreferences(int connectionTime, int latency, int bandwidth){
		socket.setPerformancePreferences(connectionTime, latency, bandwidth);
	}

	@Override
	public void setReceiveBufferSize(int size) throws SocketException{
		socket.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException{
		socket.setReuseAddress(on);
	}

	@Override
	public void setSoTimeout(int timeout) throws SocketException{
		socket.setSoTimeout(timeout);
	}

	@Override
	public int getPort(){
		return socket.getPort();
	}

}
