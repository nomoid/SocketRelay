package com.markusfeng.SocketRelay.Stream;

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

	public SocketStreamHandlerGenerator(SocketProcessorGenerator<? extends SocketProcessor<byte[]>> gen){
		generator = gen;
	}

	@Override
	public SocketHandler<byte[]> apply(Socket socket){
		return new SocketStreamHandler(socket, generator.get());
	}

}