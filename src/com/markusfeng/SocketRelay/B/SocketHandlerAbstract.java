package com.markusfeng.SocketRelay.B;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;

import com.markusfeng.Shared.Pair;
import com.markusfeng.SocketRelay.ClientMachineSocket;
import com.markusfeng.SocketRelay.A.ClientSocketWrapper;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.L.SocketListenerHandlerAbstract;

/**
 * An abstract implementation of SocketHandler.
 *
 * @author Markus Feng
 *
 * @param <T> The type of objects to read and write.
 */
public abstract class SocketHandlerAbstract<T> extends
SocketListenerHandlerAbstract<Pair<SocketHandler<T>, T>>
implements SocketHandler<T>{

	protected Socket socket;
	protected SocketProcessor<T> processor;
	private boolean init = false;
	private boolean closed = false;
	private boolean open = false;
	private boolean started = false;

	protected SocketHandlerAbstract(){

	}

	/**
	 * Creates a new SocketHandlerAbstract with the given processor.
	 * @param processor the processor to use
	 */
	public SocketHandlerAbstract(SocketProcessor<T> processor){
		this.processor = processor;
	}

	/**
	 * Creates a new SocketHandlerAbstract with the given processor and socket.
	 * Calls openSocket(socket) within this constructor.
	 * @param socket the socket to use
	 * @param processor the processor to use
	 */
	public SocketHandlerAbstract(Socket socket, SocketProcessor<T> processor){
		this(processor);
		openSocket(socket);
	}

	@Override
	public synchronized void openSocket(Socket socket){
		this.socket = socket;
		processor.attachHandler(this);
		open = true;
		notify();
	}

	@Override
	public void run(){
		try{
			if(started || closed){
				return;
			}
			started = true;
			synchronized(this){
				while(!open){
					try{
						wait();
					}
					catch(InterruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			initialize();
			synchronized(this){
				init = true;
				notifyAll();
			}
			readFromIn();
		}
		catch(IOException e){
			if(!closed){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally{
			if(!closed){
				try{
					close();
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method pushes the object just read to the processor.
	 * This method can be called by the subclass in readFromIn().
	 * @param obj the object just read
	 */
	protected void pushToProcessor(T obj){
		dispatch(new Pair<SocketHandler<T>, T>(this, obj));
		if(!processor.isInputBlockingEnabled()){
			try{
				tpe.execute(new Inputtor(obj));
			}
			catch(RejectedExecutionException e){
				throw new IllegalStateException(e);
			}	
		}
		else{
			try{
				new Inputtor(obj).run();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void push(T out) throws IOException{
		while(!init){
			synchronized(this){
				try{
					wait();
				}
				catch(InterruptedException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(!closed){
			writeToOut(out);
		}
		else{
			throw new IOException("Socket not open");
		}
	}

	@Override
	public void close() throws IOException{
		super.close();
		if(closed){
			return;
		}
		closed = true;
		processor.removeHandler(this);
		processor.close();
		getSocket().get().close();
	}

	/**
	 * A class used for pushing inputs to the processor
	 * @author Markus Feng
	 */
	protected class Inputtor implements Runnable{

		protected T in;

		public Inputtor(T inputLine){
			in = inputLine;
		}

		@Override
		public void run(){
			try{
				processor.input(in);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	@Override
	public ClientMachineSocket getSocket(){
		return new ClientSocketWrapper(socket);
	}
	
	protected boolean isClosed(){
		return closed;
	}
}
