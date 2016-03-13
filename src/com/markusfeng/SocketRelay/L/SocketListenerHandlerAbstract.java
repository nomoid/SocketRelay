package com.markusfeng.SocketRelay.L;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SocketListenerHandlerAbstract<T> implements SocketListenerHandler<T>{

	protected Set<SocketListener<T>> listeners = new HashSet<SocketListener<T>>();
	protected ExecutorService tpe;

	protected SocketListenerHandlerAbstract(){
		tpe = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1024));
	}

	@Override
	public Set<SocketListener<T>> getSocketListenerSet(){
		return listeners;
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

	protected void dispatch(T handler){
		try{
			tpe.execute(new LSocketDispatcher(handler));
		}
		catch(RejectedExecutionException e){
			throw new IllegalStateException(e);
		}
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
	public void close() throws IOException{
		tpe.shutdown();
	}
}
