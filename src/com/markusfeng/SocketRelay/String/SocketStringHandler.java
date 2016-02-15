package com.markusfeng.SocketRelay.String;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.markusfeng.SocketRelay.B.SocketHandlerAbstract;
import com.markusfeng.SocketRelay.B.SocketProcessor;

/**
 * A SocketHandler used for String data.
 *
 * @author Markus Feng
 */
public class SocketStringHandler extends SocketHandlerAbstract<String>{

	/**
	 * The OutputStream wrapper to write to
	 */
	protected PrintWriter out;

	/**
	 * The InputStream wrapper to read from
	 */
	protected BufferedReader in;

	/**
	 * Creates a new SocketStringHandler with the given processor.
	 * @param processor the processor to use
	 */
	public SocketStringHandler(SocketProcessor<String> processor){
		super(processor);
	}

	/**
	 * Creates a new SocketStringHandler with the given processor and socket.
	 * Calls openSocket(socket) within this constructor.
	 * @param socket the socket to use
	 * @param processor the processor to use
	 */
	public SocketStringHandler(Socket socket, SocketProcessor<String> processor){
		super(socket, processor);
	}

	@Override
	public void initialize() throws IOException{

		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void readFromIn() throws IOException{
		String inputLine;

		while (!isClosed() && (inputLine = in.readLine()) != null) {
			if(!isClosed()){
				try{
					pushToProcessor(inputLine);
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
	public void writeToOut(String out) throws IOException{
		this.out.println(out);
	}

}
