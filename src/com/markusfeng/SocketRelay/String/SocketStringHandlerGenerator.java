package com.markusfeng.SocketRelay.String;

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

	public SocketStringHandlerGenerator(SocketProcessorGenerator<? extends SocketProcessor<String>> gen){
		generator = gen;
	}

	@Override
	public SocketHandler<String> apply(Socket socket){
		return new SocketStringHandler(socket, generator.get());
	}

}