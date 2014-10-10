package com.markusfeng.SocketRelay.A;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.markusfeng.SocketRelay.ServerMachineSocket;
import com.markusfeng.SocketRelay.SocketServerMachine;
import com.markusfeng.SocketRelay.L.SocketListener;
import com.markusfeng.SocketRelay.L.SocketListenerHandler;

/**
 * An implementation of SocketServerMachine. Makes a new thread of the
 * Runnable in the constructor and runs it. After close() is called, instances of
 * SocketServer's methods will not have any function. close() also calls the close
 * function of the all SocketHandlers of the clients of the SocketServer.
 *
 * This class also contains methods to allow it to be a SocketListenerHandler. The
 * accept(T) function is called whenever a new incoming connection is formed.
 *
 * @author Markus Feng
 *
 * @param <T> The type of SocketHandler used in this server.
 */
public class SocketServer<T extends SocketHandler<?>> implements SocketServerMachine<T>, SocketListenerHandler<T>{

	protected boolean open;

	protected ServerSocket server;
	protected Set<SocketListener<T>> listeners;
	protected List<T> clients;
	protected SocketHandlerGenerator<T> generator;
	protected boolean started = false;
	protected boolean closed = false;

	/**
	 * Creates a SocketServer without any arguments.
	 */
	protected SocketServer(){
		clients = new LinkedList<T>();
		listeners = new CopyOnWriteArraySet<SocketListener<T>>();
	}

	/**
	 * Creates a SocketServer with the given ServerSocket and SocketHandlerGenerator
	 * @param socket the ServerSocket associated with the server
	 * @param gen the SocketHandlerGenerator associated with the server
	 * @throws IOException
	 */
	public SocketServer(ServerSocket socket, SocketHandlerGenerator<T> gen) throws IOException{
		this();
		generator = gen;
		server = socket;
		new Thread(this).start();
	}

	/**
	 * Creates a SocketServer with the given port and SocketHandlerGenerator
	 * @param port the port associated with the server's ServerSocket
	 * @param gen the SocketHandlerGenerator associated with the server
	 * @throws IOException
	 */
	public SocketServer(int port, SocketHandlerGenerator<T> gen) throws IOException{
		this(new ServerSocket(port), gen);
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
		IOException e = null;
		open = false;
		server.close();
		for(T t : clients){
			try{
				t.close();
			}
			catch(Exception e1){
				e = new IOException(e1);
			}
		}
		if(e != null){
			throw e;
		}
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
					wait();
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			while(open){
				try{
					Socket client = server.accept();
					T handler = generator.apply(client);
					clients.add(handler);
					new Thread(handler).start();
					new Thread(new LSocketDispatcher(handler)).start();
				}
				catch(Exception e){
					if(!closed){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	public ServerSocket getServerSocket(){
		return server;
	}

	@Override
	public SocketHandlerGenerator<T> getHandlerGenerator(){
		return generator;
	}

	@Override
	public List<T> getClientList(){
		return Collections.unmodifiableList(clients);
	}

	@Override
	public Set<SocketListener<T>> getSocketListenerSet(){
		return Collections.unmodifiableSet(listeners);
	}

	@Override
	public void addSocketListener(SocketListener<T> listener){
		listeners.add(listener);
	}

	@Override
	public void addSocketListeners(Collection<SocketListener<T>> listeners){
		listeners.addAll(listeners);
	}

	@Override
	public void removeSocketListener(SocketListener<T> listener){
		listeners.remove(listener);
	}

	@Override
	public void removeSocketListeners(Collection<SocketListener<T>> listeners){
		listeners.removeAll(listeners);
	}

	@Override
	public void removeAllSocketListeners(){
		listeners.clear();
	}

	/**
	 * A class for handling dispatches to the SocketListeners.
	 *
	 * @author Markus Feng
	 */
	protected class LSocketDispatcher implements Runnable{

		protected T handler;

		public LSocketDispatcher(T handler){
			this.handler = handler;
		}

		@Override
		public void run(){
			for(SocketListener<T> listener : listeners){
				try{
					listener.accept(handler);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		protected class LSocketAttachRunner implements Runnable{

			protected SocketListener<T> listener;
			protected T handler;

			public LSocketAttachRunner(SocketListener<T> listener, T handler){
				this.listener = listener;
				this.handler = handler;
			}

			@Override
			public void run(){
				try{
					listener.accept(handler);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public ServerMachineSocket getSocket(){
		return new ServerSocketWrapper(server);
	}
}
