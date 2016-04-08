package com.markusfeng.SocketRelay.Pipe;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.ExecutorService;

import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;

public class SocketPipeOutWriter<T>extends SocketPipeOutImpl<T> implements SocketPipeWriter, Closeable{

	private static final int BUFFER_SIZE = 1024;

	private Object destinationLock = new Object();
	private boolean destinationSet = false;

	protected PipedInputStream pin;
	protected PipedOutputStream pout;

	protected BufferedInputStream bis;

	protected BufferedReader br;

	protected ParsingSystem<byte[], T> streamParser;
	protected ParsingSystem<String, T> writerParser;

	protected SocketPipeIn<T> destination;

	protected boolean stringMode;

	private boolean closed;

	protected SocketPipeOutWriter() throws IOException{
		super();
		pin = new PipedInputStream();
		pout = new PipedOutputStream(pin);
		setup();
	}

	protected SocketPipeOutWriter(ExecutorService service) throws IOException{
		super(service);
		pin = new PipedInputStream();
		pout = new PipedOutputStream(pin);
		setup();
	}

	public SocketPipeOutWriter(ParsingSystem<byte[], T> parser) throws IOException{
		this();
		stringMode = false;
		streamParser = parser;
		bis = new BufferedInputStream(pin);
	}

	//Unused boolean in constructor to differentiate
	public SocketPipeOutWriter(ParsingSystem<String, T> parser, boolean stringMode) throws IOException{
		this();
		stringMode = true;
		writerParser = parser;
		br = new BufferedReader(new InputStreamReader(pin));
	}

	public SocketPipeOutWriter(ExecutorService service, ParsingSystem<byte[], T> parser) throws IOException{
		this(service);
		stringMode = false;
		streamParser = parser;
		bis = new BufferedInputStream(pin);
	}

	//Unused boolean in constructor to differentiate
	public SocketPipeOutWriter(ExecutorService service, ParsingSystem<String, T> parser, boolean stringMode)
			throws IOException{
		this(service);
		stringMode = true;
		writerParser = parser;
		br = new BufferedReader(new InputStreamReader(pin));
	}

	protected void setup(){
		executor().execute(() -> {
			try{
				synchronized(destinationLock){
					while(!destinationSet){
						try{
							destinationLock.wait();
						}
						catch(InterruptedException e){
							//TODO exception handling
							throw new RuntimeException(e);
						}
					}
				}
				if(isStringMode()){
					String inputLine;

					while(!isClosed() && (inputLine = br.readLine()) != null){
						if(!isClosed()){
							try{
								Maybe<T> maybe = writerParser.parse(inputLine);
								if(maybe.isPresent()){
									outputToHandler(destination, maybe.get(), false);
								}
							}
							catch(Exception e){
								if(!isClosed()){
									e.printStackTrace();
								}
							}
						}
					}
				}
				else{
					byte[] input = new byte[BUFFER_SIZE];
					while(!isClosed()){
						int read = bis.read(input);
						if(read < 0){
							break;
						}
						else{
							try{
								byte[] sent = input;
								if(read != sent.length){
									sent = new byte[read];
									System.arraycopy(input, 0, sent, 0, read);
								}
								Maybe<T> maybe = streamParser.parse(sent);
								if(maybe.isPresent()){
									outputToHandler(destination, maybe.get(), false);
								}
							}
							catch(Exception e){
								if(!isClosed()){
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try{
					close();
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	@Override
	public PipedInputStream getPipedInputStream(){
		return pin;
	}

	@Override
	public PipedOutputStream getPipedOutputStream(){
		return pout;
	}

	public BufferedReader getBufferedReader(){
		return br;
	}

	public BufferedInputStream getBufferedInputStream(){
		return bis;
	}

	@Override
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
		synchronized(destinationLock){
			destinationSet = true;
			destinationLock.notifyAll();
		}
		if(ex != null){
			//TODO exception handling
			throw new IOException(ex);
		}
	}

	public boolean isClosed(){
		return closed;
	}

	public void setDestination(SocketPipeIn<T> destination){
		synchronized(destinationLock){
			this.destination = destination;
			destinationSet = true;
			destinationLock.notifyAll();
		}
	}

}
