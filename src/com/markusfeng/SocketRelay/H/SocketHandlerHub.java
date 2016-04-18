package com.markusfeng.SocketRelay.H;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import com.markusfeng.Shared.Pair;
import com.markusfeng.SocketRelay.A.SocketHandleable;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketHandlerGenerator;
import com.markusfeng.SocketRelay.B.SocketHandlerAbstract;
import com.markusfeng.SocketRelay.L.SocketListener;

/**
 * A Hub to convey all information from a SocketHandler to each other.
 *
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public class SocketHandlerHub<T>extends SocketHandlerAbstract<T>
		implements SocketHandlerGenerator<SocketHandlerHub<T>>, SocketListener<Pair<SocketHandler<T>, T>>{

	protected List<Socket> sockets = Collections.synchronizedList(new LinkedList<Socket>());

	protected boolean readyToRead;
	protected boolean redirect;
	protected SocketHandlerGenerator<? extends SocketHandler<T>> gen;
	protected List<SocketHandler<T>> handlers = new LinkedList<SocketHandler<T>>();

	protected SocketHandlerHub(){

	}

	public SocketHandlerHub(SocketHandlerGenerator<? extends SocketHandler<T>> gen, boolean redirect){
		this();
		this.gen = gen;
		this.redirect = redirect;
	}

	@Override
	public void openSocket(Socket socket){
		synchronized(sockets){
			sockets.add(socket);
			SocketHandler<T> handler = gen.apply(socket);
			try{
				tpe.execute(new ReadFromInRunner(handler));
			}
			catch(RejectedExecutionException e){
				throw new IllegalStateException(e);
			}
			handlers.add(handler);
		}
	}

	@Override
	public void initialize() throws IOException{
		//Do nothing
	}

	@Override
	public void readFromIn() throws IOException{
		synchronized(SocketHandlerHub.this){
			readyToRead = true;
			SocketHandlerHub.this.notifyAll();
		}
	}

	@Override
	public void writeToOut(T obj) throws IOException{
		writeToOut(obj, null);
	}

	protected void writeToOut(T obj, SocketHandler<T> exception) throws IOException{
		for(SocketHandler<T> handler : handlers){
			if(exception != null && handler.equals(exception)){
				tpe.execute(new WriteToOutRunner(handler, obj));
			}
		}
	}

	@Override
	public SocketHandlerHub<T> apply(Socket socket){
		//openSocket(socket);
		return this;
	}

	/**
	 * Runnable to apply readFromIn to handlers.
	 *
	 * @author Markus Feng
	 */
	protected class ReadFromInRunner implements Runnable{

		protected SocketHandler<T> readHandler;

		public ReadFromInRunner(SocketHandler<T> sh){
			readHandler = sh;
		}

		@Override
		public void run(){
			try{
				synchronized(SocketHandlerHub.this){
					while(!readyToRead){
						try{
							SocketHandlerHub.this.wait();
						}
						catch(InterruptedException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if(redirect){
					readHandler.addSocketListener(SocketHandlerHub.this);
				}
				readHandler.readFromIn();
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Runnable to apply writeToOut to handlers.
	 *
	 * @author Markus Feng
	 */
	protected class WriteToOutRunner implements Runnable{

		protected SocketHandleable<T> writeHandler;
		protected T t;

		public WriteToOutRunner(SocketHandler<T> sh, T obj){
			writeHandler = sh;
			t = obj;
		}

		@Override
		public void run(){
			try{
				writeHandler.writeToOut(t);
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void accept(Pair<SocketHandler<T>, T> handler){
		if(redirect){
			try{
				writeToOut(handler.getValueTwo(), handler.getValueOne());
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
