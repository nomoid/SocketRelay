package com.markusfeng.SocketRelay.B;

import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.markusfeng.Shared.Pair;
import com.markusfeng.SocketRelay.ClientMachineSocket;
import com.markusfeng.SocketRelay.A.ClientSocketWrapper;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.L.SocketListener;
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
	protected boolean init = false;
	protected boolean closed = false;
	protected boolean open = false;
	protected boolean started = false;

	protected Set<SocketListener<Pair<SocketHandler<? extends T>, T>>> listeners;

	protected SocketHandlerAbstract(){
		listeners = new HashSet<SocketListener<Pair<
				SocketHandler<? extends T>, T>>>();
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
		dispatch(Pair.make(this, obj));
		if(!processor.isInputBlockingEnabled()){
			tpe.execute(new Inputtor(obj));
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
		if(closed){
			return;
		}
		closed = true;
		processor.removeHandler(this);
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
}
