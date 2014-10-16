package com.markusfeng.SocketRelay.C;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.B.SocketProcessor;

/**
 * An abstract implementation of SocketProcessor
 *
 * @author Markus Feng
 *
 * @param <T> The type of the objects to read and write.
 */
public abstract class SocketProcessorAbstract<T> implements SocketProcessor<T>{

	protected Set<SocketHandler<T>> handlers = new HashSet<SocketHandler<T>>();

	protected ExecutorService tpe;

	protected SocketProcessorAbstract(){
		tpe = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1024));
	}

	@Override
	public void attachHandler(SocketHandler<T> handler){
		handlers.add(handler);
	}

	@Override
	public void removeHandler(SocketHandler<T> handler){
		handlers.remove(handler);
	}

	@Override
	public void attachHandlers(Collection<SocketHandler<T>> handlers){
		handlers.addAll(handlers);
	}

	@Override
	public void removeHandlers(Collection<SocketHandler<T>> handlers){
		handlers.removeAll(handlers);
	}

	@Override
	public Set<SocketHandler<T>> getHandlers(){
		return Collections.unmodifiableSet(handlers);
	}

	@Override
	public void removeAllHandlers(){
		handlers.clear();
	}

	@Override
	public void output(T out, boolean block) throws IOException{
		if(block){
			new Outputter(null, out).output();
		}
		else{
			tpe.execute(new Outputter(null, out));
		}
	}

	@Override
	public void outputToHandler(SocketHandler<T> handler, T out, boolean block) throws IOException{
		if(block){
			new Outputter(handler, out).output();
		}
		else{
			tpe.execute(new Outputter(handler, out));
		}
	}

	/**
	 * A class used for outputting data to the handler.
	 *
	 * @author Markus Feng
	 */
	protected class Outputter implements Runnable{

		protected T text = null;
		protected SocketHandler<T> out = null;

		public Outputter(SocketHandler<T> handler, T string){
			out = handler;
			text = string;
		}

		@Override
		public void run(){
			try{
				output();
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void output() throws IOException{
			IOException tempException = null;
			if(out == null){
				for(SocketHandler<T> handler : handlers){
					try{
						handler.push(text);
					}
					catch(IOException e){
						tempException = e;
					}
				}
			}
			else{
				try{
					out.push(text);
				}
				catch(IOException e){
					tempException = e;
				}
			}
			if(tempException != null){
				throw new IOException(tempException);
			}
		}
	}

	@Override
	public boolean isInputBlockingEnabled(){
		//Does not block on input
		return false;
	}
}
