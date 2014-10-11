package com.markusfeng.SocketRelay.L;

import java.util.Collection;
import java.util.Set;

public class SocketListenerHandlerAbstract<T> implements SocketListenerHandler<T>{

	protected Set<SocketListener<T>> listeners;

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
		new Thread(new LSocketDispatcher(handler)).start();
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
}
