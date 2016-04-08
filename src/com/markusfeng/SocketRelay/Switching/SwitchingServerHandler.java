package com.markusfeng.SocketRelay.Switching;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.markusfeng.Shared.Command;
import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.Packet.ParsingSystem;
import com.markusfeng.SocketRelay.Packet.ParsingSystems;
import com.markusfeng.SocketRelay.Packet.SocketPacketHandler;

/**
 * Root of Switching Server.
 *
 * @author Markus Feng
 *
 * @param <T> type of object to be sent
 */
public class SwitchingServerHandler<T>extends SocketPacketHandler<SendingObject<T>>{

	protected SwitchingServerProcessorAbstract<T> ssp;

	public SwitchingServerHandler(Socket socket, SwitchingServerProcessorAbstract<T> processor,
			ParsingSystem<String, T> parser){
		super(socket, processor, ParsingSystems.sendingSystem(parser));
		this.ssp = processor;
	}

	public SwitchingServerHandler(Socket socket, SwitchingServerProcessorAbstract<T> processor,
			ParsingSystem<String, T> parser, InputStream in, OutputStream out){
		super(socket, processor, ParsingSystems.sendingSystem(parser), in, out);
		this.ssp = processor;
	}

	@Override
	protected Maybe<SendingObject<T>> processPacket(Command packet){
		return ssp.processPacket(packet);
	}
}
