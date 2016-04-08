package com.markusfeng.SocketRelay.H;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.markusfeng.SocketRelay.B.SocketHandlerAbstract;
import com.markusfeng.SocketRelay.B.SocketProcessor;

public abstract class SocketIOHandler<T>extends SocketHandlerAbstract<T>{

	protected InputStream inStream;
	protected OutputStream outStream;
	private boolean loadStreams = true;

	protected SocketIOHandler(){

	}

	protected SocketIOHandler(InputStream in, OutputStream out){
		this.inStream = in;
		this.outStream = out;
		loadStreams = false;
	}

	public SocketIOHandler(SocketProcessor<T> processor){
		super(processor);
	}

	public SocketIOHandler(SocketProcessor<T> processor, InputStream in, OutputStream out){
		this(processor);
		this.inStream = in;
		this.outStream = out;
		loadStreams = false;
	}

	public SocketIOHandler(Socket socket, SocketProcessor<T> processor){
		super(socket, processor);
	}

	public SocketIOHandler(Socket socket, SocketProcessor<T> processor, InputStream in, OutputStream out){
		this(socket, processor);
		this.inStream = in;
		this.outStream = out;
		loadStreams = false;
	}

	public InputStream getInputStream() throws IOException{
		if(loadStreams){
			return socket.getInputStream();
		}
		else{
			return inStream;
		}
	}

	public OutputStream getOutputStream() throws IOException{
		if(loadStreams){
			return socket.getOutputStream();
		}
		else{
			return outStream;
		}
	}
}
