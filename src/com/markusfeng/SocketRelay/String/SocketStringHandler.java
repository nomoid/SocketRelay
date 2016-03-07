package com.markusfeng.SocketRelay.String;

import java.net.Socket;

import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.Line.SocketLineHandler;

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

	@Override
	protected Maybe<String> convertToObject(String string){
		return Maybe.with(string);
	}

	@Override
	protected Maybe<String> convertFromObject(String obj){
		return Maybe.with(obj);
	}


}
