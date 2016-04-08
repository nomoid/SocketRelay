package com.markusfeng.SocketRelay.Pipe;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SocketPipeOutImpl<T> implements SocketPipeOut<T>, Closeable{

	private ExecutorService tpe;

	public SocketPipeOutImpl(){
		this(new ThreadPoolExecutor(4, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1024)));
	}

	public SocketPipeOutImpl(ExecutorService service){
		this.tpe = service;
	}

	@Override
	public void outputToHandler(SocketPipeIn<T> pipeIn, T out, boolean block) throws IOException{
		if(block){
			getOutputter(pipeIn, out).output();
		}
		else{
			try{
				executor().execute(getOutputter(pipeIn, out));
			}
			catch(RejectedExecutionException e){
				throw new IllegalStateException(e);
			}
		}
	}

	protected Outputter getOutputter(SocketPipeIn<T> pipeIn, T out){
		return new Outputter(pipeIn, out);
	}

	/**
	 * A class used for outputting data to the handler.
	 *
	 * @author Markus Feng
	 */
	protected class Outputter implements Runnable{

		protected T text = null;
		protected SocketPipeIn<T> out = null;

		public Outputter(SocketPipeIn<T> pipeIn, T string){
			out = pipeIn;
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
			out.push(text);
		}
	}

	protected ExecutorService executor(){
		return tpe;
	}

	@Override
	public void close() throws IOException{

	}
}
