package com.markusfeng.SocketRelay.H;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.markusfeng.SocketRelay.A.SocketHandleable;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketHandlerGenerator;
import com.markusfeng.SocketRelay.B.SocketHandlerAbstract;

/**
 * A Hub to convey all information from a SocketHandler to each other.
 *
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public class SocketHandlerHub<T> extends SocketHandlerAbstract<T> implements SocketHandlerGenerator<SocketHandlerHub<T>>{

	protected List<Socket> sockets = Collections.synchronizedList(
			new LinkedList<Socket>());

	protected boolean readyToRead;
	protected SocketHandlerGenerator<? extends SocketHandler<T>> gen;
	protected List<SocketHandler<T>> handlers = new LinkedList<SocketHandler<T>>();

	protected ExecutorService tpe;

	protected SocketHandlerHub(SocketHandlerGenerator<? extends SocketHandler<T>> gen){
		tpe = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1024));
		this.gen = gen;
	}

	@Override
	public void openSocket(Socket socket){
		synchronized(sockets){
			sockets.add(socket);
			SocketHandler<T> handler = gen.apply(socket);
			tpe.execute(new ReadFromInRunner(handler));
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
		for(SocketHandler<T> handler : handlers){
			tpe.execute(new WriteToOutRunner(handler, obj));
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

		protected SocketHandleable<T> readHandler;

		public ReadFromInRunner(SocketHandleable<T> sh){
			readHandler = sh;
		}

		@Override
		public void run(){
			try{
				while(open){
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
}
