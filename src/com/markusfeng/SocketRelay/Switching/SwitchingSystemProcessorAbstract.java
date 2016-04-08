package com.markusfeng.SocketRelay.Switching;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.A.SocketHandlerGenerator;
import com.markusfeng.SocketRelay.B.SocketProcessor;
import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;
import com.markusfeng.SocketRelay.C.SocketProcessorAbstract;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;
import com.markusfeng.SocketRelay.Pipe.SocketPipeIn;

public abstract class SwitchingSystemProcessorAbstract<T>extends SocketProcessorAbstract<SendingObject<T>>
		implements SocketHandlerGenerator<SwitchingSystemHandler<T>>{

	protected Map<Long, SocketPipeIn<SendingObject<T>>> map;
	protected SocketProcessorGenerator<SocketProcessor<T>> gen;
	protected ParsingSystem<String, SendingObject<T>> parser;

	public SwitchingSystemProcessorAbstract(SocketProcessorGenerator<SocketProcessor<T>> gen,
			ParsingSystem<String, SendingObject<T>> parser){
		map = new HashMap<Long, SocketPipeIn<SendingObject<T>>>();
		this.gen = gen;
		this.parser = parser;
	}

	@Override
	public SwitchingSystemHandler<T> apply(Socket socket){
		long id = SendingObjects.generateID(map.keySet());
		SwitchingSystemHandler<T> ssh = new SwitchingSystemHandler<T>(id, socket, this, parser);
		map.put(id, ssh);
		return ssh;
	}

	public abstract Maybe<SendingObject<T>> processPacket(long id, Command packet);

	/**
	 * First layer: input id
	 * Second layer: output id
	 *
	 * Swaps the layers when written to the output (output unwraps automatically)
	 *
	 * @throws IOException
	 */
	@Override
	public void input(SendingObject<T> in){
		try{
			long from = in.getSender();
			long to = in.getRecipient();
			if(map.containsKey(to)){
				SocketPipeIn<SendingObject<T>> t = map.get(to);
				outputToHandler(t, SendingObjects.make(from, to, in.getObject()), false);
			}
			else{
				//TODO Delivery failed
			}
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
