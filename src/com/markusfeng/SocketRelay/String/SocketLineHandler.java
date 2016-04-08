package com.markusfeng.SocketRelay.String;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.H.SocketIOHandler;
import com.markusfeng.SocketRelay.Pipe.SocketPipeIn;

/**
 * A socket handler than handles one line of text at a time. The text is then
 * transformed using the convert method and possibly passed to the processor.
 *
 * @author Markus Feng
 *
 * @param <T> The type of the information of the processor
 */
public abstract class SocketLineHandler<T>extends SocketIOHandler<T>{

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

	/**
	 * Creates a new SocketLineHandler with the given processor, input stream, and output stream.
	 * @param processor the processor to use
	 * @param in the input stream to use
	 * @param out the output stream to use
	 */
	public SocketLineHandler(SocketProcessor<T> processor, InputStream in, OutputStream out){
		super(processor, in, out);
	}

	/**
	 * Creates a new SocketLineHandler with the given processor, socket, input stream, and output stream.
	 * Calls openSocket(socket) within this constructor.
	 * @param socket the socket to use
	 * @param processor the processor to use
	 * @param in the input stream to use
	 * @param out the output stream to use
	 */
	public SocketLineHandler(Socket socket, SocketProcessor<T> processor, InputStream in, OutputStream out){
		super(socket, processor, in, out);
	}

	@Override
	public void initialize() throws IOException{
		out = new PrintWriter(getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(getInputStream()));
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
	 * Returns the BufferedReader input of the SocketLineHandler.
	 * @return the BufferedReader input of the SocketLineHandler.
	 */
	protected BufferedReader getInput(){
		return in;
	}

	/**
	 * Returns the PrintWriter output of the SocketLineHandler.
	 * @return the PrintWriter output of the SocketLineHandler.
	 */
	protected PrintWriter getOutput(){
		return out;
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

	/**
	 * Returns a string pipe that can be used with a SocketPipeIn to write data.
	 * @return a string pipe that can be used with a SocketPipeIn to write data.
	 */
	protected SocketPipeIn<String> getStringPipe(){
		return new SocketPipeIn<String>(){

			@Override
			public void push(String output) throws IOException{
				out.println(output);
			}

		};
	}
}
