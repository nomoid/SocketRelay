package com.markusfeng.SocketRelay.C;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.Pipe.SocketPipeIn;
import com.markusfeng.SocketRelay.Pipe.SocketPipeOutImpl;

/**
 * An abstract implementation of SocketProcessor
 *
 * @author Markus Feng
 *
 * @param <T> The type of the objects to read and write.
 */
public abstract class SocketProcessorAbstract<T>extends SocketPipeOutImpl<T> implements SocketProcessor<T>{

	protected Set<SocketHandler<T>> handlers = new HashSet<SocketHandler<T>>();

	private boolean closed;

	protected SocketProcessorAbstract(){
		super();
	}

	protected SocketProcessorAbstract(ExecutorService service){
		super(service);
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
		outputToHandler(null, out, block);
	}

	@Override
	protected Outputter getOutputter(SocketPipeIn<T> handler, T out){
		return new ProcessorOutputter(handler, out);
	}

	protected class ProcessorOutputter extends Outputter{

		public ProcessorOutputter(SocketPipeIn<T> handler, T out){
			super(handler, out);
		}

		@Override
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
					super.output();
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

	@Override
	public void close(){
		executor().shutdown();
		closed = true;
	}

	protected boolean isClosed(){
		return closed;
	}
}
