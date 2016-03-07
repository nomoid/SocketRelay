package com.markusfeng.SocketRelay.Line;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketHandlerAbstract;
import com.markusfeng.SocketRelay.B.SocketProcessor;

/**
 * A socket handler than handles one line of text at a time. The text is then
 * transformed using the convert method and possibly passed to the processor.
 *
 * @author Markus Feng
 *
 * @param <T> The type of the information of the processor
 */
public abstract class SocketLineHandler<T>extends SocketHandlerAbstract<T>{

	/**
	 * The OutputStream wrapper to write to
	 */
	protected PrintWriter out;

	/**
	 * The InputStream wrapper to read from
	 */
	protected BufferedReader in;

	/**
	 * Creates a new SocketLineHandler with the given processor.
	 * @param processor the processor to use
	 */
	public SocketLineHandler(SocketProcessor<T> processor){
		super(processor);
	}

	/**
	 * Creates a new SocketLineHandler with the given processor and socket.
	 * Calls openSocket(socket) within this constructor.
	 * @param socket the socket to use
	 * @param processor the processor to use
	 */
	public SocketLineHandler(Socket socket, SocketProcessor<T> processor){
		super(socket, processor);
	}

	@Override
	public void initialize() throws IOException{

		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void readFromIn() throws IOException{
		String inputLine;

		while(!isClosed() && (inputLine = in.readLine()) != null){
			if(!isClosed()){
				try{
					Maybe<T> maybe = convertToObject(inputLine);
					if(maybe.isPresent()){
						pushToProcessor(maybe.get());
					}
				}
				catch(Exception e){
					if(!isClosed()){
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void writeToOut(T out) throws IOException{
		Maybe<String> maybe = convertFromObject(out);
		if(maybe.isPresent()){
			this.out.println(maybe.get());
		}
	}

	/**
	 * Converts a string to an object of the parameterized type.
	 * The Maybe returned contains either a value, which will be passed to the processor,
	 * or the value will be not present, in which case the processor will not be notified.
	 * @param string The input string read from the line to be converted to into the object
	 * @return a Maybe that may or may not contain an object
	 */
	protected abstract Maybe<T> convertToObject(String string);

	/**
	 * Converts an object for the parameterized type to a string.
	 * The Maybe returned contains either a value, which will be passed to the handler,
	 * or the value will not be present, in which case the handler will not be notified.
	 *
	 * @param obj The object to be converted into the string to write.
	 * @return a Maybe that may or may not contain a string to write
	 */
	protected abstract Maybe<String> convertFromObject(T obj);
}
