package com.markusfeng.SocketRelay.Stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketHandlerGenerator;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;

/**
 * A class for generating SocketStreamHandlers
 *
 * @author Markus Feng
 */
public class SocketStreamHandlerGenerator implements SocketHandlerGenerator<SocketHandler<byte[]>>{

	protected SocketProcessorGenerator<? extends SocketProcessor<byte[]>> generator;
	protected boolean withStreams;
	protected InputStream inStream;
	protected OutputStream outStream;

	public SocketStreamHandlerGenerator(SocketProcessorGenerator<? extends SocketProcessor<byte[]>> gen){
		generator = gen;
		withStreams = false;
	}

	public SocketStreamHandlerGenerator(SocketProcessorGenerator<? extends SocketProcessor<byte[]>> gen, InputStream in,
			OutputStream out){
		generator = gen;
		inStream = in;
		outStream = out;
		withStreams = true;
	}

	@Override
	public SocketStreamHandler apply(Socket socket){
		if(withStreams){
			return new SocketStreamHandler(socket, generator.get(), inStream, outStream);
		}
		else{
			return new SocketStreamHandler(socket, generator.get());
		}
	}

}