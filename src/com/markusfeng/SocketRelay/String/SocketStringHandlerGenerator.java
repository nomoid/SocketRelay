package com.markusfeng.SocketRelay.String;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketHandlerGenerator;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;

/**
 * A class for generating SocketStringHandlers
 *
 * @author Markus Feng
 */
public class SocketStringHandlerGenerator implements SocketHandlerGenerator<SocketHandler<String>>{

	protected SocketProcessorGenerator<? extends SocketProcessor<String>> generator;
	protected boolean withStreams;
	protected InputStream inStream;
	protected OutputStream outStream;

	public SocketStringHandlerGenerator(SocketProcessorGenerator<? extends SocketProcessor<String>> gen){
		generator = gen;
		withStreams = false;
	}

	public SocketStringHandlerGenerator(SocketProcessorGenerator<? extends SocketProcessor<String>> gen, InputStream in,
			OutputStream out){
		generator = gen;
		inStream = in;
		outStream = out;
		withStreams = true;
	}

	@Override
	public SocketStringHandler apply(Socket socket){
		if(withStreams){
			return new SocketStringHandler(socket, generator.get(), inStream, outStream);
		}
		else{
			return new SocketStringHandler(socket, generator.get());
		}
	}

}