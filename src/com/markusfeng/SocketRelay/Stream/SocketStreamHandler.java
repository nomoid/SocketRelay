package com.markusfeng.SocketRelay.Stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.H.SocketIOHandler;

/**
 * A SocketHandler used for byte[] data.
 *
 * @author Markus Feng
 */
public class SocketStreamHandler extends SocketIOHandler<byte[]>{

	/**
	 * The default buffer size for streaming byte[]
	 */
	public static final int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * The OutputStream wrapper to write to
	 */
	protected BufferedOutputStream out;
	/**
	 * The InputStream wrapper to read from
	 */
	protected BufferedInputStream in;

	/**
	 * The buffer size for streaming byte[]
	 */
	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	/**
	 * Creates a new SocketStreamHandler with the given processor.
	 * @param processor the processor to use
	 */
	public SocketStreamHandler(SocketProcessor<byte[]> processor){
		super(processor);
	}

	/**
	 * Creates a new SocketStreamHandler with the given processor and socket.
	 * Calls openSocket(socket) within this constructor.
	 * @param socket the socket to use
	 * @param processor the processor to use
	 */
	public SocketStreamHandler(Socket socket, SocketProcessor<byte[]> processor){
		super(socket, processor);
	}

	public SocketStreamHandler(SocketProcessor<byte[]> processor, int bufferSize){
		super(processor);
		this.bufferSize = bufferSize;
	}

	public SocketStreamHandler(Socket socket, SocketProcessor<byte[]> processor, int bufferSize){
		super(socket, processor);
		this.bufferSize = bufferSize;
	}

	public SocketStreamHandler(SocketProcessor<byte[]> processor, InputStream in, OutputStream out){
		super(processor, in, out);
	}

	public SocketStreamHandler(Socket socket, SocketProcessor<byte[]> processor, InputStream in, OutputStream out){
		super(socket, processor, in, out);
	}

	public SocketStreamHandler(SocketProcessor<byte[]> processor, int bufferSize, InputStream in, OutputStream out){
		super(processor, in, out);
		this.bufferSize = bufferSize;
	}

	public SocketStreamHandler(Socket socket, SocketProcessor<byte[]> processor, int bufferSize, InputStream in,
			OutputStream out){
		super(socket, processor, in, out);
		this.bufferSize = bufferSize;
	}

	@Override
	public void initialize() throws IOException{
		out = new BufferedOutputStream(getOutputStream());
		in = new BufferedInputStream(getInputStream());
	}

	@Override
	public void readFromIn() throws IOException{

		byte[] buffer = new byte[bufferSize];
		int count = in.read(buffer);
		while(count >= 0){
			if(!isClosed()){
				try{
					byte[] byteHolder = new byte[count];
					System.arraycopy(buffer, 0, byteHolder, 0, count);
					pushToProcessor(byteHolder);
				}
				catch(Exception e){
					if(!isClosed()){
						e.printStackTrace();
					}
				}
				count = in.read(buffer);
			}
		}
	}

	//Up to user to make sure out is not modified
	//Make new arrays per push, or enable blocking
	@Override
	public void writeToOut(byte[] arr) throws IOException{
		out.write(arr);
	}

}
