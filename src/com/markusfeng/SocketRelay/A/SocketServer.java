package com.markusfeng.SocketRelay.A;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.markusfeng.SocketRelay.ServerMachineSocket;
import com.markusfeng.SocketRelay.SocketServerMachine;
import com.markusfeng.SocketRelay.L.SocketListener;
import com.markusfeng.SocketRelay.L.SocketListenerHandlerAbstract;

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
public class SocketServer<T extends SocketHandler<?>> extends SocketListenerHandlerAbstract<T>
implements SocketServerMachine<T>{

	protected boolean open;

	protected ServerSocket server;
	protected List<T> clients;
	protected SocketHandlerGenerator<T> generator;
	protected boolean started = false;
	protected boolean closed = false;

	/**
	 * Creates a SocketServer without any arguments.
	 */
	protected SocketServer(){
		clients = Collections.synchronizedList(new LinkedList<T>());
		listeners = new HashSet<SocketListener<T>>();
	}

	/**
	 * Creates a SocketServer with the given ServerSocket and SocketHandlerGenerator
	 * @param socket the ServerSocket associated with the server
	 * @param gen the SocketHandlerGenerator associated with the server
	 * @throws IOException
	 */
	public SocketServer(ServerSocket socket, SocketHandlerGenerator<T> gen) throws IOException{
		this();
		attachHandlerGenerator(gen);
		attachSocket(ServerSocketWrapper.get(socket));
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
	public void attachHandlerGenerator(SocketHandlerGenerator<T> generator){
		this.generator = generator;
	}

	@Override
	public void attachSocket(ServerMachineSocket socket){
		server = socket.get();
	}

	@Override
	public synchronized void open(){
		open = true;
		notify();
	}

	@Override
	public void close() throws IOException{
		super.close();
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
		synchronized(clients){
			for(T t : clients){
				try{
					t.close();
				}
				catch(Exception e1){
					e = new IOException(e1);
				}
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
					dispatch(handler);
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
	public ServerMachineSocket getSocket(){
		return new ServerSocketWrapper(server);
	}
}
