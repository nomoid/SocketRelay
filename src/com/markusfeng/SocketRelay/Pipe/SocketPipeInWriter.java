package com.markusfeng.SocketRelay.Pipe;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;

public class SocketPipeInWriter<T> implements SocketPipeIn<T>, Closeable{

	protected PipedInputStream pin;
	protected PipedOutputStream pout;

	protected BufferedOutputStream bos;

	protected BufferedWriter bw;

	protected ParsingSystem<byte[], T> streamParser;
	protected ParsingSystem<String, T> writerParser;

	protected boolean stringMode;

	private boolean closed;

	protected SocketPipeInWriter() throws IOException{
		pin = new PipedInputStream();
		pout = new PipedOutputStream(pin);
	}

	public SocketPipeInWriter(ParsingSystem<byte[], T> parser) throws IOException{
		this();
		stringMode = false;
		streamParser = parser;
		bos = new BufferedOutputStream(pout);
	}

	//Unused boolean in constructor to differentiate
	public SocketPipeInWriter(ParsingSystem<String, T> parser, boolean stringMode) throws IOException{
		this();
		stringMode = true;
		writerParser = parser;
		bw = new BufferedWriter(new OutputStreamWriter(pout));
	}

	@Override
	public void push(T out) throws IOException{
		if(isStringMode()){
			Maybe<String> maybe = writerParser.unparse(out);
			if(maybe.isPresent()){
				bw.write(maybe.get());
			}
		}
		else{
			Maybe<byte[]> maybe = streamParser.unparse(out);
			if(maybe.isPresent()){
				bos.write(maybe.get());
			}
		}
	}

	public PipedOutputStream getPipedOutputStream(){
		return pout;
	}

	public PipedInputStream getPipedInputStream(){
		return pin;
	}

	public BufferedWriter getBufferedWriter(){
		return bw;
	}

	public BufferedOutputStream getBufferedOutputStream(){
		return bos;
	}

	public boolean isStringMode(){
		return stringMode;
	}

	@Override
	public void close() throws IOException{
		IOException ex = null;
		if(closed){
			return;
		}
		closed = true;
		try{
			pin.close();
		}
		catch(IOException e){
			ex = e;
		}
		try{
			pout.close();
		}
		catch(IOException e){
			ex = e;
		}
		if(ex != null){
			//TODO exception handling
			throw new IOException(ex);
		}
	}
}
