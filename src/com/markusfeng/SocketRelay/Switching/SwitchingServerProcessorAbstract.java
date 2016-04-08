package com.markusfeng.SocketRelay.Switching;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketHandlerStreamGenerator;
import com.markusfeng.SocketRelay.C.SocketProcessorAbstract;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;
import com.markusfeng.SocketRelay.Pipe.SocketPipeIn;
import com.markusfeng.SocketRelay.Pipe.SocketPipeInWriter;
import com.markusfeng.SocketRelay.Pipe.SocketPipeOutImpl;
import com.markusfeng.SocketRelay.Pipe.SocketPipeOutWriter;

public abstract class SwitchingServerProcessorAbstract<T>extends SocketProcessorAbstract<SendingObject<T>>{

	protected Set<Closeable> closeables;

	protected SocketPipeOutImpl<T> pipeOut = new SocketPipeOutImpl<T>(executor());
	protected SocketHandlerStreamGenerator<SocketHandler<T>> gen;
	protected Map<Long, SocketPipeIn<T>> outputs;
	protected ParsingSystem<String, T> parser;

	public SwitchingServerProcessorAbstract(ParsingSystem<String, T> parser,
			SocketHandlerStreamGenerator<SocketHandler<T>> gen){
		this.parser = parser;
		this.gen = gen;
		this.outputs = new HashMap<Long, SocketPipeIn<T>>();
		closeables = new HashSet<Closeable>();
	}

	@Override
	public void input(SendingObject<T> in){
		long sender = in.getSender();
		if(outputs.containsKey(sender)){
			try{
				pipeOut.outputToHandler(outputs.get(sender), in.getObject(), false);
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try{
				outputs.put(sender, createNewPipe());
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected SocketPipeIn<T> createNewPipe() throws IOException{
		SocketPipeOutWriter<T> outWriter = null;
		SocketPipeInWriter<T> inWriter = null;
		try{
			outWriter = new SocketPipeOutWriter<T>(executor(), parser, true);
			inWriter = new SocketPipeInWriter<T>(parser, true);
			SocketHandler<T> handler = gen.getFromStreams(inWriter.getPipedInputStream(),
					outWriter.getPipedOutputStream());
			//TODO raw thread creation
			new Thread(handler).start();
			outWriter.setDestination(handler);
		}
		finally{
			if(outWriter != null){
				closeables.add(outWriter);
			}
			if(inWriter != null){
				closeables.add(inWriter);
			}
		}
		return inWriter;
	}

	protected abstract Maybe<SendingObject<T>> processPacket(Command packet);

	@Override
	public void close(){
		super.close();
		for(Closeable closeable : closeables){
			try{
				closeable.close();
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
