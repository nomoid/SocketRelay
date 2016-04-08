package com.markusfeng.SocketRelay.String;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketProcessor;

/**
 * A SocketHandler used for String data.
 *
 * @author Markus Feng
 */
public class SocketStringHandler extends SocketLineHandler<String>{

	public SocketStringHandler(SocketProcessor<String> processor){
		super(processor);
	}

	public SocketStringHandler(Socket socket, SocketProcessor<String> processor){
		super(socket, processor);
	}

	public SocketStringHandler(SocketProcessor<String> processor, InputStream in, OutputStream out){
		super(processor, in, out);
	}

	public SocketStringHandler(Socket socket, SocketProcessor<String> processor, InputStream in, OutputStream out){
		super(socket, processor, in, out);
	}

	@Override
	protected Maybe<String> convertToObject(String string){
		return Maybe.with(string);
	}

	@Override
	protected Maybe<String> convertFromObject(String obj){
		return Maybe.with(obj);
	}

}
