package com.markusfeng.SocketRelay.A;

import java.io.IOException;
import java.net.Socket;

import com.markusfeng.SocketRelay.ClientMachineSocket;
import com.markusfeng.SocketRelay.SocketClientMachine;

/**
 * An implementation of SocketClientMachine. Makes a new thread of the
 * Runnable in the constructor and runs it. After close() is called, instances of
 * SocketClient's methods will not have any function. close() also calls the close
 * function of the current SocketHandler of the SocketClient.
 *
 * @author Markus Feng
 *
 * @param <T> The type of SocketHandler used in this client.
 */
public class SocketClient<T extends SocketHandler<?>> implements SocketClientMachine<T>{

	protected boolean open;

	protected Socket client;
	protected T handler;
	private boolean started = false;
	private boolean closed = false;

	/**
	 * Creates a SocketClient without any arguments.
	 */
	protected SocketClient(){

	}

	/**
	 * Creates a SocketClient with the specified Socket and Handler
	 * @param socket the socket associated with the client
	 * @param handler the handler associated with the client
	 * @throws IOException
	 */
	public SocketClient(Socket socket, T handler) throws IOException{
		this();
		attachHandler(handler);
		attachSocket(ClientSocketWrapper.get(socket));
		new Thread(this).start();
	}

	@Override
	public void attachHandler(T handler){
		this.handler = handler;
	}

	@Override
	public void attachSocket(ClientMachineSocket socket){
		this.client = socket.get();
	}

	/**
	 * Creates a SocketClient with the specified host, port, and Handler
	 * @param host the host associated with the client's socket
	 * @param port the port associated with the client's socket
	 * @param handler the handler associated with the client
	 * @throws IOException
	 */
	public SocketClient(String host, int port, T handler) throws IOException{
		this(new Socket(host, port), handler);
	}

	@Override
	public T getHandler(){
		return handler;
	}

	@Override
	public ClientMachineSocket getSocket(){
		return new ClientSocketWrapper(client);
	}

	@Override
	public synchronized void open(){
		open = true;
		notify();
	}

	@Override
	public void close() throws IOException{
		if(closed){
			return;
		}
		closed = true;
		if(!started){
			return;
		}
		open = false;
		handler.close();
	}

	@Override
	public void run(){
		synchronized(this){
			if(started){
				return;
			}
			else{
				started = true;
			}
			while(!open){
				try{
					this.wait();
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			new Thread(handler).start();
		}

	}

	protected boolean isClosed(){
		return closed;
	}
}
